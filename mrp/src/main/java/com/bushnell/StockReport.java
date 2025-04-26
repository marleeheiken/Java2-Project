package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public final class StockReport {
    private static final String VS_GREEN_HASHCODE = "#00af74";
    private static final String WHITE_HASHCODE = "#FFFFFF";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final int SCROLLWIDTH = 1000;
    private static final int SCROLLHEIGHT = 590;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEIGHT2 = 20;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_FONT_SIZE = 8;

    private StockReport() {} // Utility class

    public static JPanel makeGUI(String dbDir) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(Color.decode(WHITE_HASHCODE));

        // Title Label
        JLabel titleLabel = new JLabel("Stock Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Report Panel
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setPreferredSize(new Dimension(SCROLLWIDTH, SCROLLHEIGHT));
        reportPanel.setPreferredSize(new Dimension(SCROLLWIDTH, SCROLLHEIGHT));
        reportPanel.setMaximumSize(new Dimension(SCROLLWIDTH, SCROLLHEIGHT));

        reportPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reportPanel.setBackground(Color.decode(WHITE_HASHCODE));

        // Column Headers
        Box textTitleBox = Box.createHorizontalBox();
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        String headerText = String.format("%95s %17s %13s        %s\n", 
            "SKU", "Price", "Stock", "Description");
        JTextArea textTitle = new JTextArea(headerText, 1, 60);
        textTitle.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        textTitle.setEditable(false);
        textTitleBox.add(textTitle);
        textTitleBox.add(Box.createRigidArea(new Dimension(10,0)));
        
        reportPanel.add(Box.createVerticalStrut(20)); //
        reportPanel.add(textTitleBox);

        // Stock Data with Scroll Pane
        JTextArea stockText = new JTextArea(40, 60);
        stockText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        stockText.setEditable(false);
        
        List<Part> allSkuList = Database.getAllSkuData();
        StringBuilder sb = new StringBuilder();
        for(Part part : allSkuList) {
            sb.append(String.format("%49s %10s %6d      %s\n", 
                part.sku, String.format("$%.2f", part.price), part.stock, part.description));
        }
        stockText.setText(sb.toString());
        stockText.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(stockText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        Box textBox = Box.createHorizontalBox();
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        textBox.add(scroll);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        reportPanel.add(textBox);

        // PDF Button
        JButton pdfButton = GUI.button("Generate PDF Report", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);
        pdfButton.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        pdfButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfButton.addActionListener(e -> generatePDF(dbDir, allSkuList, panel));
        reportPanel.add(Box.createVerticalStrut(20));
        reportPanel.add(pdfButton);

        // Assemble main panel
        panel.add(Box.createVerticalStrut(HEIGHT2));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(HEIGHT2));
        panel.add(reportPanel);
        panel.add(Box.createVerticalGlue());


        return panel;
    }

    private static void generatePDF(String dbDir, List<Part> allSkuList, JPanel parentPanel) {
        try {
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-HH.mm");
            String formattedDateTime = formatter.format(now);
            String filename = "VR-StockReport-" + formattedDateTime + ".pdf";
            String columnTitle = String.format("%40s %8s %6s  %s", "SKU", "Price", "Stock", "Description");
            
            try (PDDocument document = new PDDocument()) {
                PDPageContentStream contentStream = null;
                int skuPerPage = 45;
                int pageNum = 0;
                boolean inTextBlock = false; // Track if we're in a text block
                
                for (int i = 0; i < allSkuList.size(); i++) {
                    Part part = allSkuList.get(i);
                    
                    // Check if we need a new page (first item or page full)
                    if (contentStream == null || i % skuPerPage == 0) {
                        // Close previous content stream if exists
                        if (contentStream != null) {
                            if (inTextBlock) {
                                contentStream.endText();
                                inTextBlock = false;
                            }
                            contentStream.close();
                        }
                        
                        // Create new page
                        PDPage page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        pageNum++;
                        
                        // Add header content
                        contentStream.beginText();
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), 20);
                        contentStream.newLineAtOffset(150, 750);
                        contentStream.showText("Visual Robotics Stock Report");
                        contentStream.endText();
                        
                        // Date and page number
                        contentStream.beginText();
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
                        contentStream.newLineAtOffset(220, 730);
                        contentStream.showText(formattedDateTime + " page " + pageNum);
                        contentStream.endText();
                        
                        // Column headers
                        contentStream.beginText();
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), 10);
                        contentStream.newLineAtOffset(0, 700);
                        contentStream.showText(columnTitle);
                        contentStream.endText();
                        
                        // Draw line under headers
                        contentStream.setLineWidth(1f);
                        contentStream.moveTo(10, 695);
                        contentStream.lineTo(602, 695);
                        contentStream.stroke();
                        
                        // Start main content text block
                        contentStream.beginText();
                        contentStream.setLeading(14.5f);
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 10);
                        contentStream.newLineAtOffset(0, 685);
                        inTextBlock = true;
                    }
                    
                    // Add part information
                    String newPart = String.format("%40s %8s %4d    %s", 
                        part.sku, String.format("$%.2f", part.price), part.stock, part.description);
                    contentStream.showText(newPart);
                    contentStream.newLine();
                }
                
                // Final cleanup
                if (contentStream != null) {
                    if (inTextBlock) {
                        contentStream.endText();
                    }
                    contentStream.close();
                }
                
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