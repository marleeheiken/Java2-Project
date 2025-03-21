package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Class for analyzing demand.
 */
public class DemandAnalysis {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final String WHITE_HASHCODE = "#FFFFFF";

    /**
     * Creates and returns the main GUI panel.
     *
     * @return the configured JPanel
     */
    public JPanel makeGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);

        Color black = Color.decode(WHITE_HASHCODE);
        panel.setBackground(black);
        panel.setVisible(true);

        return panel;
    }   
}
