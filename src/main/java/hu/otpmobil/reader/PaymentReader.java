package hu.otpmobil.reader;

import hu.otpmobil.model.Payment;
import hu.otpmobil.model.PaymentMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentReader {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final CsvReader csvReader;

    public PaymentReader(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public List<Payment> readPayments(String fileName) throws IOException {
        return csvReader.readCsv(fileName, this::parsePayment, this::validatePayment);
    }

    private Payment parsePayment(String line) {
        var parts = csvReader.splitLine(line);

        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid number of fields for payment");
        }

        return new Payment(
                parts[0],
                parts[1],
                PaymentMethod.valueOf(parts[2].toUpperCase()),
                Integer.parseInt(parts[3]),
                parts[4].isEmpty() ? null : parts[4],
                parts[5].isEmpty() ? null : parts[5],
                LocalDate.parse(parts[6], DATE_FORMATTER)
        );
    }

    private boolean validatePayment(Payment payment) {
        var isValid = !payment.webshopId().isEmpty() &&
                !payment.customerId().isEmpty() &&
                payment.amount() > 0 &&
                payment.paymentDate() != null;

        if (payment.paymentMethod() == PaymentMethod.TRANSFER) {
            isValid = isValid && payment.bankAccountNumber() != null && !payment.bankAccountNumber().isEmpty();
        } else if (payment.paymentMethod() == PaymentMethod.CARD) {
            isValid = isValid && payment.cardNumber() != null && !payment.cardNumber().isEmpty();
        }

        return isValid;
    }
}
