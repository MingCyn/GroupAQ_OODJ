package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for staff appointment booking pages.
 * Implements the inheritance OOP pillar to allow both Admin and CounterStaff
 * to access the same appointment booking functionality.
 * 
 * Both Admin and CounterStaff roles inherit from this class to share
 * common appointment UI components and logic.
 */

public abstract class StaffAppointmentPage extends JPanel {
    protected LocalDate currentWeekStart;
    protected LocalDate selectedWeekStart;
    protected LocalDate selectedDate = null;
    protected String userRole;

    /**
     * Constructor that initializes the staff appointment page with user role.
     * 
     * @param userRole The role of the user accessing this page (Admin,
     *                 CounterStaff, etc.)
     */
    public StaffAppointmentPage(String userRole) {
        this.userRole = userRole;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        currentWeekStart = LocalDate.now();
        currentWeekStart = currentWeekStart.minusDays(currentWeekStart.getDayOfWeek().getValue() - 1);
        selectedWeekStart = currentWeekStart;
        initializeUI();
    }

    /**
     * Initializes the user interface for the appointment booking page.
     * This method is called during construction to build all UI components.
     */
    protected void initializeUI() {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        add(new JScrollPane(mainContainer), BorderLayout.CENTER);

        // HEADER
        mainContainer.add(createHeaderPanel());

        // WEEK NAV + DAYS
        JPanel daysPanel = new JPanel(new GridLayout(1, 7, 8, 8));
        daysPanel.setBackground(Color.WHITE);

        mainContainer.add(createWeekNavigationPanel(daysPanel));
        mainContainer.add(daysPanel);

        initializeDayButtons(daysPanel);

        // SERVICES
        mainContainer.add(createServiceSection("Normal Services", getNormalServiceTimes()));
        mainContainer.add(createServiceSection("Major Services", getMajorServiceTimes()));

        // ACTION
        if (shouldShowActionButtons()) {
            mainContainer.add(createActionPanel());
        }

        // FOOTER
        mainContainer.add(createFooterPanel());
    }
//    protected void initializeUI() {
//        JPanel wrapperPanel = new JPanel(new GridBagLayout());
//        wrapperPanel.setBackground(Color.WHITE);
//        JPanel mainContainer = new JPanel();
//        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
//        mainContainer.setBackground(Color.WHITE);
//        mainContainer.setBorder(new EmptyBorder(15, 30, 15, 30));
//        mainContainer.setPreferredSize(new Dimension(1400, 950));
//
//        for (Component comp : mainContainer.getComponents()) {
//            if (comp instanceof JComponent) {
//                ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
//            }
//        }
//
//        // 1. Header Section
//        mainContainer.add(createHeaderPanel());
//        mainContainer.add(Box.createRigidArea(new Dimension(0, 12)));
//
//        // 2. Days Selection Section
//        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
//        daysPanel.setBackground(Color.WHITE);
//        initializeDayButtons(daysPanel);
//
//        // 3. Week Navigation Section
//        mainContainer.add(createWeekNavigationPanel(daysPanel));
//        mainContainer.add(Box.createRigidArea(new Dimension(0, 5)));
//
//        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        daysPanel.setMaximumSize(new Dimension(1230, 150));
//        mainContainer.add(daysPanel);
//        mainContainer.add(Box.createRigidArea(new Dimension(0, 5)));
//
//        // 4. Normal Services Section
//        mainContainer.add(createServiceSection("Normal Services", getNormalServiceTimes()));
//        mainContainer.add(Box.createRigidArea(new Dimension(0, 0)));
//
//        // 5. Major Services Section
//        mainContainer.add(createServiceSection("Major Services", getMajorServiceTimes()));
//        mainContainer.add(Box.createRigidArea(new Dimension(0, 5)));
//
//        // 6. Action Buttons
//        if (shouldShowActionButtons()) {
//            mainContainer.add(createActionPanel());
//        }
//        mainContainer.add(Box.createRigidArea(new Dimension(0, 25)));
//
//        // 7. Footer Notice
//        mainContainer.add(createFooterPanel());
//
//        wrapperPanel.add(mainContainer);
//        add(wrapperPanel, BorderLayout.CENTER);
//    }

    protected boolean shouldShowActionButtons() {
        return true;
    }

