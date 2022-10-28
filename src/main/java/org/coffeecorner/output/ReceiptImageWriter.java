package org.coffeecorner.output;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptImageWriter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
    private static final String FORMAT_NAME = "JPEG";
    private static final String FILENAME_SUFFIX = "_receipt.jpg";

    private String outputFolder;

    public ReceiptImageWriter(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String writeToFile(BufferedImage bufferedImage) throws IOException {
        assert bufferedImage != null;

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName(FORMAT_NAME).next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(1f);

        File file = new File(this.outputFolder + File.separator + LocalDateTime.now().format(DATE_TIME_FORMATTER) + FILENAME_SUFFIX);
        ImageOutputStream outputStream = new FileImageOutputStream(file);
        jpgWriter.setOutput(outputStream);

        IIOImage outputImage = new IIOImage(bufferedImage, null, null);
        jpgWriter.write(null, outputImage, jpgWriteParam);

        jpgWriter.dispose();

        return file.getAbsolutePath();
    }

}
