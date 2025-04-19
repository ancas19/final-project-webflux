package co.com.ancas.customer_service.controller;

import co.com.ancas.customer_service.dto.PriceUpdate;
import co.com.ancas.customer_service.dto.StockPriceResponse;
import co.com.ancas.customer_service.service.TickersService;
import co.com.ancas.customer_service.util.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@RestController
@RequestMapping("/stock-prices")
public class StockPricesController {
    private static final Logger log = LoggerFactory.getLogger(StockPricesController.class);
    private final TickersService tickersService;

    public StockPricesController(TickersService tickersService) {
        this.tickersService = tickersService;
    }

    @GetMapping("/stock/{ticker}")
    public Mono<StockPriceResponse> getStocckPrice(@PathVariable("ticker") String ticker) {
        String path = "/stock/%s".formatted(ticker);
        return this.tickersService.getPrice(ticker)
                .transform(Transformer.monoLogger(path));
    }

    @GetMapping(value = "/stock/price-stream", produces = {APPLICATION_NDJSON_VALUE})
    public Flux<PriceUpdate> getProceUpdates() {
        String path = "/stock/price-stream";
        return this.tickersService.getPriceUpdates()
                .transform(Transformer.fluxLogger(path));

    }

}
