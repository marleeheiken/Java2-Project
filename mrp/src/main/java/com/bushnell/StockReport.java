package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public final class StockReport {

    private static final String WHITE_HASHCODE = "#FFFFFF";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final int SCROLLWIDTH = 1000;
    private static final int SCROLLHEIGHT = 620;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEIGHT2 = 20;

    // Private constructor to prevent instantiation
    private StockReport() {
        // Utility class - no instantiation allowed
    }
    
    public static void refresh(JPanel panel, String dbPath) {
        // Logic to update the existing panel
    }

    public static JPanel makeGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(Color.decode(WHITE_HASHCODE));

        // Create and configure the title label
        JLabel titleLabel = new JLabel("Stock Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setPreferredSize(new Dimension(SCROLLWIDTH, SCROLLHEIGHT));
        reportPanel.setMaximumSize(new Dimension(SCROLLWIDTH, SCROLLHEIGHT));
        reportPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reportPanel.setBackground(Color.decode(WHITE_HASHCODE));

        // create text title for sku list
        Box textTitleBox = Box.createHorizontalBox();
        textTitleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        String textStr = String.format("%85s %17s %13s        %s\n", "SKU", "Price", "Stock", "Description");
        JTextArea textTitle = new JTextArea(1,60);
        textTitle.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        textTitle.setEditable(false);
        textTitle.append(textStr);
        textTitle.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        textTitleBox.add(textTitle);
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        reportPanel.add(textTitleBox);

        // create text pane
        Box textBox = Box.createHorizontalBox();
        textBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textBox.setAlignmentY(Component.TOP_ALIGNMENT);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        JTextArea stockText = new JTextArea(50, 60);  // 50 rows and 60 columns
        stockText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        stockText.setEditable(false);
        JScrollPane scroll = new JScrollPane(stockText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        List<Part> allSkuList = Database.getAllSkuData();
        for( Part part : allSkuList) {
            String newPart = String.format("%44s %10s %6d      %s\n", 
            part.sku, String.format("$%.2f", part.price), part.stock, part.description);
            stockText.append(newPart);
        }
        textBox.add(scroll);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        reportPanel.add(textBox);

        // Add vertical space and title to panel
        panel.add(Box.createVerticalStrut(HEIGHT2)); // Space from top
        panel.add(titleLabel);
        panel.add(Box.createVerticalGlue()); // Pushes content to top
        panel.add(reportPanel);

        return panel;
    }
}
