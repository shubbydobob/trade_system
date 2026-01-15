import { Routes, Route, Navigate } from "react-router-dom";
import StockChartPage from "../pages/stocks/StockChartPage.tsx";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/stocks" />} />
      <Route path="/stocks" element={<StockChartPage />} />
    </Routes>
  );
};

export default AppRoutes;
