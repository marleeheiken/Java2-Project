package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class representing a bundle.
 */
public final class Bundle {
    private static final String WHITE_HASHCODE = "#FFFFFF";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 690;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int HEIGHT2 = 20;

    /**
     * Private constructor to prevent instantiation.
     */
    private Bundle() {
    }

    /**
     * Creates and returns the main Bundle panel with a centered title.
     *
     * @return JPanel containing the configured Bundle interface
     */
    public static JPanel makeGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(Color.decode(WHITE_HASHCODE));

        // Create and configure the title label
        JLabel titleLabel = new JLabel("Bundle", JLabel.CENTER);
        titleLabel.setFont(new Font("Sans-Serif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add vertical space and title to panel
        panel.add(Box.createVerticalStrut(HEIGHT2)); // Space from top
        panel.add(titleLabel);
        panel.add(Box.createVerticalGlue()); // Pushes content to top

        return panel;
    }
}
