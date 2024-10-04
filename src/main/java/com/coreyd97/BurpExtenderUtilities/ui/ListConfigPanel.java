package com.coreyd97.BurpExtenderUtilities.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class ListConfigPanel extends JPanel {

  public ListConfigPanel() {
    // Set the layout for the panel
    setLayout(new BorderLayout());

    // Add the UI components by calling separated functions
    add(createButtonGroup(), BorderLayout.WEST); // Button group on the west
    add(createTablePanel(), BorderLayout.CENTER); // Table in the center
    add(createInputAndAddButtonPanel(), BorderLayout.SOUTH); // Input and add button at the bottom
  }

  // Function to create the button group on the left side
  private JPanel createButtonGroup() {
    JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5)); // 5 buttons, 5px gap

    // Initialize and set buttons with BTN_DIM
    pasteButton       = createButton("Paste");
    loadButton        = createButton("Load ...");
    removeButton      = createButton("Remove");
    clearButton       = createButton("Clear");
    deduplicateButton = createButton("Deduplicate");

    // Add buttons to the panel
    buttonPanel.add(pasteButton);
    buttonPanel.add(loadButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(clearButton);
    buttonPanel.add(deduplicateButton);

    return buttonPanel;
  }

  // Function to create the table panel in the center
  private JScrollPane createTablePanel() {
    String[] columnNames = {""};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    table = new JTable(tableModel);
    return new JScrollPane(table);
  }

  // Function to create the input field and add button at the bottom
  private JPanel createInputAndAddButtonPanel() {
    JPanel inputPanel = new JPanel(new BorderLayout());
    inputField = new JTextField("Enter a new item");
    addButton  = createButton("Add");

    inputPanel.add(addButton, BorderLayout.WEST);
    inputPanel.add(inputField, BorderLayout.CENTER);

    // Add action listener to "Add" button to add items to the table
    addButton.addActionListener(e -> addItem());

    return inputPanel;
  }

  // Method to create a JButton with the standard size
  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setPreferredSize(BTN_DIM);
    return button;
  }

  // Method to add items from the text field to the table
  private void addItem() {
    String newItem = inputField.getText();
    if (!newItem.isEmpty()) {
      DefaultTableModel model = (DefaultTableModel) table.getModel();
      model.addRow(new Object[]{newItem});
      inputField.setText(""); // Clear the text field after adding
    }
  }

  private static final Dimension BTN_DIM = new Dimension(100, 30);

  private JTable     table;
  private JTextField inputField;
  private JButton    pasteButton, loadButton, removeButton, clearButton, deduplicateButton, addButton;
}
