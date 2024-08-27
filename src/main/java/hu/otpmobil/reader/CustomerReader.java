package hu.otpmobil.reader;

import hu.otpmobil.model.Customer;

import java.io.IOException;
import java.util.List;

public class CustomerReader {
    private final CsvReader csvReader;

    public CustomerReader(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public List<Customer> readCustomers(String fileName) throws IOException {
        return csvReader.readCsv(fileName, this::parseCustomer, this::validateCustomer);
    }

    private Customer parseCustomer(String line) {
        var parts = csvReader.splitLine(line);

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid number of fields for customer");
        }
        return new Customer(parts[0], parts[1], parts[2], parts[3]);
    }

    private boolean validateCustomer(Customer customer) {
        return !customer.webshopId().isEmpty() &&
                !customer.id().isEmpty() &&
                !customer.name().isEmpty() &&
                !customer.address().isEmpty();
    }
}
