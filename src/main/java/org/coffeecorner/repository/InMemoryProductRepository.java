package org.coffeecorner.repository;

import org.coffeecorner.model.Product;
import org.coffeecorner.model.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {

    private List<Product> inMemoryProducts = List.of(
            // beverage
            new Product(0L, "SMALL COFFEE", BigDecimal.valueOf(2.5), ProductType.BEVERAGE),
            new Product(1L, "MEDIUM COFFEE", BigDecimal.valueOf(3.0), ProductType.BEVERAGE),
            new Product(2L, "LARGE COFFEE", BigDecimal.valueOf(3.5), ProductType.BEVERAGE),
            new Product(3L, "ORANGE JUICE 0.25l", BigDecimal.valueOf(3.95), ProductType.BEVERAGE),
            // snack
            new Product(4L, "BACON ROLL", BigDecimal.valueOf(4.5), ProductType.SNACK),
            // extra
            new Product(5L, "EXTRA MILK", BigDecimal.valueOf(0.3), ProductType.EXTRA),
            new Product(6L, "FOAMED MILK", BigDecimal.valueOf(0.5), ProductType.EXTRA),
            new Product(7L, "SPECIAL ROAST COFFEE", BigDecimal.valueOf(0.9), ProductType.EXTRA)
    );

    @Override
    public List<Product> findAll() {
        return inMemoryProducts;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return inMemoryProducts.stream().filter(p -> Objects.equals(p.getId(), id)).findFirst();
    }

    @Override
    public Optional<Product> findByName(String name) {
        return inMemoryProducts.stream().filter(p -> Objects.equals(p.getName(), name)).findFirst();
    }
}
