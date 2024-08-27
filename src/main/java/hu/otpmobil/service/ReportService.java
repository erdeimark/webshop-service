package hu.otpmobil.service;

import hu.otpmobil.model.Customer;
import hu.otpmobil.model.Payment;
import hu.otpmobil.model.PaymentMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    private static final String SEPARATOR = ";";

    public void generateReport01(List<Customer> customers, List<Payment> payments, String fileName) throws IOException {
        var customerTotals = calculateCustomerTotals(customers, payments);

        var lines = customerTotals.entrySet().stream()
                .map(entry -> String.join(SEPARATOR,
                        entry.getKey().name(),
                        entry.getKey().address(),
                        String.valueOf(entry.getValue())))
                .collect(Collectors.toList());

        lines.addFirst("name;address;totalAmount");

        Files.write(Paths.get(fileName), lines);
    }

    public void generateTopCustomersReport(List<Customer> customers, List<Payment> payments, String fileName) throws IOException {
        var customerTotals = calculateCustomerTotals(customers, payments);

        var lines = customerTotals.entrySet().stream()
                .sorted(Map.Entry.<Customer, Integer>comparingByValue().reversed())
                .limit(2)
                .map(entry -> String.join(SEPARATOR,
                        entry.getKey().webshopId(),
                        entry.getKey().id(),
                        entry.getKey().name(),
                        entry.getKey().address(),
                        String.valueOf(entry.getValue())))
                .collect(Collectors.toList());

        lines.addFirst("webshopId;customerId;name;address;totalAmount");

        Files.write(Paths.get(fileName), lines);
    }

    public void generateReport02(List<Payment> payments, String fileName) throws IOException {
        var webshopTotals = payments.stream()
                .collect(Collectors.groupingBy(Payment::webshopId,
                        Collectors.groupingBy(Payment::paymentMethod,
                                Collectors.summingInt(Payment::amount))));

        var lines = webshopTotals.entrySet().stream()
                .map(entry -> String.join(SEPARATOR,
                        entry.getKey(),
                        String.valueOf(entry.getValue().getOrDefault(PaymentMethod.CARD, 0)),
                        String.valueOf(entry.getValue().getOrDefault(PaymentMethod.TRANSFER, 0))))
                .collect(Collectors.toList());

        lines.addFirst("webshopId;cardPaymentsSum;transferPaymentsSum");

        Files.write(Paths.get(fileName), lines);
    }

    private Map<Customer, Integer> calculateCustomerTotals(List<Customer> customers, List<Payment> payments) {
        var customerTotals = payments.stream()
                .collect(Collectors.groupingBy(Payment::customerId,
                        Collectors.summingInt(Payment::amount)));

        return customers.stream()
                .collect(Collectors.toMap(
                        customer -> customer,
                        customer -> customerTotals.getOrDefault(customer.id(), 0)));
    }
}
