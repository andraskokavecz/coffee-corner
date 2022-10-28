package org.coffeecorner.model;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {

    private Product product;
    private Integer quantity;
    private BigDecimal itemPrice;

    public OrderItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.itemPrice = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItem orderItem = (OrderItem) o;
        return Objects.equals(product, orderItem.product) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(itemPrice, orderItem.itemPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, itemPrice);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                '}';
    }

}
