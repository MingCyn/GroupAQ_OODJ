package auth;

import javax.swing.*;

/**
 * CustomerTimeSlotPage extends StaffAppointmentPage to reuse common appointment
 * UI components (header, week navigation, days selection, services sections,
 * footer).
 * 
 * This class demonstrates the inheritance OOP pillar by:
 * - Inheriting all layout and UI from StaffAppointmentPage
 * - Overriding only the action buttons (section 6) to provide customer-specific
 * functionality
 * - This is a concrete implementation for customer appointment booking
 */
public class CustomerTimeSlotPage extends StaffAppointmentPage {
    private AppointmentData appointmentData;
    private String selectedServiceType = null; // "Normal" or "Major"
    private String selectedStartTime = null;
    private String selectedEndTime = null;
    private java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy");
    private javax.swing.JButton previousSelectedButton = null; // Track the previously selected button globally

    public CustomerTimeSlotPage(AppointmentData appointmentData) {
        super("Customer");
        this.appointmentData = appointmentData;
    }

    // Keep empty constructor for back navigation
    public CustomerTimeSlotPage() {
        super("Customer");
        this.appointmentData = null;
    }

    @Override
    protected String getPageTitle() {
        return "Book Your TimeSlot";
    }

    /**
     * Override createServiceSection to add click handlers to time buttons
     */
    @Override
    protected javax.swing.JPanel createServiceSection(String title, String[] times) {
        javax.swing.JLabel sectionLabel = new javax.swing.JLabel(title);
        sectionLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        sectionLabel.setForeground(new java.awt.Color(120, 120, 120));
        sectionLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        javax.swing.JPanel servicePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 18));
        servicePanel.setBackground(java.awt.Color.WHITE);

        String serviceType = title.equals("Normal Services") ? "Normal" : "Major";

        for (String time : times) {
            javax.swing.JButton timeBtn = createStyledButton(time, new java.awt.Color(220, 220, 220), java.awt.Color.BLACK);
            timeBtn.setPreferredSize(new java.awt.Dimension(175, 55));
            timeBtn.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

            // Add listener to track selected time slot
            timeBtn.addActionListener(e -> {
                // Reset the previously selected button (across all service types)
                if (previousSelectedButton != null) {
                    previousSelectedButton.setBackground(new java.awt.Color(220, 220, 220));
                    previousSelectedButton.setForeground(java.awt.Color.BLACK);
                }
                
                selectedServiceType = serviceType;
                String[] timeParts = time.split(" - ");
                selectedStartTime = timeParts[0].trim();
                selectedEndTime = timeParts[1].trim();
                // Visual feedback - highlight the selected button
                timeBtn.setBackground(new java.awt.Color(100, 150, 200));
                timeBtn.setForeground(java.awt.Color.WHITE);
                
                // Track this as the currently selected button
                previousSelectedButton = timeBtn;
            });

            servicePanel.add(timeBtn);
        }

        servicePanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        servicePanel.setMaximumSize(new java.awt.Dimension(1230, 175));

        javax.swing.JPanel containerPanel = new javax.swing.JPanel();
        containerPanel.setLayout(new javax.swing.BoxLayout(containerPanel, javax.swing.BoxLayout.Y_AXIS));
        containerPanel.setBackground(java.awt.Color.WHITE);
        containerPanel.add(sectionLabel);
        containerPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 3)));
        containerPanel.add(servicePanel);

        return containerPanel;
    }

    /**
     * Overrides the action buttons to provide customer-specific buttons.
     * Section 6 - Action Buttons (Customer Version)
     * 
     * @return Custom action panel with customer buttons
     */
    @Override
    protected JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 5));
        actionPanel.setBackground(java.awt.Color.WHITE);

        // Customer-specific action buttons (you can customize these as needed)
        JButton cancelBtn = createStyledButton("Back", new java.awt.Color(245, 240, 230), java.awt.Color.BLACK);
        cancelBtn.setPreferredSize(new java.awt.Dimension(220, 60));
        cancelBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        cancelBtn.addActionListener(e -> navigateBackToCustomerAppointment());

        JButton confirmBtn = createStyledButton("Book Appointment", new java.awt.Color(60, 140, 210),
                java.awt.Color.WHITE);
        confirmBtn.setPreferredSize(new java.awt.Dimension(220, 60));
        confirmBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        confirmBtn.addActionListener(e -> bookAppointment());

        actionPanel.add(cancelBtn);
        actionPanel.add(confirmBtn);
        actionPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new java.awt.Dimension(1230, 90));

        return actionPanel;
    }

    /**
     * Navigates back from CustomerTimeSlotPage to CustomerAppointmentPage
     */
    private void navigateBackToCustomerAppointment() {
        // Find the parent HomePage frame
        javax.swing.JFrame frame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        if (frame instanceof HomePage) {
            HomePage homePage = (HomePage) frame;
            homePage.showPage(new CustomerAppointment());
        }
    }

    /**
     * Book the appointment and save to file
     */
    private void bookAppointment() {
        // Validation
        if (selectedDate == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a date", "Booking Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedServiceType == null || selectedStartTime == null || selectedEndTime == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a time slot", "Booking Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (appointmentData == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Appointment data is missing", "Booking Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Set additional appointment data
        appointmentData.setServiceType(selectedServiceType);
        appointmentData.setBookingDate(selectedDate.format(dateFormatter));
        appointmentData.setStartTime(selectedStartTime);
        appointmentData.setEndTime(selectedEndTime);

        // Get username from parent HomePage
        javax.swing.JFrame frame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
        if (frame instanceof HomePage) {
            HomePage homePage = (HomePage) frame;
            String username = homePage.getUsername();
            
            // Book appointment and save to file
            boolean success = saveAppointmentToFile(appointmentData, username);
            if (success) {
                javax.swing.JOptionPane.showMessageDialog(this, "Appointment booked successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                // Navigate back to appointment page
                homePage.showPage(new CustomerAppointment());
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to book appointment", "Booking Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Save appointment to Appointment.txt file
     */
    private boolean saveAppointmentToFile(AppointmentData data, String username) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get("data", "Appointment.txt");
            java.nio.file.Files.createDirectories(filePath.getParent());

            // Generate next Appointment ID
            String appointmentID = generateNextAppointmentID(filePath);
            data.setAppointmentID(appointmentID);

            // Format: AppointmentID, CustomerID, Username, FullName, ServiceType, ContactNumber, CarModel, CarPlate, ServiceAddOn, Remarks, TechnicianInCharge, TechnicianID, BookingDate, StartTime, EndTime, Price, Status
            String appointmentRecord = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                appointmentID,
                data.getCustomerID(),
                username,
                data.getFullName(),
                data.getServiceType(),
                data.getContactNumber(),
                data.getCarModel(),
                data.getCarPlate(),
                data.getServiceAddOn(),
                data.getRemarks(),
                "NULL", // Technician In-Charge
                "NULL", // TechnicianID
                data.getBookingDate(),
                data.getStartTime(),
                data.getEndTime(),
                "NULL", // Price
                "pending" // Status
            );

            // Append to file
            java.nio.file.Files.write(filePath, (appointmentRecord + "\n").getBytes(), 
                java.nio.file.StandardOpenOption.CREATE, 
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

            // Get last line and extract ID
            String lastLine = lines.get(lines.size() - 1);
            String lastID = lastLine.split(",")[0];
            int idNumber = Integer.parseInt(lastID.substring(5)); // Remove "APTID" prefix
            idNumber++;
            return String.format("APTID%04d", idNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return "APTID0001";
        }
    }
}