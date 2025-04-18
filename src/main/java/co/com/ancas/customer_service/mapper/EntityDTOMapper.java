package co.com.ancas.customer_service.mapper;

import co.com.ancas.customer_service.domain.Ticker;
import co.com.ancas.customer_service.dto.CustomerInformation;
import co.com.ancas.customer_service.dto.Holding;
import co.com.ancas.customer_service.dto.StockTradeRequest;
import co.com.ancas.customer_service.dto.StockTradeResponse;
import co.com.ancas.customer_service.entity.Customer;
import co.com.ancas.customer_service.entity.PortfolioItem;

import java.util.List;

public class EntityDTOMapper {
    public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> portfolioItems) {
        return new CustomerInformation(
                customer.getId(),
                customer.getName(),
                customer.getBalance(),
                portfolioItems.stream()
                        .map(portfolioItem -> new Holding(
                                portfolioItem.getTicker(),
                                portfolioItem.getQuantity()
                        ))
                        .toList()
        );
    }

    public static PortfolioItem toPortfolioItem(Long customerId, Ticker ticker) {
        return new PortfolioItem(
                null,
                customerId,
                ticker,
                0
        );
    }

    public static StockTradeResponse toStockTradeResponse(Customer customer, StockTradeRequest request) {
        return new StockTradeResponse(
                customer.getId(),
                request.ticker(),
                request.quantity(),
                request.price(),
                request.action(),
                request.totalValue(),
                customer.getBalance()
        );
    }
}
