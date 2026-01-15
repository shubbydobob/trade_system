package com.project.trade_system.stock.service.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class StockAnalysisData {

    private String ticker;
    private LocalDate tradeDate;

    private double close;
    private double tradeAmount;
    private long volume;

    private double ma5;
    private double ma10;
    private double ma20;
    private double ma60;
    private double ma120;
}
