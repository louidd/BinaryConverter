package Obj;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final String DATABASE_FOLDER = "database";
    private static final String FILE_NAME = "history.txt";
    private static final Path FILE_PATH = Paths.get(DATABASE_FOLDER, FILE_NAME);

    static {
        // Ensure the database folder exists
       File folder = new File(DATABASE_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void saveHistory(BaseConverter.Result result) {
        String entry = result.binary + "|" + result.decimal + "|" + result.hexadecimal + "|" + result.octal;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH.toFile(), true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isDuplicateEntry(BaseConverter.Result result) {
        String entry = result.binary + "|" + result.decimal + "|" + result.hexadecimal + "|" + result.octal;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(entry)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void addShowHistoryButton(JPanel panel) {
        JButton showHistoryButton = new JButton("Show History");
        showHistoryButton.setBounds(410, 450, 150, 40);
        showHistoryButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        showHistoryButton.setBackground(new Color(50, 54, 53));
        showHistoryButton.setForeground(Color.WHITE);
        panel.add(showHistoryButton);

        showHistoryButton.addActionListener(e -> {
            List<String[]> data = readHistory();
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No history found!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JFrame historyFrame = new JFrame("Conversion History");
            historyFrame.setSize(600, 700);
            historyFrame.setLocationRelativeTo(null);
            historyFrame.setResizable(false);

            String[] columnNames = {"Binary", "Decimal", "Hexadecimal", "Octal"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (String[] row : data) {
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setBackground(null);
            JScrollPane scrollPane = new JScrollPane(table);
            historyFrame.add(scrollPane);

            historyFrame.setVisible(true);
        });

    }

    private static List<String[]> readHistory() {
        List<String[]> data = new ArrayList<>();
        File file = FILE_PATH.toFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line.split("\\|"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}