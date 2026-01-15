import { useEffect, useState } from "react";
import type {StockSummary} from "../../types/stock.ts";
import {fetchStockSummary} from "../../services/stockService.ts";
import {formatNumber} from "../../utils/number.ts";

interface Props {
    ticker: string;
}

const StockSummaryPanel = ({ ticker }: Props) => {
    const [data, setData] = useState<StockSummary | null>(null);

    useEffect(() => {
        fetchStockSummary(ticker).then(setData);
    }, [ticker]);

    if (!data) return <div>Loading...</div>;

    return (
        <div style={{ marginTop: "16px" }}>
            <h4>{data.ticker} 요약</h4>
            <ul>
                <li>최근가: {formatNumber(data.latestPrice)}</li>
                <li>52주 신고가: {formatNumber(data.high52w)}</li>
                <li>52주 신저가: {formatNumber(data.low52w)}</li>
                <li>90일 평균 거래대금: {formatNumber(data.avgTradeAmount90d)}</li>

            </ul>
        </div>
    );
};

export default StockSummaryPanel;
