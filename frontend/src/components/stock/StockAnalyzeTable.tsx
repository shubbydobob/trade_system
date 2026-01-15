import {getScoreColor} from "../../utils/score.ts";

type StockAnalyze = {
    ticker: string;
    latestPrice: number;
    ma50: number;
    ma100: number;
    ma120: number;
    ma200: number;
    strategyScore: number;
};

export default function StockAnalyzeTable({data,}: { data: StockAnalyze[]; }) {
    return (
        <table style={{width: "100%", borderCollapse: "collapse"}}>
            <thead>
            <tr>
                <th>티커</th>
                <th>현재가</th>
                <th>MA50</th>
                <th>MA100</th>
                <th>MA200</th>
                <th>점수</th>
            </tr>
            </thead>
            <tbody>
            {data.map((row) => (
                <tr key={row.ticker}>
                    <td>{row.ticker}</td>
                    <td>{row.latestPrice.toFixed(2)}</td>
                    <td>{row.ma50.toFixed(2)}</td>
                    <td>{row.ma100.toFixed(2)}</td>
                    <td>{row.ma200.toFixed(2)}</td>
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
