package com.project.trade_system.stock.repository;

import com.project.trade_system.stock.domain.StockDailyPrice;
import com.project.trade_system.stock.domain.StockDailyPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface StockDailyPriceRepository extends JpaRepository<StockDailyPrice, StockDailyPriceId> {

    /**
     * 종목 최신 일봉 요약
     */
    @Query(value = """
                SELECT
                    ticker,
                    adj_close_price AS latest_price,
                    high_52w,
                    low_52w,
                    trade_date
                FROM stock_daily_price
                WHERE ticker = :ticker
                ORDER BY trade_date DESC
                LIMIT 1
            """, nativeQuery = true)
    Map<String, Object> findLatestSummary(@Param("ticker") String ticker);

    /**
     * 최근 90일 평균 거래대금
     */
    @Query(value = """
                SELECT AVG(trade_amount)
                FROM stock_daily_price
                WHERE ticker = :ticker
                  AND trade_date >= CURRENT_DATE - INTERVAL '90 days'
            """, nativeQuery = true)
    Double findAvgTradeAmount90d(@Param("ticker") String ticker);


}

