package co.com.ancas.customer_service.repositories;

import co.com.ancas.customer_service.domain.Ticker;
import co.com.ancas.customer_service.entity.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem,Long> {
    Flux<PortfolioItem> findAllByCustomerId(Long customerId);
    Mono<PortfolioItem> findByCustomerIdAndTicker(Long customerId, Ticker ticker);
}
