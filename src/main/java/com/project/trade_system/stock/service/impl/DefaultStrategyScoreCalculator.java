package com.project.trade_system.stock.service.impl;

import com.project.trade_system.stock.domain.StockDailyPrice;
import com.project.trade_system.stock.service.model.StockAnalysisData;
import com.project.trade_system.stock.service.strategy.score.StrategyScore;
import com.project.trade_system.stock.service.strategy.score.StrategyScoreCalculator;
import org.springframework.stereotype.Component;

@Component
public class DefaultStrategyScoreCalculator implements StrategyScoreCalculator {

    @Override
    public StrategyScore calculate(StockAnalysisData s) {
        StrategyScore score = new StrategyScore();

        int trend = 0;
        if (s.getMa5() > s.getMa10()) trend += 5;
        if (s.getMa10() > s.getMa20()) trend += 5;
        if (s.getMa20() > s.getMa60()) trend += 5;
        if (s.getMa60() > s.getMa120()) trend += 5;
        score.setTrend(trend);

        score.setVolume(s.getTradeAmount() > 30_000_000_000L ? 20 : 10);
        score.setMomentum(10);
        score.setRisk(10);

        score.calcTotal();
        return score;
    }
}
