package co.com.ancas.customer_service.service;

import co.com.ancas.customer_service.dto.CustomerInformation;
import co.com.ancas.customer_service.entity.Customer;
import co.com.ancas.customer_service.entity.PortfolioItem;
import co.com.ancas.customer_service.exceptions.ApplicationException;
import co.com.ancas.customer_service.mapper.EntityDTOMapper;
import co.com.ancas.customer_service.repositories.CustomerRepository;
import co.com.ancas.customer_service.repositories.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public CustomerService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public Mono<CustomerInformation> getCustomerInformation(Long id){
        return this.customerRepository.findById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(this::getCustomerInformation)
                .log();

    }

    private Mono<CustomerInformation> getCustomerInformation(Customer customer) {
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .map(items->EntityDTOMapper.toCustomerInformation(customer, items));
    }
}
