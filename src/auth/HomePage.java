package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePage extends JFrame {
    private JPanel sidebar, contentPanel;
    private JLabel lblUserName;
    private JLabel lblUserId;
    private String userRole;

    public HomePage(String role, String userName) {
        this.userRole = role; 
        setTitle("APU Automotive Service Centre");

        // Try-catch or null check is good practice for resources
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo.png"));
            this.setIconImage(logo.getImage());
        } catch (Exception e) {
            System.out.println("Logo not found, using default icon.");
        }

        // --- SCREEN SIZING FOR LAPTOP FULLSCREEN ---
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes on start
        setMinimumSize(new Dimension(1024, 768)); // Prevents UI from collapsing too small
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar Setup
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(211, 238, 252)); 
        
        // Responsive sidebar width: 20% of screen width, but between 200px and 350px
        int sWidth = (int) (screenSize.width * 0.2);
        sWidth = Math.max(220, Math.min(sWidth, 350));
        sidebar.setPreferredSize(new Dimension(sWidth, screenSize.height));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // User Profile Section - Scaled Fonts
        int nameFontSize = Math.max(16, (int) (screenSize.width * 0.015));
        int idFontSize = Math.max(12, (int) (screenSize.width * 0.010));

        lblUserName = new JLabel(userName);
        lblUserName.setFont(new Font("Arial", Font.BOLD, nameFontSize));
        lblUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Fetch User ID from file
        String userId = "N/A";
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("data", "account.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].trim().equals(userName)) {
                    userId = parts[0].trim();
                    break;
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Could not read account.txt");
        }
        
        lblUserId = new JLabel("ID: " + userId);
        lblUserId.setFont(new Font("Arial", Font.PLAIN, idFontSize));
        lblUserId.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        sidebar.add(lblUserName);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(lblUserId);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Navigation Buttons 
        addNavButton("Manage Account", new Color(255, 249, 230));
        addNavButton("Appointments", new Color(168, 208, 239));
        addNavButton("Services & Pricing", new Color(168, 208, 239));
        addNavButton("Payments", new Color(168, 208, 239));
        
        if (role.equalsIgnoreCase("Admin")) {
            addNavButton("Reports & Analytics", new Color(168, 208, 239));
        }
        
        addNavButton("Feedback", new Color(168, 208, 239));
        sidebar.add(Box.createVerticalGlue());
        addNavButton("Logout", new Color(255, 204, 204)); 
        
        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setBorder(null);
        sidebarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // 2. Main Content Area
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JLabel welcomeMsg = new JLabel("Welcome to APU Automotive Service Centre", SwingConstants.LEFT);
        int welcomeFontSize = Math.max(24, (int) (screenSize.width * 0.025));
        welcomeMsg.setFont(new Font("Arial", Font.BOLD, welcomeFontSize)); 
        welcomeMsg.setBorder(new EmptyBorder(30, 50, 0, 20));
        
        contentPanel.add(welcomeMsg, BorderLayout.NORTH);

        add(sidebarScroll, BorderLayout.WEST);
        
        JScrollPane contentScroll = new JScrollPane(contentPanel);
        contentScroll.setBorder(null);
        add(contentScroll, BorderLayout.CENTER);
    }

    private void addNavButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Font size scales with screen width
        int btnFontSize = Math.max(13, (int) (screenSize.width * 0.011));
        btn.setFont(new Font("Arial", Font.PLAIN, btnFontSize));

        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Button sizing: Fill the sidebar width nicely
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addActionListener(e -> {
            if (text.equals("Logout")) {
                // Assuming LoginPage class exists
                // new LoginPage().setVisible(true);
                dispose();
            } else {
                System.out.println("Navigating to: " + text);
            }
        });

        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage("Admin", "JOHN DOE").setVisible(true));
    }
}