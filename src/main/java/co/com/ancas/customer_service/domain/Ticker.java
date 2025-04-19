package co.com.ancas.customer_service.domain;

public enum Ticker {
    AMAZON,
    APPLE,
    GOOGLE,
    MICROSOFT;

    private Ticker() {
    }

    public static Ticker from(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception var2) {
            return null;
        }
    }
}
