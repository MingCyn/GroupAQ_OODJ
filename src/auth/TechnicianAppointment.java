package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TechnicianAppointment class extends StaffAppointmentPage to implement
 * the inheritance OOP pillar. Technician role accesses appointment 
 * functionality specific to their needs.
 * 
 * This class displays date selection and appointment table from file.
 */

public class TechnicianAppointment extends StaffAppointmentPage {
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public TechnicianAppointment(String userRole) {
        super(userRole);
    }

    public TechnicianAppointment() {
        super("Technician");
    }

    @Override
    protected boolean shouldShowActionButtons() {
        return false;
    }

    @Override
    protected String getPageTitle() {
        return "Appointment Schedule";
    }

    /**
     * Override to customize the entire UI for technicians
     * Only show date and time selection with appointment table
     */
    @Override
    protected void initializeUI() {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        add(new JScrollPane(mainContainer), BorderLayout.CENTER);

        // ================= HEADER =================
        mainContainer.add(createHeaderPanel());

        // ================= DATE AND TIME SELECTION =================
        
        // Days Selection Section - Will be centered
        JPanel daysPanel = new JPanel(new GridLayout(1, 7, 8, 8));
        daysPanel.setBackground(Color.WHITE);
        daysPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        initializeDayButtons(daysPanel);

        // Center wrapper for days panel
        JPanel daysCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        daysCenterPanel.setBackground(Color.WHITE);
        daysCenterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        daysCenterPanel.add(daysPanel);

        // Week Navigation Section
        mainContainer.add(createWeekNavigationPanel(daysPanel));
        mainContainer.add(daysCenterPanel);
        mainContainer.add(Box.createVerticalStrut(15));

        // ================= APPOINTMENT TABLE =================
        createAppointmentTable();
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        tableScrollPane.setBackground(Color.WHITE);
        
        mainContainer.add(tableScrollPane);
        
        // Load initial data
        loadAppointmentData();
    }

    /**
     * Create the appointment table with specified columns
     */
    private void createAppointmentTable() {
        String[] columnNames = {
            "Appointment ID",
            "Customer Name",
            "Service Type",
            "Car Model",
            "Car Plate",
            "Service Add-on",
            "Date",
            "Start Time",
            "End Time",
            "Contact Number",
            "Remarks",
            "Technician Name",
            "Status"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setBackground(Color.WHITE);
        appointmentTable.setForeground(Color.BLACK);
        appointmentTable.getTableHeader().setBackground(new Color(171, 209, 237));
        appointmentTable.getTableHeader().setForeground(Color.BLACK);
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        appointmentTable.setRowHeight(25);
        appointmentTable.setFont(new Font("Arial", Font.PLAIN, 11));
        appointmentTable.setGridColor(Color.LIGHT_GRAY);
        appointmentTable.setShowGrid(true);
    }

    /**
     * Load appointment data from Appointment.txt file
     */
    private void loadAppointmentData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("data", "Appointment.txt"));
            
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Skip header line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 17) {
                    // Extract required columns
                    String appointmentId = parts[0].trim();           // Column 0
                    String fullName = parts[3].trim();               // Column 3
                    String serviceType = parts[4].trim();            // Column 4
                    String carModel = parts[6].trim();               // Column 6
                    String carPlate = parts[7].trim();               // Column 7
                    String serviceAddOn = parts[8].trim();           // Column 8
                    String bookingDate = parts[12].trim();           // Column 12 - Booking Date
                    String startTime = parts[13].trim();             // Column 13 - Start Time
                    String endTime = parts[14].trim();               // Column 14 - End Time
                    String contactNumber = parts[5].trim();          // Column 5
                    String remarks = parts[9].trim();                // Column 9
                    String technicianName = parts[10].trim();        // Column 10 - Technician In-Charge
                    String status = parts[16].trim();                // Column 16
                    
                    // Add row to table
                    tableModel.addRow(new Object[] {
                        appointmentId,
                        fullName,
                        serviceType,
                        carModel,
                        carPlate,
                        serviceAddOn,
                        bookingDate,
                        startTime,
                        endTime,
                        contactNumber,
                        remarks,
                        technicianName,
                        status
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "Error loading appointment data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage("Technician", "tech").setVisible(true));
    }
}