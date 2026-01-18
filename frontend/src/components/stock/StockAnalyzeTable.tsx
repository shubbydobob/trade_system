import { getScoreColor } from "../../utils/score.ts";

type StockAnalyze = {
  ticker: string;
  latestPrice: number;
  ma5: number;
  ma10: number;
  ma20: number;
  ma60: number;
  ma120: number;
  strategyScore: number;
};

export default function StockAnalyzeTable({ data }: { data: StockAnalyze[] }) {
  console.log(data);
  return (
    <table style={{ width: "100%", borderCollapse: "collapse" }}>
      <thead>
        <tr>
          <th>티커</th>
          <th>현재가</th>
          <th>MA5</th>
          <th>MA10</th>
          <th>MA20</th>
          <th>MA60</th>
          <th>MA120</th>
          <th>점수</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row) => (
          <tr key={row.ticker}>
            <td>{row.ticker}</td>
            <td>{row.latestPrice.toFixed(2)}</td>
            <td>{row.ma5.toFixed(2)}</td>
            <td>{row.ma10.toFixed(2)}</td>
            <td>{row.ma20.toFixed(2)}</td>
            <td>{row.ma60.toFixed(2)}</td>
            <td>{row.ma120.toFixed(2)}</td>
            <td
              style={{
                fontWeight: "bold",
                color: getScoreColor(row.strategyScore),
              }}
            >
              {row.strategyScore}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
