package hu.otpmobil;

import hu.otpmobil.reader.CsvReader;
import hu.otpmobil.reader.CustomerReader;
import hu.otpmobil.reader.PaymentReader;
import hu.otpmobil.logger.LoggerService;
import hu.otpmobil.service.ReportService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        var csvReader = new CsvReader();

        var customerReader = new CustomerReader(csvReader);
        var paymentReader = new PaymentReader(csvReader);

        var reportService = new ReportService();

        try {
            var customers = customerReader.readCustomers("customer.csv");
            var payments = paymentReader.readPayments("payments.csv");

            reportService.generateReport01(customers, payments, "report01.csv");
            reportService.generateTopCustomersReport(customers, payments, "top.csv");
            reportService.generateReport02(payments, "report02.csv");

            System.out.println("Reports generated successfully.");
        } catch (IOException e) {
            LoggerService.severe("Error reading files or generating reports: " + e.getMessage());
        }
    }
}
