package org.coffeecorner.output;

import org.coffeecorner.testutils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiptImageWriterTest {

    private ReceiptImageWriter receiptImageWriter;

    @BeforeEach
    public void beforeEach() throws IOException {
        String tempOutputFolder = Files.createDirectories(Path.of(System.getProperty("java.io.tmpdir") + "coffeeCornerTmpDir")).toFile().getAbsolutePath();
        TestUtils.cleanFolder(Path.of(tempOutputFolder).toFile());

        receiptImageWriter = new ReceiptImageWriter(tempOutputFolder);
    }

    @Test
    public void testWriteToFile() throws IOException {
        // given
        BufferedImage bufferedImage = givenWeHaveABufferedImage();

        // when
        String actualFilePath = receiptImageWriter.writeToFile(bufferedImage);

        // then
        assertTrue(actualFilePath.startsWith(System.getProperty("java.io.tmpdir") + "coffeeCornerTmpDir"));
        assertTrue(actualFilePath.endsWith("_receipt.jpg"));
    }

    @Test
    public void testWriteToFileImageNull() {
        // given
        BufferedImage bufferedImage = null;

        // when
        assertThrows(AssertionError.class, () -> {
            receiptImageWriter.writeToFile(bufferedImage);
        });

        // then
    }

    private BufferedImage givenWeHaveABufferedImage() {
        BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.drawString("Lorem ipsum...", 14, 14);
        return bufferedImage;
    }
}
