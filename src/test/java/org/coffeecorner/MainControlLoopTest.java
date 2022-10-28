package org.coffeecorner;

import org.coffeecorner.input.ConsoleUserInputReader;
import org.coffeecorner.input.UserInputReader;
import org.coffeecorner.output.ReceiptImageWriter;
import org.coffeecorner.repository.CustomerStampCardRepository;
import org.coffeecorner.repository.InMemoryProductRepository;
import org.coffeecorner.repository.ProductRepository;
import org.coffeecorner.service.OrderFactory;
import org.coffeecorner.service.ReceiptImageGenerator;
import org.coffeecorner.testutils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainControlLoopTest {

    private static final String COFFEE_CORNER_TMP_DIR = "coffeeCornerTmpDir";

    private MainControlLoop mainControlLoop;

    private String tempOutputFolder;

    @BeforeEach
    public void beforeEach() throws IOException {
        ensureAndCleanTempOutputFolder();
    }

    @Test
    public void testMainLoop() throws IOException {
        // given
        givenControlLoopConfiguredWithMockedInput(List.of("Alice", "2,1", "4,2", "!C", "!E"));

        // when
        mainControlLoop.run();

        // then
        assertEquals(1, Files.list(Path.of(tempOutputFolder)).count());
    }

    @Test
    public void testMainLoop_twoOrders() throws IOException {
        // given
        givenControlLoopConfiguredWithMockedInput(List.of("Alice", "2,1", "4,2", "!C", "Bob", "0,1", "!C", "!E"));

        // when
        mainControlLoop.run();

        // then
        assertEquals(2, Files.list(Path.of(tempOutputFolder)).count());
    }

    @Test
    public void testMainLoop_createWithEmptyOrderItemList() throws IOException {
        // given
        givenControlLoopConfiguredWithMockedInput(List.of("Alice", "!C", "!E"));

        // when
        mainControlLoop.run();

        // then
        assertEquals(0, Files.list(Path.of(tempOutputFolder)).count());
    }

    @Test
    public void testMainLoop_exitAfterCustomerName() throws IOException {
        // given
        givenControlLoopConfiguredWithMockedInput(List.of("Alice", "!E"));

        // when
        mainControlLoop.run();

        // then
        assertEquals(0, Files.list(Path.of(tempOutputFolder)).count());
    }

    @Test
    public void testMainLoop_exitImmediately() throws IOException {
        // given
        givenControlLoopConfiguredWithMockedInput(List.of("!E"));

        // when
        mainControlLoop.run();

        // then
        assertEquals(0, Files.list(Path.of(tempOutputFolder)).count());
    }

    private void ensureAndCleanTempOutputFolder() throws IOException {
        tempOutputFolder = Files.createDirectories(Path.of(System.getProperty("java.io.tmpdir") + COFFEE_CORNER_TMP_DIR)).toFile().getAbsolutePath();
        TestUtils.cleanFolder(Path.of(tempOutputFolder).toFile());
    }

    private void givenControlLoopConfiguredWithMockedInput(List<String> mockUserInput) {
        mockUserInput(mockUserInput);

        ProductRepository productRepository = new InMemoryProductRepository();
        CustomerStampCardRepository customerStampCardRepository = new CustomerStampCardRepository();
        UserInputReader userInputReader = new ConsoleUserInputReader();
        OrderFactory orderFactory = new OrderFactory(productRepository, customerStampCardRepository);
        ReceiptImageGenerator receiptGenerator = new ReceiptImageGenerator();
        ReceiptImageWriter receiptImageWriter = new ReceiptImageWriter(tempOutputFolder);

        mainControlLoop = new MainControlLoop(productRepository, userInputReader, orderFactory, receiptGenerator, receiptImageWriter);
    }

    private void mockUserInput(List<String> mockUserInput) {
        String userInput = mockUserInput.stream()
                .map(input -> input + System.lineSeparator())
                .collect(Collectors.joining());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inputStream);
    }
}
