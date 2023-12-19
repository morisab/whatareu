package com.whatareu.app;

import javax.swing.SwingUtilities;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMenu::new);
    }
}
    