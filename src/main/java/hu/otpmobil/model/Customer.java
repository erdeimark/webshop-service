package hu.otpmobil.model;

public record Customer(
        String webshopId,
        String id,
        String name,
        String address
) {
}
