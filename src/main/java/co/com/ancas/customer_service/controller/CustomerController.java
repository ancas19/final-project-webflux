package co.com.ancas.customer_service.controller;

import co.com.ancas.customer_service.dto.CustomerInformation;
import co.com.ancas.customer_service.dto.StockTradeRequest;
import co.com.ancas.customer_service.dto.StockTradeResponse;
import co.com.ancas.customer_service.service.CustomerService;
import co.com.ancas.customer_service.service.TradeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final TradeService tradeService;

    public CustomerController(CustomerService customerService, TradeService tradeService) {
        this.customerService = customerService;
        this.tradeService = tradeService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Long customerId) {
        return this.customerService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Long customerId, @RequestBody Mono<StockTradeRequest> request) {
        return request.flatMap(trade->this.tradeService.trade(customerId, trade));
    }
}
