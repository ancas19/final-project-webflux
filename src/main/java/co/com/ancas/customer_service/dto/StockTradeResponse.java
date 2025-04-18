package co.com.ancas.customer_service.dto;

import co.com.ancas.customer_service.domain.Ticker;
import co.com.ancas.customer_service.domain.TradeAction;

public record StockTradeResponse(
        Long customerId,
        Ticker ticker,
        Integer quantity,
        Integer price,
        TradeAction action,
        Integer totalPrice,
        Integer balance
) {
}
