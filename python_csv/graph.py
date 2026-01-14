import pandas as pd
import matplotlib.pyplot as plt

# CSV 로드
df = pd.read_csv("data/screener-stocks-2026-01-15.csv")

# 날짜 타입 변환
df["trade_date"] = pd.to_datetime(df["trade_date"])

# 특정 종목 선택
aapl = df[df["ticker"] == "AAPL"].sort_values("trade_date")

# 이동평균 계산
aapl["ma_20"] = aapl["adj_close_price"].rolling(20).mean()
aapl["ma_60"] = aapl["adj_close_price"].rolling(60).mean()
aapl["ma_120"] = aapl["adj_close_price"].rolling(120).mean()

# 그래프
plt.figure(figsize=(12,6))
plt.plot(aapl["trade_date"], aapl["adj_close_price"], label="Price")
plt.plot(aapl["trade_date"], aapl["ma_20"], label="MA 20")
plt.plot(aapl["trade_date"], aapl["ma_60"], label="MA 60")
plt.plot(aapl["trade_date"], aapl["ma_120"], label="MA 120")

plt.legend()
plt.title("AAPL Price & Moving Averages")
plt.show()
