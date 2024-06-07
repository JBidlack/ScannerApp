package app;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window win = new Window();
            win.setup();
        });
        
    }
}