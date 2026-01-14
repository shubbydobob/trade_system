import yfinance as yf
import pandas as pd
from datetime import datetime, timedelta
import os

# =========================
# 설정
# =========================

START_DATE = (datetime.now() - timedelta(days=365 * 5)).strftime("%Y-%m-%d")
END_DATE = datetime.now().strftime("%Y-%m-%d")

OUTPUT_DIR = "data"
os.makedirs(OUTPUT_DIR, exist_ok=True)

# =========================
# Universe 정의
# =========================

NASDAQ_TICKERS = [
    "AAPL", "MSFT", "NVDA", "AMZN", "META", "GOOGL", "TSLA"
]

# NYSE_TICKERS = [
#     "JPM", "BAC", "WMT", "KO", "DIS", "XOM", "CVX", "JNJ",
#     "PG", "V", "MA"
# ]

# ETF_TICKERS = [
#     "SPY", "QQQ", "IWM", "DIA",
#     "SOXX", "XLK", "XLF"
# ]

# LEVERAGED_ETF_TICKERS = [
#     "TQQQ", "SQQQ", "SOXL", "SOXS", "UPRO", "SPXL"
# ]

ALL_TICKERS = list(set(
    NASDAQ_TICKERS
    # + NYSE_TICKERS
    # + ETF_TICKERS
    # + LEVERAGED_ETF_TICKERS
))

# =========================
# 데이터 수집 함수
# =========================

def fetch_ticker_data(ticker: str) -> pd.DataFrame:
    print(f"[FETCH] {ticker}")

    df = yf.download(
        ticker,
        start=START_DATE,
        end=END_DATE,
        auto_adjust=False,
        progress=False
    )

    if df.empty:
        print(f"[SKIP] {ticker} (no data)")
        return pd.DataFrame()

    # 🔥 핵심 1: 컬럼 평탄화 (MultiIndex 방어)
    if isinstance(df.columns, pd.MultiIndex):
        df.columns = df.columns.get_level_values(0)

    df.reset_index(inplace=True)

    # 날짜 (date 타입으로 고정)
    df["trade_date"] = pd.to_datetime(df["Date"]).dt.date

    # 티커
    df["ticker"] = ticker

    # 🔥 핵심 2: Series로 강제
    adj_close = pd.to_numeric(df["Adj Close"], errors="coerce")
    volume = pd.to_numeric(df["Volume"], errors="coerce")

    # 거래대금
    df["trade_amount"] = adj_close * volume

    # 52주 신고가 / 최저가 (Adj Close 기준)
    df["high_52w"] = adj_close.rolling(window=252).max()
    df["low_52w"] = adj_close.rolling(window=252).min()

    return df[[
        "trade_date",
        "ticker",
        "Open",
        "High",
        "Low",
        "Adj Close",
        "Volume",
        "trade_amount",
        "high_52w",
        "low_52w"
    ]]

# =========================
# 메인 실행
# =========================

all_data = []

for ticker in ALL_TICKERS:
    try:
        df = fetch_ticker_data(ticker)
        if not df.empty:
            all_data.append(df)
    except Exception as e:
        print(f"[ERROR] {ticker}: {e}")

if not all_data:
    raise RuntimeError("No data fetched. Check tickers or network.")

final_df = pd.concat(all_data, ignore_index=True)

# 컬럼명 정리 (DB 기준)
final_df.rename(columns={
    "Open": "open_price",
    "High": "high_price",
    "Low": "low_price",
    "Adj Close": "adj_close_price",
    "Volume": "volume"
}, inplace=True)

# =========================
# 거래대금 상위 필터링
# (최근 90일 평균 기준)
# =========================

cutoff_date = datetime.now().date() - timedelta(days=90)

recent_df = final_df[
    final_df["trade_date"] >= cutoff_date
]

top200_tickers = (
    recent_df
    .groupby("ticker")["trade_amount"]
    .mean()
    .sort_values(ascending=False)
    .head(200)
    .index
)

filtered_df = (
    final_df[final_df["ticker"].isin(top200_tickers)]
    .sort_values(["ticker", "trade_date"])
)

# =========================
# CSV 저장
# =========================

output_path = os.path.join(
    OUTPUT_DIR,
    f"screener-stocks-{datetime.now().strftime('%Y-%m-%d')}.csv"
)

filtered_df.to_csv(output_path, index=False)

print("\n✅ CSV saved successfully")
print(f"📄 Path    : {output_path}")
print(f"📊 Rows    : {len(filtered_df)}")
print(f"🏷️  Tickers : {filtered_df['ticker'].nunique()}")
