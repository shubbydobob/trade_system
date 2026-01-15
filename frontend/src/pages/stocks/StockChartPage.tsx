import {useState} from "react";
import PageLayout from "../../components/common/PageLayout.tsx";
import StockSelector from "../../components/stock/StockSelector.tsx";
import {NASDAQ_TICKERS} from "../../constants/stock.ts";
import TradingViewChart from "../../components/stock/TradingViewChart.tsx";
import StockSummaryPanel from "../../components/stock/StockSummaryPanel.tsx";

const StockChartPage = () => {
    const [ticker, setTicker] = useState<string>("AAPL");

    return (
        <PageLayout title="Stock Chart">
            <StockSelector
                tickers={NASDAQ_TICKERS}
                value={ticker}
                onChange={setTicker}
            />

            <TradingViewChart ticker={ticker}/>
            <StockSummaryPanel ticker={ticker}/>
        </PageLayout>
    );
};

export default StockChartPage;
