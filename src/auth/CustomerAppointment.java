package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class CustomerAppointment extends JPanel {

    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField contactNumberField;
    private JTextField carModelField;
    private JTextField carPlateField;
    private JTextField serviceAddOnField;
    private JTextField remarksField;

    public CustomerAppointment() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(20, 30, 20, 30));

        add(new JScrollPane(mainContainer), BorderLayout.CENTER);

        // ================= HEADER =================
        JLabel titleLabel = new JLabel("Appointment Creation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator();

        mainContainer.add(titleLabel);
        mainContainer.add(Box.createVerticalStrut(5));
        mainContainer.add(separator);
        mainContainer.add(Box.createVerticalStrut(15));

        // ================= TOP INFO =================
        JPanel topInfoPanel = new JPanel(new GridBagLayout());
        topInfoPanel.setBackground(Color.WHITE);
        topInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180)); // limit height

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0; // prevent stretching

        // LEFT: IMAGE
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        Image logoImg = null;
        URL logoUrl = getClass().getResource("/images/logo.png");
        if (logoUrl != null) {
            logoImg = new ImageIcon(logoUrl).getImage();
        } else {
            File f = new File("images/logo.png");
            if (f.exists()) {
                logoImg = new ImageIcon(f.getAbsolutePath()).getImage();
            }
        }

        if (logoImg != null) {
            Image scaledImg = logoImg.getScaledInstance(250, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
        } else {
            imageLabel.setText("[ Logo Image ]");
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        topInfoPanel.add(imageLabel, gbc);

        // RIGHT: CONTACT
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBackground(Color.WHITE);

        JLabel c1 = new JLabel("Contact Us to book an");
        c1.setFont(new Font("Arial", Font.BOLD, 40));
        JLabel c2 = new JLabel("Appointment");
        c2.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel sub = new JLabel("WhatsApp, Call or Email us");
        sub.setForeground(Color.DARK_GRAY);

        JLabel phone = new JLabel("📞 +(60)127744638");
        phone.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel email = new JLabel("✉ @car8service@gmail.com");
        email.setFont(new Font("Arial", Font.PLAIN, 18));

        contactPanel.add(c1);
        contactPanel.add(c2);
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(sub);
        contactPanel.add(Box.createVerticalStrut(10));
        contactPanel.add(phone);
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(email);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        topInfoPanel.add(contactPanel, gbc);

        mainContainer.add(topInfoPanel);
        mainContainer.add(Box.createVerticalStrut(10)); // reduce space after top panel

        // ================= BANNER =================
        JLabel banner = new JLabel("Book your Service", SwingConstants.CENTER);
        banner.setOpaque(true);
        banner.setBackground(new Color(193, 230, 252));
        banner.setBorder(new EmptyBorder(12, 10, 12, 10));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContainer.add(banner);
        mainContainer.add(Box.createVerticalStrut(5)); // reduced gap

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(6, 10, 6, 10);
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1;
        f.weighty = 0; // prevent extra vertical spacing
        f.anchor = GridBagConstraints.NORTH; // align at top

        int row = 0;

        // Add Full Name and Service Add-on on the same row
        fullNameField = new JTextField();
        serviceAddOnField = new JTextField();
        row = addField(formPanel, f, row, "Full Name", fullNameField, "Service Add-on (Optional)", serviceAddOnField);
        
        // Continue with remaining fields
        emailField = new JTextField();
        remarksField = new JTextField();
        row = addField(formPanel, f, row, "Email", emailField, "Remarks", remarksField);
        
        // Create "Check Available TimeSlots" button with action listener
        contactNumberField = new JTextField();
        JButton checkSlotBtn = new JButton("Check Available TimeSlots");
        checkSlotBtn.addActionListener(e -> navigateToCustomerTimeSlotPage());
        row = addField(formPanel, f, row, "Contact Number", contactNumberField, "Slot", checkSlotBtn);
        
        // Add Car Model field (left side only)
        f.gridy = row;
        f.gridx = 0;
        formPanel.add(new JLabel("Car Model"), f);
        f.gridx = 1;
        carModelField = new JTextField();
        formPanel.add(carModelField, f);
        row++;

        // Last row
        f.gridy = row;
        f.gridx = 0;
        formPanel.add(new JLabel("Car Plate"), f);

        f.gridx = 1;
        carPlateField = new JTextField();
        formPanel.add(carPlateField, f);

        // Add filler to push content to top
        f.gridy = row + 1;
        f.gridx = 0;
        f.weighty = 1.0;
        f.fill = GridBagConstraints.BOTH;
        formPanel.add(Box.createVerticalGlue(), f);

        mainContainer.add(formPanel);
    }

    private int addField(JPanel panel, GridBagConstraints f, int row,
            String label1, JComponent field1,
            String label2, JComponent field2) {

        f.gridy = row;

        f.gridx = 0;
        panel.add(new JLabel(label1), f);

        f.gridx = 1;
        panel.add(field1, f);

        f.gridx = 2;
        panel.add(new JLabel(label2), f);

        f.gridx = 3;
        panel.add(field2, f);

        return row + 1;
    }
    
    /**
     * Navigates from CustomerAppointment to CustomerTimeSlotPage
     * Captures and passes form data
     */
    private void navigateToCustomerTimeSlotPage() {
        // Validate that all fields are filled
        if (fullNameField.getText().trim().isEmpty() || 
            emailField.getText().trim().isEmpty() ||
            contactNumberField.getText().trim().isEmpty() ||
            carModelField.getText().trim().isEmpty() ||
            carPlateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create AppointmentData object with form values
        // Set optional fields as NULL if empty
        String serviceAddOn = serviceAddOnField.getText().trim();
        String remarks = remarksField.getText().trim();
        
        AppointmentData appointmentData = new AppointmentData(
            fullNameField.getText().trim(),
            emailField.getText().trim(),
            contactNumberField.getText().trim(),
            carModelField.getText().trim(),
            carPlateField.getText().trim(),
            serviceAddOn.isEmpty() ? "NULL" : serviceAddOn,
            remarks.isEmpty() ? "NULL" : remarks
        );

        // Get current logged-in user from parent HomePage
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof HomePage) {
            HomePage homePage = (HomePage) frame;
            appointmentData.setCustomerID(homePage.getUserId());
            homePage.showPage(new CustomerTimeSlotPage(appointmentData));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage("Customer", "JOHN DOE").setVisible(true));
    }
}