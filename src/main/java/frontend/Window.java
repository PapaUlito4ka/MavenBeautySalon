package frontend;

import java.awt.BorderLayout;
import javax.swing.*;
import frontend.Interface;

public class Window {
    private JFrame window;
    private Interface interface_;

    public Window(int width, int height) {
        window = new JFrame("BeautySalon");
        window.setSize(width, height);
        window.setLocation(100, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interface_ = new Interface(window);
        window.setVisible(true);
    }

}
