package org.coffeecorner.input;

import org.coffeecorner.model.Customer;

import java.util.List;
import java.util.Objects;

public class UserInput {

    private Customer customer;
    private List<OrderItemInput> orderItemInputs;

    public UserInput(Customer customer, List<OrderItemInput> orderItemInputs) {
        this.customer = customer;
        this.orderItemInputs = orderItemInputs;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItemInput> getOrderItemInputs() {
        return orderItemInputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInput userInput = (UserInput) o;
        return Objects.equals(customer, userInput.customer) && Objects.equals(orderItemInputs, userInput.orderItemInputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, orderItemInputs);
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "customer=" + customer +
                ", orderItemInputs=" + orderItemInputs +
                '}';
    }
}
