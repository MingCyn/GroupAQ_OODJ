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

		// Try classpath resource first, then fallback to common filesystem locations
		Image logoImg = null;
		java.net.URL logoUrl = getClass().getResource("/images/logo.png");
		if (logoUrl != null) {
			logoImg = new ImageIcon(logoUrl).getImage();
		} else {
			java.io.File f = new java.io.File("images/logo.png");
			if (f.exists()) {
				logoImg = new ImageIcon(f.getAbsolutePath()).getImage();
			} else {
				f = new java.io.File("src/images/logo.png");
				if (f.exists()) {
					logoImg = new ImageIcon(f.getAbsolutePath()).getImage();
				}
			}
		}
		if (logoImg != null) {
			this.setIconImage(logoImg);
		}

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// 1. Sidebar Setup
		sidebar = new JPanel();
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(new Color(211, 238, 252));

		// Responsive sidebar width
		int sWidth = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.2);
		sWidth = Math.max(200, Math.min(sWidth, 350));
		sidebar.setPreferredSize(new Dimension(sWidth, 0));
		sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

		// User Profile Section
		lblUserName = new JLabel(userName);
		// Responsive font
		int fontTitleSize = Math.max(16, (int) (sWidth * 0.08));
		lblUserName.setFont(new Font("Arial", Font.BOLD, fontTitleSize));
		lblUserName.setAlignmentX(Component.CENTER_ALIGNMENT);

		String userId = "";
		try {
			java.util.List<String> lines = java.nio.file.Files
					.readAllLines(java.nio.file.Paths.get("data", "account.txt"));
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
		lblUserId.setFont(new Font("Arial", Font.PLAIN, Math.max(12, fontTitleSize - 4)));
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
		sidebarScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		// 2. Main Content Area
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);

		JLabel welcomeMsg = new JLabel("Welcome to APU Automotive Service Centre", SwingConstants.LEFT);
		int welcomeFontSize = Math.max(20,
				Math.min(36, (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.025)));
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

		// Responsive button font
		int btnFontSize = Math.max(14,
				Math.min(20, (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.015)));
		btn.setFont(new Font("Arial", Font.PLAIN, btnFontSize));

		btn.setBackground(bgColor);
		btn.setForeground(Color.BLACK);
		btn.setFocusPainted(false);

		// Make buttons fill width but have fixed height
		Dimension btnSize = new Dimension(Integer.MAX_VALUE, 60);
		btn.setMaximumSize(btnSize);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);

		btn.addActionListener(e -> {
			if (text.equals("Logout")) {
				new LoginPage().setVisible(true);
				dispose();
			} else if (text.equals("Appointments")) {
				contentPanel.removeAll();
				String detectedRole = userRole; // Default to the role passed to HomePage
				String technicianID = ""; // Store technician ID

				// Read account.txt to verify role (assuming role is column 4 / index 3)
				try {
					java.util.List<String> lines = java.nio.file.Files
							.readAllLines(java.nio.file.Paths.get("data", "account.txt"));
					for (String line : lines) {
						String[] parts = line.split(",");
						if (parts.length >= 4 && parts[1].trim().equalsIgnoreCase(lblUserName.getText().trim())) {
							detectedRole = parts[3].trim();
							technicianID = parts[0].trim(); // Get the user ID (first column) for technician
							break;
						}
					}
				} catch (java.io.IOException ex) {
					ex.printStackTrace();
				}

				// Show appointment page based on user role
				// Customer role: CustomerAppointment
				// Admin & CounterStaff roles: AdminAppointment (shared through inheritance)
				// Technician role: TechnicianAppointment (separate implementation)
				if (detectedRole.equalsIgnoreCase("Customer")) {
					contentPanel.add(new CustomerAppointment(), BorderLayout.CENTER);
				} else if (detectedRole.equalsIgnoreCase("Technician")) {
					// Technician has dedicated TechnicianAppointment page
					// Pass the technician ID so they only see their assigned appointments
					contentPanel.add(new TechnicianAppointment(detectedRole, technicianID), BorderLayout.CENTER);
				} else if (detectedRole.equalsIgnoreCase("Admin") || detectedRole.equalsIgnoreCase("CounterStaff")) {
					// Admin and CounterStaff share AdminAppointment through inheritance
					contentPanel.add(new AdminAppointment(detectedRole), BorderLayout.CENTER);
				} else {
					// Fallback with default role
					contentPanel.add(new AdminAppointment(detectedRole), BorderLayout.CENTER);
				}
				contentPanel.revalidate();
				contentPanel.repaint();
			} else {
				System.out.println("Navigating to: " + text);
			}
		});

		sidebar.add(btn);
		sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
	}

	/**
	 * Navigate to a new page by replacing the contentPanel
	 * @param page The JPanel to display
	 */
	public void showPage(JPanel page) {
		contentPanel.removeAll();
		contentPanel.add(page, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	/**
	 * Get the current user ID
	 * @return The user ID from the sidebar label
	 */
	public String getUserId() {
		return lblUserId.getText();
	}

	/**
	 * Get the current username
	 * @return The username from the sidebar label
	 */
	public String getUsername() {
		return lblUserName.getText();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new HomePage("Admin", "JOHN DOE").setVisible(true));
	}
}