    protected JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(getPageTitle());
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        title.setFont(new Font("Arial", Font.BOLD, 24));

        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    protected String getPageTitle() {
        return "Select Your Timeslot For Your Appointment";
    }

//    protected JPanel createWeekNavigationPanel(JPanel daysPanel) {
//        JPanel navPanel = new JPanel(new BorderLayout());
//        navPanel.setBackground(Color.WHITE);
//        navPanel.setMaximumSize(new Dimension(1230, 65));
//
//        JButton currentWeekBtn = createStyledButton("Current Week", new Color(171, 209, 237), Color.BLACK);
//        currentWeekBtn.setPreferredSize(new Dimension(130, 48));
//        currentWeekBtn.setFont(new Font("Arial", Font.BOLD, 13));
//
//        JLabel weekLabel = new JLabel(getFormattedWeekLabel(selectedWeekStart), SwingConstants.CENTER);
//        weekLabel.setFont(new Font("Arial", Font.BOLD, 27));
//
//        JPanel arrowsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
//        arrowsPanel.setBackground(Color.WHITE);
//
//        JButton prevBtn = createStyledButton("<", new Color(171, 209, 237), Color.BLACK);
//        prevBtn.setPreferredSize(new Dimension(48, 48));
//        prevBtn.setFont(new Font("Arial", Font.BOLD, 16));
//
//        JButton nextBtn = createStyledButton(">", new Color(171, 209, 237), Color.BLACK);
//        nextBtn.setPreferredSize(new Dimension(48, 48));
//        nextBtn.setFont(new Font("Arial", Font.BOLD, 16));
//
//        arrowsPanel.add(prevBtn);
//        arrowsPanel.add(nextBtn);
//        navPanel.add(currentWeekBtn, BorderLayout.WEST);
//        navPanel.add(weekLabel, BorderLayout.CENTER);
//        navPanel.add(arrowsPanel, BorderLayout.EAST);
//        navPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//     // Helper to update previous button state
//        Runnable updatePrevButtonState = () -> {
//            boolean isCurrentWeek = selectedWeekStart.equals(currentWeekStart);
//            prevBtn.setEnabled(!isCurrentWeek);
//        };
//
//        currentWeekBtn.addActionListener(e -> {
//            selectedWeekStart = currentWeekStart;
//            updateWeekDisplay(weekLabel);
//            updateDayButtons(daysPanel);
//            updatePrevButtonState.run();
//        });
//
//        prevBtn.addActionListener(e -> {
//            selectedWeekStart = selectedWeekStart.minusWeeks(1);
//            updateWeekDisplay(weekLabel);
//            updateDayButtons(daysPanel);
//            updatePrevButtonState.run();
//        });
//
//        nextBtn.addActionListener(e -> {
//            selectedWeekStart = selectedWeekStart.plusWeeks(1);
//            updateWeekDisplay(weekLabel);
//            updateDayButtons(daysPanel);
//            updatePrevButtonState.run();
//        });
//
//        // Set initial state
//        updatePrevButtonState.run();
//
//        return navPanel;
//    }
    
    protected JPanel createWeekNavigationPanel(JPanel daysPanel) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // ===== LEFT: Current Week =====
        JButton current = createStyledButton("Current Week", new Color(171, 209, 237), Color.BLACK);
        current.setFont(new Font("Arial", Font.PLAIN, 12));
        current.setMargin(new Insets(6, 12, 6, 12));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(current);

        // ===== CENTER: Week Label =====
        JLabel label = new JLabel(getFormattedWeekLabel(selectedWeekStart), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        // ===== RIGHT: Arrows =====
        JButton prev = createStyledButton("<", new Color(171, 209, 237), Color.BLACK);
        JButton next = createStyledButton(">", new Color(171, 209, 237), Color.BLACK);

        prev.setMargin(new Insets(6, 10, 6, 10));
        next.setMargin(new Insets(6, 10, 6, 10));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(prev);
        rightPanel.add(next);

        // ===== ADD TO MAIN PANEL =====
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        // ===== LOGIC (UNCHANGED) =====
        Runnable updatePrev = () -> prev.setEnabled(!selectedWeekStart.equals(currentWeekStart));

        current.addActionListener(e -> {
            selectedWeekStart = currentWeekStart;
            updateWeekDisplay(label);
            updateDayButtons(daysPanel);
            updatePrev.run();
        });

        prev.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.minusWeeks(1);
            updateWeekDisplay(label);
            updateDayButtons(daysPanel);
            updatePrev.run();
        });

        next.addActionListener(e -> {
            selectedWeekStart = selectedWeekStart.plusWeeks(1);
            updateWeekDisplay(label);
            updateDayButtons(daysPanel);
            updatePrev.run();
        });

        updatePrev.run();
        return panel;
    }
    
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
                // Default behavior - can be overridden by subclasses
            });

            gridPanel.add(btn);
        }

        container.add(labelWrapper);
        container.add(Box.createVerticalStrut(8));
        container.add(gridPanel);

        return container;
    }

    protected String[] getNormalServiceTimes() {
        return new String[] {
                "8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
                "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00"
        };
    }

    protected String[] getMajorServiceTimes() {
        return new String[] {
                "8:00 - 11:00", "9:00 - 12:00", "10:00 - 13:00", "14:00 - 17:00",
                "15:00 - 18:00", "16:00 - 19:00", "17:00 - 20:00"
        };
    }

