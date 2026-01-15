package com.project.trade_system.stock.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class StockSummaryResponse {

    private String ticker;
    private Double latestPrice;
    private Double high52w;
    private Double low52w;
    private Double avgTradeAmount90d;
    private LocalDate lastTradeDate;

    /**
     * Native Query 결과(Map) → DTO 변환
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

    private static Double toDouble(Object value) {
        if (value == null) return null;
        return ((Number) value).doubleValue();
    }
}
