package co.com.ancas.customer_service.dto;

import co.com.ancas.customer_service.domain.Ticker;

public record StockPriceResponse(
        Ticker ticker,
        Integer price
) {
}
