package com.project.trade_system.stock.service.strategy.score;

import com.project.trade_system.stock.service.model.StockAnalysisData;

public interface StrategyScoreCalculator {
    StrategyScore calculate(StockAnalysisData stock);
}
