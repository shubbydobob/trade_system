package com.project.trade_system.stock.dto;

import com.project.trade_system.stock.service.model.StockAnalysisData;
import com.project.trade_system.stock.service.strategy.score.StrategyScore;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class StockSummaryResponse {

    // ===== 공통 =====
    private String ticker;
    private Double latestPrice;
    private Double high52w;
    private Double low52w;
    private Double avgTradeAmount90d;
    private LocalDate lastTradeDate;

    // ===== 분석 전용 =====
    private Double ma5;
    private Double ma10;
    private Double ma20;
    private Double ma60;
    private Double ma120;

    private Integer strategyScore;
    private Integer trendScore;
    private Integer volumeScore;
    private Integer momentumScore;
    private Integer riskScore;

    /**
     * ✅ 기존 요약 조회용 (Native Query)
     * 절대 수정하지 않음
     */
    public static StockSummaryResponse from(
            Map<String, Object> latestRow,
            Double avgTradeAmount90d
    ) {
        return StockSummaryResponse.builder()
                .ticker((String) latestRow.get("ticker"))
                .latestPrice(toDouble(latestRow.get("latest_price")))
                .high52w(toDouble(latestRow.get("high_52w")))
                .low52w(toDouble(latestRow.get("low_52w")))
                .lastTradeDate((LocalDate) latestRow.get("trade_date"))
                .avgTradeAmount90d(avgTradeAmount90d)
                .build();
    }

    /**
     * 🔥 분석 엔진 전용
     * 조건검색 + 전략 점수 결과 조립
     */
    public static StockSummaryResponse fromAnalysis(
            StockAnalysisData data,
            StrategyScore score
    ) {
        return StockSummaryResponse.builder()
                .ticker(data.getTicker())
                .latestPrice(data.getClose())
                .lastTradeDate(data.getTradeDate())

                .ma5(data.getMa5())
                .ma10(data.getMa10())
                .ma20(data.getMa20())
                .ma60(data.getMa60())
                .ma120(data.getMa120())

                .strategyScore(score.getTotal())
                .trendScore(score.getTrend())
                .volumeScore(score.getVolume())
                .momentumScore(score.getMomentum())
                .riskScore(score.getRisk())
                .build();
    }

    private static Double toDouble(Object value) {
        if (value == null) return null;
        return ((Number) value).doubleValue();
    }
}
