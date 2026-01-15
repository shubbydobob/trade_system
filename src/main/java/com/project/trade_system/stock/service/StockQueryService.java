package com.project.trade_system.stock.service;

import com.project.trade_system.stock.dto.StockSummaryResponse;
import com.project.trade_system.stock.repository.StockDailyPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockQueryService {

    private final StockDailyPriceRepository repository;

    public StockSummaryResponse getSummary(String ticker) {
        Map<String, Object> latest = repository.findLatestSummary(ticker);
        Double avgTradeAmount90d = repository.findAvgTradeAmount90d(ticker);

        return StockSummaryResponse.from(latest, avgTradeAmount90d);
    }
}

