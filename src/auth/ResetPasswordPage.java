package auth;

import javax.swing.*;
import java.awt.*;

public class ResetPasswordPage extends JFrame {
    public ResetPasswordPage(String username) {
        setTitle("Reset Password - " + username);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(username), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField newPass = new JPasswordField(15);
        panel.add(newPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField confirmPass = new JPasswordField(15);
        panel.add(confirmPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton resetBtn = new JButton("Reset Password");
        resetBtn.addActionListener(e -> {
            String p1 = new String(newPass.getPassword());
            String p2 = new String(confirmPass.getPassword());
            if (p1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Minimal behavior: inform user and close. Real implementation should update data/account.txt.
            JOptionPane.showMessageDialog(this, "Password reset for " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginPage().setVisible(true);
        });
        panel.add(resetBtn, gbc);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
