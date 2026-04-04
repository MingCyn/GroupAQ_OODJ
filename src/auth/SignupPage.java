package auth;

import javax.swing.*;
import java.awt.*;

public class SignupPage extends JFrame {
    public SignupPage() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(new JLabel("Signup page placeholder"));
        panel.add(Box.createVerticalStrut(10));
        JButton close = new JButton("Close");
        close.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
        panel.add(close);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}