//    protected JPanel createActionPanel() {
//        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
//        actionPanel.setBackground(Color.WHITE);
//
//        JButton cancelBtn = createStyledButton("Return To Page", new Color(245, 240, 230), Color.BLACK);
//        cancelBtn.setPreferredSize(new Dimension(220, 60));
//        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
//
//        JButton confirmBtn = createStyledButton("Confirm Booking", new Color(60, 140, 210), Color.WHITE);
//        confirmBtn.setPreferredSize(new Dimension(220, 60));
//        confirmBtn.setFont(new Font("Arial", Font.BOLD, 16));
//
//        actionPanel.add(cancelBtn);
//        actionPanel.add(confirmBtn);
//        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        actionPanel.setMaximumSize(new Dimension(1230, 90));
//
//        return actionPanel;
//    }
    
    protected JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton cancel = createStyledButton("Return To Page", new Color(245, 240, 230), Color.BLACK);
        JButton confirm = createStyledButton("Confirm Booking", new Color(60, 140, 210), Color.WHITE);

        panel.add(cancel);
        panel.add(confirm);

        return panel;
    }

//    protected JPanel createFooterPanel() {
//        JPanel footerPanel = new JPanel();
//        footerPanel.setBackground(Color.WHITE);
//        footerPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
//
//        JLabel noticeLabel = new JLabel("IMPORTANT NOTICE ~ All of the worker and technician work from 8am to 7pm",
//                SwingConstants.CENTER);
//        noticeLabel.setOpaque(true);
//        noticeLabel.setBackground(new Color(171, 209, 237));
//        noticeLabel.setBorder(new EmptyBorder(15, 20, 15, 20));
//        noticeLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        noticeLabel.setPreferredSize(new Dimension(1230, 50));
//        footerPanel.add(noticeLabel);
//        footerPanel.setMaximumSize(new Dimension(1230, 80));
//        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        return footerPanel;
//    }
    
    protected JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(
                "IMPORTANT NOTICE ~ All workers operate from 8am to 7pm",
                SwingConstants.CENTER
        );

        label.setOpaque(true);
        label.setBackground(new Color(171, 209, 237));
        label.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    protected String getFormattedWeekLabel(LocalDate weekStart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return "Week of " + weekStart.format(formatter);
    }

    protected void initializeDayButtons(JPanel daysPanel) {
        updateDayButtons(daysPanel);
    }

    protected void updateDayButtons(JPanel panel) {
        panel.removeAll();

        for (int i = 0; i < 7; i++) {
            LocalDate date = selectedWeekStart.plusDays(i);

            String text = "<html><center>" +
                    date.getDayOfMonth() + "<br>" +
                    date.getDayOfWeek().toString().substring(0, 3) +
                    "</center></html>";

            JButton btn = createStyledButton(
                    text,
                    selectedDate != null && selectedDate.equals(date)
                            ? new Color(50, 130, 200)
                            : new Color(171, 209, 237),
                    Color.BLACK
            );

            LocalDate clicked = date;
            btn.addActionListener(e -> {
                selectedDate = clicked;
                updateDayButtons(panel);
            });

            panel.add(btn);
        }

        panel.revalidate();
        panel.repaint();
    }

    protected void updateWeekDisplay(JLabel weekLabel) {
        weekLabel.setText(getFormattedWeekLabel(selectedWeekStart));
    }

    protected JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        return new RoundedButton(text, bgColor, fgColor);
    }

    protected class RoundedButton extends JButton {
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

    public String getUserRole() {
        return userRole;
    }
}
