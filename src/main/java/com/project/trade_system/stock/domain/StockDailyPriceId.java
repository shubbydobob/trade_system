package com.project.trade_system.stock.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class StockDailyPriceId implements Serializable {

    private String ticker;
    private LocalDate tradeDate;

    public StockDailyPriceId() {
    }

    public StockDailyPriceId(String ticker, LocalDate tradeDate) {
        this.ticker = ticker;
        this.tradeDate = tradeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockDailyPriceId)) return false;
        StockDailyPriceId that = (StockDailyPriceId) o;
        return Objects.equals(ticker, that.ticker)
                && Objects.equals(tradeDate, that.tradeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, tradeDate);
    }
}
