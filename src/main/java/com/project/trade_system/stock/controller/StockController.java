package com.project.trade_system.stock.controller;

import com.project.trade_system.common.response.ApiResponse;
import com.project.trade_system.stock.dto.StockSummaryResponse;
import com.project.trade_system.stock.service.StockAnalysisService;
import com.project.trade_system.stock.service.StockQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockQueryService stockQueryService;
    private final StockAnalysisService stockAnalysisService;

    @GetMapping("/{ticker}/summary")
    public StockSummaryResponse summary(@PathVariable String ticker) {
        return stockQueryService.getSummary(ticker);
    }

    /**
     * 🔥 조건검색 + 전략 점수 분석
     * 예: /api/stocks/analyze?date=2026-01-15&limit=20
     */
    @GetMapping("/analyze")
    public ApiResponse<List<StockSummaryResponse>> analyze(@RequestParam(required = false) LocalDate date, @RequestParam(defaultValue = "20") int limit) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now().minusDays(1);
        return ApiResponse.ok(stockAnalysisService.analyze(targetDate, limit));
    }
}

