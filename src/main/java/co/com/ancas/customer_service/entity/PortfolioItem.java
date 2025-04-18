package co.com.ancas.customer_service.entity;

import co.com.ancas.customer_service.domain.Ticker;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("portfolio_item")
public class PortfolioItem {
    @Id
    private Long id;
    private Long customerId;
    private Ticker ticker;
    private Integer quantity;

    public PortfolioItem(Long id, Long customerId, Ticker ticker, Integer quantity) {
        this.id = id;
        this.customerId = customerId;
        this.ticker = ticker;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
