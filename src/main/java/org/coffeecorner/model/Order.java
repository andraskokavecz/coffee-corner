package org.coffeecorner.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Order {

    private Long id;
    private List<OrderItem> items;
    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal total;
    private Customer customer;

    public Order(List<OrderItem> items, BigDecimal subTotal, BigDecimal discount, Customer customer) {
        this.id = OrderSequence.SEQUENCE.getAndIncrement();
        this.items = items;
        this.subTotal = subTotal;
        this.discount = discount;
        this.total = subTotal.subtract(discount);
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(items, order.items) && Objects.equals(subTotal, order.subTotal) && Objects.equals(discount, order.discount) && Objects.equals(total, order.total) && Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items, subTotal, discount, total, customer);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", subTotal=" + subTotal +
                ", discount=" + discount +
                ", total=" + total +
                ", customer=" + customer +
                '}';
    }

    private static class OrderSequence {

        public static final AtomicLong SEQUENCE = new AtomicLong();

    }

}
