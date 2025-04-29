package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;

public final class DemandAnalysis {
    public static final Color VS_GREEN = Color.decode("#00af74");
    private static final String WHITE_HASHCODE = "#FFFFFF";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEIGHT2 = 20;
    private static final int COMBOBOX_HEIGHT = 35;
    private static final int POSITION0 = 0;
    private static final int TEXT_FONT_SIZE = 20;
    private static final int SMALLER_FONT_SIZE = 15;
    private static final int VALUE_WIDTH = 300;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_FONT_SIZE = 8;

    private static DefaultTableModel tableModel;
    private static JTable resultTable;
    

    // Private constructor to prevent instantiation
    private DemandAnalysis() {
        // Utility class - no instantiation allowed
    }

    /**
     * Creates and returns the main GUI panel.
     *
     * @return the configured JPanel
     */
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
        JLabel titleLabel = new JLabel("Demand Analysis");
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, TITLE_FONT_SIZE));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());
        
        panel.add(Box.createVerticalStrut(HEIGHT2));
        panel.add(titlePanel);

            // Main content panel
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS)); 
        comboBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        comboBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
        comboBoxPanel.setBorder(BorderFactory.createEmptyBorder(POSITION0, POSITION0, POSITION0, 178));

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

        // Desired Quantity Panel
        JPanel quantityBoxPanel = new JPanel();
        quantityBoxPanel.setLayout(new BoxLayout(quantityBoxPanel, BoxLayout.X_AXIS)); 
        quantityBoxPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); 
        quantityBoxPanel.setBackground(Color.decode(WHITE_HASHCODE));
    
        JLabel quantityFieldLabel = new JLabel("desired quantity:");
        quantityFieldLabel.setFont(new Font("Sans-Serif", Font.PLAIN, TEXT_FONT_SIZE));
        
        // Create spinner with min=1, max=99, initial=1, step=1
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

        // Configure the spinner's text field
        JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        spinnerTextField.setColumns(4); // Adjust width
        spinnerTextField.setHorizontalAlignment(JTextField.LEFT);

        // Make sure only valid numbers are entered
        DefaultFormatterFactory factory = (DefaultFormatterFactory) spinnerTextField.getFormatterFactory();
        NumberFormatter formatter = (NumberFormatter) factory.getDefaultFormatter();
        formatter.setAllowsInvalid(false);

        spinner.setFont(new Font("Sans-Serif", Font.PLAIN, SMALLER_FONT_SIZE));
        spinner.setPreferredSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        spinner.setMaximumSize(new Dimension(VALUE_WIDTH, COMBOBOX_HEIGHT));
        
        quantityBoxPanel.add(quantityFieldLabel);
        quantityBoxPanel.add(Box.createRigidArea(new Dimension(25, 0))); // 5px width
        quantityBoxPanel.add(spinner);

        // Create and configure the table (replace old table code)
        String[] columnNames = {"SKU", "Need", "Stock", "Description"};
        Object[][] data = {};
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        resultTable = new JTable(tableModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(700, 290));
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(700, 290));
        scrollPane.setMaximumSize(new Dimension(700, 290));
        
        JPanel tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableContainer.setBackground(Color.decode(WHITE_HASHCODE));
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); 
        tableContainer.add(Box.createVerticalStrut(30));
        tableContainer.add(scrollPane);

        JButton pdfButton = GUI.button("Generate PDF Report", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);
        pdfButton.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        pdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfButton.addActionListener(e -> generateDemandPDF(skuList, spinner, panel));

        // Replace your current button code with:
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.decode(WHITE_HASHCODE));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(pdfButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // Add components to panels
        comboBoxPanel.add(skuBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(7));
        comboBoxPanel.add(descriptionBoxPanel);
        comboBoxPanel.add(Box.createVerticalStrut(7)); 
        comboBoxPanel.add(quantityBoxPanel);
        
        panel.add(Box.createVerticalStrut(60));
        panel.add(comboBoxPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(tableContainer);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);

        // Update your SKU listener:
        ActionListener skuListListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sku = (String) skuList.getSelectedItem();
                Part part = Database.getSkuData(sku);
                descriptionLabel.setText(part.description);
                updateDemandTable(skuList, spinner);  // Update table instead of list
            }
        };
        
        // Add debug prints to verify listener attachment
        System.out.println("Adding SKU listener to: " + skuList);
        skuList.addActionListener(skuListListener);

        System.out.println("Adding spinner listener to: " + spinner);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateDemandTable(skuList, spinner);
            }
        });
        // Initial calculation
        updateDemandTable(skuList, spinner);  // Initialize table

        return panel;
    }

    private static void updateDemandTable(JComboBox<String> skuList, JSpinner spinner) {
        tableModel.setRowCount(0);
        
        String sku = (String) skuList.getSelectedItem();
        int quantity = (Integer) spinner.getValue();
        Part mainPart = Database.getSkuData(sku);
        
        /* 
        // Always show the main part first
        tableModel.addRow(new Object[]{
            mainPart.sku,
            quantity,  // Show full requested quantity
            mainPart.stock,
            mainPart.description
        });*/
        
        // Calculate needed quantity (requested - stock)
        int neededQuantity = Math.max(quantity - mainPart.stock, 0);
        
        // Only proceed if we actually need something
        if (neededQuantity > 0) {
            // Get required parts - pass the ORIGINAL QUERY PARAMS:
            // - SKU as-is
            // - FULL quantity (not neededQuantity)
            List<Part> requiredParts = Database.getRequiredStock(sku, quantity);
            
            for (Part part : requiredParts) {
                if (part.sku.equals(sku)) continue; // skip the main part

                Part fullPartData = Database.getSkuData(part.sku);
                int displayQty = Math.max(part.quantity - fullPartData.stock, 0);
                /* 
                Part fullPartData = Database.getSkuData(part.sku);
                // Calculate display quantity (what we're short)
                int displayQty = Math.max(part.quantity - fullPartData.stock, 0);
                */
                tableModel.addRow(new Object[]{
                    part.sku,
                    displayQty,  // Show only the shortage amount
                    fullPartData.stock,
                    part.description
                });
            }
        }
    }

        private static void getRawComponents(String sku, int qty, Map<String, Integer> result) {
            if (Database.isRawComponent(sku)) {
                result.put(sku, result.getOrDefault(sku, 0) + qty);
                return;
            }
            
            Map<String, Integer> children = Database.getSubcomponents(sku);
            for (Map.Entry<String, Integer> entry : children.entrySet()) {
                getRawComponents(entry.getKey(), qty * entry.getValue(), result);
            }
        }

        private static void generateDemandPDF(JComboBox<String> skuList, JSpinner spinner, JPanel parentPanel) {
            try {
                // Get current date/time for filename
                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-HH.mm");
                String formattedDateTime = formatter.format(now);
                String filename = "VR-DemandAnalysis-" + formattedDateTime + ".pdf";
                String dbDir = new File(Database.DBName.replace("jdbc:sqlite:", "")).getParent();
                
                // Get selected values
                String sku = (String) skuList.getSelectedItem();
                int quantity = (Integer) spinner.getValue();
                Part mainPart = Database.getSkuData(sku);

                // CHANGE 1: Calculate the actual needed quantity first
                int neededQuantity = Math.max(quantity - mainPart.stock, 0);

                // CHANGE 2: Pass the neededQuantity instead of full quantity
                Map<String, Integer> neededComponents = new HashMap<>();
                getRawComponents(sku, neededQuantity, neededComponents);

                // [Keep ALL the rest of your existing code exactly as is]
                Map<String, Integer> componentsToOrder = new LinkedHashMap<>();
                Map<String, Integer> criticalItems = new LinkedHashMap<>();
                int totalItemsToOrder = 0;

                for (Map.Entry<String, Integer> entry : neededComponents.entrySet()) {
                    Part part = Database.getSkuData(entry.getKey());
                    int needed = entry.getValue();
                    if (part.stock < needed) {
                        int toOrder = needed - part.stock;
                        componentsToOrder.put(entry.getKey(), toOrder);
                        totalItemsToOrder += toOrder;
                        if (part.stock == 0) {
                            criticalItems.put(entry.getKey(), toOrder);
                        }
                    }
                }
                
                // If nothing needs to be ordered, show message and return
                if (componentsToOrder.isEmpty()) {
                    JOptionPane.showMessageDialog(parentPanel,
                        "No components need to be ordered - stock is sufficient.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Create PDF document
                try (PDDocument document = new PDDocument()) {
                    PDPageContentStream contentStream = null;
                    int itemsPerPage = 45;
                    int itemCount = 0;
                    int pageNum = 1;
                    
                    // Centered column format
                    String columnTitle = String.format("%40s %7s %7s   %s", 
                        "SKU", "Order", "Stock", "Description");
                    
                    // Convert to list for easier counting
                    List<Map.Entry<String, Integer>> entries = new ArrayList<>(componentsToOrder.entrySet());
                    
                    for (int i = 0; i < entries.size(); i++) {
                        Map.Entry<String, Integer> entry = entries.get(i);
                        Part part = Database.getSkuData(entry.getKey());
                        boolean isCritical = (part.stock == 0);
                        
                        // Create new page if needed
                        if (i % itemsPerPage == 0) {
                            if (contentStream != null) {
                                contentStream.endText();
                                contentStream.close();
                            }
                            
                            PDPage page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            
                            // Header (centered)
                            contentStream.beginText();
                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), 20);
                            contentStream.newLineAtOffset(130, 750);
                            contentStream.showText("Visual Robotics Demand Analysis");
                            contentStream.endText();
                            
                            // Metadata (centered)
                            contentStream.beginText();
                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
                            contentStream.newLineAtOffset(220, 730);
                            contentStream.showText(formattedDateTime + " - page " + pageNum);
                            contentStream.endText();
                            
                            // Summary information (left-aligned)
                            contentStream.beginText();
                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 10);
                            contentStream.newLineAtOffset(50, 710);
                            contentStream.showText("For SKU: " + sku + " - " + mainPart.description);
                            contentStream.newLineAtOffset(0, -15);
                            contentStream.showText("Desired Quantity: " + quantity);
                            contentStream.newLineAtOffset(0, -15);
                            contentStream.showText("Total Components to Order: " + totalItemsToOrder);
                            contentStream.newLineAtOffset(0, -15);
                            contentStream.showText("Critical Items (zero stock): " + criticalItems.size());
                            contentStream.endText();
                            
                            // Column headers (positioned just above the line)
                            contentStream.beginText();
                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), 10);
                            contentStream.newLineAtOffset(50, 645); 
                            contentStream.showText(String.format("%30s %7s %7s   %s",
                                "SKU", "Order", "Stock", "Description"));
                            contentStream.endText();
                            
                            // Header line (full width)
                            contentStream.setLineWidth(1f);
                            contentStream.moveTo(50, 640);
                            contentStream.lineTo(550, 640);
                            contentStream.stroke();
                            
                            contentStream.beginText();
                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 10);
                            contentStream.setLeading(14.5f);
                            contentStream.newLineAtOffset(50, 630);  // 10px below line at y=640
                            
                            pageNum++;
                        }
                        
                        // Highlight critical items in red
                        if (isCritical) {
                            contentStream.setNonStrokingColor(Color.RED);
                        }
                        
                        // Centered component line (matches original format)
                        String line = String.format("%30s %6d %6d     %s",
                            entry.getKey(), 
                            entry.getValue(),
                            part.stock, 
                            part.description);
                        contentStream.showText(line);
                        contentStream.newLine();
                        
                        // Reset color if we changed it
                        if (isCritical) {
                            contentStream.setNonStrokingColor(Color.BLACK);
                        }
                        
                        itemCount++;
                    }
                    
                    // Final cleanup
                    if (contentStream != null) {
                        contentStream.endText();
                        contentStream.close();
                    }
                    
                    // Save PDF
                    document.save(new File(Paths.get(dbDir, filename).toString()));
                    JOptionPane.showMessageDialog(parentPanel,
                        "PDF saved to:\n" + filename, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentPanel,
                        "Error generating PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
            
}


     