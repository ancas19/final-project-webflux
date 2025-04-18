package co.com.ancas.customer_service.service;

import co.com.ancas.customer_service.dto.StockTradeRequest;
import co.com.ancas.customer_service.dto.StockTradeResponse;
import co.com.ancas.customer_service.entity.Customer;
import co.com.ancas.customer_service.entity.PortfolioItem;
import co.com.ancas.customer_service.exceptions.ApplicationException;
import co.com.ancas.customer_service.mapper.EntityDTOMapper;
import co.com.ancas.customer_service.repositories.CustomerRepository;
import co.com.ancas.customer_service.repositories.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TradeService {
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public TradeService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public Mono<StockTradeResponse> trade(Long customerId, StockTradeRequest request) {
        return switch (request.action()) {
            case BUY -> this.buyStock(customerId, request);
            case SELL -> this.sellStock(customerId, request);
        };
    }

    private Mono<StockTradeResponse> buyStock(Long customerId, StockTradeRequest request) {
        Mono<Customer> customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationException.customerNotFound(customerId))
                .filter(customer -> customer.getBalance() >= request.totalValue())
                .switchIfEmpty(ApplicationException.insufficientBalance(customerId));
        Mono<PortfolioItem> portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, request.ticker())
                .defaultIfEmpty(EntityDTOMapper.toPortfolioItem(customerId, request.ticker()));
        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(tuple -> this.executeBuy(tuple.getT1(), tuple.getT2(), request));
    }

    private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItemMono, StockTradeRequest request) {
        portfolioItemMono.setQuantity(portfolioItemMono.getQuantity() + request.quantity());
        customer.setBalance(customer.getBalance() - request.totalValue());
        return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItemMono))
                .thenReturn(EntityDTOMapper.toStockTradeResponse(customer, request));
    }

    private Mono<StockTradeResponse> sellStock(Long customerId, StockTradeRequest request) {
        Mono<Customer> customerMono = this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationException.customerNotFound(customerId));
        Mono<PortfolioItem> portfolioItemMono = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, request.ticker())
                .filter(pi -> pi.getQuantity() >= request.quantity())
                .switchIfEmpty(ApplicationException.insufficientShares(customerId));

        return customerMono.zipWhen(customer -> portfolioItemMono)
                .flatMap(tuple -> this.executeSell(tuple.getT1(), tuple.getT2(), request));
    }

    private Mono<StockTradeResponse> executeSell(Customer t1, PortfolioItem t2, StockTradeRequest request) {
        t2.setQuantity(t2.getQuantity() - request.quantity());
        t1.setBalance(t1.getBalance() + request.totalValue());
        return Mono.zip(this.customerRepository.save(t1), this.portfolioItemRepository.save(t2))
                .thenReturn(EntityDTOMapper.toStockTradeResponse(t1, request));
    }
}
