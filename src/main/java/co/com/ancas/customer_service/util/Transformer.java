package co.com.ancas.customer_service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

public class Transformer {
    private static final Logger log= LoggerFactory.getLogger(Transformer.class);

    public static <T> UnaryOperator<Mono<T>> monoLogger(String name) {
        return (mono) -> mono.doFirst(() -> log.info("received {}", name)).doOnCancel(() -> log.info("cancelled {}", name)).doOnSuccess((s) -> log.info("completed: {}", name));
    }

    public static <T> UnaryOperator<Flux<T>> fluxLogger(String name) {
        return (mono) -> mono.doFirst(() -> log.info("received {}", name)).doOnCancel(() -> log.info("cancelled {}", name)).doOnComplete(() -> log.info("completed: {}", name));
    }
}
