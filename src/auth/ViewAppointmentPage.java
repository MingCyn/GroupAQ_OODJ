package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewAppointmentPage displays all appointments from Appointment.txt in a table format
 * with date filtering capability, similar to the appointment booking page.
 * 
 * Features:
 * - Display appointments in a table with relevant details
 * - Filter appointments by selected date
 * - Week navigation to select different weeks
 * - Day selection interface for filtering
 * - Responsive design consistent with other appointment pages
 */
public class ViewAppointmentPage extends JPanel {
    private LocalDate currentWeekStart;
    private LocalDate selectedWeekStart;
    private LocalDate selectedDate = null;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private List<String[]> allAppointments;
    private List<String[]> displayedAppointments; // Store the displayed appointment data for updates
    private JLabel customDateLabel;
    private JPanel daysPanel; // Store reference to days panel

    public ViewAppointmentPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Initialize week dates
        currentWeekStart = LocalDate.now();
        currentWeekStart = currentWeekStart.minusDays(currentWeekStart.getDayOfWeek().getValue() - 1);
        selectedWeekStart = currentWeekStart;
        
        // Initialize appointment data
        allAppointments = new ArrayList<>();
        displayedAppointments = new ArrayList<>();
        loadAppointments();
        
        initializeUI();
    }

    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(15, 30, 15, 30));
        mainContainer.setPreferredSize(new Dimension(1400, 950));

        // 1. Header Section
        mainContainer.add(createHeaderPanel());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 2. Days Selection Section - Store reference
        daysPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        daysPanel.setBackground(Color.WHITE);
        initializeDayButtons(daysPanel);

        // 3. Week Navigation Section
        mainContainer.add(createWeekNavigationPanel(daysPanel));
        mainContainer.add(Box.createRigidArea(new Dimension(0, 5)));

        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        daysPanel.setMaximumSize(new Dimension(1230, 150));
        mainContainer.add(daysPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // 4. Table Section
        mainContainer.add(createTablePanel());
        mainContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        // 5. Back Button
        mainContainer.add(createBackButtonPanel());
        mainContainer.add(Box.createVerticalGlue());

        wrapperPanel.add(mainContainer);
        add(wrapperPanel, BorderLayout.CENTER);
    }

    /**
     * Create header panel with title
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(1230, 55));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("View Appointments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.LIGHT_GRAY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(separator, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Create week navigation panel with previous/next buttons
     */
    private JPanel createWeekNavigationPanel(JPanel daysPanel) {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setMaximumSize(new Dimension(1230, 65));

        JButton currentWeekBtn = createStyledButton("Current Week", new Color(171, 209, 237), Color.BLACK);
        currentWeekBtn.setPreferredSize(new Dimension(130, 48));
        currentWeekBtn.setFont(new Font("Arial", Font.BOLD, 13));

        customDateLabel = new JLabel(getFormattedWeekLabel(selectedWeekStart), SwingConstants.CENTER);
        customDateLabel.setFont(new Font("Arial", Font.BOLD, 27));

        JPanel arrowsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        arrowsPanel.setBackground(Color.WHITE);

        JButton prevBtn = createStyledButton("<", new Color(171, 209, 237), Color.BLACK);
        prevBtn.setPreferredSize(new Dimension(48, 48));
        prevBtn.setFont(new Font("Arial", Font.BOLD, 16));

        JButton nextBtn = createStyledButton(">", new Color(171, 209, 237), Color.BLACK);
        nextBtn.setPreferredSize(new Dimension(48, 48));
        nextBtn.setFont(new Font("Arial", Font.BOLD, 16));

        arrowsPanel.add(prevBtn);
        arrowsPanel.add(nextBtn);
        navPanel.add(currentWeekBtn, BorderLayout.WEST);
        navPanel.add(customDateLabel, BorderLayout.CENTER);
        navPanel.add(arrowsPanel, BorderLayout.EAST);
        navPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Always enable previous button to allow viewing past weeks
        prevBtn.setEnabled(true);

        currentWeekBtn.addActionListener(e -> {
            selectedWeekStart = currentWeekStart;
            selectedDate = null;
            updateWeekDisplay(customDateLabel);
            updateDayButtons(daysPanel);
            updateTableData();
        });

        prevBtn.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            selectedDate = null;
            updateWeekDisplay(customDateLabel);
            updateDayButtons(daysPanel);
            updateTableData();
        });

        nextBtn.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.plusWeeks(1);
            selectedDate = null;
            updateWeekDisplay(customDateLabel);
            updateDayButtons(daysPanel);
            updateTableData();
        });

        return navPanel;
    }

    /**
     * Create the table panel showing appointments
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(1230, 500));

        // Create table model with all requested columns
        String[] columnNames = {"Full Name", "Service Type", "Contact", "Car Model", "Car Plate", 
                               "Service Add-on", "Remarks", "Technician", "Booking Date", "Start Time", 
                               "End Time", "Price(RM)"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable - use "Assign Technician" button instead
                return false;
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setFont(new Font("Arial", Font.PLAIN, 11));
        appointmentTable.setRowHeight(30);
        appointmentTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue
        appointmentTable.setSelectionForeground(Color.BLACK);
        appointmentTable.setShowGrid(true);
        appointmentTable.setGridColor(new Color(220, 220, 220));
        appointmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        appointmentTable.setColumnSelectionAllowed(false); // Disable column selection
        appointmentTable.setRowSelectionAllowed(true); // Enable row selection

        // Customize header
        JTableHeader header = appointmentTable.getTableHeader();
        header.setBackground(new Color(171, 209, 237));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));

        // Set column widths
        appointmentTable.getColumnModel().getColumn(0).setPreferredWidth(110); // Full Name
        appointmentTable.getColumnModel().getColumn(1).setPreferredWidth(90);  // Service Type
        appointmentTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Contact
        appointmentTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Car Model
        appointmentTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Car Plate
        appointmentTable.getColumnModel().getColumn(5).setPreferredWidth(168); // Service Add-on
        appointmentTable.getColumnModel().getColumn(6).setPreferredWidth(169); // Remarks
        appointmentTable.getColumnModel().getColumn(7).setPreferredWidth(90); // Technician
        appointmentTable.getColumnModel().getColumn(8).setPreferredWidth(110); // Booking Date
        appointmentTable.getColumnModel().getColumn(9).setPreferredWidth(70); // Start Time
        appointmentTable.getColumnModel().getColumn(10).setPreferredWidth(70); // End Time
        appointmentTable.getColumnModel().getColumn(11).setPreferredWidth(70); // Price

        // Load initial table data
        displayedAppointments.clear();
        updateTableData();

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Create back button panel with action buttons
     */
    private JPanel createBackButtonPanel() {
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        backPanel.setBackground(Color.WHITE);
        backPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        backPanel.setMaximumSize(new Dimension(1230, 60));

        JButton viewWeeklyBtn = createStyledButton("View Weekly", new Color(100, 150, 200), Color.WHITE);
        viewWeeklyBtn.setPreferredSize(new Dimension(180, 50));
        viewWeeklyBtn.setFont(new Font("Arial", Font.BOLD, 14));

        viewWeeklyBtn.addActionListener(e -> {
            selectedDate = null; // Clear date filter to show entire week
            updateDayButtons(daysPanel); // Update day buttons to deselect any selected day
            updateTableData(); // Refresh table to show all appointments for the week
        });

        JButton backBtn = createStyledButton("Back to Appointments", new Color(200, 200, 200), Color.BLACK);
        backBtn.setPreferredSize(new Dimension(200, 50));
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));

        backBtn.addActionListener(e -> {
            Component frame = SwingUtilities.getWindowAncestor(this);
            if (frame instanceof HomePage) {
                HomePage homePage = (HomePage) frame;
                homePage.showPage(new AdminAppointment(homePage.getUserId() != null ? "Admin" : "CounterStaff"));
            }
        });

        JButton assignTechnicianBtn = createStyledButton("Assign Technician", new Color(100, 180, 100), Color.WHITE);
        assignTechnicianBtn.setPreferredSize(new Dimension(180, 50));
        assignTechnicianBtn.setFont(new Font("Arial", Font.BOLD, 14));

        assignTechnicianBtn.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this,
                    "Please select an appointment row first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Show technician selection dialog
            showTechnicianSelectionDialog(selectedRow);
        });

        JButton cancelAppointmentBtn = createStyledButton("Cancel Appointment", new Color(220, 100, 100), Color.WHITE);
        cancelAppointmentBtn.setPreferredSize(new Dimension(180, 50));
        cancelAppointmentBtn.setFont(new Font("Arial", Font.BOLD, 14));

        cancelAppointmentBtn.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this,
                    "Please select an appointment row first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirm cancellation
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this appointment?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (cancelAppointment(selectedRow)) {
                    JOptionPane.showMessageDialog(this,
                        "Appointment cancelled successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    updateTableData(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to cancel appointment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backPanel.add(viewWeeklyBtn);
        backPanel.add(backBtn);
        backPanel.add(assignTechnicianBtn);
        backPanel.add(cancelAppointmentBtn);
        return backPanel;
    }

    /**
     * Initialize day buttons for the selected week
     */
    private void initializeDayButtons(JPanel daysPanel) {
        updateDayButtons(daysPanel);
    }

    /**
     * Update day buttons based on selected week
     */
    private void updateDayButtons(JPanel daysPanel) {
        daysPanel.removeAll();

        for (int i = 0; i < 7; i++) {
            LocalDate date = selectedWeekStart.plusDays(i);
            String dayName = date.getDayOfWeek().toString().substring(0, 3).toUpperCase();
            String dayNumber = String.valueOf(date.getDayOfMonth());
            String buttonText = "<html><center>" + dayNumber + "<br>" + dayName + "</center></html>";
            boolean isSelected = (selectedDate != null && selectedDate.equals(date));
            Color bgColor = isSelected ? new Color(50, 130, 200) : new Color(171, 209, 237);
            Color fgColor = isSelected ? Color.WHITE : Color.BLACK;
            JButton dayBtn = createStyledButton(buttonText, bgColor, fgColor);
            dayBtn.setPreferredSize(new Dimension(135, 90));
            dayBtn.setFont(new Font("Arial", Font.BOLD, 17));
            LocalDate clickedDate = date;
            dayBtn.addActionListener(e -> {
                selectedDate = clickedDate;
                updateDayButtons(daysPanel);
                updateTableData();
            });
            daysPanel.add(dayBtn);
        }
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    /**
     * Update table data based on selected date filter
     */
    private void updateTableData() {
        tableModel.setRowCount(0); // Clear existing rows
        displayedAppointments.clear(); // Clear displayed appointments tracking

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        
        // Define week end date
        LocalDate weekEnd = selectedWeekStart.plusDays(6);

        for (String[] appointment : allAppointments) {
            // appointment format: [id, customerid, username, fullname, servicetype, contact, carmodel, carplate, 
            //                      serviceaddon, remarks, technician, technicianid, bookingdate, starttime, endtime, price, status]
            String bookingDate = appointment[12].trim();
            String status = appointment[16].trim();

            // Skip cancelled appointments
            if (status.equalsIgnoreCase("Cancelled")) {
                continue;
            }

            // Parse the appointment date to compare with week range
            LocalDate appointmentDate = null;
            try {
                appointmentDate = LocalDate.parse(bookingDate, formatter);
            } catch (Exception e) {
                // Skip appointments with invalid dates
                continue;
            }

            // Filter by selected date if one is selected, otherwise filter by week
            if (selectedDate != null) {
                // Show only appointments for the selected date
                if (!appointmentDate.equals(selectedDate)) {
                    continue;
                }
            } else {
                // Show only appointments within the selected week
                if (appointmentDate.isBefore(selectedWeekStart) || appointmentDate.isAfter(weekEnd)) {
                    continue;
                }
            }

            // Store appointment data for potential updates
            displayedAppointments.add(appointment);

            // Prepare row data - display all columns except customerid and technicianid
            Object[] rowData = {
                formatValue(appointment[3]), // 0: Full Name
                formatValue(appointment[4]), // 1: Service Type
                formatValue(appointment[5]), // 2: Contact
                formatValue(appointment[6]), // 3: Car Model
                formatValue(appointment[7]), // 4: Car Plate
                formatValue(appointment[8]), // 5: Service Add-on
                formatValue(appointment[9]), // 6: Remarks
                formatValue(appointment[10]), // 7: Technician
                appointment[12].trim(),     // 8: Booking Date
                appointment[13].trim(),     // 9: Start Time
                appointment[14].trim(),     // 10: End Time
                formatValue(appointment[15]) // 11: Price
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Cancel an appointment by setting its status to "Cancelled"
     */
    private boolean cancelAppointment(int selectedRowIndex) {
        if (selectedRowIndex < 0 || selectedRowIndex >= displayedAppointments.size()) {
            return false;
        }

        String[] appointment = displayedAppointments.get(selectedRowIndex);
        String appointmentId = appointment[0]; // Get appointment ID

        try {
            // Read all appointments
            java.util.List<String> allLines = new java.util.ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("data/Appointment.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }
            br.close();

            // Update the appointment with Cancelled status
            java.util.List<String> updatedLines = new java.util.ArrayList<>();
            for (String currentLine : allLines) {
                if (currentLine.startsWith("#")) {
                    // Keep header
                    updatedLines.add(currentLine);
                } else if (!currentLine.isEmpty()) {
                    String[] parts = currentLine.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                    if (parts.length >= 17) {
                        String id = parts[0].trim();
                        if (id.equals(appointmentId)) {
                            // Update status to Cancelled
                            parts[16] = "Cancelled";
                            String updatedLine = String.join(",", parts);
                            updatedLines.add(updatedLine);
                        } else {
                            updatedLines.add(currentLine);
                        }
                    } else {
                        updatedLines.add(currentLine);
                    }
                }
            }

            // Write updated appointments back to file
            java.nio.file.Path appointmentPath = java.nio.file.Paths.get("data", "Appointment.txt");
            java.nio.file.Files.write(appointmentPath, updatedLines);
            
            // Reload appointments to reflect changes
            loadAppointments();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Show technician selection dialog to assign a technician to the selected appointment
     */
    private void showTechnicianSelectionDialog(int selectedRowIndex) {
        // Load technicians from account.txt
        java.util.List<String> technicians = loadTechniciansFromFile();

        if (technicians.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No technicians available in the system.",
                "No Technicians",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create dialog with technician selection
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Assign Technician");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title
        JLabel titleLabel = new JLabel("Select Technician to Assign");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // Technician dropdown
        JComboBox<String> technicianCombo = new JComboBox<>(technicians.toArray(new String[0]));
        technicianCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        technicianCombo.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = 1;
        panel.add(technicianCombo, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton confirmBtn = new JButton("Assign");
        confirmBtn.setPreferredSize(new Dimension(100, 35));
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 12));
        confirmBtn.setBackground(new Color(100, 180, 100));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(200, 200, 200));
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.setFocusPainted(false);

        confirmBtn.addActionListener(e -> {
            String selectedTechnician = (String) technicianCombo.getSelectedItem();
            if (selectedTechnician != null && selectedRowIndex >= 0 && selectedRowIndex < displayedAppointments.size()) {
                String[] appointmentData = displayedAppointments.get(selectedRowIndex);
                String bookingDate = appointmentData[12].trim();
                String startTime = appointmentData[13].trim();
                String endTime = appointmentData[14].trim();

                // Check if technician is available at this timeslot
                if (!isTechnicianAvailable(selectedTechnician, bookingDate, startTime, endTime)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Technician is unavailable at this timeslot. Kindly assign to another technician",
                        "Technician Unavailable",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                displayedAppointments.get(selectedRowIndex)[10] = selectedTechnician;
                saveTechnicianAssignment(selectedRowIndex);
                dialog.dispose();
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridy = 2;
        gbc.insets = new java.awt.Insets(20, 10, 10, 10);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**
     * Check if a technician is available at a specific date and time
     */
    private boolean isTechnicianAvailable(String technicianName, String bookingDate, String startTime, String endTime) {
        try {
            java.nio.file.Path availabilityPath = java.nio.file.Paths.get("data", "availability.txt");
            if (!java.nio.file.Files.exists(availabilityPath)) {
                return true; // No availability records yet, so technician is available
            }

            BufferedReader br = new BufferedReader(new FileReader("data/availability.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                // Skip header
                if (line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String recordTech = parts[0].trim();
                    String recordDate = parts[1].trim();
                    String recordStartTime = parts[2].trim();
                    String recordEndTime = parts[3].trim();

                    // Check if technician has a conflicting appointment
                    if (recordTech.equalsIgnoreCase(technicianName) && 
                        recordDate.equalsIgnoreCase(bookingDate)) {
                        
                        // Check for time overlap
                        if (timesOverlap(startTime, endTime, recordStartTime, recordEndTime)) {
                            br.close();
                            return false; // Technician is not available
                        }
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // Technician is available
    }

    /**
     * Check if two time slots overlap
     */
    private boolean timesOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            java.time.LocalTime start1 = java.time.LocalTime.parse(startTime1, timeFormatter);
            java.time.LocalTime end1 = java.time.LocalTime.parse(endTime1, timeFormatter);
            java.time.LocalTime start2 = java.time.LocalTime.parse(startTime2, timeFormatter);
            java.time.LocalTime end2 = java.time.LocalTime.parse(endTime2, timeFormatter);

            // Check if there's any overlap (not including boundary times)
            // Overlap occurs only if: start1 < end2 AND start2 < end1
            // This allows touching boundaries (e.g., 10:00-11:00 and 11:00-12:00)
            return start1.isBefore(end2) && start2.isBefore(end1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Record technician availability (adds to unavailable times)
     */
    private void recordTechnicianUnavailability(String technicianName, String bookingDate, String startTime, String endTime) {
        try {
            java.nio.file.Path availabilityPath = java.nio.file.Paths.get("data", "availability.txt");
            
            // Create file if it doesn't exist
            if (!java.nio.file.Files.exists(availabilityPath)) {
                String header = "#technician_name,booking_date,start_time,end_time\n";
                java.nio.file.Files.write(availabilityPath, header.getBytes(), 
                    java.nio.file.StandardOpenOption.CREATE);
            }

            // Append the availability record
            String record = String.format("%s,%s,%s,%s\n", technicianName, bookingDate, startTime, endTime);
            java.nio.file.Files.write(availabilityPath, record.getBytes(), 
                java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove technician unavailability record (when reassigning)
     */
    private void removeTechnicianUnavailability(String technicianName, String bookingDate, String startTime, String endTime) {
        try {
            java.nio.file.Path availabilityPath = java.nio.file.Paths.get("data", "availability.txt");
            
            if (!java.nio.file.Files.exists(availabilityPath)) {
                return;
            }

            // Read all lines from availability.txt
            java.util.List<String> allLines = new java.util.ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("data/availability.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }
            br.close();

            // Filter out the specific technician record
            java.util.List<String> updatedLines = new java.util.ArrayList<>();
            for (String currentLine : allLines) {
                if (currentLine.startsWith("#")) {
                    // Keep header
                    updatedLines.add(currentLine);
                } else if (!currentLine.isEmpty()) {
                    String[] parts = currentLine.split(",");
                    if (parts.length >= 4) {
                        String recordTech = parts[0].trim();
                        String recordDate = parts[1].trim();
                        String recordStartTime = parts[2].trim();
                        String recordEndTime = parts[3].trim();

                        // Skip this record if it matches the one we want to remove
                        if (recordTech.equalsIgnoreCase(technicianName) && 
                            recordDate.equalsIgnoreCase(bookingDate) &&
                            recordStartTime.equals(startTime) &&
                            recordEndTime.equals(endTime)) {
                            // Skip this line (don't add to updatedLines)
                            continue;
                        }
                    }
                    updatedLines.add(currentLine);
                }
            }

            // Write the filtered lines back to file
            FileWriter fw = new FileWriter("data/availability.txt");
            for (String updatedLine : updatedLines) {
                fw.write(updatedLine + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all technicians from account.txt
     */
    private java.util.List<String> loadTechniciansFromFile() {
        java.util.List<String> technicians = new java.util.ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/account.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String role = parts[3].trim();
                    if (role.equalsIgnoreCase("Technician")) {
                        String userName = parts[1].trim();
                        technicians.add(userName);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return technicians;
    }

    /**
     * Save technician assignment to Appointment.txt
     */
    private void saveTechnicianAssignment(int rowIndex) {
        try {
            if (rowIndex < 0 || rowIndex >= displayedAppointments.size()) {
                return;
            }

            String[] appointmentData = displayedAppointments.get(rowIndex);
            String technicianName = appointmentData[10].trim();
            String bookingDate = appointmentData[12].trim();
            String startTime = appointmentData[13].trim();
            String endTime = appointmentData[14].trim();
            String appointmentID = appointmentData[0].trim();

            // Read all lines from Appointment.txt to find the old technician (if any)
            java.util.List<String> allLines = new java.util.ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("data/Appointment.txt"));
            String line;
            String oldTechnician = null;
            
            while ((line = br.readLine()) != null) {
                allLines.add(line);
                // Find the old technician assigned to this appointment
                if (!line.startsWith("#") && !line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].trim().equals(appointmentID)) {
                        oldTechnician = parts[10].trim();
                    }
                }
            }
            br.close();

            // If there was an old technician assigned, remove their unavailability record
            if (oldTechnician != null && !oldTechnician.equals("NULL") && !oldTechnician.isEmpty()) {
                removeTechnicianUnavailability(oldTechnician, bookingDate, startTime, endTime);
            }

            // Update the appointment with new technician
            for (int i = 0; i < allLines.size(); i++) {
                String currentLine = allLines.get(i);
                if (!currentLine.startsWith("#") && !currentLine.isEmpty()) {
                    String[] parts = currentLine.split(",");
                    if (parts.length > 0 && parts[0].trim().equals(appointmentID)) {
                        // Update the technician field (index 10)
                        parts[10] = technicianName;
                        // Rebuild the line
                        String newLine = String.join(",", parts);
                        allLines.set(i, newLine);
                        break;
                    }
                }
            }

            // Write back to file
            FileWriter fw = new FileWriter("data/Appointment.txt");
            for (String updatedLine : allLines) {
                fw.write(updatedLine + "\n");
            }
            fw.close();

            // Record the new technician's unavailability
            if (!technicianName.equals("NULL")) {
                recordTechnicianUnavailability(technicianName, bookingDate, startTime, endTime);
            }

            // Reload appointments to reflect changes
            loadAppointments();
            updateTableData();
            
            JOptionPane.showMessageDialog(this, 
                "Technician assigned successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error saving technician assignment: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load appointments from Appointment.txt
     */
    private void loadAppointments() {
        allAppointments.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/Appointment.txt"));
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isHeader) {
                    if (line.startsWith("#")) {
                        isHeader = false;
                    }
                    continue;
                }

                // Parse appointment data
                String[] parts = line.split(",");
                if (parts.length >= 17) {
                    allAppointments.add(parts);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading appointments: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Get formatted week label
     */
    private String getFormattedWeekLabel(LocalDate weekStart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return "Week of " + weekStart.format(formatter);
    }

    /**
     * Update week display label
     */
    private void updateWeekDisplay(JLabel label) {
        label.setText(getFormattedWeekLabel(selectedWeekStart));
    }

    /**
     * Format appointment value - return empty string for NULL values
     */
    private String formatValue(String value) {
        if (value == null || value.trim().equals("NULL") || value.trim().isEmpty()) {
            return "";
        }
        return value.trim();
    }

    /**
     * Create styled button
     */
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        return new RoundedButton(text, bgColor, fgColor);
    }

    /**
     * Inner class for rounded button styling
     */
    private class RoundedButton extends JButton {
        private static final int ARC_WIDTH = 15;
        private static final int ARC_HEIGHT = 15;

        public RoundedButton(String text, Color bgColor, Color fgColor) {
            super(text);
            setBackground(bgColor);
            setForeground(fgColor);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
            setFont(new Font("Arial", Font.PLAIN, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                               java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            g2.setColor(new Color(200, 200, 200));
            g2.setStroke(new java.awt.BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            super.paintComponent(g);
        }
    }
}
