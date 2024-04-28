package com.bwdesigngroup.ignition.project_scan.designer.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfirmationDialog extends JDialog {
    private boolean confirmed = false;

    public ConfirmationDialog(Frame owner) {
        super(owner, true);
        initComponents();
    }

    private void initComponents() {
        setSize(325, 125);
		setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
		setTitle("Update Project");

        // Icon
        ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.questionIcon");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(iconLabel, BorderLayout.WEST);

        // Message
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>A new version of the project is available. <br>Would you like to update?</div></html>");
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        messageLabel.setOpaque(false);
        add(messageLabel, BorderLayout.CENTER);

        // Buttons
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                setVisible(false);
            }
        });

        JButton ignoreButton = new JButton("Ignore");
        ignoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                setVisible(false);
            }
        });

        updateButton.setFocusPainted(false);
        ignoreButton.setFocusPainted(false);

        // Create a color #14B0ED for the update button, with white text color
        updateButton.setBackground(new Color(20, 176, 237));
        updateButton.setForeground(Color.WHITE);

        // Create a color #F9FBFC for the ignore button, with #6F757B text color
        ignoreButton.setBackground(new Color(249, 251, 252));
        ignoreButton.setForeground(new Color(111, 117, 123));

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(ignoreButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add some space between buttons
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}