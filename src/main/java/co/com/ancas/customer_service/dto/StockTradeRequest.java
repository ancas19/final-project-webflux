package co.com.ancas.customer_service.dto;

import co.com.ancas.customer_service.domain.Ticker;
import co.com.ancas.customer_service.domain.TradeAction;

public record StockTradeRequest(
        Ticker ticker,
        Integer quantity,
        Integer price,
        TradeAction action
) {

    public Integer totalValue() {
        return quantity * price;
    }
}
