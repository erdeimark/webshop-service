package hu.otpmobil.model;

import java.time.LocalDate;

public record Payment(
        String webshopId,
        String customerId,
        PaymentMethod paymentMethod,
        Integer amount,
        String bankAccountNumber,
        String cardNumber,
        LocalDate paymentDate
) {
}
