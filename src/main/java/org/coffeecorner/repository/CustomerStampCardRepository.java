package org.coffeecorner.repository;

import org.coffeecorner.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerStampCardRepository {

    private Map<Customer, Integer> customerStampCard = new HashMap<>();

    public int initIfNeeded(Customer customer) {
        return customerStampCard.computeIfAbsent(customer, key -> 0);
    }

    public void addEntry(Customer customer, Integer quantity) {
        customerStampCard.put(customer, quantity);
    }
}
