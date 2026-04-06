package auth;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginPage extends JFrame {
	public LoginPage() {
        setTitle("APU Automotive Service Centre - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        LoginPanel loginPanel = new LoginPanel();
        getContentPane().add(loginPanel, BorderLayout.CENTER);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setBounds(25, 10, 108, 87);
        loginPanel.add(lblNewLabel);
        
        URL iconUrl = LoginPage.class.getResource("/images/APU-logo.png");
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(108, 108, Image.SCALE_SMOOTH);
            lblNewLabel.setIcon(new ImageIcon(scaledImg));
        } else {
            // Resource not found; leave label text or set a default
            lblNewLabel.setText("");
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}

class LoginPanel extends JPanel {
    private static final Color BG_COLOR = new Color(230, 242, 250);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;

    public LoginPanel() {
        setLayout(null);
        setBackground(BG_COLOR);

        // Title
        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(0, 30, 600, 35);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("USERNAME:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameLabel.setBounds(40, 100, 150, 40);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        usernameField.setBounds(190, 100, 350, 40);
        add(usernameField);

        JLabel passwordLabel = new JLabel("PASSWORD:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setBounds(40, 160, 150, 40);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setEchoChar('\u2022');
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        passwordField.setBounds(190, 160, 350, 40);
        add(passwordField);

        // Show Password
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 16));
        showPasswordCheckbox.setBackground(BG_COLOR);
        showPasswordCheckbox.setBounds(40, 215, 180, 25);
        showPasswordCheckbox.setIcon(new CheckBoxIcon(false));
        showPasswordCheckbox.setSelectedIcon(new CheckBoxIcon(true));
        
        showPasswordCheckbox.addItemListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022'); // Bullet character
            }
        });
        add(showPasswordCheckbox);

        // Reset Password
        JLabel resetPasswordLabel = new JLabel("<html><u>Reset Password</u></html>");
        resetPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        resetPasswordLabel.setForeground(Color.BLUE);
        resetPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetPasswordLabel.setBounds(400, 215, 150, 25);
        resetPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String inputUser = usernameField.getText().trim();
                boolean userExists = false;
                try {
                    java.io.File file = new java.io.File("data/account.txt");
                    if (file.exists()) {
                        java.util.Scanner scanner = new java.util.Scanner(file);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            String[] parts = line.split(",");
                            if (parts.length >= 2) {
                                String fileUser = parts[1].trim();
                                if (fileUser.equals(inputUser)) {
                                    userExists = true;
                                    break;
                                }
                            }
                        }
                        scanner.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (userExists) {
                    SwingUtilities.getWindowAncestor(LoginPanel.this).dispose();
                    new ResetPasswordPage(inputUser);
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Incorrect Username", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(resetPasswordLabel);

        BlueRoundedButton loginButton = new BlueRoundedButton("LOGIN");
        loginButton.setBounds(40, 280, 230, 50);
        loginButton.addActionListener(e -> {
            String inputUser = usernameField.getText();
            String inputPass = new String(passwordField.getPassword());
            boolean success = false;
            
            String userRole = "";
            String userName = "";
            
            try {
                java.io.File file = new java.io.File("data/account.txt");
                if (file.exists()) {
                    java.util.Scanner scanner = new java.util.Scanner(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",");
                        if (parts.length >= 4) {
                            String fileUser = parts[1].trim();
                            String filePass = parts[2].trim();
                            if (fileUser.equals(inputUser) && filePass.equals(inputPass)) {
                                success = true;
                                userRole = parts[3].trim();
                                userName = fileUser;
                                break;
                            }
                        }
                    }
                    scanner.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading account.txt", "File Error", JOptionPane.ERROR_MESSAGE);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Login Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose();
                new HomePage(userRole, userName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Input Data", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(loginButton);

        BlueRoundedButton signUpButton = new BlueRoundedButton("SIGN UP");
        signUpButton.setBounds(310, 280, 230, 50);
        signUpButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new SignupPage();
        });
        add(signUpButton);
    }
}

class CheckBoxIcon implements Icon {
    private boolean selected;
    private static final int SIZE = 20;

    public CheckBoxIcon(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, SIZE, SIZE);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, SIZE, SIZE);

        if (selected) {
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawLine(x + 4, y + 10, x + 8, y + 15);
            g2.drawLine(x + 8, y + 15, x + 16, y + 5);
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() { return SIZE; }
    @Override
    public int getIconHeight() { return SIZE; }
}

class BlueRoundedButton extends JButton {
    private static final Color NORMAL_COLOR = new Color(108, 186, 245);
    
    public BlueRoundedButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 22));
        setForeground(Color.WHITE);
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