package auth;

import javax.swing.*;
import java.awt.*;

public class SignupPage extends JFrame {
	public SignupPage() {
        setTitle("APU Automotive Service Centre - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 460);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        SignupPanel signupPanel = new SignupPanel(this);
        getContentPane().add(signupPanel, BorderLayout.CENTER);
        
        JLabel lblNewLabel = new JLabel(""); 
        lblNewLabel.setBounds(25, 10, 108, 87);
        signupPanel.add(lblNewLabel);
        ImageIcon icon = new ImageIcon(LoginPage.class.getResource("/images/APU-logo.png"));

        // 2. Scale it to match your label size (108x108)
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(108, 108, Image.SCALE_SMOOTH);

        // 3. Set the scaled version to your label
        lblNewLabel.setIcon(new ImageIcon(scaledImg));

        setVisible(true);
    }
}

class SignupPanel extends JPanel {
    private static final Color BG_COLOR = new Color(230, 242, 250);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    public SignupPanel(JFrame parentFrame) {
        setLayout(null);
        setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("SIGNUP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(0, 30, 600, 35);
        add(titleLabel);

        // Label and field for username, password, confirm password
        JLabel usernameLabel = new JLabel("USERNAME:", SwingConstants.RIGHT);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameLabel.setBounds(10, 100, 230, 40);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        usernameField.setBounds(250, 100, 290, 40);
        add(usernameField);

        JLabel passwordLabel = new JLabel("PASSWORD:", SwingConstants.RIGHT);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setBounds(10, 160, 230, 40);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        passwordField.setBounds(250, 160, 290, 40);
        add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("CONFIRM PASSWORD:", SwingConstants.RIGHT);
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        confirmPasswordLabel.setBounds(0, 220, 240, 40);
        add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 20));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        confirmPasswordField.setBounds(250, 220, 290, 40);
        add(confirmPasswordField);

        BlueRoundedButton confirmButton = new BlueRoundedButton("CONFIRM");
        confirmButton.setBounds(65, 310, 210, 50);
        confirmButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());
            
            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Incorrect Password Input", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maxId = 0;
            try {
                java.io.File file = new java.io.File("data/account.txt");
                if (file.exists()) {
                    java.util.Scanner scanner = new java.util.Scanner(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",");
                        if (parts.length > 0) {
                            String id = parts[0].trim();
                            if (id.startsWith("CTM")) {
                                try {
                                    int num = Integer.parseInt(id.substring(3));
                                    if (num > maxId) {
                                        maxId = num;
                                    }
                                } catch (Exception ex) {}
                            }
                        }
                    }
                    scanner.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String newId = String.format("CTM%04d", maxId + 1);
            try {
                java.io.FileWriter writer = new java.io.FileWriter("data/account.txt", true);
                // format: UserID, Username, Password, Role, Contact, Email
                writer.write(newId + ", " + user + ", " + pass + ", Customer, None, None\n");
                writer.close();
                JOptionPane.showMessageDialog(this, "Sign Up Successful!\nYour UserID is: " + newId);
                parentFrame.dispose();
                new LoginPage();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(confirmButton);

        GreyRoundedButton cancelButton = new GreyRoundedButton("CANCEL");
        cancelButton.setBounds(315, 310, 210, 50);
        cancelButton.addActionListener(e -> {
            parentFrame.dispose();
            new LoginPage();
        });
        add(cancelButton);
    }
}

class GreyRoundedButton extends JButton {
    private static final Color NORMAL_COLOR = new Color(175, 175, 175);
    public GreyRoundedButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 24));
        setForeground(Color.BLACK);
        setBackground(NORMAL_COLOR);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        FontMetrics fm = g2.getFontMetrics();
        int cx = (getWidth() - fm.stringWidth(getText())) / 2;
        int cy = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        
        g2.setColor(getForeground());
        g2.drawString(getText(), cx, cy);
        g2.dispose();
    }
}