package org.coffeecorner;

import org.coffeecorner.input.UserInput;
import org.coffeecorner.input.UserInputReader;
import org.coffeecorner.model.Order;
import org.coffeecorner.output.ReceiptImageWriter;
import org.coffeecorner.repository.ProductRepository;
import org.coffeecorner.service.OrderFactory;
import org.coffeecorner.service.ReceiptImageGenerator;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainControlLoop {

    private ProductRepository productRepository;
    private UserInputReader userInputReader;
    private OrderFactory orderFactory;
    private ReceiptImageGenerator receiptGenerator;
    private ReceiptImageWriter receiptImageWriter;

    public MainControlLoop(ProductRepository productRepository,
                           UserInputReader userInputReader,
                           OrderFactory orderFactory,
                           ReceiptImageGenerator receiptGenerator,
                           ReceiptImageWriter receiptImageWriter) {
        this.productRepository = productRepository;
        this.userInputReader = userInputReader;
        this.orderFactory = orderFactory;
        this.receiptGenerator = receiptGenerator;
        this.receiptImageWriter = receiptImageWriter;
    }

    public void run() {
        while (true) {
            listAllProducts();

            UserInput userInput = userInputReader.readUserInput();

            if (userInput == null) {
                break;
            } else if (userInput.getOrderItemInputs().isEmpty()) {
                System.out.println("[WARN] No user input was enterred.");
            } else {
                Order order = orderFactory.createOrder(userInput);

                System.out.println();
                System.out.println("Order created successfully with order id: " + order.getId());

                System.out.println("Generating receipt image...");
                BufferedImage receiptImage = receiptGenerator.generateImage(order);

                System.out.println("Writing receipt image to file...");
                try {
                    String filepath = receiptImageWriter.writeToFile(receiptImage);
                    System.out.println("Successfully written receipt image to file to: " + filepath);
                } catch (IOException ioe) {
                    System.out.println("[ERROR] Error during writing receipt image to file: " + ioe.getMessage());
                }
            }
        }
    }

    private void listAllProducts() {
        System.out.println();
        System.out.println("List of available products:");

        productRepository.findAll().stream()
                .map(p -> "  " + p.getId() + " - " + p.getName())
                .forEach(System.out::println);
    }
}
