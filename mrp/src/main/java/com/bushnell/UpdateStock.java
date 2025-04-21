package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class UpdateStock {

    private static final String WHITE_HASHCODE = "#FFFFFF";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEIGHT2 = 20;
    private static final int COMBOBOX_HEIGHT = 35;
    private static final int POSITION50 = 50;
    private static final int POSITION20 = 20;
    private static final int POSITION10 = 10;
    private static final int POSITION0 = 0;
    private static final int TEXT_FONT_SIZE = 20;
    private static final int SMALLER_FONT_SIZE = 15;
    private static final int VALUE_WIDTH = 300;


    private UpdateStock() {
        // Utility class - no instantiation allowed
    }

    public static JPanel makeGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(Color.decode(WHITE_HASHCODE));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(Box.createHorizontalGlue());
        JLabel titleLabel = new JLabel("Update Stock");
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, TITLE_FONT_SIZE));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());
        
        panel.add(Box.createVerticalStrut(HEIGHT2));
        panel.add(titlePanel);
        panel.add(Box.createVerticalStrut(POSITION20));

        // Main content panel
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS)); 
        comboBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        comboBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(POSITION0, POSITION0, POSITION0, 235));

        // SKU Selection
        JPanel skuBoxPanel = new JPanel();
        skuBoxPanel.setLayout(new BoxLayout(skuBoxPanel, BoxLayout.X_AXIS)); 
        skuBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); 
        skuBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
               
        JLabel skuLabel = new JLabel("sku:");
        skuLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));
        skuLabel.setBorder(BorderFactory.createEmptyBorder(POSITION0, POSITION0, POSITION0, POSITION0));
        String[] skuArray = Database.getSkuList();
        JComboBox<String> skuList = new JComboBox<>(skuArray);
        skuList.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        skuList.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT)); // Add this
        skuList.setMinimumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        skuList.setAlignmentX(Component.RIGHT_ALIGNMENT);
        skuBoxPanel.add(skuLabel);
        skuBoxPanel.add(Box.createRigidArea(new Dimension(25, 0))); 
        skuBoxPanel.add(skuList);

        // Get initial part data
        String sku = (String) skuList.getSelectedItem();
        Part initialPart = Database.getSkuData(sku);

        // Description Panel
        JPanel descriptionBoxPanel = new JPanel();
        descriptionBoxPanel.setLayout(new BoxLayout(descriptionBoxPanel, BoxLayout.X_AXIS)); 
        descriptionBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        descriptionBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
           
        JLabel descFieldLabel = new JLabel("description:");
        descFieldLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));

        JLabel descriptionLabel = new JLabel(initialPart.description);
        descriptionLabel.setFont(new Font("Sans-Serif", Font.PLAIN, SMALLER_FONT_SIZE));
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(POSITION0, 5, POSITION0, POSITION0));
        descriptionLabel.setMinimumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        descriptionLabel.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        descriptionLabel.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        descriptionLabel.setHorizontalAlignment(JLabel.LEFT);
        
        descriptionBoxPanel.add(descFieldLabel);
        descriptionBoxPanel.add(Box.createRigidArea(new Dimension(25, 0))); // 5px width
        descriptionBoxPanel.add(descriptionLabel);

        // Price Panel
        JPanel priceBoxPanel = new JPanel();
        priceBoxPanel.setLayout(new BoxLayout(priceBoxPanel, BoxLayout.X_AXIS)); 
        priceBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        priceBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
                
        JLabel priceFieldLabel = new JLabel("price:");
        priceFieldLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));

        JLabel dollarLabel = new JLabel("$");
        dollarLabel.setFont(new Font("Sans-Serif", Font.PLAIN, SMALLER_FONT_SIZE));

        JTextField priceField = new JTextField(String.format("%.2f", initialPart.price));
        priceField.setFont(new Font("Sans-Serif", Font.PLAIN, SMALLER_FONT_SIZE));
        priceField.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        priceField.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        priceField.setHorizontalAlignment(JLabel.LEFT);
        
        priceBoxPanel.add(priceFieldLabel);
        priceBoxPanel.add(Box.createRigidArea(new Dimension(14, 0))); // 5px width
        priceBoxPanel.add(dollarLabel);
        priceBoxPanel.add(priceField);

        priceField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String sku = (String) skuList.getSelectedItem();
                try {
                    double newPrice = Double.parseDouble(priceField.getText());
                    boolean success = Database.updatePrice(sku, newPrice);
                    if (!success) {
                        JOptionPane.showMessageDialog(priceField, 
                            "Failed to update price", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        // Reset to current value
                        Part part = Database.getSkuData(sku);
                        priceField.setText(String.format("%.2f", part.price));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(priceField, 
                        "Please enter a valid price", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    // Reset to current value
                    Part part = Database.getSkuData(sku);
                    priceField.setText(String.format("%.2f", part.price));
                }
            }
        });

            // Stock Panel
            JPanel stockBoxPanel = new JPanel();
            stockBoxPanel.setLayout(new BoxLayout(stockBoxPanel, BoxLayout.X_AXIS)); 
            stockBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); 
            stockBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
        
            JLabel stockFieldLabel = new JLabel("stock:");
            stockFieldLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));


            JTextField stockField = new JTextField(Integer.toString(initialPart.stock));
            stockField.setFont(new Font("Sans-Serif", Font.PLAIN, SMALLER_FONT_SIZE));
            stockField.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
            stockField.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
            stockField.setHorizontalAlignment(JLabel.LEFT);
            
            stockBoxPanel.add(stockFieldLabel);
            stockBoxPanel.add(Box.createRigidArea(new Dimension(25, 0))); // 5px width
            stockBoxPanel.add(stockField);


            stockField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String sku = (String) skuList.getSelectedItem();
                    try {
                        int newStock = Integer.parseInt(stockField.getText());
                        boolean success = Database.updateStock(sku, newStock);
                        if (!success) {
                            JOptionPane.showMessageDialog(stockField, 
                                "Failed to update stock", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                            // Reset to current value
                            Part part = Database.getSkuData(sku);
                            stockField.setText(Integer.toString(part.stock));
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(stockField, 
                            "Please enter a valid stock quantity", 
                            "Input Error", 
                            JOptionPane.ERROR_MESSAGE);
                        // Reset to current value
                        Part part = Database.getSkuData(sku);
                        stockField.setText(Integer.toString(part.stock));
                    }
                }
            });


        // SKU Selection Listener
      
        ActionListener skuListListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sku = (String) skuList.getSelectedItem();
                Part part = Database.getSkuData(sku);
                descriptionLabel.setText(part.description);
                priceField.setText(String.format("%.2f", part.price));
                stockField.setText(Integer.toString(part.stock));
            }
        };
       

        skuList.addActionListener(skuListListener); 

        // Add components to panels
        comboBoxPanel.add(skuBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(POSITION10));
        comboBoxPanel.add(descriptionBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(POSITION10));
        comboBoxPanel.add(priceBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(POSITION10));
        comboBoxPanel.add(stockBoxPanel);

        panel.add(Box.createVerticalStrut(POSITION50));
        panel.add(comboBoxPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}