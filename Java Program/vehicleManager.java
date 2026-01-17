import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.sql.*;

public class vehicleManager           // vehicle manager
{

    void addVehicle() {
        JFrame frame = new JFrame("Add New Vehicle");
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        Font titleFont = new Font("Arial", Font.BOLD, 28);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(44, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Add New Vehicle", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 60, 0));

        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 25));
        formPanel.setBackground(new Color(44, 62, 80));


        JLabel lblModel = new JLabel("Name:");
        lblModel.setForeground(Color.WHITE);
        lblModel.setFont(labelFont);
        JTextField txtModel = new JTextField();
        txtModel.setFont(fieldFont);


        JLabel lblReg = new JLabel("Registration Number:");
        lblReg.setForeground(Color.WHITE);
        lblReg.setFont(labelFont);
        JTextField txtReg = new JTextField();
        txtReg.setFont(fieldFont);


        JLabel lblType = new JLabel("Vehicle Type:");
        lblType.setForeground(Color.WHITE);
        lblType.setFont(labelFont);

        // String[] vehicleTypes = {"Car", "Bike", "Van"};
        JComboBox<String> comboType = new JComboBox<>();
        comboType.setFont(fieldFont);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT Vehicle_type FROM VehicleType";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboType.addItem(rs.getString("Vehicle_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton btnAddType = new JButton("Add New Type");
        btnAddType.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAddType.setBackground(Color.LIGHT_GRAY);

        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.setBackground(new Color(44, 62, 80));
        typePanel.add(comboType, BorderLayout.CENTER);
        typePanel.add(btnAddType, BorderLayout.EAST);


        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setFont(labelFont);
        JTextField txtPrice = new JTextField();
        txtPrice.setFont(fieldFont);

        JLabel lblAvailable = new JLabel("Available:");
        lblAvailable.setForeground(Color.WHITE);
        lblAvailable.setFont(labelFont);

        JRadioButton yesBtn = new JRadioButton("Yes");
        JRadioButton noBtn = new JRadioButton("No");

        yesBtn.setFont(fieldFont);
        noBtn.setFont(fieldFont);
        yesBtn.setForeground(Color.WHITE);
        noBtn.setForeground(Color.WHITE);
        yesBtn.setBackground(new Color(44, 62, 80));
        noBtn.setBackground(new Color(44, 62, 80));

        ButtonGroup availabilityGroup = new ButtonGroup();
        availabilityGroup.add(yesBtn);
        availabilityGroup.add(noBtn);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        radioPanel.setBackground(new Color(44, 62, 80));
        radioPanel.add(yesBtn);
        radioPanel.add(noBtn);

        formPanel.add(lblModel);            formPanel.add(txtModel);
        formPanel.add(lblReg);              formPanel.add(txtReg);
        formPanel.add(lblType);             formPanel.add(typePanel);
        formPanel.add(lblPrice);            formPanel.add(txtPrice);
        formPanel.add(lblAvailable);        formPanel.add(radioPanel);
        mainPanel.add(formPanel);

        // Submit Button Panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 18));
        btnPanel.add(btnSubmit);

        mainPanel.add(btnPanel);

        frame.add(mainPanel);
        frame.setVisible(true);


