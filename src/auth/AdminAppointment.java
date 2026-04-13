package auth;

import javax.swing.*;

/**
 * AdminAppointment class extends StaffAppointmentPage to implement
 * the inheritance OOP pillar. Both Admin and CounterStaff roles
 * access the same appointment booking functionality through this class.
 * 
 * This class inherits all common appointment functionality from
 * StaffAppointmentPage,
 * allowing code reuse and maintaining a single source of truth for appointment
 * UI.
 */

public class AdminAppointment extends StaffAppointmentPage {

    public AdminAppointment(String userRole) {
        super(userRole);
    }

    public AdminAppointment() {
        super("Admin");
    }

    @Override
    protected String getPageTitle() {
        return "Create Appointment For Walk-in/Phone-in Customers";
    }

    /**
     * Override createActionPanel to show only "View Appointment" button
     * (without "Confirm Booking" button) for admin appointments
     */
    @Override
    protected JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 5));
        actionPanel.setBackground(java.awt.Color.WHITE);

        JButton viewAppointmentBtn = createStyledButton("View Appointment", new java.awt.Color(245, 240, 230), java.awt.Color.BLACK);
        viewAppointmentBtn.setPreferredSize(new java.awt.Dimension(220, 60));
        viewAppointmentBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

        actionPanel.add(viewAppointmentBtn);
        actionPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new java.awt.Dimension(1230, 90));

        return actionPanel;
    }

    /**
     * Override createServiceSection to add click handlers to time buttons
     */
    @Override
    protected JPanel createServiceSection(String title, String[] times) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        sectionLabel.setForeground(new java.awt.Color(120, 120, 120));
        sectionLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JPanel servicePanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 18));
        servicePanel.setBackground(java.awt.Color.WHITE);

        String serviceType = title.equals("Normal Services") ? "Normal" : "Major";

        for (String time : times) {
            JButton timeBtn = createStyledButton(time, new java.awt.Color(220, 220, 220), java.awt.Color.BLACK);
            timeBtn.setPreferredSize(new java.awt.Dimension(175, 55));
            timeBtn.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

            // Add listener to show appointment dialog
            timeBtn.addActionListener(e -> {
                // Check if a day is selected first
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Please select a date before choosing a time slot.", 
                        "No Date Selected", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String[] timeParts = time.split(" - ");
                String startTime = timeParts[0].trim();
                String endTime = timeParts[1].trim();
                showAppointmentDialog(serviceType, startTime, endTime, selectedDate);
            });

            servicePanel.add(timeBtn);
        }

        servicePanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        servicePanel.setMaximumSize(new java.awt.Dimension(1230, 175));

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(java.awt.Color.WHITE);
        containerPanel.add(sectionLabel);
        containerPanel.add(Box.createRigidArea(new java.awt.Dimension(0, 3)));
        containerPanel.add(servicePanel);

        return containerPanel;
    }

    /**
     * Show a dialog to collect appointment details from admin/counter staff
     */
    private void showAppointmentDialog(String serviceType, String startTime, String endTime, java.time.LocalDate appointmentDate) {
        JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Appointment Creation", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(0, 0, 10, 0);

        int row = 0;

        // Title
        JLabel titleLabel = new JLabel("Appointment Creation");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        gbc.gridy = row++;
        panel.add(titleLabel, gbc);

        gbc.insets = new java.awt.Insets(0, 0, 5, 0);

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name");
        gbc.gridy = row++;
        panel.add(fullNameLabel, gbc);
        
        JTextField fullNameField = new JTextField();
        setPlaceholder(fullNameField, "e.g. John Doe");
        gbc.gridy = row++;
        panel.add(fullNameField, gbc);

        // Contact Number
        JLabel contactLabel = new JLabel("Contact Number");
        gbc.gridy = row++;
        panel.add(contactLabel, gbc);
        
        JTextField contactField = new JTextField();
        setPlaceholder(contactField, "e.g. +60123456789");
        gbc.gridy = row++;
        panel.add(contactField, gbc);

        // Car Model
        JLabel carModelLabel = new JLabel("Car Model");
        gbc.gridy = row++;
        panel.add(carModelLabel, gbc);
        
        JTextField carModelField = new JTextField();
        setPlaceholder(carModelField, "e.g. Toyota Camry");
        gbc.gridy = row++;
        panel.add(carModelField, gbc);

        // Car Plate
        JLabel carPlateLabel = new JLabel("Car Plate");
        gbc.gridy = row++;
        panel.add(carPlateLabel, gbc);
        
        JTextField carPlateField = new JTextField();
        setPlaceholder(carPlateField, "e.g. VCG8888");
        gbc.gridy = row++;
        panel.add(carPlateField, gbc);

        // Service Add-on (Optional)
        JLabel serviceAddOnLabel = new JLabel("Services Add-on (Optional)");
        gbc.gridy = row++;
        panel.add(serviceAddOnLabel, gbc);
        
        JTextField serviceAddOnField = new JTextField();
        setPlaceholder(serviceAddOnField, "e.g. Tyre Alignment");
        gbc.gridy = row++;
        panel.add(serviceAddOnField, gbc);

        // Remarks
        JLabel remarksLabel = new JLabel("Remarks (Optional)");
        gbc.gridy = row++;
        panel.add(remarksLabel, gbc);
        
        JTextArea remarksArea = new JTextArea(3, 20);
        setPlaceholder(remarksArea, "e.g. Customer requested additional cleaning");
        JScrollPane remarksScroll = new JScrollPane(remarksArea);
        gbc.gridy = row++;
        gbc.weighty = 1.0;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        panel.add(remarksScroll, gbc);

        // Buttons
        gbc.weighty = 0;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(15, 0, 0, 0);
        gbc.gridy = row++;
        
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));
        
        // Cancel button - outlined style
        JButton cancelBtn = createOutlinedButton("Cancel");
        cancelBtn.setPreferredSize(new java.awt.Dimension(120, 45));
        
        // Confirm button - filled style
        JButton confirmBtn = createFilledButton("Confirm");
        confirmBtn.setPreferredSize(new java.awt.Dimension(120, 45));

        cancelBtn.addActionListener(e -> dialog.dispose());
        confirmBtn.addActionListener(e -> {
            // Validate required fields
            String fullNameText = fullNameField.getText().trim();
            String contactText = contactField.getText().trim();
            String carModelText = carModelField.getText().trim();
            String carPlateText = carPlateField.getText().trim();
            String serviceAddOnText = serviceAddOnField.getText().trim();
            String remarksText = remarksArea.getText().trim();

            // Check for empty values (not placeholders, but actual empty)
            if (fullNameText.isEmpty() || fullNameText.equals("e.g. John Doe") ||
                contactText.isEmpty() || contactText.equals("e.g. Anderson Lau") ||
                carModelText.isEmpty() || carModelText.equals("e.g. Toyota") ||
                carPlateText.isEmpty() || carPlateText.equals("e.g. VCG8888")) {
                
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter all the required field to create appointment", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Optional fields - replace with NULL if empty or contains placeholder
            String finalServiceAddOn = serviceAddOnText.isEmpty() || serviceAddOnText.equals("e.g. Tyre Alignment") ? "NULL" : serviceAddOnText;
            String finalRemarks = remarksText.isEmpty() || remarksText.equals("e.g. Customer requested additional cleaning") ? "NULL" : remarksText;

            // Save appointment
            boolean success = saveAdminAppointment(
                fullNameText,
                contactText,
                carModelText,
                carPlateText,
                finalServiceAddOn,
                finalRemarks,
                serviceType,
                startTime,
                endTime,
                appointmentDate
            );

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Appointment created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to create appointment", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmBtn);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**
     * Save admin-created appointment to Appointment.txt
     */
    private boolean saveAdminAppointment(String fullName, String contactNumber, String carModel, 
                                        String carPlate, String serviceAddOn, String remarks, 
                                        String serviceType, String startTime, String endTime, java.time.LocalDate appointmentDate) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get("data", "Appointment.txt");
            java.nio.file.Files.createDirectories(filePath.getParent());

            // Check if file exists and has header
            boolean fileExists = java.nio.file.Files.exists(filePath);
            boolean hasHeader = false;
            
            if (fileExists) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(filePath);
                if (!lines.isEmpty() && lines.get(0).startsWith("#")) {
                    hasHeader = true;
                }
            }

            // Generate next Appointment ID
            String appointmentID = generateNextAppointmentID(filePath);

            // Format appointment date
            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy");
            String bookingDate = appointmentDate.format(dateFormatter);

            // Format: AppointmentID, CustomerID, Username, fullName, ServiceType, ContactNumber, CarModel, CarPlate, ServiceAddOn, Remarks, TechnicianInCharge, TechnicianID, BookingDate, StartTime, EndTime, Price, Status
            String appointmentRecord = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                appointmentID,
                "NULL", // CustomerID (NULL for admin booking)
                "NULL", // Username (NULL for admin booking)
                fullName,
                serviceType,
                contactNumber,
                carModel,
                carPlate,
                serviceAddOn,
                remarks,
                "NULL", // Technician In-Charge
                "NULL", // TechnicianID
                bookingDate,
                startTime,
                endTime,
                "NULL", // Price
                "pending" // Status
            );

            // If file doesn't exist or doesn't have header, add header first
            if (!fileExists || !hasHeader) {
                String header = "#appointmentid,customerid,username,fullname,servicetype,contact number,car model,car plate,service add on,remarks,technician incharge,technicianID,booking date,start time,end time,price,status\n";
                
                if (fileExists && !hasHeader) {
                    // File exists but no header - read existing content and rewrite with header
                    java.util.List<String> existingLines = java.nio.file.Files.readAllLines(filePath);
                    java.util.List<String> newContent = new java.util.ArrayList<>();
                    newContent.add(header.trim());
                    newContent.addAll(existingLines);
                    java.nio.file.Files.write(filePath, newContent);
                } else {
                    // File doesn't exist - create with header
                    java.nio.file.Files.write(filePath, header.getBytes(), 
                        java.nio.file.StandardOpenOption.CREATE);
                }
            }

            // Append the new appointment record
            java.nio.file.Files.write(filePath, (appointmentRecord + "\n").getBytes(), 
                java.nio.file.StandardOpenOption.APPEND);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generate the next Appointment ID
     */
    private String generateNextAppointmentID(java.nio.file.Path filePath) {
        try {
            if (!java.nio.file.Files.exists(filePath)) {
                return "APTID0001";
            }

            java.util.List<String> lines = java.nio.file.Files.readAllLines(filePath);
            if (lines.isEmpty()) {
                return "APTID0001";
            }

            // Find the last non-header line
            String lastLine = "";
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (!lines.get(i).startsWith("#")) {
                    lastLine = lines.get(i);
                    break;
                }
            }

            if (lastLine.isEmpty()) {
                return "APTID0001";
            }

            String lastID = lastLine.split(",")[0];
            int idNumber = Integer.parseInt(lastID.substring(5)); // Remove "APTID" prefix
            idNumber++;
            return String.format("APTID%04d", idNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return "APTID0001";
        }
    }

    /**
     * Set placeholder text for JTextField
     */
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(new java.awt.Color(150, 150, 150));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new java.awt.Color(150, 150, 150));
                }
            }
        });
    }

    /**
     * Set placeholder text for JTextArea
     */
    private void setPlaceholder(JTextArea area, String placeholder) {
        area.setText(placeholder);
        area.setForeground(new java.awt.Color(150, 150, 150));
        
        area.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(placeholder);
                    area.setForeground(new java.awt.Color(150, 150, 150));
                }
            }
        });
    }

    /**
     * Create outlined button (Cancel style)
     */
    private JButton createOutlinedButton(String text) {
        return new RoundedOutlinedButton(text);
    }

    /**
     * Create filled button (Confirm style)
     */
    private JButton createFilledButton(String text) {
        return new RoundedFilledButton(text);
    }

    /**
     * Inner class for rounded outlined button
     */
    private class RoundedOutlinedButton extends JButton {
        private static final int ARC_WIDTH = 15;
        private static final int ARC_HEIGHT = 15;

        public RoundedOutlinedButton(String text) {
            super(text);
            setBackground(java.awt.Color.WHITE);
            setForeground(java.awt.Color.BLACK);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw border
            g2.setColor(java.awt.Color.BLACK);
            g2.setStroke(new java.awt.BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            
            super.paintComponent(g);
        }
    }

    /**
     * Inner class for rounded filled button
     */
    private class RoundedFilledButton extends JButton {
        private static final int ARC_WIDTH = 15;
        private static final int ARC_HEIGHT = 15;

        public RoundedFilledButton(String text) {
            super(text);
            setBackground(new java.awt.Color(60, 140, 210));
            setForeground(java.awt.Color.WHITE);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw filled background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage("Admin", "AdminAPU").setVisible(true));
    }
}