package co.com.ancas.customer_service.service;

import co.com.ancas.customer_service.domain.Ticker;
import co.com.ancas.customer_service.dto.PriceUpdate;
import co.com.ancas.customer_service.dto.StockPriceResponse;
import co.com.ancas.customer_service.exceptions.BadRequestException;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TickersService {
    private final Map<Ticker, Integer> map = new HashMap<>();
    private final Sinks.Many<PriceUpdate> sink = Sinks.many().replay().limit(1);

    @PostConstruct
    public void init() {
        this.map.put(Ticker.APPLE, 100);
        this.map.put(Ticker.GOOGLE, 100);
        this.map.put(Ticker.AMAZON, 100);
        this.map.put(Ticker.MICROSOFT, 100);
    }


    public Mono<StockPriceResponse> getPrice(String ticker) {
        return Mono.justOrEmpty((Integer) this.map.get(Ticker.from(ticker)))
                .map(price -> new StockPriceResponse(Ticker.from(ticker), price))
                .switchIfEmpty(Mono.error(new BadRequestException("Ticker not found")));
    }

    public Flux<PriceUpdate> getPriceUpdates() {
        return this.sink.asFlux();
    }

    @Scheduled(fixedRate = 1000L)
    public void updatePrices() {
        this.map.keySet().forEach((k) -> {
           this.map.computeIfPresent(k,(k1,v)->Math.max(1,v+this.randomValue()));
           this.sink.tryEmitNext(new PriceUpdate(k.toString(), this.map.get(k), LocalDateTime.now()));
        });
    }

    private Integer randomValue() {
        return ThreadLocalRandom.current().nextInt(-3, 4);
    }
}
