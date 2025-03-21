package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * HomeScreen class for creating the main GUI panel.
 */
public class HomeScreen {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final String BLACK_HASHCODE = "#000000";

    /**
     * Creates and returns the main GUI panel.
     *
     * @return the configured JPanel
     */
    public JPanel makeGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        Color black = Color.decode(BLACK_HASHCODE);
        mainPanel.setBackground(black);
        mainPanel.setVisible(true);

        return mainPanel;
    }
}
