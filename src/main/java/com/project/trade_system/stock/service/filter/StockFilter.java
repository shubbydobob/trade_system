package com.project.trade_system.stock.service.filter;

import com.project.trade_system.stock.service.model.StockAnalysisData;

public interface StockFilter {
    boolean test(StockAnalysisData stock);
}
