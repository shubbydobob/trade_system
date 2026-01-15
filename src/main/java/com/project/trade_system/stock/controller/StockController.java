package com.project.trade_system.stock.controller;

import com.project.trade_system.stock.dto.StockSummaryResponse;
import com.project.trade_system.stock.service.StockQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockQueryService service;

    @GetMapping("/{ticker}/summary")
    public StockSummaryResponse summary(@PathVariable String ticker) {
        return service.getSummary(ticker);
    }
}

