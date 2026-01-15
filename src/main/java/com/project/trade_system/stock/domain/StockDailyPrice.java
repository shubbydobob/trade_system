package com.project.trade_system.stock.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stock_daily_price")
@IdClass(StockDailyPriceId.class)
@Getter
@NoArgsConstructor
public class StockDailyPrice {

    @Id
    @Column(name = "ticker")
    private String ticker;

    @Id
    @Column(name = "trade_date")
    private LocalDate tradeDate;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "adj_close_price")
    private Double adjClosePrice;

    @Column(name = "high_52w")
    private Double high52w;

    @Column(name = "low_52w")
    private Double low52w;

    @Column(name = "trade_amount")
    private Double tradeAmount;
}
