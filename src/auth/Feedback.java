package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Feedback extends JPanel {

    private String currentUserID;
    private String currentUsername;
    private String currentUserRole;

    private JPanel feedbackPanel;
    private JLabel emptyLabel;
    private JScrollPane scrollPane;
    private List<String[]> allFeedback = new ArrayList<>();
    private Set<String> likedFeedback = new HashSet<>(); // Track which feedback the current user has liked

    public Feedback(String userID, String username, String userRole) {
        this.currentUserID = userID;
        this.currentUsername = username;
        this.currentUserRole = userRole;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initializeUI();
        loadFeedbackFromFile();
        displayFeedback();
    }

    private void initializeUI() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(20, 40, 20, 40));

        // ================= HEADER =================
        JLabel titleLabel = new JLabel("Customer Reviews & Feedback");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(titleLabel);
        main.add(Box.createVerticalStrut(5));

        JSeparator separator = new JSeparator();
        main.add(separator);
        main.add(Box.createVerticalStrut(15));

        // ===== FEEDBACK LIST =====
        feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
        feedbackPanel.setBackground(Color.WHITE);

        // ===== EMPTY LABEL =====
        emptyLabel = new JLabel("No reviews available yet", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setVisible(false);
        main.add(emptyLabel);

        main.add(feedbackPanel);

        // ===== WRAPPER TO FORCE TOP ALIGNMENT =====
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(main, BorderLayout.NORTH);

        // ===== SCROLL =====
        scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadFeedbackFromFile() {
        allFeedback.clear();

        try (BufferedReader br = new BufferedReader(new FileReader("data/Feedback.txt"))) {
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                // Trim all parts to remove leading/trailing whitespace
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }
                allFeedback.add(parts);
                System.out.println("Loaded feedback: " + parts[0] + " | ReplyTo: " + (parts.length > 6 ? parts[6] : "N/A"));
            }
            System.out.println("Total feedback loaded: " + allFeedback.size());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading feedback: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayFeedback() {
        feedbackPanel.removeAll();

        boolean hasData = false;

        System.out.println("Display feedback called. Total feedback: " + allFeedback.size());

        // Display main feedback (where replyTo is "None")
        for (String[] feedback : allFeedback) {
            String replyTo = feedback.length > 6 ? feedback[6] : "None";
            System.out.println("Checking feedback: " + feedback[0] + " | ReplyTo: " + replyTo);

            if (replyTo.equalsIgnoreCase("None")) {
                System.out.println("Adding main feedback: " + feedback[0]);
                feedbackPanel.add(createFeedbackCard(feedback));
                feedbackPanel.add(Box.createVerticalStrut(15));
                hasData = true;

                // Display replies for this feedback
                String feedbackID = feedback[0];

                // Display replies for this feedback
                for (String[] reply : allFeedback) {
                    String replyToID = reply.length > 6 ? reply[6] : "None";
                    if (replyToID.equalsIgnoreCase(feedbackID)) {
                        System.out.println("Adding reply: " + reply[0]);
                        feedbackPanel.add(createReplyCard(reply, feedbackID));
                        feedbackPanel.add(Box.createVerticalStrut(10));
                    }
                }
            }
        }

        System.out.println("Has data: " + hasData);
        emptyLabel.setVisible(!hasData);
        feedbackPanel.revalidate();
        feedbackPanel.repaint();
    }

    private JPanel createFeedbackCard(String[] feedback) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(15, 10, 15, 10));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        String feedbackID = feedback[0];
        String appointmentID = feedback[1];
        String customerID = feedback[2];
        String userRole = feedback[3];
        String rating = feedback.length > 4 ? feedback[4] : "0";
        String message = feedback.length > 5 ? feedback[5] : "";
        String createdDate = feedback.length > 7 ? feedback[7] : "";
        int likes = feedback.length > 8 ? Integer.parseInt(feedback[8]) : 0;

        String customerName = getNameByID(customerID);

        // ===== AVATAR =====
        JLabel avatar = new JLabel(customerName.substring(0, 1).toUpperCase(), SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(66, 133, 244));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 16));

        // ===== TEXT INFO =====
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(customerName + " (" + userRole + ")");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel starsLabel = new JLabel(getStarString(Integer.parseInt(rating)));
        starsLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        starsLabel.setForeground(new Color(255, 180, 0));

        JLabel dateLabel = new JLabel(createdDate);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        dateLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(starsLabel);
        textPanel.add(dateLabel);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        header.setBackground(Color.WHITE);
        header.add(avatar);
        header.add(textPanel);

        // ===== MESSAGE =====
        JLabel messageLabel = new JLabel("<html><div style='width:350px'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        messageLabel.setBorder(new EmptyBorder(10, 50, 10, 0));

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        footer.setBackground(Color.WHITE);

        boolean userLiked = likedFeedback.contains(feedbackID);

        JButton likeBtn = new JButton(userLiked ? "❤" + likes : "🤍" + likes);
        likeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        likeBtn.setBorderPainted(false);
        likeBtn.setContentAreaFilled(false);
        likeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        likeBtn.setForeground(Color.GRAY);

        likeBtn.addActionListener(e -> {
            boolean isCurrentlyLiked = likedFeedback.contains(feedbackID);
            if (isCurrentlyLiked) {
                likedFeedback.remove(feedbackID);
                updateFeedbackLikes(feedbackID, likes - 1);
            } else {
                likedFeedback.add(feedbackID);
                updateFeedbackLikes(feedbackID, likes + 1);
            }
            loadFeedbackFromFile();
            displayFeedback();
        });

        JButton replyBtn = new JButton("Reply");
        replyBtn.setBorderPainted(false);
        replyBtn.setContentAreaFilled(false);
        replyBtn.setForeground(new Color(66, 133, 244));
        replyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        replyBtn.addActionListener(e -> showReplyDialog(feedbackID));

        footer.add(likeBtn);
        footer.add(replyBtn);

        // ===== THREE DOT MENU =====
        if (currentUserID.equals(customerID)) {
            JButton menuBtn = createThreeDotMenu(feedbackID, customerID, false);
            footer.add(menuBtn);
        }

        container.add(header, BorderLayout.NORTH);
        container.add(messageLabel, BorderLayout.CENTER);
        container.add(footer, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createReplyCard(String[] reply, String mainFeedbackID) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(5, 60, 5, 10)); // indent like Google replies
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        String feedbackID = reply[0];
        String userRole = reply[3];
        String replyMessage = reply.length > 5 ? reply[5] : "";
        String createdDate = reply.length > 7 ? reply[7] : "";
        String replyUserID = reply[2];

        String replyUserName = getNameByID(replyUserID);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        // LEFT SIDE (name + date)
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(replyUserName + " (" + userRole + ")");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel dateLabel = new JLabel(createdDate);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        dateLabel.setForeground(Color.GRAY);

        left.add(nameLabel);
        left.add(dateLabel);

        header.add(left, BorderLayout.WEST);

        // ===== MESSAGE =====
        JLabel messageLabel = new JLabel("<html>" + replyMessage + "</html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        // ===== FOOTER WITH LIKE AND MENU =====
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);

        int replyLikes = reply.length > 8 ? Integer.parseInt(reply[8]) : 0;
        boolean replyUserLiked = likedFeedback.contains(feedbackID);

        JButton replyLikeBtn = new JButton(replyUserLiked ? "❤" + replyLikes : "🤍" + replyLikes);
        replyLikeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        replyLikeBtn.setBorderPainted(false);
        replyLikeBtn.setContentAreaFilled(false);
        replyLikeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        replyLikeBtn.setForeground(Color.GRAY);

        replyLikeBtn.addActionListener(e -> {
            boolean isCurrentlyLiked = likedFeedback.contains(feedbackID);
            if (isCurrentlyLiked) {
                likedFeedback.remove(feedbackID);
                updateFeedbackLikes(feedbackID, replyLikes - 1);
            } else {
                likedFeedback.add(feedbackID);
                updateFeedbackLikes(feedbackID, replyLikes + 1);
            }
            loadFeedbackFromFile();
            displayFeedback();
        });

        JPanel leftFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftFooter.setBackground(Color.WHITE);
        leftFooter.add(replyLikeBtn);

        JButton replyToReplyBtn = new JButton("Reply");
        replyToReplyBtn.setBorderPainted(false);
        replyToReplyBtn.setContentAreaFilled(false);
        replyToReplyBtn.setForeground(new Color(66, 133, 244));
        replyToReplyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        replyToReplyBtn.addActionListener(e -> showReplyDialog(mainFeedbackID));
        leftFooter.add(replyToReplyBtn);

        if (currentUserID.equals(replyUserID)) {
            JButton replyMenuBtn = createThreeDotMenu(feedbackID, replyUserID, false);
            leftFooter.add(replyMenuBtn);
        }

        footer.add(leftFooter, BorderLayout.WEST);

        // ===== ADD TO CONTAINER =====
        container.add(header, BorderLayout.NORTH);
        container.add(messageLabel, BorderLayout.CENTER);
        container.add(footer, BorderLayout.SOUTH);

        return container;
    }

    private JButton createThreeDotMenu(String feedbackID, String feedbackUserID) {
        return createThreeDotMenu(feedbackID, feedbackUserID, true);
    }

    private JButton createThreeDotMenu(String feedbackID, String feedbackUserID, boolean withBackground) {
        JButton menuBtn = new JButton("⋮");
        menuBtn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        menuBtn.setFocusPainted(false);
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (withBackground) {
            menuBtn.setPreferredSize(new Dimension(35, 35));
            menuBtn.setBackground(new Color(200, 200, 200));
            menuBtn.setForeground(Color.BLACK);
        } else {
            menuBtn.setBorderPainted(false);
            menuBtn.setContentAreaFilled(false);
            menuBtn.setForeground(Color.GRAY);
        }

        menuBtn.addActionListener(e -> {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem editItem = new JMenuItem("Edit");
            editItem.addActionListener(ae -> showEditDialog(feedbackID, feedbackUserID));
            popupMenu.add(editItem);

            JMenuItem deleteItem = new JMenuItem("Delete");
            deleteItem.addActionListener(ae -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this feedback?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteFeedback(feedbackID);
                }
            });
            popupMenu.add(deleteItem);

            popupMenu.show(menuBtn, 0, menuBtn.getHeight());
        });

        return menuBtn;
    }

    private void showReplyDialog(String feedbackID) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Reply to Feedback");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Write your reply:");
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        JTextArea replyArea = new JTextArea(6, 40);
        replyArea.setFont(new Font("Arial", Font.PLAIN, 12));
        replyArea.setLineWrap(true);
        replyArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(replyArea);
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        JButton submitBtn = new JButton("Submit Reply");

        cancelBtn.addActionListener(e -> dialog.dispose());

        submitBtn.addActionListener(e -> {
            String replyText = replyArea.getText().trim();
            if (replyText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a reply");
                return;
            }

            addReplyToFeedback(feedbackID, replyText);
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Reply submitted successfully");
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(submitBtn);

        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addReplyToFeedback(String feedbackID, String replyText) {
        File inputFile = new File("data/Feedback.txt");
        File tempFile = new File("data/temp.txt");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))
        ) {

            String line;
            boolean first = true;
            String newFeedbackID = generateFeedbackID();
            String createdDate = getCurrentDate();

            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();

                if (first) {
                    first = false;
                    continue;
                }
            }

            // Add new reply
            String appointmentID = getAppointmentIDByFeedbackID(feedbackID);
            String replyLine = newFeedbackID + "," + appointmentID + "," + currentUserID + "," + 
                              currentUserRole + ",N/A," + replyText + "," + feedbackID + "," + createdDate + ",0";
            bw.write(replyLine);
            bw.newLine();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error adding reply");
            return;
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        loadFeedbackFromFile();
        displayFeedback();
    }

    private void showEditDialog(String feedbackID, String feedbackUserID) {
        // Find the feedback entry
        String[] targetFeedback = null;
        for (String[] fb : allFeedback) {
            if (fb[0].equals(feedbackID)) {
                targetFeedback = fb;
                break;
            }
        }

        if (targetFeedback == null) return;

        String currentMessage = targetFeedback.length > 5 ? targetFeedback[5].trim() : "";

        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Feedback");
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Edit your feedback:");
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        JTextArea feedbackArea = new JTextArea(currentMessage);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 12));
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        JButton saveBtn = new JButton("Save");

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String newMessage = feedbackArea.getText().trim();
            if (newMessage.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a message");
                return;
            }

            updateFeedback(feedbackID, newMessage);
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Feedback updated successfully");
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateFeedback(String feedbackID, String newMessage) {
        File inputFile = new File("data/Feedback.txt");
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

                String[] parts = line.split(",", -1);

                if (parts[0].equals(feedbackID)) {
                    parts[5] = newMessage;
                    line = String.join(",", parts);
                }

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating feedback");
            return;
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        loadFeedbackFromFile();
        displayFeedback();
    }

    private void deleteFeedback(String feedbackID) {
        File inputFile = new File("data/Feedback.txt");
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

                if (!parts[0].equals(feedbackID)) {
                    bw.write(line);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting feedback");
            return;
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }

        loadFeedbackFromFile();
        displayFeedback();

        JOptionPane.showMessageDialog(this, "Feedback deleted successfully");
    }

    private void updateFeedbackLikes(String feedbackID, int newLikeCount) {
        File inputFile = new File("data/Feedback.txt");
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

                if (parts.length > 0 && parts[0].equals(feedbackID)) {
                    // Update the likes column (index 8)
                    if (parts.length <= 8) {
                        // If the likes column doesn't exist, add it
                        while (parts.length < 9) {
                            line = line + ",0";
                            parts = line.split(",");
                        }
                    }
                    parts[8] = String.valueOf(newLikeCount);
                    line = String.join(",", parts);
                }

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating likes");
            return;
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        }
    }

    private String getNameByID(String userID) {
        try {
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("data", "account.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[0].trim().equals(userID)) {
                    return parts[1].trim();
                }
            }
        } catch (IOException e) {
            // Return ID if name not found
        }
        return userID;
    }

    private String getAppointmentIDByFeedbackID(String feedbackID) {
        for (String[] fb : allFeedback) {
            if (fb[0].equals(feedbackID)) {
                return fb.length > 1 ? fb[1].trim() : "";
            }
        }
        return "";
    }

    private String generateFeedbackID() {
        int maxID = 0;

        for (String[] fb : allFeedback) {
            if (fb.length > 0) {
                String feedbackID = fb[0].trim();
                if (feedbackID.startsWith("FB")) {
                    try {
                        int id = Integer.parseInt(feedbackID.substring(2));
                        if (id > maxID) {
                            maxID = id;
                        }
                    } catch (NumberFormatException e) {
                        // Skip if not a valid ID
                    }
                }
            }
        }

        return String.format("FB%03d", maxID + 1);
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(new java.util.Date());
    }

    private String getStarString(int count) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stars.append("★");
        }
        for (int i = count; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
}

