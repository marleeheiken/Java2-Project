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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public final class Bundle {
    public static final Color VS_GREEN = Color.decode("#00af74");
    private static final String WHITE_HASHCODE = "#FFFFFF";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEIGHT2 = 20;
    private static final int COMBOBOX_HEIGHT = 35;
    private static final int POSITION20 = 20;
    private static final int POSITION0 = 0;
    private static final int TEXT_FONT_SIZE = 20;
    private static final int SMALLER_FONT_SIZE = 15;
    private static final int VALUE_WIDTH = 300;
    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 51;

    private static final int BUTTON_FONT_SIZE = 14;


    private Bundle() {
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
        JLabel titleLabel = new JLabel("Bundle");
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
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(POSITION0, POSITION0, POSITION0, 206));

        // SKU Selection
        JPanel skuBoxPanel = new JPanel();
        skuBoxPanel.setLayout(new BoxLayout(skuBoxPanel, BoxLayout.X_AXIS)); 
        skuBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); 
        skuBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
               
        JLabel skuLabel = new JLabel("sku:");
        skuLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));
        skuLabel.setBorder(BorderFactory.createEmptyBorder(POSITION0, POSITION0, POSITION0, POSITION0));
        String[] skuArray = Database.getSkuSubList();
        JComboBox<String> skuList = new JComboBox<>(skuArray);
        // In your Bundle panel's SKU combo box setup:
        skuList.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        skuList.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT)); // ADD THIS
        skuList.setMinimumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));   // ADD THIS        
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


        // Stock Panel
        JPanel stockBoxPanel = new JPanel();
        stockBoxPanel.setLayout(new BoxLayout(stockBoxPanel, BoxLayout.X_AXIS)); 
        stockBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); 
        stockBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
    
        JLabel stockFieldLabel = new JLabel("stock:");
        stockFieldLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));


        JLabel stockLabel = new JLabel(Integer.toString(initialPart.stock));
        stockLabel.setFont(new Font("Sans-Serif", Font.PLAIN, SMALLER_FONT_SIZE));
        stockLabel.setBorder(BorderFactory.createEmptyBorder(POSITION0, 5, POSITION0, POSITION0));
        stockLabel.setMinimumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        stockLabel.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        stockLabel.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        stockLabel.setHorizontalAlignment(JLabel.LEFT);
        
        stockBoxPanel.add(stockFieldLabel);
        stockBoxPanel.add(Box.createRigidArea(new Dimension(25, 0))); // 5px width
        stockBoxPanel.add(stockLabel);

        // Bundle Button
        JPanel buttonBoxPanel = new JPanel();
        buttonBoxPanel.setLayout(new BoxLayout(buttonBoxPanel, BoxLayout.Y_AXIS)); 
        buttonBoxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
        JButton bundleButton = GUI.button("Bundle", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);
        boolean initialCanBundle = Database.isBundlePossible((String)skuList.getSelectedItem());
        bundleButton.setBackground(initialCanBundle ? VS_GREEN : Color.LIGHT_GRAY);
        bundleButton.setOpaque(true);


        stockLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String sku = (String) skuList.getSelectedItem();
                try {
                    int newStock = Integer.parseInt(stockLabel.getText());
                    boolean success = Database.updateStock(sku, newStock);
                    if (!success) {
                        JOptionPane.showMessageDialog(stockLabel, 
                            "Failed to update stock", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        // Reset to current value
                        Part part = Database.getSkuData(sku);
                        stockLabel.setText(Integer.toString(part.stock));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(stockLabel, 
                        "Please enter a valid stock quantity", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    // Reset to current value
                    Part part = Database.getSkuData(sku);
                    stockLabel.setText(Integer.toString(part.stock));
                }
            }
        });

        // Replace your current table creation code with:
        String[] columnNames = {"Stock", "Qty", "Part", "Description"};
        Object[][] data = {};
        JTable subcomponentsTable = new JTable(new DefaultTableModel(data, columnNames));
        subcomponentsTable.setPreferredScrollableViewportSize(new Dimension(700, 150));

        JScrollPane scrollPane = new JScrollPane(subcomponentsTable);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(700, 150));
        scrollPane.setMaximumSize(new Dimension(700, 150));

        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableContainer.setBackground(Color.decode(WHITE_HASHCODE));
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); 
        tableContainer.add(Box.createVerticalStrut(30));
        tableContainer.add(scrollPane);

        // Update the SKU selection listener to also populate the subcomponents table
        ActionListener skuListListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sku = (String) skuList.getSelectedItem();
                Part part = Database.getSkuData(sku);
                descriptionLabel.setText(part.description);
                stockLabel.setText(Integer.toString(part.stock));
                
                // Update subcomponents table
                DefaultTableModel model = (DefaultTableModel) subcomponentsTable.getModel();
                model.setRowCount(0); // Clear existing rows
                
                // Get subcomponents from database
                Map<String, Integer> components = Database.getSubcomponents(sku);
                
                // Add each component to the table
                for (Map.Entry<String, Integer> entry : components.entrySet()) {
                    String childSku = entry.getKey();
                    int quantity = entry.getValue();
                    
                    // Get child part details
                    Part childPart = Database.getSkuData(childSku);
                    
                    // Add row to table: Stock, Qty, Part, Description
                    model.addRow(new Object[]{
                        childPart.stock,
                        quantity,
                        childSku,
                        childPart.description
                    });
                }

                boolean canBundle = Database.isBundlePossible(sku);
                bundleButton.setEnabled(canBundle);
                bundleButton.setBackground(canBundle ? VS_GREEN : Color.LIGHT_GRAY);
                bundleButton.setBackground(canBundle ? VS_GREEN : Color.LIGHT_GRAY);
                
            }
        };

        skuList.addActionListener(skuListListener);


// Add action to Bundle button
bundleButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String sku = (String) skuList.getSelectedItem();
        if (sku != null) {
            boolean success = Database.performBundle(sku);
            if (success) {
                JOptionPane.showMessageDialog(panel, 
                    "Bundle operation completed successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the data
                skuListListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "refresh"));
            } else {
                JOptionPane.showMessageDialog(panel, 
                    "Cannot bundle - insufficient components in stock", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        boolean canBundle = Database.isBundlePossible(sku);
        bundleButton.setBackground(canBundle ? VS_GREEN : Color.LIGHT_GRAY);
        bundleButton.setEnabled(canBundle);
    }
});


        // Add components to panels
        comboBoxPanel.add(skuBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(7));
        comboBoxPanel.add(descriptionBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(7)); 
        comboBoxPanel.add(stockBoxPanel);
        
        buttonBoxPanel.add(Box.createVerticalStrut(20));
        buttonBoxPanel.add(bundleButton);
        
        panel.add(Box.createVerticalStrut(40));
        panel.add(comboBoxPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonBoxPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(tableContainer);
        panel.add(Box.createVerticalGlue());


                
        return panel;

        // Temporarily set distinctive backgrounds to identify the "line"
    }
}