package com.coreyd97.BurpExtenderUtilities.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.awt.datatransfer.DataFlavor;
import java.util.Vector;

public class ListConfigPanel extends JPanel {

  public ListConfigPanel() {
    // Set the layout for the panel
    setLayout(new BorderLayout());

    // Initialize the table model and set it to the table
    String[] columnNames = {""};
    tableModel = new DefaultTableModel(columnNames, 0);
    table      = new JTable(tableModel);

    // Add the UI components by calling separated functions
    add(createButtonGroup(), BorderLayout.WEST); // Button group on the west
    add(new JScrollPane(table), BorderLayout.CENTER); // Table in the center
    add(createInputAndAddButtonPanel(), BorderLayout.SOUTH); // Input and add button at the bottom
  }

  public Vector<Vector> getTableData(){
    return tableModel.getDataVector();
  }

  private static final Dimension BTN_DIM = new Dimension(100, 30);

  private final DefaultTableModel tableModel;
  private final JTable            table;
  private       JTextField        inputField;

  // Function to create the button group on the left side
  private JPanel createButtonGroup() {
    JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5)); // 5 buttons, 5px gap

    // Initialize and set buttons with BTN_DIM, each with its own ActionListener
    JButton pasteButton       = createButton("Paste", new PasteButtonListener());
    JButton loadButton        = createButton("Load ...", new LoadButtonListener());
    JButton removeButton      = createButton("Remove", new RemoveButtonListener());
    JButton clearButton       = createButton("Clear", new ClearButtonListener());
    JButton deduplicateButton = createButton("Deduplicate", new DeduplicateButtonListener());

    // Add buttons to the panel
    buttonPanel.add(pasteButton);
    buttonPanel.add(loadButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(clearButton);
    buttonPanel.add(deduplicateButton);

    return buttonPanel;
  }

  // Function to create the input field and add button at the bottom
  private JPanel createInputAndAddButtonPanel() {
    JPanel inputPanel = new JPanel(new BorderLayout());
    inputField = new JTextField("Enter a new item");
    JButton addButton = createButton("Add", new AddButtonListener());

    inputPanel.add(addButton, BorderLayout.WEST);
    inputPanel.add(inputField, BorderLayout.CENTER);

    return inputPanel;
  }

  // Method to create a JButton with the standard size and attach ActionListener
  private static JButton createButton(String text, ActionListener listener) {
    JButton button = new JButton(text);
    button.setPreferredSize(BTN_DIM);
    button.addActionListener(listener);
    return button;
  }

  private class AddButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String newItem = inputField.getText();
      if (!newItem.isEmpty()) {
        tableModel.addRow(new Object[]{newItem}); // Add to table model
        inputField.setText(""); // Clear the text field after adding
      }
    }
  }

  private class PasteButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        String clipboardContents = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        String[] lines = clipboardContents.split("\\r?\\n");

        for (String line : lines) {
          tableModel.addRow(new Object[]{line});
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error pasting from clipboard: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private class LoadButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser = new JFileChooser();
      int returnValue = fileChooser.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
          String line;
          while ((line = reader.readLine()) != null) {
            tableModel.addRow(new Object[]{line}); // Add to table model
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }

  private class RemoveButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      int selectedRow = table.getSelectedRow();
      if (selectedRow != -1) {
        tableModel.removeRow(selectedRow); // Remove from the table model
      }
    }
  }

  private class ClearButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      tableModel.setRowCount(0); // Clear the table model
    }
  }

  private class DeduplicateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      int selectedRow = table.getSelectedRow();
      if (selectedRow != -1) { // Check if a row is selected
        // Get the value of the selected row
        String valueToDuplicate = (String) tableModel.getValueAt(selectedRow, 0);
        tableModel.addRow(new Object[]{valueToDuplicate}); // Duplicate the selected row in the table model
      }
    }
  }

}
