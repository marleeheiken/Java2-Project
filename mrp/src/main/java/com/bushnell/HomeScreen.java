package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * HomeScreen class for creating the main GUI panel.
 */
public class HomeScreen {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final String BLACK_HASHCODE = "#000000";
    private static final int LOGO_WIDTH = 180;
    private static final int LOGO_HEIGHT = 51;
    private static final int LOGO_POSITION = 10;
    private static final int TEXT_FONT_SIZE = 16;
    private static final int TEXT_SPACING = 5;
    private static final int BUTTON_WIDTH = LOGO_WIDTH;
    private static final int BUTTON_HEIGHT = 51;
    private static final int BUTTON_SPACING_HEIGHT = 10;
    private static final int BUTTON_SPACING_WIDTH = 0;
    private static final int BUTTON_FONT_SIZE = 14;

    /**
     * Creates and returns the main GUI panel.
     *
     * @return the configured JPanel
     */
    public JPanel makeGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        Color black = Color.decode(BLACK_HASHCODE);
        mainPanel.setBackground(black);
        mainPanel.setVisible(true);

        // Create a vertical box to hold logo and text
        Box logoBox = Box.createVerticalBox();
        logoBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add VisualRobotics logo
        ImageIcon roboticsLogo = new ImageIcon(getClass().getResource("/VisualRoboticsLogo.png"));
        Image scaledRoboticsLogo = roboticsLogo.getImage().getScaledInstance(
            LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledRoboticsLogo));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoBox.add(Box.createRigidArea(new Dimension(LOGO_POSITION, LOGO_POSITION)));
        logoBox.add(logoLabel);

        // Add MRP System label below the logo
        JLabel mrpLabel = new JLabel("MRP System");
        mrpLabel.setFont(new Font("Arial", Font.BOLD, TEXT_FONT_SIZE));
        mrpLabel.setForeground(Color.WHITE);
        mrpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoBox.add(Box.createRigidArea(new Dimension(0, TEXT_SPACING)));
        logoBox.add(mrpLabel);

        Box buttonsBox = Box.createVerticalBox();
        buttonsBox.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        // Add the logoBox and buttonBox to the main panel
        mainPanel.add(logoBox);
        // Add some space between logo and buttons
        mainPanel.add(Box.createRigidArea(new Dimension(BUTTON_SPACING_WIDTH, BUTTON_SPACING_HEIGHT)));
        mainPanel.add(buttonsBox);

        return mainPanel;
    }
}
