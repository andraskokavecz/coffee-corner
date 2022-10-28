package org.coffeecorner.service;

import org.coffeecorner.input.OrderItemInput;
import org.coffeecorner.input.UserInput;
import org.coffeecorner.model.*;
import org.coffeecorner.repository.CustomerStampCardRepository;
import org.coffeecorner.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderFactory {

    private ProductRepository productRepository;
    private CustomerStampCardRepository customerStampCardRepository;

    public OrderFactory(ProductRepository productRepository,
                        CustomerStampCardRepository customerStampCardRepository) {
        this.productRepository = productRepository;
        this.customerStampCardRepository = customerStampCardRepository;
    }

    public Order createOrder(UserInput userInput) {
        assert userInput != null;
        assert userInput.getOrderItemInputs() != null;

        Customer customer = userInput.getCustomer();
        List<OrderItemInput> orderItemInputs = userInput.getOrderItemInputs();

        List<OrderItem> orderItems = mapToOrderItems(orderItemInputs);

        BigDecimal subTotal = orderItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.valueOf(0.0), BigDecimal::add);

        BigDecimal every5thBeverageDiscount = calculateEvery5thBeverageDiscount(customer, orderItems);
        BigDecimal beveragesAndSnackDiscount = calculateBeveragesAndSnackDiscount(orderItems);
        BigDecimal discount = every5thBeverageDiscount.add(beveragesAndSnackDiscount);

        return new Order(orderItems, subTotal, discount, customer);
    }

    private List<OrderItem> mapToOrderItems(List<OrderItemInput> orderItemInputs) {
        return orderItemInputs.stream()
                .map(input -> new AbstractMap.SimpleEntry<>(input, productRepository.findById(input.getProductId())))
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> new OrderItem(entry.getValue().get(), entry.getKey().getQuantity()))
                .collect(toList());
    }

    private BigDecimal calculateEvery5thBeverageDiscount(Customer customer, List<OrderItem> orderItems) {
        BigDecimal every5thBeverageDiscount = BigDecimal.valueOf(0.0);

        List<Product> beveragesInCurrentOrder = orderItems.stream()
                .flatMap(orderItem -> Collections.nCopies(orderItem.getQuantity(), orderItem.getProduct()).stream())
                .filter(product -> product.getType().equals(ProductType.BEVERAGE))
                .collect(toList());

        int numOfBeveragesInStampCard = customerStampCardRepository.initIfNeeded(customer);

        int totalNumOfBeverages = numOfBeveragesInStampCard + beveragesInCurrentOrder.size();

        int firstDiscountedBeverageIndex = 5 - numOfBeveragesInStampCard - 1;

        if (beveragesInCurrentOrder.isEmpty()) {
            // no beverages in current order => do nothing
        } else if (firstDiscountedBeverageIndex > beveragesInCurrentOrder.size()) {
            // too few on current order => just record the new stampcard number
            customerStampCardRepository.addEntry(customer, totalNumOfBeverages);
        } else {
            for (int i = firstDiscountedBeverageIndex; i < beveragesInCurrentOrder.size(); i += 5) {
                Product discountedBeverage = beveragesInCurrentOrder.get(i);
                every5thBeverageDiscount = every5thBeverageDiscount.add(discountedBeverage.getPrice());
            }

            customerStampCardRepository.addEntry(customer, totalNumOfBeverages % 5);
        }

        return every5thBeverageDiscount;
    }

    private BigDecimal calculateBeveragesAndSnackDiscount(List<OrderItem> orderItems) {
        boolean hasBothBeverageAndSnack = orderItems.stream()
                .map(item -> item.getProduct().getType())
                .filter(productType -> productType.equals(ProductType.BEVERAGE) || productType.equals(ProductType.SNACK))
                .distinct()
                .limit(2)
                .count() == 2;

        if (hasBothBeverageAndSnack) {
            List<Product> extras = orderItems.stream()
                    .map(OrderItem::getProduct)
                    .filter(product -> product.getType().equals(ProductType.EXTRA))
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(toList());

            if (!extras.isEmpty()) {
                Product cheapestExtra = extras.get(0);
                return cheapestExtra.getPrice();
            }
        }

        return BigDecimal.valueOf(0.0);
    }
}
