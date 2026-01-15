package com.project.trade_system.stock.service.strategy.score;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrategyScore {

    private int trend;
    private int volume;
    private int momentum;
    private int risk;
    private int total;

    public void calcTotal() {
        this.total = trend + volume + momentum + risk;
    }
}
