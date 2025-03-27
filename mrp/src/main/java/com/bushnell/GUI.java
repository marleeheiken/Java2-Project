package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;

/**
 * Class for managing the GUI.
 */
public final class GUI {
    private static final String VS_GREEN_HASHCODE = "#00af74";

    // Private constructor to prevent instantiation
    private GUI() {
        // Utility class - no instantiation allowed
    }

    /**
     * Sets consistent dimensions for a component.
     * @param component The component to resize
     * @param w The width in pixels
     * @param h The height in pixels
     */

    // set dimensions of a component
    public static void setDimension(Component component, int w, int h) {
        component.setMinimumSize(new Dimension(w, h));
        component.setPreferredSize(new Dimension(w, h));
        component.setMaximumSize(new Dimension(w, h));
    }

    /**
     * Creates a pre-styled JButton with consistent dimensions.
     * @param textStr The button text
     * @param width The button width
     * @param height The button height
     * @param fontSize The text font size
     * @return The configured JButton
     */

    // create a centered button
    public static JButton button(String textStr, int width, int height, int fontSize) {
        JButton button = new JButton(textStr);
        setDimension(button, width, height);
        button.setFont(new Font("Sans-Serif", Font.BOLD, fontSize));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add these lines for styling:
        Color vsGreen = Color.decode(VS_GREEN_HASHCODE);
        button.setBackground(vsGreen);
        button.setForeground(Color.WHITE); // For better text contrast
        button.setOpaque(true); // Required for background to show
        button.setBorderPainted(false);
        button.setFocusPainted(false); // Optional: removes focus rectangle

        return button;
    }

}
