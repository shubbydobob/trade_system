package com.project.trade_system.common.util;

import com.project.trade_system.stock.domain.StockDailyPrice;

import java.util.Comparator;
import java.util.List;

public class MovingAverageUtils {

    public static double calculate(
            List<StockDailyPrice> prices,
            int period
    ) {
        return prices.stream()
                .sorted(Comparator.comparing(
                        StockDailyPrice::getTradeDate
                ).reversed())
                .limit(period)
                .mapToDouble(StockDailyPrice::getAdjClosePrice)
                .average()
                .orElse(0);
    }
}
