package com.bushnell;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * HomeScreen class for creating the main GUI panel.
 */
public class HomeScreen {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final String BLACK_HASHCODE = "#000000";
    private static final int LOGO_WIDTH = 180;
    private static final int LOGO_HEIGHT = 51;
    private static final int POSITION = 10;
    private static final int TEXT_FONT_SIZE = 16;
    private static final int TEXT_SPACING = 5;
    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 51;
    private static final int BUTTON_SPACING_HEIGHT = 10;
    private static final int BUTTON_SPACING_WIDTH = 0;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final int CARDBOX_WIDTH = 1080;
    private static final int CARDBOX_HEIGHT = 720;
    private static final int LEFTBOX_WIDTH = 200;
    private static final int LEFTBOX_HEIGHT = 720;

    /**
     * Creates and returns the main GUI panel.
     *
     * @return the configured JPanel
     */
    public JPanel makeGUI(String appDir) {

        // set database directory
        // provided directory is where jar file is
        // this is in mrp/target, so go out 2 directories 
        // to get to where the database is
        Path jarPath = Paths.get(appDir);
        String dbPath = jarPath.getParent().getParent().toString();
        Database.setDBDirectory(dbPath);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        Color black = Color.decode(BLACK_HASHCODE);
        mainPanel.setBackground(black);
        mainPanel.setVisible(true);

        // Create a vertical box to hold logo and text
        Box leftBox = Box.createVerticalBox();
        leftBox.setPreferredSize(new Dimension(LEFTBOX_WIDTH, LEFTBOX_HEIGHT));
        leftBox.setMaximumSize(new Dimension(LEFTBOX_WIDTH, LEFTBOX_HEIGHT));
        leftBox.setBorder(BorderFactory.createLineBorder(Color.black));
        leftBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftBox.setAlignmentY(Component.TOP_ALIGNMENT);

        // Create a vertical box to hold logo and text
        Box logoBox = Box.createVerticalBox();
        logoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add VisualRobotics logo
        ImageIcon roboticsLogo = new ImageIcon(getClass().getResource("/VisualRoboticsLogo.png"));
        Image scaledRoboticsLogo = roboticsLogo.getImage().getScaledInstance(
            LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledRoboticsLogo));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoBox.add(logoLabel);
        logoBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));


        // Add MRP System label below the logo
        JLabel mrpLabel = new JLabel("MRP System");
        mrpLabel.setFont(new Font("Arial", Font.BOLD, TEXT_FONT_SIZE));
        mrpLabel.setForeground(Color.WHITE);
        mrpLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // For some reason this makes it go to the left 
        logoBox.add(Box.createRigidArea(new Dimension(0, TEXT_SPACING)));
        logoBox.add(mrpLabel);

        Box buttonsBox = Box.createVerticalBox();
        buttonsBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton updateButton = GUI.button("Update Stock", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);
        JButton reportButton = GUI.button("Stock Report", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);
        JButton bundleButton = GUI.button("Bundle", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);
        JButton demandButton = GUI.button("Demand Analysis", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_FONT_SIZE);

        buttonsBox.add(updateButton);
        buttonsBox.add(Box.createRigidArea(new Dimension(BUTTON_SPACING_WIDTH, BUTTON_SPACING_HEIGHT)));
        buttonsBox.add(reportButton);
        buttonsBox.add(Box.createRigidArea(new Dimension(BUTTON_SPACING_WIDTH, BUTTON_SPACING_HEIGHT)));
        buttonsBox.add(bundleButton);
        buttonsBox.add(Box.createRigidArea(new Dimension(BUTTON_SPACING_WIDTH, BUTTON_SPACING_HEIGHT)));
        buttonsBox.add(demandButton);
        buttonsBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));


        // Add the logoBox and buttonBox to the main panel
        leftBox.add(logoBox);
        // Add some space between logo and buttons
        leftBox.add(Box.createRigidArea(new Dimension(BUTTON_SPACING_WIDTH, BUTTON_SPACING_HEIGHT)));
        leftBox.add(buttonsBox);

        // Add the leftBox to the main panel
        mainPanel.add(leftBox);

        Box cardBox = Box.createVerticalBox();
        cardBox.setPreferredSize(new Dimension(CARDBOX_WIDTH, CARDBOX_HEIGHT));
        cardBox.setMaximumSize(new Dimension(CARDBOX_WIDTH, CARDBOX_HEIGHT));

        cardBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.black),
            BorderFactory.createEmptyBorder(POSITION, POSITION, POSITION, POSITION)  // Add 10px margin inside
        ));

        cardBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardBox.setAlignmentY(Component.TOP_ALIGNMENT);
/*
        // create panels for each sub-menu
        JPanel updateStockPanel = UpdateStock.makeGUI();
        JPanel stockReportPanel = new JPanel();
        try {
            stockReportPanel    = StockReport.makeGUI(dbPath);
        } catch(Exception e) {
            e.printStackTrace(System.err);
            stockReportPanel = new JPanel(); // Ensure panel exists even if error occurs

        }
        JPanel bundlePanel = Bundle.makeGUI();
        JPanel demandAnalysisPanel = DemandAnalysis.makeGUI();

        // create a card panel (only one panel visible at a time)
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(updateStockPanel, "updateStock");
        cardPanel.add(stockReportPanel, "stockReport");
        cardPanel.add(bundlePanel, "bundle");
        cardPanel.add(demandAnalysisPanel, "demand"); 
        cardBox.add(cardPanel);
        */
        JPanel cardPanel = new JPanel(new CardLayout());

        // Create initial panels
        try {
            JPanel updateStockPanel = UpdateStock.makeGUI();
            cardPanel.add(updateStockPanel, "updateStock");
            
            JPanel stockReportPanel = StockReport.makeGUI(dbPath);
            cardPanel.add(stockReportPanel, "stockReport");
            
            JPanel bundlePanel = Bundle.makeGUI();
            cardPanel.add(bundlePanel, "bundle");
            
            JPanel demandAnalysisPanel = DemandAnalysis.makeGUI();
            cardPanel.add(demandAnalysisPanel, "demand");
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }

        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, "updateStock");
        
        cardBox.add(cardPanel);
        mainPanel.add(cardBox);

        // button listeners
        updateButton.addActionListener(e -> {
            try {
                JPanel updatePanel = UpdateStock.makeGUI();
                cardPanel.add(updatePanel, "updateStock");
                ((CardLayout)cardPanel.getLayout()).show(cardPanel, "updateStock");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        // Report button listener (already correct - keeps the scroll pane handling)
        reportButton.addActionListener(e -> {
            try {
                Component reportPanel = StockReport.makeGUI(dbPath);
                cardPanel.add(reportPanel, "stockReport");
                
                // Reset scroll position
                if (reportPanel instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane)reportPanel;
                    scrollPane.getViewport().setViewPosition(new Point(0, 0));
                }
                
                ((CardLayout)cardPanel.getLayout()).show(cardPanel, "stockReport");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        // Bundle button listener
        bundleButton.addActionListener(e -> {
            try {
                JPanel bundlePanel = Bundle.makeGUI();
                cardPanel.add(bundlePanel, "bundle");
                ((CardLayout)cardPanel.getLayout()).show(cardPanel, "bundle");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        // Demand button listener
        demandButton.addActionListener(e -> {
            try {
                JPanel demandPanel = DemandAnalysis.makeGUI();
                cardPanel.add(demandPanel, "demand");
                ((CardLayout)cardPanel.getLayout()).show(cardPanel, "demand");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        return mainPanel;
    }
}
