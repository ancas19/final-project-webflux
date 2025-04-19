package co.com.ancas.customer_service;

import co.com.ancas.customer_service.domain.Ticker;
import co.com.ancas.customer_service.domain.TradeAction;
import co.com.ancas.customer_service.dto.StockTradeRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);
    @Autowired
    private WebTestClient webTestClient;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("server.port", () -> "9090");
        registry.add("postgres.host", () -> "localhost");
        registry.add("postgres.port", () -> "5432");
        registry.add("postgres.database", () -> "webflux");
        registry.add("postgres.user", () -> "ancas19");
        registry.add("postgres.pass", () -> "P0stgr3s#P4ssw0rd");
        registry.add("logging.level.root", () -> "INFO");

    }

    @Test
    void customerInformation() {
        this.getCustomerInformation(1L, HttpStatus.OK)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.balance").isEqualTo(10000);
    }

    @Test
    void customerInformationNotFound() {
        this.getCustomerInformation(100L, HttpStatus.NOT_FOUND);
    }

    @Test
    void trade() {
        StockTradeRequest stockTradeRequest = new StockTradeRequest(Ticker.AMAZON, 10, 40, TradeAction.BUY);
        this.treade(1L, stockTradeRequest, HttpStatus.OK)
                .jsonPath("$.totalPrice").isEqualTo(400)
                .jsonPath("$.balance").isEqualTo(9600);

		stockTradeRequest = new StockTradeRequest(Ticker.AMAZON, 10, 40, TradeAction.SELL);
		this.treade(1L, stockTradeRequest, HttpStatus.OK)
				.jsonPath("$.totalPrice").isEqualTo(400)
				.jsonPath("$.balance").isEqualTo(10000);
    }

	@Test
	void insufficientBalance() {
		StockTradeRequest stockTradeRequest = new StockTradeRequest(Ticker.AMAZON, 10, 10000, TradeAction.BUY);
		this.treade(1L, stockTradeRequest, HttpStatus.BAD_REQUEST);
	}

	@Test
	void insufficientShares() {
		StockTradeRequest stockTradeRequest = new StockTradeRequest(Ticker.AMAZON, 10, 40, TradeAction.SELL);
		this.treade(1L, stockTradeRequest, HttpStatus.BAD_REQUEST);
	}

    private WebTestClient.BodyContentSpec treade(Long id, StockTradeRequest stockTradeRequest, HttpStatus status) {
        return this.webTestClient.post()
                .uri("/customers/{id}/trade", id)
                .bodyValue(stockTradeRequest)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(response -> log.info("{}", new String(Objects.requireNonNull(response.getResponseBody()))));
    }

	private WebTestClient.BodyContentSpec getCustomerInformation(Long id, HttpStatus status) {
		return this.webTestClient.get()
				.uri("/customers/{id}", id)
				.exchange()
				.expectStatus().isEqualTo(status)
				.expectBody()
				.consumeWith(response -> log.info("{}", new String(Objects.requireNonNull(response.getResponseBody()))));
	}
}
