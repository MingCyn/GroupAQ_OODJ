package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class CustomerAppointment extends JPanel {

    public CustomerAppointment() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // A wrapper panel to constrain the maximum width and keep it centered
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.setBackground(Color.WHITE);

        // Main content container
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainContainer.setPreferredSize(new Dimension(900, 800));

        // 1. Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(850, 50));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Appointment Creation");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.GRAY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(separator, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        mainContainer.add(headerPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. Top Info Section (Image + Contact Info)
        JPanel topInfoPanel = new JPanel(new BorderLayout(20, 0));
        topInfoPanel.setBackground(Color.WHITE);
        topInfoPanel.setMaximumSize(new Dimension(850, 250));
        topInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Top Left: Image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(450, 220));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // optional frame for visibility
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Load Logo Image
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
            Image scaledImg = logoImg.getScaledInstance(450, 220, java.awt.Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
            imageLabel.setBorder(null); // remove border if image is found
        } else {
            imageLabel.setText("[ Logo Image Placeholder ]");
        }

        topInfoPanel.add(imageLabel, BorderLayout.WEST);

        // Top Right: Contact Info
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBackground(Color.WHITE);
        contactPanel.setBorder(new EmptyBorder(10, 20, 10, 10));

        JLabel contactTitle1 = new JLabel("Contact US to book an");
        contactTitle1.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel contactTitle2 = new JLabel("Appointment");
        contactTitle2.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("WhatsApps, Call or Email us at");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.DARK_GRAY);

        // Phone and Email Labels
        JLabel phoneLabel = new JLabel("\uD83D\uDCDE +(60)127744638"); // Phone Emoji as icon replacement
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel emailLabel = new JLabel("\u2709 @car8service@gmail.com"); // Envelope Emoji as icon replacement
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        contactPanel.add(contactTitle1);
        contactPanel.add(contactTitle2);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactPanel.add(subtitle);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contactPanel.add(phoneLabel);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contactPanel.add(emailLabel);

        topInfoPanel.add(contactPanel, BorderLayout.CENTER);

        mainContainer.add(topInfoPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. Online Appointment Section
        JLabel makeApptLabel = new JLabel("Make an online appointment");
        makeApptLabel.setFont(new Font("Arial", Font.BOLD, 20));
        makeApptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(makeApptLabel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        // Banner Button
        JLabel bookBanner = new JLabel("Book your Service", SwingConstants.CENTER);
        bookBanner.setOpaque(true);
        bookBanner.setBackground(new Color(193, 230, 252));
        bookBanner.setFont(new Font("Arial", Font.PLAIN, 16));
        bookBanner.setMaximumSize(new Dimension(850, 40));
        bookBanner.setPreferredSize(new Dimension(850, 40));
        bookBanner.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Slightly rounded corners effect (using empty border just for spacing)
        bookBanner.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainContainer.add(bookBanner);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // 4. Form Section
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(850, 250));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left Column of Form
        JPanel leftCol = new JPanel(new GridBagLayout());
        leftCol.setBackground(Color.WHITE);
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(10, 10, 10, 10);
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
        gbcLeft.anchor = GridBagConstraints.WEST;

        addFormField(leftCol, gbcLeft, 0, "Full Name", new JTextField(15));
        addFormField(leftCol, gbcLeft, 1, "Email:", new JTextField(15));
        addFormField(leftCol, gbcLeft, 2, "Contact Number:", new JTextField(15));
        addFormField(leftCol, gbcLeft, 3, "Car Models", new JTextField(15));
        addFormField(leftCol, gbcLeft, 4, "Car Plate Number:", new JTextField(15));

        // Create Top Right Container Structure
        JPanel rightCol = new JPanel(new GridBagLayout());
        rightCol.setBackground(Color.WHITE);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(10, 10, 10, 10);
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.anchor = GridBagConstraints.WEST;

        addFormField(rightCol, gbcRight, 0, "Select type of Services:",
                new JComboBox<>(new String[] { "", "Normal Service", "Major Service" }));
        addFormField(rightCol, gbcRight, 1, "Service add on:", new JTextField(15));
        addFormField(rightCol, gbcRight, 2, "Others Remarks:", new JTextField(15));

        // Custom appointment slot field for right col
        gbcRight.gridy = 3;
        gbcRight.gridx = 0;
        gbcRight.weightx = 0.3;
        JLabel slotLabel = new JLabel("Appointment Slots:");
        slotLabel.setFont(new Font("Arial", Font.BOLD, 12));
        rightCol.add(slotLabel, gbcRight);

        gbcRight.gridx = 1;
        gbcRight.weightx = 0.7;
        JButton checkSlotBtn = new JButton("Check available time slot");
        checkSlotBtn.setBackground(new Color(60, 140, 210));
        checkSlotBtn.setForeground(Color.WHITE);
        checkSlotBtn.setFocusPainted(false);
        checkSlotBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        checkSlotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // This button can be used to navigate to AppointmentTimeSlot later
        rightCol.add(checkSlotBtn, gbcRight);

        // Push right column up so fields align with left
        gbcRight.gridy = 4;
        gbcRight.weighty = 1.0;
        rightCol.add(Box.createGlue(), gbcRight);

        formPanel.add(leftCol);
        formPanel.add(rightCol);

        mainContainer.add(formPanel);

        wrapperPanel.add(mainContainer);
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent inputComp) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 0.4;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        if (inputComp instanceof JTextField) {
            ((JTextField) inputComp).setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        panel.add(inputComp, gbc);
    }
}