        // Add new type dialog
        btnAddType.addActionListener(e -> {
            JTextField typeField = new JTextField();
            JTextField rentField = new JTextField();

            Object[] message = {
                    "Enter new vehicle type:", typeField,
                    "Enter average rental price:", rentField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Vehicle Type", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String newType = typeField.getText().trim();
                String rentText = rentField.getText().trim();

                if (newType.isEmpty() || rentText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both vehicle type and rental price.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    double avgRent = Double.parseDouble(rentText);

                    try (Connection conn = DatabaseConnection.getConnection()) {
                        // Check if type already exists
                        PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM VehicleType WHERE Vehicle_type = ?");
                        checkStmt.setString(1, newType);
                        ResultSet rs = checkStmt.executeQuery();
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(frame, "Vehicle type already exists.", "Duplicate Type", JOptionPane.WARNING_MESSAGE);
                        } else {
                            // Insert new type
                            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO VehicleType (Vehicle_type, Average_rent) VALUES (?, ?)");
                            insertStmt.setString(1, newType);
                            insertStmt.setDouble(2, avgRent);
                            int inserted = insertStmt.executeUpdate();
                            if (inserted > 0) {
                                comboType.addItem(newType);
                                comboType.setSelectedItem(newType);
                                JOptionPane.showMessageDialog(frame, "New vehicle type added successfully!");
                            }
                        }
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for rental price.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error while adding new vehicle type.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action listener
        btnSubmit.addActionListener(e -> {
            try {
                String model = txtModel.getText().trim();
                String regNum = txtReg.getText().trim();
                String type = comboType.getSelectedItem().toString().trim();
                String priceText = txtPrice.getText().trim();
                String status = yesBtn.isSelected() ? "available" : "not_available";

                Connection conn = DatabaseConnection.getConnection();

                if (model.isEmpty() || regNum.isEmpty() || priceText.isEmpty() || (!yesBtn.isSelected() && !noBtn.isSelected())) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!regNum.matches("[A-Z]{1,3}-\\d{1,4}")) {
                    JOptionPane.showMessageDialog(frame, "Registration number must be in format ABC-1234.", "Invalid Format", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double price = Double.parseDouble(priceText);

                // Check uniqueness of registration number
                PreparedStatement checkReg = conn.prepareStatement("SELECT * FROM Vehicles WHERE Registration_no = ?");
                checkReg.setString(1, regNum);
                ResultSet regResult = checkReg.executeQuery();
                if (regResult.next()) {
                    JOptionPane.showMessageDialog(frame, "Registration number already exists in database.", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int typeId = -1;
                String typeQuery = "SELECT TypeId FROM VehicleType WHERE Vehicle_type = ?";
                PreparedStatement ps1 = conn.prepareStatement(typeQuery);
                ps1.setString(1, type);
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    typeId = rs.getInt("TypeId");
                } else {
                    PreparedStatement insertType = conn.prepareStatement("INSERT INTO VehicleType (Vehicle_type) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                    insertType.setString(1, type);
                    insertType.executeUpdate();
                    ResultSet genKeys = insertType.getGeneratedKeys();
                    if (genKeys.next()) {
                        typeId = genKeys.getInt(1);
                    }
                }

                // Insert into Vehicles
                String insertQuery = "INSERT INTO Vehicles (Vehicle_name, Registration_no, TypeId, VehicleStatus, Price) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(insertQuery);
                ps.setString(1, model);
                ps.setString(2, regNum);
                ps.setInt(3, typeId);
                ps.setString(4, status);
                ps.setDouble(5, price);


                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Vehicle added to database successfully!");
                    frame.dispose();
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid price!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to add vehicle. Please check input or DB connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }


    void searchVehicle() {

        JFrame frame = new JFrame("Search Vehicle");
        frame.setSize(950, 520);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // Top panel with vertical BoxLayout for label + search options
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title label on top
        JLabel titleLabel = new JLabel("Search Vehicle");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel);

        // Panel for radio buttons and inputs
        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        searchOptionsPanel.setBackground(new Color(245, 246, 250));

        JLabel lblSearchBy = new JLabel("Search by:");
        lblSearchBy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchOptionsPanel.add(lblSearchBy);

        // Radio buttons for search mode
        JRadioButton rbIdOrReg = new JRadioButton("ID or Registration No.");
        JRadioButton rbType = new JRadioButton("Vehicle Type");
        ButtonGroup group = new ButtonGroup();
        group.add(rbIdOrReg);
        group.add(rbType);
        rbIdOrReg.setSelected(true);

        rbIdOrReg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbType.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        rbIdOrReg.setBackground(new Color(245, 246, 250));
        rbType.setBackground(new Color(245, 246, 250));

        searchOptionsPanel.add(rbIdOrReg);
        searchOptionsPanel.add(rbType);

        // Text field for ID/Reg search
        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchOptionsPanel.add(txtSearch);

        // Instead of static array, fetch types from DB for combo box
        JComboBox<String> comboType = new JComboBox<>();
        comboType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboType.setVisible(false);
        searchOptionsPanel.add(comboType);

        // Load vehicle types from database into combo box
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Vehicle_type FROM VehicleType");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboType.addItem(rs.getString("Vehicle_type"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load vehicle types.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add option to add new vehicle type in combo box popup
        comboType.setEditable(true);


        searchOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(searchOptionsPanel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Name", "Registration Number", "Type", "Available", "Price", "Rental Price"};
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

        // Footer panel with Search and Close buttons
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

        // Radio buttons toggle inputs
        rbIdOrReg.addActionListener(e -> {
            txtSearch.setVisible(true);
            comboType.setVisible(false);
        });

        rbType.addActionListener(e -> {
            txtSearch.setVisible(false);
            comboType.setVisible(true);
        });

        // Search button action
        btnSearch.addActionListener(e -> {
            model.setRowCount(0); // clear table

            boolean found = false;

            try (Connection conn = DatabaseConnection.getConnection()) {
                if (rbIdOrReg.isSelected()) {
                    String search = txtSearch.getText().trim();
                    if (search.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter ID or Registration Number to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String query = "SELECT v.VehicleID, v.Vehicle_name, v.Registration_no, vt.Vehicle_type, v.VehicleStatus , v.Price, vt.Average_rent " +
                            "FROM Vehicles v JOIN VehicleType vt ON v.TypeId = vt.TypeId " +
                            "WHERE v.VehicleID = ? OR v.Registration_no = ?";
                    try (PreparedStatement pst = conn.prepareStatement(query)) {
                        // Try parsing search as int for ID, else set to -1 to avoid error
                        int idSearch = -1;
                        try {
                            idSearch = Integer.parseInt(search);
                        } catch (NumberFormatException ignored) {
                        }

                        pst.setInt(1, idSearch);
                        pst.setString(2, search);
                        ResultSet rs = pst.executeQuery();

                        while (rs.next()) {
                            found = true;
                            model.addRow(new Object[]{
                                    rs.getInt("VehicleID"),
                                    rs.getString("Vehicle_name"),
                                    rs.getString("Registration_no"),
                                    rs.getString("Vehicle_type"),
                                    rs.getString("VehicleStatus").equalsIgnoreCase("available") ? "Yes" : "No",
                                    rs.getDouble("Price"),
                                    rs.getDouble("Average_rent")
                            });
                        }
                    }

                } else if (rbType.isSelected()) {
                    String selectedType = comboType.getSelectedItem().toString().trim();
                    if (selectedType.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please select or enter a vehicle type.", "Input Required", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String query = "SELECT v.VehicleID, v.Vehicle_name, v.Registration_no, vt.Vehicle_type, v.VehicleStatus, v.Price , vt.Average_rent " +
                            "FROM Vehicles v JOIN VehicleType vt ON v.TypeId = vt.TypeId " +
                            "WHERE vt.Vehicle_type = ?";
                    try (PreparedStatement pst = conn.prepareStatement(query)) {
                        pst.setString(1, selectedType);
                        ResultSet rs = pst.executeQuery();

                        while (rs.next()) {
                            found = true;
                            model.addRow(new Object[]{
                                    rs.getInt("VehicleID"),
                                    rs.getString("Vehicle_name"),
                                    rs.getString("Registration_no"),
                                    rs.getString("Vehicle_type"),
                                    rs.getString("VehicleStatus").equalsIgnoreCase("available") ? "Yes" : "No",
                                    rs.getDouble("Price"),
                                    rs.getDouble("Average_rent")
                            });
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to execute search query.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame, "No vehicles found for the given criteria.", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setVisible(true);
    }


    void displayVehicle() {

        JFrame frame = new JFrame("Display Vehicles");
        frame.setSize(950, 520);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));

        // Top panel for label and filter combo box
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title label on top
        JLabel titleLabel = new JLabel("Display Vehicles");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel);

        // Filter combo box panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        filterPanel.setBackground(new Color(245, 246, 250));

        filterPanel.add(new JLabel("Filter:"));
        String[] options = {"Available Vehicles", "Not Available Vehicles", "All Vehicles"};
        JComboBox<String> filterCombo = new JComboBox<>(options);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        filterCombo.setPreferredSize(new Dimension(220, 35));
        filterPanel.add(filterCombo);

        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(filterPanel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Name", "Registration Number", "Type", "Available", "Price", "Rental Price"};
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

        // Footer with Close button
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

        Runnable refreshTable = () -> {
            model.setRowCount(0);

            String filter = filterCombo.getSelectedItem().toString();

            String query = "SELECT v.VehicleID, v.Vehicle_name, v.Registration_no, vt.Vehicle_type, v.VehicleStatus, v.Price, vt.Average_rent\n" +
                    "FROM Vehicles v\n" +
                    "JOIN VehicleType vt ON v.TypeId = vt.TypeId";

            // Add WHERE clause for filter
            if (filter.equals("Available Vehicles")) {
                query += " WHERE v.VehicleStatus like 'available'";
            } else if (filter.equals("Not Available Vehicles")) {
                query += " WHERE v.VehicleStatus != 'available'";
            }

            query += " Order By v.VehicleID";

            try (Connection conn = DatabaseConnection.getConnection(); // Implement this method to return your DB connection
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                boolean hasData = false;

                while (rs.next()) {
                    hasData = true;
                    int id = rs.getInt("VehicleID");
                    String modelName = rs.getString("Vehicle_name");
                    String regNum = rs.getString("Registration_no");
                    String typeName = rs.getString("Vehicle_type");
                    String status = rs.getString("VehicleStatus");
                    double sellPrice = rs.getDouble("Price");
                    double rentalPrice = rs.getDouble("Average_rent");


                    model.addRow(new Object[]{
                            id, modelName, regNum, typeName, status, sellPrice, rentalPrice
                    });
                }

                if (!hasData) {
                    JOptionPane.showMessageDialog(frame, "No vehicles available for the selected filter.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error loading data from database:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };

        filterCombo.addActionListener(e -> refreshTable.run());

        refreshTable.run(); // initial load
        frame.setVisible(true);

    }


    void deleteVehicle(JFrame parentFrame) {
        String input = JOptionPane.showInputDialog(parentFrame, "Enter the Vehicle ID or Registration No to delete:");
        if (input == null || input.trim().isEmpty()) return;

        Connection conn = null;
        PreparedStatement checkVehicleStmt = null;
        PreparedStatement checkRentalStmt = null;
        PreparedStatement deleteStmt = null;
        ResultSet rsVehicle = null;
        ResultSet rsRental = null;

        int vehicleId = -1;
        String regNo = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Determine whether input is numeric (ID) or string (Registration No)
            boolean isNumeric = input.matches("\\d+");

            // Fetch vehicle by either ID or Registration No
            String checkVehicleQuery = isNumeric
                    ? "SELECT * FROM Vehicles WHERE VehicleID = ?"
                    : "SELECT * FROM Vehicles WHERE Registration_no = ?";
            checkVehicleStmt = conn.prepareStatement(checkVehicleQuery);
            if (isNumeric) {
                checkVehicleStmt.setInt(1, Integer.parseInt(input.trim()));
            } else {
                checkVehicleStmt.setString(1, input.trim());
            }

            rsVehicle = checkVehicleStmt.executeQuery();

            if (!rsVehicle.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Vehicle not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            // Extract actual VehicleID and Registration_no for further use
            vehicleId = rsVehicle.getInt("VehicleID");
            regNo = rsVehicle.getString("Registration_no");

            // Check if vehicle is rented
            String checkRentalQuery = "SELECT * FROM Rentals WHERE VehicleID = ?";
            checkRentalStmt = conn.prepareStatement(checkRentalQuery);
            checkRentalStmt.setInt(1, vehicleId);
            rsRental = checkRentalStmt.executeQuery();

            if (rsRental.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Cannot delete vehicle. It is currently rented.", "Restricted", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Do you really want to delete vehicle: " + regNo + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String deleteQuery = "DELETE FROM Vehicles WHERE VehicleID = ?";
                deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, vehicleId);
                int affectedRows = deleteStmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Vehicle deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to delete vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Deletion canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parentFrame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rsVehicle != null) rsVehicle.close();
            } catch (SQLException ignored) {
            }
            try {
                if (rsRental != null) rsRental.close();
            } catch (SQLException ignored) {
            }
            try {
                if (checkVehicleStmt != null) checkVehicleStmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (checkRentalStmt != null) checkRentalStmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (deleteStmt != null) deleteStmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {
            }
        }
    }


    public void updateVehicle(JFrame parentFrame) {
        String input = JOptionPane.showInputDialog(parentFrame, "Enter Vehicle ID or Registration Number to Update:");
        if (input == null || input.trim().isEmpty()) return;

        input = input.trim();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String fetchQuery = "SELECT V.*, VT.Vehicle_type, VT.Average_rent, VT.TypeId FROM Vehicles V JOIN VehicleType VT ON V.TypeId = VT.TypeId WHERE V.VehicleID = ? OR V.Registration_no = ?";
            PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery);
            try {
                fetchStmt.setInt(1, Integer.parseInt(input));  // Try to treat as ID
            } catch (NumberFormatException e) {
                fetchStmt.setInt(1, -1);  // Invalid ID, force mismatch
            }
            fetchStmt.setString(2, input);
            ResultSet rs = fetchStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Vehicle not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int vehicleId = rs.getInt("VehicleID");
            String currentModel = rs.getString("Vehicle_name");
            String currentReg = rs.getString("Registration_no");
            String currentType = rs.getString("Vehicle_type");
            int currentTypeId = rs.getInt("TypeId");
            double currentAverageRent = rs.getDouble("Average_rent");
            String currentStatus = rs.getString("VehicleStatus");
            double currentPrice = rs.getDouble("Price");

            String[] choices = {"Update Availability", "Update Vehicle Type Rent", "Update Full Details"};
            int actionChoice = JOptionPane.showOptionDialog(parentFrame, "What would you like to update?",
                    "Update Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

            if (actionChoice == -1) return;

            // 1. Update availability only
            if (actionChoice == 0) {
                JComboBox<String> statusBox = new JComboBox<>(new String[]{"available", "rented", "sold", "not_available"});
                statusBox.setSelectedItem(currentStatus);
                int result = JOptionPane.showConfirmDialog(parentFrame, statusBox, "Update Vehicle Availability", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String newStatus = (String) statusBox.getSelectedItem();
                    PreparedStatement updateStatus = conn.prepareStatement("UPDATE Vehicles SET VehicleStatus = ? WHERE VehicleID = ?");
                    updateStatus.setString(1, newStatus);
                    updateStatus.setInt(2, vehicleId);
                    if (updateStatus.executeUpdate() > 0)
                        JOptionPane.showMessageDialog(parentFrame, "Availability updated successfully.");
                }
                return;
            }

            // 2. Update VehicleType's average rent
            if (actionChoice == 1) {
                String newRent = JOptionPane.showInputDialog(parentFrame, "Current average rent: " + currentAverageRent + "\nEnter new average rent for " + currentType + ":");
                if (newRent == null || newRent.trim().isEmpty()) return;
                try {
                    double rentValue = Double.parseDouble(newRent.trim());
                    PreparedStatement updateRent = conn.prepareStatement("UPDATE VehicleType SET Average_rent = ? WHERE TypeId = ?");
                    updateRent.setDouble(1, rentValue);
                    updateRent.setInt(2, currentTypeId);
                    if (updateRent.executeUpdate() > 0)
                        JOptionPane.showMessageDialog(parentFrame, "Average rent updated successfully.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid rent input.");
                }
                return;
            }

            // 3. Update full details
            JTextField modelField = new JTextField(currentModel);
            JTextField regField = new JTextField(currentReg);
            JTextField priceField = new JTextField(String.valueOf(currentPrice));
            JTextField avgRentField = new JTextField(String.valueOf(currentAverageRent));

            JComboBox<String> typeCombo = new JComboBox<>();
            try (PreparedStatement typeStmt = conn.prepareStatement("SELECT Vehicle_type FROM VehicleType");
                 ResultSet typeRs = typeStmt.executeQuery()) {
                while (typeRs.next()) {
                    typeCombo.addItem(typeRs.getString("Vehicle_type"));
                }
            }
            typeCombo.setSelectedItem(currentType);

            JButton addTypeBtn = new JButton("Add Type");
            addTypeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            addTypeBtn.setBackground(new Color(52, 152, 219));
            addTypeBtn.setForeground(Color.WHITE);
            addTypeBtn.addActionListener(e -> {
                String newType = JOptionPane.showInputDialog(parentFrame, "Enter new vehicle type:");
                if (newType != null && !newType.trim().isEmpty()) {
                    String avgRentInput = JOptionPane.showInputDialog(parentFrame, "Enter average rent for \"" + newType.trim() + "\":");
                    if (avgRentInput == null || avgRentInput.trim().isEmpty()) return;

                    try {
                        double avgRent = Double.parseDouble(avgRentInput.trim());
                        try (PreparedStatement insertType = conn.prepareStatement("INSERT INTO VehicleType (Vehicle_type, Average_rent) VALUES (?, ?)")) {
                            insertType.setString(1, newType.trim());
                            insertType.setDouble(2, avgRent);
                            insertType.executeUpdate();
                            typeCombo.addItem(newType.trim());
                            typeCombo.setSelectedItem(newType.trim());
                        }
                    } catch (NumberFormatException en) {
                        JOptionPane.showMessageDialog(parentFrame, "Invalid rent input. Type was not added.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame, "Type may already exist or another error occurred.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            JComboBox<String> statusBox = new JComboBox<>(new String[]{"available", "rented", "sold", "not_available"});
            statusBox.setSelectedItem(currentStatus);

            JPanel formPanel = new JPanel(new GridLayout(7, 2, 12, 12));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
            formPanel.setBackground(new Color(245, 245, 245));

            Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
            Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

            JLabel[] labels = {
                    new JLabel("Model:"), new JLabel("Reg No:"), new JLabel("Price:"),
                    new JLabel("Type:"), new JLabel(""), new JLabel("Status:"),
                    new JLabel("Avg Rent of Type:")
            };
            for (JLabel lbl : labels) lbl.setFont(labelFont);

            modelField.setFont(fieldFont);
            regField.setFont(fieldFont);
            priceField.setFont(fieldFont);
            avgRentField.setFont(fieldFont);
            typeCombo.setFont(fieldFont);
            statusBox.setFont(fieldFont);

            formPanel.add(labels[0]); formPanel.add(modelField);
            formPanel.add(labels[1]); formPanel.add(regField);
            formPanel.add(labels[2]); formPanel.add(priceField);
            formPanel.add(labels[3]); formPanel.add(typeCombo);
            formPanel.add(labels[4]); formPanel.add(addTypeBtn);
            formPanel.add(labels[5]); formPanel.add(statusBox);
            formPanel.add(labels[6]); formPanel.add(avgRentField);

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            JLabel titleLabel = new JLabel("Edit Vehicle ID: " + vehicleId, JLabel.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(41, 128, 185));

            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(parentFrame, mainPanel, "Update Vehicle Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newModel = modelField.getText().trim();
                String newReg = regField.getText().trim();
                String priceText = priceField.getText().trim();
                String avgRentText = avgRentField.getText().trim();
                String newType = (String) typeCombo.getSelectedItem();
                String newStatus = (String) statusBox.getSelectedItem();

                if (newModel.isEmpty() || newReg.isEmpty() || priceText.isEmpty() || newType == null || newStatus == null || avgRentText.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double newPrice, newAvgRent;
                try {
                    newPrice = Double.parseDouble(priceText);
                    newAvgRent = Double.parseDouble(avgRentText);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement getIdStmt = conn.prepareStatement("SELECT TypeId FROM VehicleType WHERE Vehicle_type = ?");
                getIdStmt.setString(1, newType);
                ResultSet typeRs = getIdStmt.executeQuery();
                int typeId = -1;
                if (typeRs.next()) {
                    typeId = typeRs.getInt("TypeId");
                }

                if (typeId == -1) {
                    JOptionPane.showMessageDialog(parentFrame, "Selected type not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                conn.setAutoCommit(false);
                try {
                    PreparedStatement updateVehicle = conn.prepareStatement("UPDATE Vehicles SET Vehicle_name=?, Registration_no=?, TypeId=?, VehicleStatus=?, Price=? WHERE VehicleID=?");
                    updateVehicle.setString(1, newModel);
                    updateVehicle.setString(2, newReg);
                    updateVehicle.setInt(3, typeId);
                    updateVehicle.setString(4, newStatus);
                    updateVehicle.setDouble(5, newPrice);
                    updateVehicle.setInt(6, vehicleId);
                    updateVehicle.executeUpdate();

                    PreparedStatement updateAvg = conn.prepareStatement("UPDATE VehicleType SET Average_rent = ? WHERE TypeId = ?");
                    updateAvg.setDouble(1, newAvgRent);
                    updateAvg.setInt(2, typeId);
                    updateAvg.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(parentFrame, "Vehicle and Type details updated successfully.");
                } catch (Exception ex) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(parentFrame, "Update failed: " + ex.getMessage());
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Registration number already exists!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }





}



