package co.com.ancas.customer_service.dto;

import co.com.ancas.customer_service.domain.Ticker;

public record Holding(
        Ticker ticker,
        Integer quantity
) {
}
