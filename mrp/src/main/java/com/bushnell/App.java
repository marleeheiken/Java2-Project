package com.bushnell;

import java.io.File;

import javax.swing.JFrame;

/**
 * Main application class.
 */
public final class App {
    private App() {
    }

    /**
     * Main method to start the application.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        HomeScreen homeScreen = new HomeScreen();

        JFrame frame = new JFrame("Visual Robotics MRP Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // get location of jar file (where PDF file should go)
        String jarPath = App.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        File jarFile = new File(jarPath);
        String jarDirectoryPath = jarFile.getParent();
        //System.out.println("Path to the JAR file: " + jarDirectoryPath);


        // Add the HomeScreen panel to the JFrame
        frame.add(homeScreen.makeGUI(jarDirectoryPath));

        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
    }
}
