package org.coffeecorner.service;

import org.coffeecorner.input.OrderItemInput;
import org.coffeecorner.input.UserInput;
import org.coffeecorner.model.Customer;
import org.coffeecorner.model.Order;
import org.coffeecorner.model.Product;
import org.coffeecorner.model.ProductType;
import org.coffeecorner.repository.CustomerStampCardRepository;
import org.coffeecorner.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

public class OrderFactoryTest {

    private static final String LARGE_COFFEE = "LARGE COFFEE";
    private static final String BACON_ROLL = "BACON ROLL";
    private static final String FOAMED_MILK = "FOAMED MILK";
    private static final String SPECIAL_ROAST_COFFEE = "SPECIAL ROAST COFFEE";
    private static final String ORANGE_JUICE_0_25_L = "ORANGE JUICE 0.25l";

    private OrderFactory orderFactory;

    private ProductRepository productRepository;
    private CustomerStampCardRepository customerStampCardRepository;

    @BeforeEach
    public void beforeEach() {
        productRepository = new MockProductRepository();
        customerStampCardRepository = new CustomerStampCardRepository();

        orderFactory = new OrderFactory(productRepository, customerStampCardRepository);
    }

    @Test
    public void testCreateOrder() {
        // given
        UserInput userInput = createUserInput("Alice",
                entry(LARGE_COFFEE, 2),
                entry(BACON_ROLL, 3));

        // when
        Order order = orderFactory.createOrder(userInput);

        // then
        assertNotNull(order.getId());
        assertEquals(BigDecimal.valueOf(18.0), order.getSubTotal());
        assertEquals(BigDecimal.valueOf(0.0), order.getDiscount());
        assertEquals(BigDecimal.valueOf(18.0), order.getTotal());
        assertEquals("Alice", order.getCustomer().getFirstName());
    }

    @Test
    public void testCreateOrderWithBeverageAndSnackDiscount() {
        // given
        UserInput userInput = createUserInput("Alice",
                entry(LARGE_COFFEE, 2),
                entry(BACON_ROLL, 3),
                entry(FOAMED_MILK, 1),
                entry(SPECIAL_ROAST_COFFEE, 1)
        );

        // when
        Order order = orderFactory.createOrder(userInput);

        // then
        assertNotNull(order.getId());
        assertEquals(BigDecimal.valueOf(19.5), order.getSubTotal());
        assertEquals(BigDecimal.valueOf(0.5), order.getDiscount());
        assertEquals(BigDecimal.valueOf(19.0), order.getTotal());
        assertEquals("Alice", order.getCustomer().getFirstName());
    }

    @Test
    public void testCreateOrderWithEvery5thBeverageDiscount() {
        // given
        UserInput userInput = createUserInput("Alice",
                entry(LARGE_COFFEE, 6)
        );

        // when
        Order order = orderFactory.createOrder(userInput);

        // then
        assertNotNull(order.getId());
        assertEquals(BigDecimal.valueOf(18.0), order.getSubTotal());
        assertEquals(BigDecimal.valueOf(3.0), order.getDiscount());
        assertEquals(BigDecimal.valueOf(15.0), order.getTotal());
        assertEquals("Alice", order.getCustomer().getFirstName());
    }

    @Test
    public void testCreateOrderWithEvery5thBeverageDiscount_multipleOrderItems() {
        // given
        UserInput userInput = createUserInput("Alice",
                entry(LARGE_COFFEE, 2),
                entry(LARGE_COFFEE, 2),
                entry(LARGE_COFFEE, 2)
        );

        // when
        Order order = orderFactory.createOrder(userInput);

        // then
        assertNotNull(order.getId());
        assertEquals(BigDecimal.valueOf(18.0), order.getSubTotal());
        assertEquals(BigDecimal.valueOf(3.0), order.getDiscount());
        assertEquals(BigDecimal.valueOf(15.0), order.getTotal());
        assertEquals("Alice", order.getCustomer().getFirstName());
    }

    @Test
    public void testCreateOrderWithEvery5thBeverageDiscount_noDiscount() {
        // given
        UserInput userInput = createUserInput("Alice",
                entry(LARGE_COFFEE, 4)
        );

        // when
        Order order = orderFactory.createOrder(userInput);

        // then
        assertNotNull(order.getId());
        assertEquals(BigDecimal.valueOf(12.0), order.getSubTotal());
        assertEquals(BigDecimal.valueOf(0.0), order.getDiscount());
        assertEquals(BigDecimal.valueOf(12.0), order.getTotal());
        assertEquals("Alice", order.getCustomer().getFirstName());
    }

    @Test
    public void testCreateOrderOrderItemsEmptyList() {
        // given
        UserInput userInput = new UserInput(new Customer("Alice"), Collections.emptyList());

        // when
        Order order = orderFactory.createOrder(userInput);

        // then
        assertNotNull(order.getId());
        assertEquals(BigDecimal.valueOf(0.0), order.getSubTotal());
        assertEquals(BigDecimal.valueOf(0.0), order.getDiscount());
        assertEquals(BigDecimal.valueOf(0.0), order.getTotal());
        assertEquals("Alice", order.getCustomer().getFirstName());
    }

    @Test
    public void testCreateOrderOrderItemsNull() {
        // given
        UserInput userInput = new UserInput(new Customer("Alice"), null);

        // when
        assertThrows(AssertionError.class, () -> {
            orderFactory.createOrder(userInput);
        });
    }

    private UserInput createUserInput(String customerFirstName, AbstractMap.SimpleEntry<String, Integer>... items) {
        List<OrderItemInput> orderItemInputs = Arrays.stream(items)
                .map(entry -> new OrderItemInput(productRepository.findByName(entry.getKey()).get().getId(), entry.getValue()))
                .collect(toList());
        return new UserInput(new Customer(customerFirstName), orderItemInputs);
    }

    private AbstractMap.SimpleEntry entry(String productName, Integer quantity) {
        return new AbstractMap.SimpleEntry(productName, quantity);
    }

    private static class MockProductRepository implements ProductRepository {

        private List<Product> mockProducts = List.of(
                new Product(0L, LARGE_COFFEE, BigDecimal.valueOf(3), ProductType.BEVERAGE),
                new Product(1L, BACON_ROLL, BigDecimal.valueOf(4), ProductType.SNACK),
                new Product(2L, FOAMED_MILK, BigDecimal.valueOf(0.5), ProductType.EXTRA),
                new Product(3L, SPECIAL_ROAST_COFFEE, BigDecimal.valueOf(1), ProductType.EXTRA),
                new Product(4L, ORANGE_JUICE_0_25_L, BigDecimal.valueOf(5), ProductType.BEVERAGE)
        );

        @Override
        public List<Product> findAll() {
            return mockProducts;
        }

        @Override
        public Optional<Product> findById(Long id) {
            return mockProducts.stream().filter(p -> Objects.equals(p.getId(), id)).findFirst();
        }

        @Override
        public Optional<Product> findByName(String name) {
            return mockProducts.stream().filter(p -> Objects.equals(p.getName(), name)).findFirst();
        }
    }
}
