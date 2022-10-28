package org.coffeecorner.input;

import java.util.Objects;

public class OrderItemInput {

    private Long productId;
    private Integer quantity;

    public OrderItemInput(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItemInput orderItemInput = (OrderItemInput) o;
        return Objects.equals(productId, orderItemInput.productId) && Objects.equals(quantity, orderItemInput.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "OrderItemInput{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
