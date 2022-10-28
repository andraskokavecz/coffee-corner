package org.coffeecorner;

import org.coffeecorner.input.ConsoleUserInputReader;
import org.coffeecorner.output.ReceiptImageWriter;
import org.coffeecorner.repository.CustomerStampCardRepository;
import org.coffeecorner.repository.InMemoryProductRepository;
import org.coffeecorner.repository.ProductRepository;
import org.coffeecorner.service.OrderFactory;
import org.coffeecorner.service.ReceiptImageGenerator;

public class CoffeeCornerApp {

    public static void main(String[] args) {
        System.out.println("***********************************");
        System.out.println("*  Welcome to Coffee Corner app!  *");
        System.out.println("***********************************");

        if (args.length != 1) {
            System.out.println("[ERROR] Wrong number of arguments, usage: java org.coffeecorner.App outputFolder");
            System.exit(-1);
        }

        String outputFolder = args[0];

        // simulating app context, bean creation and dependency injection, e.g. as it is done via Spring
        ProductRepository productRepository = new InMemoryProductRepository();
        CustomerStampCardRepository customerStampCardRepository = new CustomerStampCardRepository();
        ConsoleUserInputReader userInputReader = new ConsoleUserInputReader();
        OrderFactory orderFactory = new OrderFactory(productRepository, customerStampCardRepository);
        ReceiptImageGenerator receiptGenerator = new ReceiptImageGenerator();
        ReceiptImageWriter receiptImageWriter = new ReceiptImageWriter(outputFolder);
        MainControlLoop mainControlLoop = new MainControlLoop(productRepository, userInputReader, orderFactory, receiptGenerator, receiptImageWriter);

        mainControlLoop.run();
    }
}
