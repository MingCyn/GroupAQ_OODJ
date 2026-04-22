package auth;

import javax.swing.*;
import java.awt.*;

/**
 * RescheduleTimeSlotPage extends StaffAppointmentPage to allow customers 
 * to reschedule their existing appointments by selecting a new date and time.
 * 
 * This class demonstrates the inheritance OOP pillar by:
 * - Inheriting all layout and UI from StaffAppointmentPage
 * - Overriding the action buttons to provide reschedule-specific functionality
 * - Allowing customers to select a new date and time for rescheduling
 */
public class RescheduleTimeSlotPage extends StaffAppointmentPage {
    private String selectedServiceType = null; // "Normal" or "Major"
    private String selectedStartTime = null;
    private String selectedEndTime = null;
    private java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy");
    private JButton previousSelectedButton = null;
    private String[] currentAppointment;
    private MyAppointment myAppointmentPanel;

    public RescheduleTimeSlotPage(String[] appointmentData, MyAppointment myAppointmentPanel) {
        super("Customer");
        this.currentAppointment = appointmentData;
        this.myAppointmentPanel = myAppointmentPanel;
    }

    @Override
    protected String getPageTitle() {
        return "Reschedule Your Appointment";
    }

    /**
     * Override createServiceSection to add click handlers to time buttons
     */
    @Override
    protected JPanel createServiceSection(String title, String[] times) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionLabel.setForeground(new Color(120, 120, 120));
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        servicePanel.setBackground(Color.WHITE);

        String serviceType = title.equals("Normal Services") ? "Normal" : "Major";

        for (String time : times) {
            JButton timeBtn = createStyledButton(time, new Color(220, 220, 220), Color.BLACK);
            timeBtn.setPreferredSize(new Dimension(175, 55));
            timeBtn.setFont(new Font("Arial", Font.PLAIN, 14));

            // Add listener to track selected time slot
            timeBtn.addActionListener(e -> {
                // Reset the previously selected button (across all service types)
                if (previousSelectedButton != null) {
                    previousSelectedButton.setBackground(new Color(220, 220, 220));
                    previousSelectedButton.setForeground(Color.BLACK);
                }
                
                selectedServiceType = serviceType;
                String[] timeParts = time.split(" - ");
                selectedStartTime = timeParts[0].trim();
                selectedEndTime = timeParts[1].trim();
                // Visual feedback - highlight the selected button
                timeBtn.setBackground(new Color(100, 150, 200));
                timeBtn.setForeground(Color.WHITE);
                
                // Track this as the currently selected button
                previousSelectedButton = timeBtn;
            });

            servicePanel.add(timeBtn);
        }

        servicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        servicePanel.setMaximumSize(new Dimension(1230, 175));

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(Color.WHITE);
        containerPanel.add(sectionLabel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        containerPanel.add(servicePanel);

        return containerPanel;
    }

    /**
     * Override action buttons to provide reschedule-specific functionality
     */
    @Override
    protected JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
        actionPanel.setBackground(Color.WHITE);

        JButton confirmBtn = createStyledButton("Confirm Reschedule", new Color(171, 209, 237), Color.BLACK);
        confirmBtn.setPreferredSize(new Dimension(220, 60));
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 16));

        JButton cancelBtn = createStyledButton("Cancel", new Color(245, 240, 230), Color.BLACK);
        cancelBtn.setPreferredSize(new Dimension(220, 60));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));

        confirmBtn.addActionListener(e -> {
            if (selectedDate == null || selectedStartTime == null) {
                JOptionPane.showMessageDialog(this, 
                    "Please select both a date and a time slot before confirming.",
                    "Incomplete Selection",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirm reschedule
            confirmReschedule();
        });

        cancelBtn.addActionListener(e -> {
            // Navigate back to MyAppointment
            navigateBackToMyAppointment();
        });

        actionPanel.add(confirmBtn);
        actionPanel.add(cancelBtn);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionPanel.setMaximumSize(new Dimension(1230, 90));

        return actionPanel;
    }

    /**
     * Confirm the reschedule and update the appointment data
     */
    private void confirmReschedule() {
        String newDate = selectedDate.format(dateFormatter);
        String newStartTime = selectedStartTime;

        // Update the appointment in the data file
        myAppointmentPanel.updateAppointmentDateTime(currentAppointment, newDate, newStartTime);

        // Navigate back to MyAppointment
        navigateBackToMyAppointment();
    }

    /**
     * Navigate back to MyAppointment panel
     */
    private void navigateBackToMyAppointment() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            if (frame instanceof HomePage) {
                ((HomePage) frame).showPage(myAppointmentPanel);
            }
        }
    }
}
