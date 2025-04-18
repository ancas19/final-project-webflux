package co.com.ancas.customer_service.domain;

public enum Messages {

    MESSAGE_USER_NOT_FOUND("Custimer [id=%s] not found."),
    MESSAGE_INSSUFFICIENT_BALANCE("Customer [id=%s] does not have enough funds to complete the transaction."),
    MESSAGE_INSSUFFICIENT_SAHRES("Customer [id=%s] does not have enough shares to complete the transaction."),;

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
