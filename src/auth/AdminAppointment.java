package auth;

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminAppointment extends StaffAppointmentPage {

    public AdminAppointment(String userRole) {
        super(userRole);
    }

    public AdminAppointment() {
        super("Admin");
    }

    @Override
    protected boolean shouldShowActionButtons() {
        return false;
    }

    @Override
    protected String getPageTitle() {
        return "Create Appointment For Walk-in/Phone-in Customers";
    }

    // =========================
    // RESPONSIVE SERVICE SECTION
    // =========================
    @Override
    protected JPanel createServiceSection(String title, String[] times) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);

        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelWrapper.setBackground(Color.WHITE);
        labelWrapper.add(sectionLabel);

        // ===== GRID PANEL =====
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 12, 12)); 
        // 👆 4 columns fixed = CLEAN ALIGNMENT

        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        String serviceType = title.equals("Normal Services") ? "Normal" : "Major";

        for (String time : times) {

            JButton btn = createStyledButton(
                    "<html><center>" + time + "</center></html>",
                    new Color(235, 235, 235),
                    Color.BLACK
            );

            // ✅ SMALL + UNIFORM BUTTON SIZE
            btn.setPreferredSize(new Dimension(40, 35));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setFocusPainted(false);

            btn.addActionListener(e -> {
                String[] parts = time.split(" - ");
                showAppointmentDialog(
                        serviceType,
                        parts[0].trim(),
                        parts[1].trim()
                );
            });

            gridPanel.add(btn);
        }

        container.add(labelWrapper);
        container.add(Box.createVerticalStrut(8));
        container.add(gridPanel);

        return container;
    }

    // =========================
    // RESPONSIVE DIALOG
    // =========================
    private void showAppointmentDialog(String serviceType, String startTime, String endTime) {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Appointment Creation",
                true
        );

        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(6, 6, 6, 6);

        int row = 0;

        JLabel title = new JLabel("Appointment Creation");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = row++;
        panel.add(title, gbc);

        JTextField fullName = new JTextField();
        JTextField contact = new JTextField();
        JTextField carModel = new JTextField();
        JTextField carPlate = new JTextField();
        JTextField serviceAddOn = new JTextField();
        JTextArea remarks = new JTextArea(3, 20);

        setPlaceholder(fullName, "Full Name");
        setPlaceholder(contact, "Contact Number");
        setPlaceholder(carModel, "Car Model");
        setPlaceholder(carPlate, "Car Plate");
        setPlaceholder(serviceAddOn, "Service Add-on (Optional)");
        setPlaceholder(remarks, "Remarks (Optional)");

        panel.add(labeled("Full Name"), next(gbc, row++, panel));
        panel.add(fullName, next(gbc, row++, panel));

        panel.add(labeled("Contact Number"), next(gbc, row++, panel));
        panel.add(contact, next(gbc, row++, panel));

        panel.add(labeled("Car Model"), next(gbc, row++, panel));
        panel.add(carModel, next(gbc, row++, panel));

        panel.add(labeled("Car Plate"), next(gbc, row++, panel));
        panel.add(carPlate, next(gbc, row++, panel));

        panel.add(labeled("Service Add-on"), next(gbc, row++, panel));
        panel.add(serviceAddOn, next(gbc, row++, panel));

        panel.add(labeled("Remarks"), next(gbc, row++, panel));

        gbc.gridy = row++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(new JScrollPane(remarks), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        JButton cancel = createOutlinedButton("Cancel");
        JButton confirm = createFilledButton("Confirm");

        cancel.addActionListener(e -> dialog.dispose());

        confirm.addActionListener(e -> {

            if (isInvalid(fullName, contact, carModel, carPlate)) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in all required fields",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = saveAdminAppointment(
                    fullName.getText().trim(),
                    contact.getText().trim(),
                    carModel.getText().trim(),
                    carPlate.getText().trim(),
                    serviceAddOn.getText().trim(),
                    remarks.getText().trim(),
                    serviceType,
                    startTime,
                    endTime
            );

            JOptionPane.showMessageDialog(dialog,
                    success ? "Appointment created!" : "Failed to create appointment");

            if (success) dialog.dispose();
        });

        buttonPanel.add(cancel);
        buttonPanel.add(confirm);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setMinimumSize(new Dimension(420, 500));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // =========================
    // VALIDATION
    // =========================
    private boolean isInvalid(JTextField a, JTextField b, JTextField c, JTextField d) {
        return a.getText().trim().isEmpty()
                || b.getText().trim().isEmpty()
                || c.getText().trim().isEmpty()
                || d.getText().trim().isEmpty();
    }

    // =========================
    // FILE SAVE (UNCHANGED LOGIC)
    // =========================
    private boolean saveAdminAppointment(
            String fullName, String contactNumber, String carModel,
            String carPlate, String serviceAddOn, String remarks,
            String serviceType, String startTime, String endTime) {

        try {
            Path filePath = Paths.get("data", "Appointment.txt");
            Files.createDirectories(filePath.getParent());

            String appointmentID = generateNextAppointmentID(filePath);

            LocalDate today = LocalDate.now();
            String bookingDate = today.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));

            String record = String.join(",",
                    appointmentID,
                    "NULL",
                    "NULL",
                    fullName,
                    serviceType,
                    contactNumber,
                    carModel,
                    carPlate,
                    serviceAddOn,
                    remarks,
                    "NULL",
                    "NULL",
                    bookingDate,
                    startTime,
                    endTime,
                    "NULL",
                    "pending"
            );

            Files.write(filePath,
                    (record + "\n").getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateNextAppointmentID(Path filePath) {
        try {
            if (!Files.exists(filePath)) return "APTID0001";

            List<String> lines = Files.readAllLines(filePath);

            for (int i = lines.size() - 1; i >= 0; i--) {
                if (!lines.get(i).startsWith("#")) {
                    String lastID = lines.get(i).split(",")[0];
                    int num = Integer.parseInt(lastID.substring(5));
                    return String.format("APTID%04d", num + 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "APTID0001";
    }

    // =========================
    // UI HELPERS
    // =========================
    private GridBagConstraints next(GridBagConstraints gbc, int row, JPanel panel) {
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        return gbc;
    }

    private JLabel labeled(String text) {
        return new JLabel(text);
    }

    private void setPlaceholder(JTextField field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void setPlaceholder(JTextArea area, String text) {
        area.setText(text);
        area.setForeground(Color.GRAY);

        area.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (area.getText().equals(text)) {
                    area.setText("");
                    area.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(text);
                    area.setForeground(Color.GRAY);
                }
            }
        });
    }

    private JButton createOutlinedButton(String text) {
        return new JButton(text);
    }

    private JButton createFilledButton(String text) {
        return new JButton(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new HomePage("Admin", "AdminAPU").setVisible(true)
        );
    }
}