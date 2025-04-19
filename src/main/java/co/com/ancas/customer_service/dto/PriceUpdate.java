package co.com.ancas.customer_service.dto;

import java.time.LocalDateTime;

public record PriceUpdate(
        String ticker,
        Integer price,
        LocalDateTime time
) {
}
