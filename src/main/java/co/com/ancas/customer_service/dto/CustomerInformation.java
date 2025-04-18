package co.com.ancas.customer_service.dto;

import java.util.List;

public record CustomerInformation(
        Long id,
        String name,
        Integer balance,
        List<Holding> holdings
) {
}
