package org.coffeecorner.input;

import org.coffeecorner.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUserInputReader implements UserInputReader {

    private Scanner scanner;

    public ConsoleUserInputReader() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public UserInput readUserInput() {

        System.out.println();
        System.out.println("Please enter the first name of the customer for bonus program:");
        System.out.print("User input > ");

        if (!scanner.hasNextLine()) {
            return null;
        }
        String customerInput = scanner.nextLine();

        if (customerInput.equals("!E")) {
            return null;
        }
        Customer customer = new Customer(customerInput);

        List<OrderItemInput> orderItemInputs = new ArrayList<>();

        System.out.println();
        System.out.println("Please enter order items in the format of: ID,QUANTITY");
        System.out.println("- or type '!C' to create the current order");
        System.out.println("- or type '!E' to exit the application");
        System.out.println();

        while (true) {
            System.out.print("User input > ");

            if (!scanner.hasNextLine()) {
                return null;
            }
            String nextInput = scanner.nextLine();

            if (nextInput.equals("!E")) {
                return null;
            } else if (nextInput.equals("!C")) {
                return new UserInput(customer, orderItemInputs);
            } else {
                String[] inputParts = nextInput.split(",");

                if (inputParts.length != 2) {
                    System.out.println("[ERROR] Wrong format for input: " + nextInput);
                    continue;
                }

                Long productId;
                try {
                    productId = Long.parseLong(inputParts[0]);
                } catch (NumberFormatException nfe) {
                    System.out.println("[ERROR] Could not parse user input: productId");
                    continue;
                }

                Integer quantity;
                try {
                    quantity = Integer.parseInt(inputParts[1]);
                } catch (NumberFormatException nfe) {
                    System.out.println("[ERROR] Could not parse user input: quantity");
                    continue;
                }

                if (quantity < 1) {
                    System.out.println("[ERROR] Quantity has to be greater than 0.");
                    continue;
                }

                orderItemInputs.add(new OrderItemInput(productId, quantity));
            }
        }
    }
}
