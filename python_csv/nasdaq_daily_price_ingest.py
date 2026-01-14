"""
=========================================================
File      : nasdaq_daily_price_ingest.py
Purpose   : NASDAQ 종목 일봉 시세 수집 → PostgreSQL 적재
DB        : PostgreSQL
Author    : Quant Trading System
=========================================================
"""

# =========================================================
# 1. IMPORT
# =========================================================

import os
from datetime import datetime, timedelta

import pandas as pd
import yfinance as yf
from sqlalchemy import create_engine, Table, MetaData
from sqlalchemy.dialects.postgresql import insert


# =========================================================
# 2. CONFIG
# =========================================================

# 수집 기간 (최근 5년)
START_DATE = (datetime.now() - timedelta(days=365 * 5)).strftime("%Y-%m-%d")
END_DATE = datetime.now().strftime("%Y-%m-%d")

# NASDAQ Universe (초기 단계)
NASDAQ_TICKERS = [
    "AAPL",
    "MSFT",
    "NVDA",
    "AMZN",
    "META",
    "GOOGL",
    "TSLA",
]

# DB 연결 정보
DB_URL = (
    "postgresql+psycopg2://"
    "trade_user:trade_password@localhost:5432/trade_system"
)

ENGINE = create_engine(
    DB_URL,
    pool_size=5,
    max_overflow=10,
)


# =========================================================
# 3. DATA FETCH
# =========================================================

def fetch_ticker_data(ticker: str) -> pd.DataFrame:
    """
    단일 종목 일봉 시세 수집
    """

    print(f"[FETCH] {ticker}")

    df = yf.download(
        ticker,
        start=START_DATE,
        end=END_DATE,
        auto_adjust=False,
        progress=False,
    )

    if df.empty:
        print(f"[SKIP] {ticker} (no data)")
        return pd.DataFrame()

    # MultiIndex 방어
    if isinstance(df.columns, pd.MultiIndex):
        df.columns = df.columns.get_level_values(0)

    df.reset_index(inplace=True)

    df["trade_date"] = pd.to_datetime(df["Date"]).dt.date
    df["ticker"] = ticker

    adj_close = pd.to_numeric(df["Adj Close"], errors="coerce")
    volume = pd.to_numeric(df["Volume"], errors="coerce")

    df["trade_amount"] = adj_close * volume

    # 52주 고/저 (252 거래일)
    df["high_52w"] = adj_close.rolling(window=252).max()
    df["low_52w"] = adj_close.rolling(window=252).min()

    return df[
        [
            "trade_date",
            "ticker",
            "Open",
            "High",
            "Low",
            "Adj Close",
            "Volume",
            "trade_amount",
            "high_52w",
            "low_52w",
        ]
    ]


# =========================================================
# 4. DATA NORMALIZE
# =========================================================

def normalize_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    """
    DB 스키마 기준 컬럼 정규화
    """

    df = df.rename(
        columns={
            "Open": "open_price",
            "High": "high_price",
            "Low": "low_price",
            "Adj Close": "adj_close_price",
            "Volume": "volume",
        }
    )

    return df[
        [
            "trade_date",
            "ticker",
            "open_price",
            "high_price",
            "low_price",
            "adj_close_price",
            "volume",
            "trade_amount",
            "high_52w",
            "low_52w",
        ]
    ]


# =========================================================
# 5. UPSERT (PostgreSQL)
# =========================================================

def upsert_stock_daily_price(df: pd.DataFrame):
    """
    stock_daily_price 테이블 UPSERT
    PK: (ticker, trade_date)
    """

    metadata = MetaData()
    table = Table(
        "stock_daily_price",
        metadata,
        autoload_with=ENGINE,
        schema="public",
    )

    records = df.to_dict(orient="records")
    stmt = insert(table).values(records)

    update_columns = {
        col.name: stmt.excluded[col.name]
        for col in table.columns
        if col.name not in ("ticker", "trade_date", "created_at")
    }

    stmt = stmt.on_conflict_do_update(
        index_elements=["ticker", "trade_date"],
        set_=update_columns,
    )

    with ENGINE.begin() as conn:
        conn.execute(stmt)


# =========================================================
# 6. MAIN
# =========================================================

def main():
    all_data = []

    for ticker in NASDAQ_TICKERS:
        try:
            df = fetch_ticker_data(ticker)
            if not df.empty:
                all_data.append(df)
        except Exception as e:
            print(f"[ERROR] {ticker}: {e}")

    if not all_data:
        raise RuntimeError("No data fetched.")

    merged_df = pd.concat(all_data, ignore_index=True)
    normalized_df = normalize_dataframe(merged_df)

    upsert_stock_daily_price(normalized_df)

    print("\n✅ NASDAQ daily prices upsert completed")
    print(f"📊 Rows    : {len(normalized_df)}")
    print(f"🏷️  Tickers : {normalized_df['ticker'].nunique()}")


# =========================================================
# 7. ENTRY POINT
# =========================================================

if __name__ == "__main__":
    main()
