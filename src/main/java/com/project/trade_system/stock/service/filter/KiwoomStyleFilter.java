package com.project.trade_system.stock.service.filter;

import com.project.trade_system.stock.service.model.StockAnalysisData;

public class KiwoomStyleFilter implements StockFilter {

    public KiwoomStyleFilter(double minTradeAmount) {
    }

    @Override
    public boolean test(StockAnalysisData s) {

        if (s.getClose() < 1) return false;
        if (s.getVolume() < 20_000) return false;

        return s.getMa5() > s.getMa10()
                && s.getMa10() > s.getMa20()
                && s.getMa20() > s.getMa60()
                && s.getMa60() > s.getMa120();
    }
}
