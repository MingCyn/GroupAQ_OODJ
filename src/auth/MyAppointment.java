package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyAppointment extends JPanel {

    private String customerID;
    private String username;

    private JPanel listPanel;
    private JTextField searchField;
    private String currentFilter = "All";
    private List<String[]> allAppointments = new ArrayList<>();
    private JLabel emptyLabel;

    private List<JButton> tabButtons = new ArrayList<>();

    public MyAppointment(String customerID, String username) {
        this.customerID = customerID;
        this.username = username;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initializeUI();
        loadAppointmentsFromFile();
        displayAppointments();
    }

    private void initializeUI() {

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(20, 40, 20, 40));

        // ================= HEADER WITH BACK BUTTON =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel titleLabel = new JLabel("My Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        
        JButton backBtn = new JButton("← Back");
        backBtn.setPreferredSize(new Dimension(100, 40));
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setBackground(new Color(171, 209, 237)); // Light blue color
        backBtn.setForeground(Color.BLACK);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> goBack());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);
        
        main.add(headerPanel);
        main.add(Box.createVerticalStrut(5));

        JSeparator separator = new JSeparator();
        main.add(separator);
        main.add(Box.createVerticalStrut(15));

        // ===== FILTER TABS =====
        JPanel tabPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        tabPanel.setBackground(Color.WHITE);
        tabPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        String[] tabs = {"All", "Pending", "Confirmed", "Completed", "History"};

        for (String tab : tabs) {
            JButton btn = new JButton(tab);
            styleTab(btn, tab.equals("All"));

            btn.addActionListener(e -> {
                currentFilter = tab;
                updateTabStyle(btn);
                displayAppointments();
            });

            tabButtons.add(btn);
            tabPanel.add(btn);
        }

        main.add(tabPanel);
        main.add(Box.createVerticalStrut(10));

        // ===== SEARCH BAR =====
     // ===== MODERN SEARCH BAR =====
        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        searchField = new JTextField();
        searchField.setBorder(new EmptyBorder(8, 15, 8, 15));
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBackground(new Color(245, 247, 250));

        searchField.setToolTipText("Search by car plate or name");

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                displayAppointments();
            }
        });

        searchWrapper.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1, true));
        searchWrapper.add(searchField, BorderLayout.CENTER);

        main.add(searchWrapper);
        main.add(Box.createVerticalStrut(20));

        // ===== LIST =====
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        main.add(listPanel);

        // ===== EMPTY =====
        emptyLabel = new JLabel("No appointment has been made yet", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setVisible(false);
        main.add(emptyLabel);

     // ===== WRAPPER TO FORCE TOP ALIGNMENT =====
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(main, BorderLayout.NORTH);

        // ===== SCROLL =====
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // smoother scroll (optional)

        add(scroll, BorderLayout.CENTER);
    }

    // ===== TAB STYLE =====
    private void styleTab(JButton btn, boolean active) {
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            btn.setBackground(new Color(180, 210, 235));
        } else {
            btn.setBackground(new Color(220, 235, 250));
        }
    }

    private void updateTabStyle(JButton selected) {
        for (JButton b : tabButtons) {
            styleTab(b, b == selected);
        }
    }

    // ===== LOAD FILE =====
    private void loadAppointmentsFromFile() {
        allAppointments.clear();

        try (BufferedReader br = new BufferedReader(new FileReader("data/Appointment.txt"))) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {

                if (first) {
                    first = false;
                    continue;
                }

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts[1].equals(customerID)) {
                    allAppointments.add(parts);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments");
        }
    }

    // ===== DISPLAY =====
    private void displayAppointments() {

        listPanel.removeAll();

        String keyword = searchField.getText().toLowerCase();
        boolean hasData = false;

        for (String[] p : allAppointments) {

            String status = p[16];

            if (!currentFilter.equals("All")) {

                if (currentFilter.equals("History")) {
                    if (!(status.equalsIgnoreCase("Completed") ||
                          status.equalsIgnoreCase("Paid") ||
                          status.equalsIgnoreCase("Cancelled"))) {
                        continue;
                    }
                } else {
                    if (!status.equalsIgnoreCase(currentFilter)) continue;
                }
            }

            if (!keyword.isEmpty() &&
                    !p[7].toLowerCase().contains(keyword) &&
                    !p[2].toLowerCase().contains(keyword)) continue;

            listPanel.add(createCard(p));
            listPanel.add(Box.createVerticalStrut(15));

            hasData = true;
        }

        emptyLabel.setVisible(!hasData);
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ===== CARD =====
    private JPanel createCard(String[] p) {

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(235, 243, 250));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        String service = p[4];
        String carModel = p[6];
        String date = p[12];
        String time = p[13];
        String statusText = p[16];

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(60, 130, 190));
        header.setBorder(new EmptyBorder(12, 18, 12, 18));

        // LEFT SIDE (TITLE + DATE)
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(new Color(60, 130, 190));

        JLabel title = new JLabel(service + " service for " + carModel);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel dateLabel = new JLabel(date + ", " + time);
        dateLabel.setForeground(new Color(220, 235, 255));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        left.add(title);
        left.add(dateLabel);

        // RIGHT SIDE (STATUS + ARROW)
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(new Color(60, 130, 190));

        JLabel status = new JLabel("  ●  " + statusText + "  ");
        status.setOpaque(true);
        status.setBackground(getStatusColor(statusText));
        status.setForeground(Color.BLACK);
        status.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel arrow = new JLabel("▼");
        arrow.setForeground(Color.WHITE);

        right.add(status);
        right.add(arrow);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        // ===== DETAILS =====
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBackground(new Color(235, 243, 250));
        details.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Info Grid
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBackground(new Color(235, 243, 250));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        infoPanel.add(new JLabel("Appointment ID"));
        infoPanel.add(new JLabel(p[0]));

        infoPanel.add(new JLabel("Service"));
        infoPanel.add(new JLabel(service));

        infoPanel.add(new JLabel("Car Plate"));
        infoPanel.add(new JLabel(p[7]));

        infoPanel.add(new JLabel("Total Price"));
        infoPanel.add(new JLabel("RM " + p[15]));

        details.add(infoPanel);
        details.add(Box.createVerticalStrut(15));

        // ===== CONDITIONAL BUTTONS BASED ON STATUS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(new Color(235, 243, 250));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        if ("Pending".equalsIgnoreCase(statusText)) {
            JButton cancel = new JButton("Cancel Appointment");
            cancel.setPreferredSize(new Dimension(400, 35));
            cancel.setFont(new Font("Arial", Font.PLAIN, 12));
            cancel.setBackground(new Color(220, 100, 100));
            cancel.setForeground(Color.WHITE);
            cancel.setFocusPainted(false);
            cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancel.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this appointment?",
                        "Confirm Cancel",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    cancelAppointment(p);
                }
            });

            JButton reschedule = new JButton("Reschedule");
            reschedule.setPreferredSize(new Dimension(400, 35));
            reschedule.setFont(new Font("Arial", Font.PLAIN, 12));
            reschedule.setBackground(new Color(100, 150, 220));
            reschedule.setForeground(Color.WHITE);
            reschedule.setFocusPainted(false);
            reschedule.setCursor(new Cursor(Cursor.HAND_CURSOR));
            reschedule.addActionListener(e -> rescheduleAppointment(p));

            buttonPanel.add(cancel);
            buttonPanel.add(reschedule);

        } else if ("Confirmed".equalsIgnoreCase(statusText)) {
            JButton makeRemark = new JButton("Change Remark");
            makeRemark.setPreferredSize(new Dimension(800, 35));
            makeRemark.setFont(new Font("Arial", Font.PLAIN, 12));
            makeRemark.setBackground(new Color(100, 180, 150));
            makeRemark.setForeground(Color.WHITE);
            makeRemark.setFocusPainted(false);
            makeRemark.setCursor(new Cursor(Cursor.HAND_CURSOR));
            makeRemark.addActionListener(e -> showRemarkDialog(p));

            buttonPanel.add(makeRemark);

        } else if ("Completed".equalsIgnoreCase(statusText)) {
            JButton makePay = new JButton("Make Payment");
            makePay.setPreferredSize(new Dimension(400, 35));
            makePay.setFont(new Font("Arial", Font.PLAIN, 12));
            makePay.setBackground(new Color(100, 180, 100));
            makePay.setForeground(Color.WHITE);
            makePay.setFocusPainted(false);
            makePay.setCursor(new Cursor(Cursor.HAND_CURSOR));
            makePay.addActionListener(e -> makePayment(p));

            JButton viewComment = new JButton("View Comment");
            viewComment.setPreferredSize(new Dimension(400, 35));
            viewComment.setFont(new Font("Arial", Font.PLAIN, 12));
            viewComment.setBackground(new Color(180, 150, 100));
            viewComment.setForeground(Color.WHITE);
            viewComment.setFocusPainted(false);
            viewComment.setCursor(new Cursor(Cursor.HAND_CURSOR));
            viewComment.addActionListener(e -> showCommentDialog(p));

            buttonPanel.add(makePay);
            buttonPanel.add(viewComment);
        }

        details.add(buttonPanel);

        details.setVisible(false);

        // ===== EXPAND =====
        header.setCursor(new Cursor(Cursor.HAND_CURSOR));
        header.addMouseListener(new MouseAdapter() {
            boolean open = false;

            public void mouseClicked(MouseEvent e) {
                open = !open;
                details.setVisible(open);
                arrow.setText(open ? "▲" : "▼");
                container.revalidate();
            }
        });

        container.add(header, BorderLayout.NORTH);
        container.add(details, BorderLayout.CENTER);

        return container;
    }

    // ===== BACK =====
    private void goBack() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof HomePage) {
            ((HomePage) frame).showPage(new CustomerAppointment());
        }
    }
    
    private void cancelAppointment(String[] selectedAppointment) {

        File inputFile = new File("data/Appointment.txt");
        File tempFile = new File("data/temp.txt");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))
        ) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {

                if (first) {
                    bw.write(line);
                    bw.newLine();
                    first = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts[0].equals(selectedAppointment[0]) &&   // Appointment ID
                	    parts[1].equals(selectedAppointment[1]) &&   // Customer ID
                	    parts[12].equals(selectedAppointment[12]) && // Date
                	    parts[13].equals(selectedAppointment[13])) { // Time

                	    parts[16] = "Cancelled"; // or "Cancelled"
                	    line = String.join(",", parts);
                	}
                
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error cancelling appointment");
            return;
        }

        // replace original file
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        // refresh UI
        loadAppointmentsFromFile();
        displayAppointments();

        JOptionPane.showMessageDialog(this, "Appointment cancelled successfully");
    }

    private void rescheduleAppointment(String[] selectedAppointment) {
        // Navigate to RescheduleTimeSlotPage to let user select new date and time
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            if (frame instanceof HomePage) {
                ((HomePage) frame).showPage(new RescheduleTimeSlotPage(selectedAppointment, this));
            }
        }
    }

    public void updateAppointmentDateTime(String[] selectedAppointment, String newDate, String newTime) {
        File inputFile = new File("data/Appointment.txt");
        File tempFile = new File("data/temp.txt");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))
        ) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {

                if (first) {
                    bw.write(line);
                    bw.newLine();
                    first = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts[0].equals(selectedAppointment[0]) &&   // Appointment ID
                    parts[1].equals(selectedAppointment[1]) &&   // Customer ID
                    parts[12].equals(selectedAppointment[12]) && // Current Date
                    parts[13].equals(selectedAppointment[13])) { // Current Time

                    parts[12] = newDate;  // Update date
                    parts[13] = newTime;  // Update time
                    line = String.join(",", parts);
                }

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error rescheduling appointment");
            return;
        }

        // replace original file
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        // refresh UI
        loadAppointmentsFromFile();
        displayAppointments();

        JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully");
    }

    private void showRemarkDialog(String[] selectedAppointment) {
        // Create a dialog for managing remarks
        JDialog dialog = new JDialog();
        dialog.setTitle("Make/Change Remark");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Enter or edit your remark:");
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        JTextArea remarkArea = new JTextArea(selectedAppointment[9]);
        remarkArea.setFont(new Font("Arial", Font.PLAIN, 12));
        remarkArea.setLineWrap(true);
        remarkArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            String newRemark = remarkArea.getText().trim();
            updateAppointmentRemark(selectedAppointment, newRemark);
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateAppointmentRemark(String[] selectedAppointment, String newRemark) {
        File inputFile = new File("data/Appointment.txt");
        File tempFile = new File("data/temp.txt");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))
        ) {

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {

                if (first) {
                    bw.write(line);
                    bw.newLine();
                    first = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts[0].equals(selectedAppointment[0]) &&   // Appointment ID
                    parts[1].equals(selectedAppointment[1])) {   // Customer ID

                    parts[9] = newRemark;  // Update remark
                    line = String.join(",", parts);
                }

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating remark");
            return;
        }

        // replace original file
        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        // refresh UI
        loadAppointmentsFromFile();
        displayAppointments();

        JOptionPane.showMessageDialog(this, "Remark updated successfully");
    }

    private void makePayment(String[] selectedAppointment) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof HomePage) {
            // Pass appointment details to payment page
            // This assumes you have a PaymentPage or similar class
            // For now, we'll show a simple dialog
            JOptionPane.showMessageDialog(
                this,
                "Redirecting to payment page for Appointment ID: " + selectedAppointment[0] +
                "\nAmount: RM " + selectedAppointment[15],
                "Make Payment",
                JOptionPane.INFORMATION_MESSAGE
            );
            // TODO: Navigate to actual payment page
            // ((HomePage) frame).showPage(new PaymentPage(selectedAppointment));
        }
    }

    private void showCommentDialog(String[] selectedAppointment) {
        // Create a dialog to display technician comments
        JDialog dialog = new JDialog();
        dialog.setTitle("Technician Comment");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Technician Comment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));

        String comment = selectedAppointment[17]; // Technician comment is at index 17
        if (comment == null || comment.trim().isEmpty() || comment.equals("NULL")) {
            comment = "No comment provided";
        }

        JTextArea commentArea = new JTextArea(comment);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 12));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(commentArea);
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(15));

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeBtn);

        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return new Color(255, 235, 190); // light yellow

            case "confirmed":
                return new Color(190, 230, 255); // light blue

            case "completed":
            case "paid":
                return new Color(200, 255, 200); // light green

            case "cancelled":
                return new Color(200, 200, 200); // gray

            default:
                return new Color(220, 220, 220);
        }
    }
}