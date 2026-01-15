export interface StockSummary {
  ticker: string;
  latestPrice: number;
  high52w: number;
  low52w: number;
  avgTradeAmount90d: number;
  tradeDate: string;
}