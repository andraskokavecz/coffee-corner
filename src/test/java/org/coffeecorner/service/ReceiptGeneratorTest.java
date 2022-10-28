package org.coffeecorner.service;

import org.coffeecorner.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiptGeneratorTest {

    private ReceiptImageGenerator receiptImageGenerator;

    @BeforeEach
    public void beforeEach() {
        receiptImageGenerator = new ReceiptImageGenerator();
    }

    @Test
    public void testGenerateImage() {
        // given
        Product coffeeLarge = new Product(0L, "LARGE COFFEE", BigDecimal.valueOf(3.5), ProductType.BEVERAGE);
        Product baconRoll = new Product(1L, "BACON ROLL", BigDecimal.valueOf(4.5), ProductType.SNACK);
        Product foamedMilk = new Product(2L, "FOAMED MILK", BigDecimal.valueOf(0.5), ProductType.EXTRA);

        List<OrderItem> orderItems = List.of(
                new OrderItem(coffeeLarge, 2),
                new OrderItem(baconRoll, 3),
                new OrderItem(foamedMilk, 1));

        Order order = new Order(orderItems, BigDecimal.valueOf(21.0), BigDecimal.valueOf(0.0), new Customer("Alice"));

        // when
        BufferedImage bufferedImage = receiptImageGenerator.generateImage(order);

        // then
        assertNotNull(bufferedImage);
        assertTrue(bufferedImage.getData().getDataBuffer().getSize() > 0);
    }

}
