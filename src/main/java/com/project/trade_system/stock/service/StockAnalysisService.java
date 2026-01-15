package com.project.trade_system.stock.service;

import com.project.trade_system.common.util.MovingAverageUtils;
import com.project.trade_system.stock.domain.StockDailyPrice;
import com.project.trade_system.stock.dto.StockSummaryResponse;
import com.project.trade_system.stock.repository.StockDailyPriceRepository;
import com.project.trade_system.stock.service.filter.KiwoomStyleFilter;
import com.project.trade_system.stock.service.filter.StockFilter;
import com.project.trade_system.stock.service.model.StockAnalysisData;
import com.project.trade_system.stock.service.strategy.score.StrategyScore;
import com.project.trade_system.stock.service.strategy.score.StrategyScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockAnalysisService {

    private final StockDailyPriceRepository repository;
    private final StrategyScoreCalculator scoreCalculator;


    public List<StockSummaryResponse> analyze(
            LocalDate date,
            int limit
    ) {
        StockFilter filter = new KiwoomStyleFilter(30_000_000_000L);

        // 1) 분석 대상 티커 목록 (예: 해당 일자 존재하는 티커)
        List<String> tickers = repository.findTickersByDate(date);

        List<StockSummaryResponse> results = new ArrayList<>();

        for (String ticker : tickers) {

            List<StockDailyPrice> history =
                    repository.findHistory(
                            ticker, date
                    );

            if (history.size() < 200) continue;

            StockAnalysisData data =
                    StockAnalysisData.builder()
                            .ticker(ticker)
                            .tradeDate(date)
                            .close(history.get(0).getAdjClosePrice())
                            .tradeAmount(history.get(0).getTradeAmount())
                            .volume(history.get(0).getVolume())
                            .ma5(MovingAverageUtils.calculate(history, 5))
                            .ma10(MovingAverageUtils.calculate(history, 10))
                            .ma20(MovingAverageUtils.calculate(history, 20))
                            .ma60(MovingAverageUtils.calculate(history, 60))
                            .ma120(MovingAverageUtils.calculate(history, 120))
                            .build();

            if (!filter.test(data)) continue;

            StrategyScore score = scoreCalculator.calculate(data);

            results.add(
                    StockSummaryResponse.fromAnalysis(data, score)
            );
        }

        // 2) 점수 기준 정렬 + TOP N
        return results.stream()
                .sorted(
                        Comparator.comparing(
                                StockSummaryResponse::getStrategyScore
                        ).reversed()
                )
                .limit(limit)
                .toList();
    }
}
