import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class LibraryManagementSystem extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, authorField, isbnField, publisherField, quantityField, locationField;
    private BookDAO bookDAO;
    
    public LibraryManagementSystem() {
        bookDAO = new BookDAO();
        initializeUI();
        loadBooks();
    }
    
    private void initializeUI() {
        setTitle("图书管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("书名:"));
        titleField = new JTextField();
        inputPanel.add(titleField);
        
        inputPanel.add(new JLabel("作者:"));
        authorField = new JTextField();
        inputPanel.add(authorField);
        
        inputPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        inputPanel.add(isbnField);
        
        inputPanel.add(new JLabel("出版社:"));
        publisherField = new JTextField();
        inputPanel.add(publisherField);
        
        inputPanel.add(new JLabel("数量:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);
        
        inputPanel.add(new JLabel("位置:"));
        locationField = new JTextField();
        inputPanel.add(locationField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton updateButton = new JButton("更新");
        JButton deleteButton = new JButton("删除");
        JButton clearButton = new JButton("清空");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        inputPanel.add(buttonPanel);
        
        // Create table
        String[] columnNames = {"ID", "书名", "作者", "ISBN", "出版社", "数量", "位置"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        
        // Add components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add action listeners
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        clearButton.addActionListener(e -> clearFields());
        
        // Add table selection listener
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    titleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    authorField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    isbnField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    publisherField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    quantityField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    locationField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                }
            }
        });
        
        add(mainPanel);
    }
    
    private void loadBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            tableModel.setRowCount(0);
            for (Book book : books) {
                Object[] row = {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getPublisher(),
                    book.getQuantity(),
                    book.getLocation()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载图书失败: " + e.getMessage());
        }
    }
    
    private void addBook() {
        try {
            Book book = new Book(
                0,
                titleField.getText(),
                authorField.getText(),
                isbnField.getText(),
                publisherField.getText(),
                Integer.parseInt(quantityField.getText()),
                locationField.getText()
            );
            
            bookDAO.addBook(book);
            loadBooks();
            clearFields();
            JOptionPane.showMessageDialog(this, "图书添加成功！");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "添加图书失败: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数量！");
        }
    }
    
    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要更新的图书！");
            return;
        }
        
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Book book = new Book(
                id,
                titleField.getText(),
                authorField.getText(),
                isbnField.getText(),
                publisherField.getText(),
                Integer.parseInt(quantityField.getText()),
                locationField.getText()
            );
            
            bookDAO.updateBook(book);
            loadBooks();
            clearFields();
            JOptionPane.showMessageDialog(this, "图书更新成功！");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "更新图书失败: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数量！");
        }
    }
    
    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的图书！");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这本图书吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                bookDAO.deleteBook(id);
                loadBooks();
                clearFields();
                JOptionPane.showMessageDialog(this, "图书删除成功！");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除图书失败: " + e.getMessage());
            }
        }
    }
    
    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        publisherField.setText("");
        quantityField.setText("");
        locationField.setText("");
        bookTable.clearSelection();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryManagementSystem().setVisible(true);
        });
    }
} 