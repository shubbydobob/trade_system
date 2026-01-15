interface Props {
  tickers: string[];
  value: string;
  onChange: (ticker: string) => void;
}

const StockSelector = ({ tickers, value, onChange }: Props) => {
  return (
    <select
      value={value}
      onChange={(e) => onChange(e.target.value)}
      style={{ marginBottom: "16px" }}
    >
      {tickers.map((ticker) => (
        <option key={ticker} value={ticker}>
          {ticker}
        </option>
      ))}
    </select>
  );
};

export default StockSelector;
