package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminAppointment extends JPanel {
    private LocalDate currentWeekStart;
    private LocalDate selectedWeekStart;
    private LocalDate selectedDate = null; 

    public AdminAppointment() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        currentWeekStart = LocalDate.now();
        currentWeekStart = currentWeekStart.minusDays(currentWeekStart.getDayOfWeek().getValue() - 1);
        selectedWeekStart = currentWeekStart;
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(25, 35, 25, 35));
        mainContainer.setMaximumSize(new Dimension(1300, 1250));
        mainContainer.setPreferredSize(new Dimension(1300, 1250));

        for (Component comp : mainContainer.getComponents()) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        }

        // 1. Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(1230, 65));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Appointment Creation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.LIGHT_GRAY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(separator, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        mainContainer.add(headerPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 3. Days Selection Section (declare early for use in navigation listeners)
        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 25));
        daysPanel.setBackground(Color.WHITE);
        initializeDayButtons(daysPanel);

        // 2. Week Navigation Section
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setMaximumSize(new Dimension(1230, 55));
        JButton currentWeekBtn = createStyledButton("Current Week", new Color(171, 209, 237), Color.BLACK);
        currentWeekBtn.setPreferredSize(new Dimension(130, 48));
        currentWeekBtn.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel weekLabel = new JLabel(getFormattedWeekLabel(selectedWeekStart), SwingConstants.CENTER);
        weekLabel.setFont(new Font("Arial", Font.BOLD, 27));
        JPanel arrowsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        arrowsPanel.setBackground(Color.WHITE);
        JButton prevBtn = createStyledButton("<", new Color(171, 209, 237), Color.BLACK);
        prevBtn.setPreferredSize(new Dimension(48, 45));
        prevBtn.setFont(new Font("Arial", Font.BOLD, 16));
        JButton nextBtn = createStyledButton(">", new Color(171, 209, 237), Color.BLACK);
        nextBtn.setPreferredSize(new Dimension(48, 45));
        nextBtn.setFont(new Font("Arial", Font.BOLD, 16));
        arrowsPanel.add(prevBtn);
        arrowsPanel.add(nextBtn);
        navPanel.add(currentWeekBtn, BorderLayout.WEST);
        navPanel.add(weekLabel, BorderLayout.CENTER);
        navPanel.add(arrowsPanel, BorderLayout.EAST);
        navPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentWeekBtn.addActionListener(e -> {
            selectedWeekStart = currentWeekStart;
            updateWeekDisplay(weekLabel);
            updateDayButtons(daysPanel);
        });

        prevBtn.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            updateWeekDisplay(weekLabel);
            updateDayButtons(daysPanel);
        });

        nextBtn.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.plusWeeks(1);
            updateWeekDisplay(weekLabel);
            updateDayButtons(daysPanel);
        });

        mainContainer.add(navPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 18)));
        
        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(daysPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // 4. Normal Services Section
        JLabel normalLabel = new JLabel("Normal Services");
        normalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        normalLabel.setForeground(new Color(120, 120, 120));
        normalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(normalLabel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel normalServicesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        normalServicesPanel.setBackground(Color.WHITE);
        String[] normalTimes = {
                "8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00"
        };
        for (String time : normalTimes) {
            JButton timeBtn = createStyledButton(time, new Color(220, 220, 220), Color.BLACK);
            timeBtn.setPreferredSize(new Dimension(175, 55));
            timeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            normalServicesPanel.add(timeBtn);
        }
        normalServicesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(normalServicesPanel);

        // 5. Major Services Section
        JLabel majorLabel = new JLabel("Major Services");
        majorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        majorLabel.setForeground(new Color(120, 120, 120));
        majorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(majorLabel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel majorServicesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        majorServicesPanel.setBackground(Color.WHITE);
        String[] majorTimes = { "8:00 - 11:00", "9:00 - 12:00", "10:00 - 13:00", "14:00 - 17:00", "15:00 - 18:00", "16:00 - 19:00", "17:00 - 20:00" };
        for (String time : majorTimes) {
            JButton timeBtn = createStyledButton(time, new Color(220, 220, 220), Color.BLACK);
            timeBtn.setPreferredSize(new Dimension(175, 55));
            timeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            majorServicesPanel.add(timeBtn);
        }
        majorServicesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(majorServicesPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 0)));


        // 6. Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        actionPanel.setBackground(Color.WHITE);
        JButton cancelBtn = createStyledButton("Return To Page", new Color(245, 240, 230), Color.BLACK);
        cancelBtn.setPreferredSize(new Dimension(220, 60));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
        JButton confirmBtn = createStyledButton("Confirm Booking", new Color(60, 140, 210), Color.WHITE);
        confirmBtn.setPreferredSize(new Dimension(220, 60));
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 16));
        actionPanel.add(cancelBtn);
        actionPanel.add(confirmBtn);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContainer.add(actionPanel);
        wrapperPanel.add(mainContainer);
        add(wrapperPanel, BorderLayout.CENTER);

        // 7. Footer Notice
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(0, 35, 25, 35));

        JLabel noticeLabel = new JLabel("IMPORTANT NOTICE ~ All of the worker and technician work from 8am to 7pm",
                SwingConstants.CENTER);
        noticeLabel.setOpaque(true);
        noticeLabel.setBackground(new Color(171, 209, 237));
        noticeLabel.setBorder(new EmptyBorder(15, 20, 15, 20));
        noticeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        noticeLabel.setPreferredSize(new Dimension(1230, 50));
        footerPanel.add(noticeLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private String getFormattedWeekLabel(LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return "Week of " + weekStart.format(formatter);
    }

    private void initializeDayButtons(JPanel daysPanel) {
        updateDayButtons(daysPanel);
    }

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
            dayBtn.setPreferredSize(new Dimension(115, 80));
            dayBtn.setFont(new Font("Arial", Font.BOLD, 15));
            LocalDate clickedDate = date;
            dayBtn.addActionListener(e -> {
                selectedDate = clickedDate;
                updateDayButtons(daysPanel);
            });
            daysPanel.add(dayBtn);
        }
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private void updateWeekDisplay(JLabel weekLabel) {
        weekLabel.setText(getFormattedWeekLabel(selectedWeekStart));
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        return new RoundedButton(text, bgColor, fgColor);
    }

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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            g2.setColor(new Color(200, 200, 200));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            super.paintComponent(g);
        }
    }
}