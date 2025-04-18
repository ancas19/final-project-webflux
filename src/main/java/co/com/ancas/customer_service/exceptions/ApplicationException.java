package co.com.ancas.customer_service.exceptions;


import co.com.ancas.customer_service.domain.Messages;
import reactor.core.publisher.Mono;

public class ApplicationException {

    public static <T> Mono<T> customerNotFound(Long customerId) {
        return Mono.error(new NotFoundException(Messages.MESSAGE_USER_NOT_FOUND.getMessage().formatted(customerId)));
    }

    public static <T> Mono<T> insufficientBalance(Long customerId) {
        return Mono.error(new BadRequestException(Messages.MESSAGE_INSSUFFICIENT_BALANCE.getMessage().formatted(customerId)));
    }

    public static <T> Mono<T> insufficientShares(Long customerId) {
        return Mono.error(new BadRequestException(Messages.MESSAGE_INSSUFFICIENT_SAHRES.getMessage().formatted(customerId)));
    }
}
