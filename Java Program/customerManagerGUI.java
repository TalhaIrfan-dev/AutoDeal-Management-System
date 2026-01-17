import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.sql.*;

public class customerManagerGUI {

    // Add Customer
    void addCustomer( JFrame parentFrame) {
        JFrame frame = new JFrame("Add New Customer");
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(parentFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        Font titleFont = new Font("Arial", Font.BOLD, 28);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(44, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Add New Customer", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 60, 0));
        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 25));
        formPanel.setBackground(new Color(44, 62, 80));


        JLabel lblName = new JLabel("Name:");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(labelFont);
        JTextField txtName = new JTextField();
        txtName.setFont(fieldFont);

        JLabel lblPhone = new JLabel("Phone Number:");
        lblPhone.setForeground(Color.WHITE);
        lblPhone.setFont(labelFont);
        JTextField txtPhone = new JTextField();
        txtPhone.setFont(fieldFont);

        JLabel lblCnic = new JLabel("CNIC Number:");
        lblCnic.setForeground(Color.WHITE);
        lblCnic.setFont(labelFont);
        JTextField txtCnic = new JTextField();
        txtCnic.setFont(fieldFont);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(labelFont);
        JTextField txtEmail = new JTextField();
        txtEmail.setFont(fieldFont);

        formPanel.add(lblName); formPanel.add(txtName);
        formPanel.add(lblPhone); formPanel.add(txtPhone);
        formPanel.add(lblCnic); formPanel.add(txtCnic);
        formPanel.add(lblEmail); formPanel.add(txtEmail);

        mainPanel.add(formPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JButton btnAdd = new JButton("Add Customer");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdd.setBackground(new Color(39, 174, 96));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancel.setBackground(new Color(192, 57, 43));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        btnPanel.add(btnAdd);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(btnCancel);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnPanel);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Action Listeners
        btnCancel.addActionListener(e -> frame.dispose());

        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String cnic = txtCnic.getText().trim();
            String email = txtEmail.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || cnic.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name, CNIC and Phone fields are required.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Format validation
            if (!phone.matches("^03\\d{2}\\s\\d{7}$")) {
                JOptionPane.showMessageDialog(frame, "Phone number must be in format: 03XX XXXXXXX", "Invalid Phone Format", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!cnic.matches("^\\d{5}-\\d{7}-\\d{1}$")) {
                JOptionPane.showMessageDialog(frame, "CNIC must be in format: XXXXX-XXXXXXX-X", "Invalid CNIC Format", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // DB Connection
                Connection conn = DatabaseConnection.getConnection();

                // Check for duplicates
                String checkSQL = "SELECT * FROM Customers WHERE CustomerCNIC = ? OR Phone_no = ? OR Email = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
                checkStmt.setString(1, cnic);
                checkStmt.setString(2, phone);
                checkStmt.setString(3, email);
                ResultSet rs = checkStmt.executeQuery();

                while (rs.next()) {
                    String existingCnic = rs.getString("CustomerCNIC");
                    String existingPhone = rs.getString("Phone_no");
                    String existingEmail = rs.getString("Email");

                    if (existingCnic != null && existingCnic.equals(cnic)) {
                        JOptionPane.showMessageDialog(frame, "This CNIC is already registered.", "Duplicate CNIC", JOptionPane.ERROR_MESSAGE);
                        conn.close(); return;
                    }
                    if (existingPhone != null && existingPhone.equals(phone)) {
                        JOptionPane.showMessageDialog(frame, "This Phone number is already registered.", "Duplicate Phone", JOptionPane.ERROR_MESSAGE);
                        conn.close(); return;
                    }
                    if (existingEmail != null && !email.isEmpty() && existingEmail.equals(email)) {
                        JOptionPane.showMessageDialog(frame, "This Email is already registered.", "Duplicate Email", JOptionPane.ERROR_MESSAGE);
                        conn.close(); return;
                    }
                }

                // Insert if no duplicates
                String sql = "INSERT INTO Customers (CustomerName, CustomerCNIC, Phone_no, Email) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, cnic);
                pst.setString(3, phone);
                pst.setString(4, email.isEmpty() ? null : email);

                pst.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(frame, "Customer added successfully!");
                frame.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price format!", "Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Display Customers
    void displayCustomers(JFrame parentFrame) {
        JFrame frame = new JFrame("Display Customers");
        frame.setSize(950, 520);
        frame.setLocationRelativeTo(parentFrame);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Display Customers");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Name", "Phone", "CNIC", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 246, 250) : Color.WHITE);
                }
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(52, 73, 94));
        table.setSelectionBackground(new Color(41, 128, 185));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableHeader.setBackground(new Color(41, 128, 185));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 35));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Footer with Close
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(new Color(245, 246, 250));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(new Color(41, 128, 185));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> frame.dispose());

        footerPanel.add(closeButton);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Fetch and populate from DB
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT CustomerID, CustomerName, Phone_no, CustomerCNIC, Email FROM Customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("CustomerID");
                String name = rs.getString("CustomerName");
                String phone = rs.getString("Phone_no");
                String cnic = rs.getString("CustomerCNIC");
                String email = rs.getString("Email");
                model.addRow(new Object[]{id, name, phone, cnic, email});
            }
            conn.close();

            if (!hasData) {
                JOptionPane.showMessageDialog(parentFrame, "No customers to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error fetching customers:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        frame.setVisible(true);
    }

    // Search Customer
    void searchCustomer( JFrame parentFrame) {
        JFrame frame = new JFrame("Search Customer");
        frame.setSize(950, 520);
        frame.setLocationRelativeTo(parentFrame);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Search Customer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel);

        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        searchOptionsPanel.setBackground(new Color(245, 246, 250));

        JLabel lblSearchBy = new JLabel("Search by:");
        lblSearchBy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchOptionsPanel.add(lblSearchBy);

        JRadioButton rbId = new JRadioButton("ID");
        JRadioButton rbCnic = new JRadioButton("CNIC");
        JRadioButton rbName = new JRadioButton("Name");
        ButtonGroup group = new ButtonGroup();
        group.add(rbId);
        group.add(rbCnic);
        group.add(rbName);
        rbId.setSelected(true);

        for (JRadioButton rb : new JRadioButton[]{rbId, rbCnic, rbName}) {
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            rb.setBackground(new Color(245, 246, 250));
            searchOptionsPanel.add(rb);
        }

        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchOptionsPanel.add(txtSearch);

        searchOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(searchOptionsPanel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Phone", "CNIC", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 246, 250) : Color.WHITE);
                }
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(52, 73, 94));
        table.setSelectionBackground(new Color(41, 128, 185));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableHeader.setBackground(new Color(41, 128, 185));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 35));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Footer with buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(new Color(245, 246, 250));

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(new Color(41, 128, 185));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(100, 35));
        footerPanel.add(btnSearch);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(new Color(41, 128, 185));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setPreferredSize(new Dimension(100, 35));
        btnClose.addActionListener(e -> frame.dispose());
        footerPanel.add(btnClose);

        frame.add(footerPanel, BorderLayout.SOUTH);

        // Search button logic
        btnSearch.addActionListener(e -> {
            model.setRowCount(0); // clear table
            String search = txtSearch.getText().trim();
            if (search.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter search value.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (rbCnic.isSelected()) {
                // CNIC pattern: 5 digits - 7 digits - 1 digit (e.g., 12345-1234567-1)
                if (!search.matches("\\d{5}-\\d{7}-\\d")) {
                    JOptionPane.showMessageDialog(frame, "Please enter CNIC in proper format (e.g., 12345-1234567-1).", "Invalid CNIC", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String query = "";
            if (rbId.isSelected()) {
                query = "SELECT * FROM Customers WHERE CustomerID = ?";
            } else if (rbCnic.isSelected()) {
                query = "SELECT * FROM Customers WHERE CustomerCNIC = ?";
            } else if (rbName.isSelected()) {
                query = "SELECT * FROM Customers WHERE CustomerName = ?";
            }

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, search);
                ResultSet rs = pst.executeQuery();

                boolean found = false;
                while (rs.next()) {
                    int id = rs.getInt("CustomerID");
                    String name = rs.getString("CustomerName");
                    String phone = rs.getString("Phone_no");
                    String cnic = rs.getString("CustomerCNIC");
                    String email = rs.getString("Email");

                    model.addRow(new Object[]{id, name, phone, cnic, email});
                    found = true;
                }

                if (!found) {
                    JOptionPane.showMessageDialog(frame, "No customer found for the given criteria.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        frame.setVisible(true);
    }

    public void updateCustomer(JFrame parentFrame) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String search = JOptionPane.showInputDialog(parentFrame, "Enter Customer ID, CNIC or Phone Number to Update:");
            if (search == null || search.trim().isEmpty()) return;

            search = search.trim();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Customers WHERE CustomerID = ? OR CustomerCNIC = ? OR Phone_no = ?");
            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parentFrame, "No customer found with the given details.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int customerId = rs.getInt("CustomerID");
            String currentName = rs.getString("CustomerName");
            String currentPhone = rs.getString("Phone_no");
            String currentCnic = rs.getString("CustomerCNIC");
            String currentEmail = rs.getString("Email");

            String[] options = {"Edit Phone & Email", "Edit Whole Details"};
            int choice = JOptionPane.showOptionDialog(parentFrame,
                    "Choose what you want to edit for Customer ID: " + customerId,
                    "Edit Customer",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == -1) return;

            // Common UI setup
            Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
            Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
            Color bgColor = new Color(236, 240, 241);

            JPanel formPanel = new JPanel(new GridLayout((choice == 0) ? 2 : 4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
            formPanel.setBackground(bgColor);

            JTextField nameField = new JTextField(currentName);
            JTextField phoneField = new JTextField(currentPhone);
            JTextField cnicField = new JTextField(currentCnic);
            JTextField emailField = new JTextField(currentEmail);

            JLabel titleLabel = new JLabel("Update Customer ID: " + customerId, JLabel.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(41, 128, 185));

            if (choice == 0) {
                JLabel phoneLabel = new JLabel("Phone Number (#### #######):");
                JLabel emailLabel = new JLabel("Email:");

                phoneLabel.setFont(labelFont);
                emailLabel.setFont(labelFont);
                phoneField.setFont(fieldFont);
                emailField.setFont(fieldFont);

                formPanel.add(phoneLabel); formPanel.add(phoneField);
                formPanel.add(emailLabel); formPanel.add(emailField);
            } else {
                JLabel[] labels = {
                        new JLabel("Name:"), new JLabel("Phone Number (#### #######):"),
                        new JLabel("CNIC (#####-#######-#):"), new JLabel("Email:")
                };

                JTextField[] fields = {nameField, phoneField, cnicField, emailField};
                for (int i = 0; i < labels.length; i++) {
                    labels[i].setFont(labelFont);
                    fields[i].setFont(fieldFont);
                    formPanel.add(labels[i]);
                    formPanel.add(fields[i]);
                }
            }

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(parentFrame, mainPanel, "Update Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                String newPhone = phoneField.getText().trim();
                String newCnic = cnicField.getText().trim();
                String newEmail = emailField.getText().trim();

                if ((choice == 1) && (newName.isEmpty() || newPhone.isEmpty() || newCnic.isEmpty() || newEmail.isEmpty())) {
                    JOptionPane.showMessageDialog(parentFrame, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newPhone.matches("^[0-9]{4} [0-9]{7}$")) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid phone number format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (choice == 1 && !newCnic.matches("^[0-9]{5}-[0-9]{7}-[0-9]$")) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid CNIC format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newEmail.matches("^(.+)@(.+)$")) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement check;
                if (choice == 0) {
                    check = conn.prepareStatement("SELECT * FROM Customers WHERE (Phone_no = ? OR Email = ?) AND CustomerID != ?");
                    check.setString(1, newPhone);
                    check.setString(2, newEmail);
                    check.setInt(3, customerId);
                } else {
                    check = conn.prepareStatement("SELECT * FROM Customers WHERE (Phone_no = ? OR CustomerCNIC = ? OR Email = ?) AND CustomerID != ?");
                    check.setString(1, newPhone);
                    check.setString(2, newCnic);
                    check.setString(3, newEmail);
                    check.setInt(4, customerId);
                }

                ResultSet checkRs = check.executeQuery();
                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(parentFrame, "Phone, CNIC or Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement update;
                if (choice == 0) {
                    update = conn.prepareStatement("UPDATE Customers SET Phone_no = ?, Email = ? WHERE CustomerID = ?");
                    update.setString(1, newPhone);
                    update.setString(2, newEmail);
                    update.setInt(3, customerId);
                } else {
                    update = conn.prepareStatement("UPDATE Customers SET CustomerName = ?, Phone_no = ?, CustomerCNIC = ?, Email = ? WHERE CustomerID = ?");
                    update.setString(1, newName);
                    update.setString(2, newPhone);
                    update.setString(3, newCnic);
                    update.setString(4, newEmail);
                    update.setInt(5, customerId);
                }
                update.executeUpdate();
                JOptionPane.showMessageDialog(parentFrame, "Customer details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete Customer
    void deleteCustomer(JFrame parentFrame) {
        String input = JOptionPane.showInputDialog(parentFrame, "Enter Customer ID / CNIC (#####-#######-#)/ Phone_no(#### #######) to delete:");
        if (input == null || input.trim().isEmpty()) return; // Cancel or empty input

        input = input.trim();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Search for customer by ID, CNIC, or Name
            String query = "SELECT * FROM Customers WHERE CustomerID = ? OR CustomerCNIC = ? OR Phone_no = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, input);
            ps.setString(2, input);
            ps.setString(3, input);
            rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int customerId = rs.getInt("CustomerID");
            String name = rs.getString("CustomerName");
            String phone = rs.getString("Phone_no");
            String cnic = rs.getString("CustomerCNIC");
            String email = rs.getString("Email");

            // Check if customer is in Rentals table
            String rentalCheck = "SELECT * FROM Rentals WHERE CustomerID = ?";
            PreparedStatement rentalPs = conn.prepareStatement(rentalCheck);
            rentalPs.setInt(1, customerId);
            ResultSet rentalRs = rentalPs.executeQuery();

            if (rentalRs.next()) {
                JOptionPane.showMessageDialog(parentFrame,
                        "This customer currently has active rentals and cannot be deleted.",
                        "Blocked",
                        JOptionPane.WARNING_MESSAGE);
                rentalRs.close();
                rentalPs.close();
                return;
            }
            rentalRs.close();
            rentalPs.close();

            // Confirm delete
            StringBuilder customerDetails = new StringBuilder();
            customerDetails.append("Customer Details:\n")
                    .append("ID: ").append(customerId).append("\n")
                    .append("Name: ").append(name).append("\n")
                    .append("Phone: ").append(phone).append("\n")
                    .append("CNIC: ").append(cnic).append("\n")
                    .append("Email: ").append(email).append("\n\n")
                    .append("Do you really want to delete this customer?");

            int confirm = JOptionPane.showConfirmDialog(parentFrame, customerDetails.toString(), "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(parentFrame, "Deletion canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Delete from database
            String deleteQuery = "DELETE FROM Customers WHERE CustomerID = ?";
            PreparedStatement deletePs = conn.prepareStatement(deleteQuery);
            deletePs.setInt(1, customerId);
            int rowsAffected = deletePs.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(parentFrame, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Customer deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            deletePs.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
