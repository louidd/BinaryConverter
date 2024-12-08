package Obj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConverterGUI {
    public void start() {
        // Main frame setup
        JFrame frame = new JFrame("Number Base Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Gradient background panel
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(58, 123, 213), getWidth(), getHeight(), new Color(0, 210, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        panel.setBounds(0, 0, 600, 700);
        frame.add(panel);

        // Title label
        JLabel titleLabel = new JLabel("Base Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 20, 600, 40);
        panel.add(titleLabel);

        // Input base label and combo box
        JLabel baseLabel = new JLabel("Input Base:");
        baseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        baseLabel.setForeground(Color.WHITE);
        baseLabel.setBounds(50, 100, 120, 30);
        panel.add(baseLabel);

        String[] bases = {"Binary", "Decimal", "Hexadecimal", "Octal"};
        JComboBox<String> baseSelector = new JComboBox<>(bases);
        baseSelector.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        baseSelector.setBounds(200, 100, 300, 30);
        panel.add(baseSelector);

        // Input value label and text field
        JLabel inputLabel = new JLabel("Input Value:");
        inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setBounds(50, 150, 120, 30);
        panel.add(inputLabel);

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputField.setBounds(200, 150, 300, 30);
        panel.add(inputField);

        // Output labels and text fields
        String[] outputLabels = {"Binary", "Decimal", "Hexadecimal", "Octal"};
        JTextField[] outputFields = new JTextField[outputLabels.length];
        int yPosition = 200;

        for (int i = 0; i < outputLabels.length; i++) {
            JLabel label = new JLabel(outputLabels[i] + ":");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            label.setForeground(Color.WHITE);
            label.setBounds(50, yPosition, 120, 30);
            panel.add(label);

            JTextField textField = new JTextField();
            textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            textField.setEditable(false);
            textField.setBounds(200, yPosition, 300, 30);
            panel.add(textField);

            outputFields[i] = textField;
            yPosition += 50;
        }

        // Buttons
        JButton convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        convertButton.setBackground(new Color(0, 153, 76));
        convertButton.setForeground(Color.WHITE);
        convertButton.setBounds(20, 450, 150, 40);
        panel.add(convertButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetButton.setBackground(new Color(204, 0, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBounds(218, 450, 150, 40);
        panel.add(resetButton);

        // Show history button
        HistoryManager.addShowHistoryButton(panel);

        // Action Listeners
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String base = (String) baseSelector.getSelectedItem();
                    String input = inputField.getText().trim();

                    if (input.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a value to convert!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Perform conversion
                    BaseConverter.Result result = BaseConverter.convert(input, base);

                    // Display results
                    outputFields[0].setText(result.binary);
                    outputFields[1].setText(result.decimal);
                    outputFields[2].setText(result.hexadecimal);
                    outputFields[3].setText(result.octal);

                    // Save to history if not a duplicate
                    if (!HistoryManager.isDuplicateEntry(result)) {
                        HistoryManager.saveHistory(result);
                    } else {
                        JOptionPane.showMessageDialog(frame, "This conversion is already in history.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Conversion Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please check your value and base.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        resetButton.addActionListener(e -> {
            inputField.setText("");
            for (JTextField field : outputFields) {
                field.setText("");
            }
            baseSelector.setSelectedIndex(0);
        });

        // Display the frame
        frame.setVisible(true);
    }
}
