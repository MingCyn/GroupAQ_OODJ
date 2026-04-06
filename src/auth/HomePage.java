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

	        ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo.png"));


	     this.setIconImage(logo.getImage());
	        // Responsive sizing: use screen size instead of a fixed size
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        // Set the frame to the full screen size and maximize it
	        setSize(screenSize);
	        setExtendedState(JFrame.MAXIMIZED_BOTH);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout());

	        // 1. Sidebar Setup
	        sidebar = new JPanel();
	        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
	        sidebar.setBackground(new Color(211, 238, 252)); 
	        // Make sidebar width a percentage of the screen width so it scales
	        int sidebarWidth = Math.max(220, (int) (screenSize.width * 0.22));
	        sidebar.setPreferredSize(new Dimension(sidebarWidth, screenSize.height));
	        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

	        // User Profile Section
	        // Scale fonts based on screen width to keep readability across sizes
	        int nameFontSize = Math.max(16, (int) (screenSize.width * 0.018));
	        int idFontSize = Math.max(12, (int) (screenSize.width * 0.012));

	        lblUserName = new JLabel(userName);
	        lblUserName.setFont(new Font("Arial", Font.BOLD, nameFontSize));
	        lblUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        String userId = "";
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
	            e.printStackTrace();
	        }
	        
	        lblUserId = new JLabel(userId);
	        lblUserId.setFont(new Font("Arial", Font.PLAIN, idFontSize));
	        lblUserId.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        sidebar.add(Box.createRigidArea(new Dimension(0,40)));
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
	            addNavButton("Reports & Analytics", new Color(168,208,239));
	        }
	        
	        addNavButton("Feedback", new Color(168,208,239));
	        
	        sidebar.add(Box.createVerticalGlue());

	        addNavButton("Logout", new Color(255, 204, 204)); 

	        // 2. Main Content Area
	        contentPanel = new JPanel();
	        contentPanel.setLayout(new BorderLayout());
	        contentPanel.setBackground(Color.WHITE);
	        
	        JLabel welcomeMsg = new JLabel("Welcome to APU Automotive Service Centre", SwingConstants.LEFT);
	        // Scale welcome font larger for different screens
	        int welcomeFontSize = Math.max(20, (int) (screenSize.width * 0.03));
	        welcomeMsg.setFont(new Font("Arial", Font.BOLD, welcomeFontSize)); 
	        
	        welcomeMsg.setBorder(new EmptyBorder(30,50,0,20));
	        contentPanel.add(welcomeMsg, BorderLayout.NORTH);

	        add(sidebar, BorderLayout.WEST);
	        add(contentPanel, BorderLayout.CENTER);
	    }

	private void addNavButton(String text, Color bgColor) {
		JButton btn = new JButton(text);

		// Fixed: Added missing closing parenthesis
		// Compute sizes dynamically from the sidebar width and screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int sidebarWidth = sidebar.getPreferredSize().width;
		int btnWidth = Math.max(160, (int) (sidebarWidth * 0.85));
		int btnHeight = Math.max(40, (int) (screenSize.height * 0.06));

		int btnFontSize = Math.max(12, (int) (screenSize.width * 0.0125));
		btn.setFont(new Font("Arial", Font.PLAIN, btnFontSize));

		btn.setBackground(bgColor);
		btn.setForeground(Color.BLACK);
		btn.setFocusPainted(false);

		Dimension btnSize = new Dimension(btnWidth, btnHeight);
		btn.setPreferredSize(btnSize);
		btn.setMaximumSize(btnSize);
		btn.setMinimumSize(btnSize);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);

		btn.addActionListener(e -> {
			if (text.equals("Logout")) {
				new LoginPage().setVisible(true);
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