import axios from "axios";
import type {StockSummary} from "../types/stock.ts";

export const fetchStockSummary = async (ticker: string) => {
  const res = await axios.get<StockSummary>(`/api/stocks/${ticker}/summary`);
  return res.data;
};
