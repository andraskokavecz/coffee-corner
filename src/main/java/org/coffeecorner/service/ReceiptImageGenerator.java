package org.coffeecorner.service;

import org.coffeecorner.model.Order;
import org.coffeecorner.model.OrderItem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ReceiptImageGenerator {

    private static final double _8CM_IN_INCHES = 3.1496063;
    private static final double _8CM_IN_PIXELS = _8CM_IN_INCHES * 72;
    private static final String SEPARATOR_LINE = "---------------------------------------";

    public BufferedImage generateImage(Order order) {
        int widthInPixels = (int) _8CM_IN_PIXELS;
        int heightInPixels = (int) _8CM_IN_PIXELS * 2;
        BufferedImage bufferedImage = new BufferedImage(widthInPixels, heightInPixels, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        setBackgroundColor(g2d, widthInPixels, heightInPixels);
        setTextColor(g2d);

        int yCoor = 40;
        yCoor = addHeaderText(g2d, yCoor);
        setMainFont(g2d);
        yCoor = addSeparator(g2d, yCoor);
        yCoor = addCustomerInfo(g2d, yCoor, order);
        yCoor = addOrderItems(g2d, yCoor, order);
        addTotal(order, g2d, yCoor);

        return bufferedImage;
    }

    private void setBackgroundColor(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
    }

    private void setTextColor(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
    }

    private int addHeaderText(Graphics2D g2d, int yCoor) {
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.drawString("Charlene's Coffee Corner", 14, yCoor);
        yCoor += 20;
        return yCoor;
    }

    private void setMainFont(Graphics2D g2d) {
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
    }

    private int addSeparator(Graphics2D g2d, int yCoor) {
        g2d.drawString(SEPARATOR_LINE, 14, yCoor);
        yCoor += 20;
        return yCoor;
    }

    private int addCustomerInfo(Graphics2D g2d, int yCoor, Order order) {
        g2d.drawString("Customer: " + order.getCustomer().getFirstName(), 14, yCoor);
        yCoor += 60;
        return yCoor;
    }

    private int addOrderItems(Graphics2D g2d, int yCoor, Order order) {
        for (OrderItem orderItem : order.getItems()) {
            String quantityLabel = orderItem.getQuantity() + " x ";
            String nameLabel = orderItem.getProduct().getName() + " (" + orderItem.getProduct().getPrice() + ")";
            String priceLabel = orderItem.getItemPrice() + " CHF";

            int spaceLength = SEPARATOR_LINE.length() - quantityLabel.length() - nameLabel.length() - priceLabel.length();
            g2d.drawString(quantityLabel + nameLabel + generateSpace(spaceLength) + priceLabel, 14, yCoor);

            yCoor += 20;
        }
        yCoor += 40;

        return yCoor;
    }

    private void addTotal(Order order, Graphics2D g2d, int yCoor) {
        String totalLabel = "TOTAL:";
        String totalAmountLabel = order.getTotal() + " CHF";

        int spaceLength = SEPARATOR_LINE.length() - totalLabel.length() - totalAmountLabel.length();
        g2d.drawString(totalLabel + generateSpace(spaceLength) + totalAmountLabel, 14, yCoor);
    }

    private String generateSpace(int length) {
        return String.format("%1$" + length + "s", "");
    }

}
