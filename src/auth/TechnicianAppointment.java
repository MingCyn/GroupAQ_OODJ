package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
 * This class provides a comprehensive dashboard with:
 * - Status filtering (Pending, Completed, All)
 * - Date-based filtering
 * - Mark complete functionality
 * - Dashboard statistics
 * - Appointment history
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
    protected String getPageTitle() {
        return "Technician Dashboard";
    }

    /**
     * Override to customize the entire UI for technicians
     * Dashboard with date selection and appointment table
     */
    @Override
    protected void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        // ================= TITLE SECTION =================
        JLabel titleLabel = new JLabel(getPageTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(titleLabel);
        
        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setForeground(Color.LIGHT_GRAY);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(titleSeparator);
        mainContainer.add(Box.createVerticalStrut(5));

        // ================= DATE FILTER SECTION =================
        JPanel dateFilterPanel = createCustomDateFilterPanel();
        dateFilterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(dateFilterPanel);
        mainContainer.add(Box.createVerticalStrut(15));

        // ================= APPOINTMENT TABLE =================
        createAppointmentTable();
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        tableScrollPane.setBackground(Color.WHITE);
        tableScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainContainer.add(tableScrollPane);

        // Wrap in scroll pane for main container
        JScrollPane mainScrollPane = new JScrollPane(mainContainer);
        mainScrollPane.setBackground(Color.WHITE);
        add(mainScrollPane, BorderLayout.CENTER);
        
        // Load initial data
        loadAppointmentData();
    }

    /**
     * Create statistics panel showing pending, completed, and total appointments
     */
    /**
     * Create a custom date filter panel with clean alignment
     * Shows week navigation and day selection
     */
    private JPanel createCustomDateFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.WHITE);
        
        // ===== Week Navigation Bar =====
        JPanel weekNavBar = new JPanel(new BorderLayout());
        weekNavBar.setBackground(Color.WHITE);
        weekNavBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        weekNavBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Current Week Button
        JButton currentWeekBtn = createStyledButton("Current Week", new Color(171, 209, 237), Color.BLACK);
        currentWeekBtn.setPreferredSize(new Dimension(130, 45));
        currentWeekBtn.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Week Label (centered)
        JLabel weekLabel = new JLabel(getFormattedWeekLabel(selectedWeekStart), SwingConstants.CENTER);
        weekLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Navigation Buttons
        JButton prevBtn = createStyledButton("<", new Color(171, 209, 237), Color.BLACK);
        prevBtn.setPreferredSize(new Dimension(45, 45));
        prevBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton nextBtn = createStyledButton(">", new Color(171, 209, 237), Color.BLACK);
        nextBtn.setPreferredSize(new Dimension(45, 45));
        nextBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel navButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        navButtonsPanel.setBackground(Color.WHITE);
        navButtonsPanel.add(prevBtn);
        navButtonsPanel.add(nextBtn);
        
        weekNavBar.add(currentWeekBtn, BorderLayout.WEST);
        weekNavBar.add(weekLabel, BorderLayout.CENTER);
        weekNavBar.add(navButtonsPanel, BorderLayout.EAST);
        
        filterPanel.add(weekNavBar);
        filterPanel.add(Box.createVerticalStrut(15));
        
        // ===== Day Selection Panel =====
        JPanel daysPanel = new JPanel(new GridLayout(1, 7, 10, 0));
        daysPanel.setBackground(Color.WHITE);
        daysPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        updateDayButtons(daysPanel);
        
        // Setup navigation button listeners
        Runnable updatePrevButtonState = () -> {
            boolean isCurrentWeek = selectedWeekStart.equals(currentWeekStart);
            prevBtn.setEnabled(!isCurrentWeek);
        };
        
        currentWeekBtn.addActionListener(e -> {
            selectedWeekStart = currentWeekStart;
            weekLabel.setText(getFormattedWeekLabel(selectedWeekStart));
            updateDayButtons(daysPanel);
            updatePrevButtonState.run();
        });
        
        prevBtn.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            weekLabel.setText(getFormattedWeekLabel(selectedWeekStart));
            updateDayButtons(daysPanel);
            updatePrevButtonState.run();
        });
        
        nextBtn.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.plusWeeks(1);
            weekLabel.setText(getFormattedWeekLabel(selectedWeekStart));
            updateDayButtons(daysPanel);
            updatePrevButtonState.run();
        });
        
        updatePrevButtonState.run();
        
        filterPanel.add(daysPanel);
        
        return filterPanel;
    }

    /**
     * Helper method to format week label
     */
    protected String getFormattedWeekLabel(LocalDate weekStart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return "Week of " + weekStart.format(formatter);
    }

    /**
     * Create the appointment table with action buttons
     */
    private void createAppointmentTable() {
        String[] columnNames = {
            "Appointment ID",
            "Customer Name",
            "Service Type",
            "Time",
            "Status",
            "Actions"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Actions column is editable (for button)
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setBackground(Color.WHITE);
        appointmentTable.setForeground(Color.BLACK);
        appointmentTable.getTableHeader().setBackground(new Color(171, 209, 237));
        appointmentTable.getTableHeader().setForeground(Color.BLACK);
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        appointmentTable.setRowHeight(50);
        appointmentTable.setFont(new Font("Arial", Font.PLAIN, 11));
        appointmentTable.setGridColor(Color.LIGHT_GRAY);
        appointmentTable.setShowGrid(true);

        // Add custom button renderer and editor for Actions column
        appointmentTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        appointmentTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        appointmentTable.getColumnModel().getColumn(5).setPreferredWidth(120);
    }

    /**
     * Load all appointment data with status filtering
     * Calls loadAppointmentDataByFilter to apply current filter
     */
    private void loadAppointmentData() {
        loadAppointmentDataByFilter();
    }

    /**
     * Load appointment data filtered by status AND date
     */
    private void loadAppointmentDataByFilter() {
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
                    String appointmentId = parts[0].trim();
                    String fullName = parts[3].trim();
                    String serviceType = parts[4].trim();
                    String bookingDate = parts[12].trim();
                    String startTime = parts[13].trim();
                    String endTime = parts[14].trim();
                    String status = parts[16].trim();
                    
                    // Filter by date if selected
                    boolean matchesFilter = true;
                    if (selectedDate != null) {
                        String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
                        if (!bookingDate.equals(selectedDateStr)) {
                            matchesFilter = false;
                        }
                    }
                    
                    if (matchesFilter) {
                        String timeSlot = startTime + " - " + endTime;
                        tableModel.addRow(new Object[] {
                            appointmentId,
                            fullName,
                            serviceType,
                            timeSlot,
                            status,
                            "Mark Complete"
                        });
                    }
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

    /**
     * Load appointment data filtered by the selected date
     * Shows only appointments that match the selected date
     * 
     * @param selectedDate The date to filter appointments by
     */
    private void loadAppointmentDataByDate(LocalDate selectedDate) {
        loadAppointmentDataByFilter();
    }

    /**
     * Override updateDayButtons to make day buttons smaller for better fit
     * AND filter appointment table when a date is selected
     */
    @Override
    protected void updateDayButtons(JPanel daysPanel) {
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
            
            // Smaller button size for technician view
            dayBtn.setPreferredSize(new Dimension(100, 70));
            dayBtn.setFont(new Font("Arial", Font.BOLD, 13));
            
            LocalDate clickedDate = date;
            dayBtn.addActionListener(e -> {
                selectedDate = clickedDate;
                // Filter appointment table based on selected date
                loadAppointmentDataByDate(selectedDate);
                updateDayButtons(daysPanel);
            });
            daysPanel.add(dayBtn);
        }
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    /**
     * Override createServiceSection to center the time buttons
     */
    @Override
    protected JPanel createServiceSection(String title, String[] times) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionLabel.setForeground(new Color(120, 120, 120));
        sectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Use CENTER layout instead of LEFT
        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 18));
        servicePanel.setBackground(Color.WHITE);

        for (String time : times) {
            JButton timeBtn = createStyledButton(time, new Color(220, 220, 220), Color.BLACK);
            timeBtn.setPreferredSize(new Dimension(175, 55));
            timeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            servicePanel.add(timeBtn);
        }

        servicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        containerPanel.add(sectionLabel);
        containerPanel.add(Box.createVerticalStrut(8));
        containerPanel.add(servicePanel);

        return containerPanel;
    }

    /**
     * Update statistics based on pending and completed counts
     */
    /**
     * Mark an appointment as complete
     * Updates the Appointment.txt file with new status
     */
    public void markAppointmentComplete(String appointmentId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("data", "Appointment.txt"));
            
            // Update the status for the appointment
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 17 && parts[0].trim().equals(appointmentId)) {
                    parts[16] = "Completed"; // Update status to Completed
                    lines.set(i, String.join(",", parts));
                    break;
                }
            }
            
            // Write back to file
            Files.write(Paths.get("data", "Appointment.txt"), lines);
            
            // Refresh table
            loadAppointmentDataByFilter();
            
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "Appointment marked as completed!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                "Error updating appointment: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Custom JButton renderer for table cells
     */
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(76, 175, 80));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 11));
            return this;
        }
    }

    /**
     * Custom JButton editor for table cells
     */
    public static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private TechnicianAppointment parent;
        private String appointmentId;

        public ButtonEditor(JCheckBox checkBox, TechnicianAppointment parent) {
            super(checkBox);
            this.parent = parent;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(76, 175, 80));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 11));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            
            // Get appointment ID from the first column
            appointmentId = (String) table.getValueAt(row, 0);
            
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Mark appointment as complete
                if (parent != null && appointmentId != null) {
                    parent.markAppointmentComplete(appointmentId);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage("Technician", "tech").setVisible(true));
    }
}