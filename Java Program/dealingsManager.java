import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.sql.*;


public class dealingsManager                       // Manager that maanages Rent car and Sell car
{
    public void rentVehicle(JFrame parentFrame) {
        Connection conn = DatabaseConnection.getConnection();

        JFrame rentFrame = new JFrame("Rent Vehicle");
        rentFrame.setSize(900, 800);
        rentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rentFrame.setLayout(new BorderLayout());
        rentFrame.setLocationRelativeTo(null);

        Color backgroundColor = new Color(44, 62, 80);
        Font titleFont = new Font("Arial", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel("Rent a Vehicle");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(backgroundColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(backgroundColor);
        JLabel typeLabel = new JLabel("Select Vehicle Type: ");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(Color.WHITE);

        JComboBox<String> comboType = new JComboBox<>();
        comboType.setFont(fieldFont);

        // Populate vehicle types
        try (PreparedStatement ps = conn.prepareStatement("SELECT Vehicle_type FROM VehicleType");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboType.addItem(rs.getString("Vehicle_type"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(rentFrame, "Failed to load vehicle types: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        topPanel.add(typeLabel);
        topPanel.add(comboType);

        String[] columnNames = {"ID", "Name", "Type", "Registration Number"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setRowHeight(25);
        table.setFont(fieldFont);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(Color.GRAY);
        table.setSelectionForeground(Color.YELLOW);

        // Update table on type change
        comboType.addActionListener(e -> {
            String selectedType = (String) comboType.getSelectedItem();
            tableModel.setRowCount(0);
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT v.VehicleID, v.Vehicle_name, t.Vehicle_type, v.Registration_no " +
                            "FROM Vehicles v JOIN VehicleType t ON v.TypeId = t.TypeId " +
                            "WHERE t.Vehicle_type = ? AND v.VehicleStatus = 'available'")) {
                ps.setString(1, selectedType);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("VehicleID"),
                            rs.getString("Vehicle_name"),
                            rs.getString("Vehicle_type"),
                            rs.getString("Registration_no")
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(rentFrame, "Error loading vehicles: " + ex.getMessage());
            }
        });

        if (comboType.getItemCount() > 0) {
            comboType.setSelectedIndex(0);
        }

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel vehicleIdLabel = new JLabel("Enter Vehicle ID:");
        JLabel nameLabel = new JLabel("Customer Name:");
        JLabel phoneLabel = new JLabel("Phone Number:");
        JLabel cnicLabel = new JLabel("CNIC:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel customPriceLabel = new JLabel("Custom Rental Price:");

        JLabel[] labels = {vehicleIdLabel, nameLabel, phoneLabel, cnicLabel, emailLabel, customPriceLabel};
        for (JLabel label : labels) {
            label.setFont(labelFont);
            label.setForeground(Color.WHITE);
        }

        JTextField vehicleIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField cnicField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField customPriceField = new JTextField();
        customPriceField.setEnabled(false);

        JCheckBox useCustomPrice = new JCheckBox("Use Custom Price?");
        useCustomPrice.setBackground(backgroundColor);
        useCustomPrice.setForeground(Color.WHITE);
        useCustomPrice.setFont(labelFont);

        useCustomPrice.addActionListener(e -> customPriceField.setEnabled(useCustomPrice.isSelected()));

        JTextField[] fields = {vehicleIdField, nameField, phoneField, cnicField, emailField, customPriceField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
        }

        formPanel.add(vehicleIdLabel); formPanel.add(vehicleIdField);
        formPanel.add(nameLabel); formPanel.add(nameField);
        formPanel.add(phoneLabel); formPanel.add(phoneField);
        formPanel.add(cnicLabel); formPanel.add(cnicField);
        formPanel.add(emailLabel); formPanel.add(emailField);
        formPanel.add(customPriceLabel); formPanel.add(customPriceField);

        JButton rentButton = new JButton("Rent Vehicle");
        rentButton.setFont(new Font("Arial", Font.BOLD, 18));
        rentButton.setBackground(new Color(70, 130, 180));
        rentButton.setForeground(Color.WHITE);

        JTextArea receiptArea = new JTextArea(10, 40);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        receiptArea.setBackground(Color.BLACK);
        receiptArea.setForeground(Color.GREEN);
        JScrollPane receiptScroll = new JScrollPane(receiptArea);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(backgroundColor);
        actionPanel.add(useCustomPrice);
        actionPanel.add(rentButton);

        bottomPanel.add(actionPanel, BorderLayout.NORTH);
        bottomPanel.add(receiptScroll, BorderLayout.CENTER);

        rentButton.addActionListener(e -> {
            String vehicleIdText = vehicleIdField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String cnic = cnicField.getText().trim();
            String email = emailField.getText().trim();

            if (vehicleIdText.isEmpty() || name.isEmpty() || phone.isEmpty() || cnic.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(rentFrame, "Please fill all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!cnic.matches("^[0-9]{5}-[0-9]{7}-[0-9]{1}$")) {
                JOptionPane.showMessageDialog(rentFrame, "Invalid CNIC format. Use 12345-1234567-1", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!phone.matches("^[0-9]{4} [0-9]{7}$")) {
                JOptionPane.showMessageDialog(rentFrame, "Invalid Phone format. Use 0300 1234567", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int vehicleId;
            try {
                vehicleId = Integer.parseInt(vehicleIdText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(rentFrame, "Invalid Vehicle ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Check vehicle availability and get type
                PreparedStatement checkVehicle = conn.prepareStatement("SELECT Vehicle_name, VehicleStatus, TypeId FROM Vehicles WHERE VehicleID = ?");
                checkVehicle.setInt(1, vehicleId);
                ResultSet rsV = checkVehicle.executeQuery();
                if (!rsV.next() || !rsV.getString("VehicleStatus").equals("available")) {
                    JOptionPane.showMessageDialog(rentFrame, "Vehicle not found or not available.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String model = rsV.getString("Vehicle_name");
                int typeId = rsV.getInt("TypeId");

                // Get rental price from VehicleType
                double rentalPrice = 1000; // fallback default
                PreparedStatement psPrice = conn.prepareStatement("SELECT Average_rent FROM VehicleType WHERE TypeId = ?");
                psPrice.setInt(1, typeId);
                ResultSet rsPrice = psPrice.executeQuery();
                if (rsPrice.next()) {
                    rentalPrice = rsPrice.getDouble("Average_rent");
                }

                if (useCustomPrice.isSelected()) {
                    try {
                        rentalPrice = Double.parseDouble(customPriceField.getText().trim());
                        if (rentalPrice <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(rentFrame, "Invalid custom price.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Handle customer
                int customerId = -1;
                PreparedStatement psCust = conn.prepareStatement("SELECT CustomerID FROM Customers WHERE CustomerCNIC = ?");
                psCust.setString(1, cnic);
                ResultSet rs = psCust.executeQuery();
                if (rs.next()) {
                    customerId = rs.getInt("CustomerID");
                } else {
                    PreparedStatement insertCust = conn.prepareStatement(
                            "INSERT INTO Customers (CustomerName, CustomerCNIC, Phone_no, Email) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    insertCust.setString(1, name);
                    insertCust.setString(2, cnic);
                    insertCust.setString(3, phone);
                    insertCust.setString(4, email);
                    insertCust.executeUpdate();
                    ResultSet keys = insertCust.getGeneratedKeys();
                    if (keys.next()) customerId = keys.getInt(1);
                }

                // Update vehicle and insert into Rentals
                PreparedStatement updateVehicle = conn.prepareStatement("UPDATE Vehicles SET VehicleStatus = 'rented' WHERE VehicleID = ?");
                updateVehicle.setInt(1, vehicleId);
                updateVehicle.executeUpdate();

                PreparedStatement rentalInsert = conn.prepareStatement("INSERT INTO Rentals (VehicleID, CustomerID, rental_price) VALUES (?, ?, ?)");
                rentalInsert.setInt(1, vehicleId);
                rentalInsert.setInt(2, customerId);
                rentalInsert.setDouble(3, rentalPrice);
                rentalInsert.executeUpdate();

                StringBuilder receipt = new StringBuilder();
                receipt.append("******** RENTAL RECEIPT ********\n");
                receipt.append("Customer Name : ").append(name).append("\n");
                receipt.append("Phone Number  : ").append(phone).append("\n");
                receipt.append("CNIC          : ").append(cnic).append("\n");
                receipt.append("Email         : ").append(email).append("\n\n");
                receipt.append("Vehicle ID    : ").append(vehicleId).append("\n");
                receipt.append("Vehicle Name : ").append(model).append("\n");
                receipt.append("Rental Price  : Rs. ").append(rentalPrice).append("\n");
                receipt.append("Date & Time   : ").append(java.time.LocalDateTime.now()).append("\n");
                receipt.append("********************************\n");

                receiptArea.setText(receipt.toString());

                JOptionPane.showMessageDialog(rentFrame, "Vehicle rented successfully!");
                comboType.setSelectedItem(comboType.getSelectedItem()); // Refresh table

            } catch (SQLIntegrityConstraintViolationException dup) {
                JOptionPane.showMessageDialog(rentFrame, "Duplicate customer CNIC/Email/Phone.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(rentFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(backgroundColor);
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(bottomPanel, BorderLayout.SOUTH);

        rentFrame.add(titleLabel, BorderLayout.NORTH);
        rentFrame.add(centerPanel, BorderLayout.CENTER);
        rentFrame.add(southPanel, BorderLayout.SOUTH);

        rentFrame.setVisible(true);
    }



    public void DisplayOngoing(JFrame parentFrame)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Establish database connection
            conn = DatabaseConnection.getConnection();

            String query = "SELECT c.CustomerName, c.CustomerCNIC, v.VehicleID, v.Vehicle_name, v.Registration_no , r.rental_date, r.rental_price " +
                    "FROM Rentals r " +
                    "JOIN Customers c ON r.CustomerID = c.CustomerID " +
                    "JOIN Vehicles v ON r.VehicleID = v.VehicleID";

            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            // Check if result set is empty
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(parentFrame, "No ongoing rentals.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // GUI setup
            JFrame frame = new JFrame("Ongoing Rentals");
            frame.setSize(950, 520);
            frame.setLocationRelativeTo(parentFrame);
            frame.setLayout(new BorderLayout(10, 10));
            frame.getContentPane().setBackground(new Color(245, 246, 250));

            // Top panel
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setBackground(new Color(245, 246, 250));
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            JLabel titleLabel = new JLabel("Ongoing Rentals");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(new Color(41, 128, 185));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            topPanel.add(titleLabel);
            frame.add(topPanel, BorderLayout.NORTH);

            // Table setup
            String[] columns = {"Customer Name", "CNIC", "Vehicle ID", "Vheicel Name", "Registration Number", "Rental Date", "Rental Price"};
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

            // Fill data
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("CustomerName"),
                        rs.getString("CustomerCNIC"),
                        rs.getInt("VehicleID"),
                        rs.getString("Vehicle_Name"),
                        rs.getString("Registration_no"),
                        rs.getString("rental_date"),
                        rs.getDouble("rental_Price")
                });
            }

            // Footer panel
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

            frame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parentFrame, "Error fetching data from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayAvailableVehicles(JFrame parentFrame) {
        JFrame frame = new JFrame("Available Vehicles");
        frame.setSize(1100, 520);  // Slightly wider to fit extra columns
        frame.setLocationRelativeTo(parentFrame);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));

        // Top panel for title
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Available Vehicles");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Name", "Registration Number", "Type", "Rental Price", "Selling Price", "Available"};
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

        // Connect to database and fetch available vehicles
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            String query = "SELECT v.VehicleID, v.Vehicle_name, v.Registration_no, vt.Vehicle_type, v.Price, vt.Average_rent " +
                    "FROM Vehicles v " +
                    "JOIN VehicleType vt ON v.TypeId = vt.TypeId " +
                    "WHERE v.VehicleStatus = 'available'";

            ResultSet rs = stmt.executeQuery(query);

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                model.addRow(new Object[]{
                        rs.getInt("VehicleID"),
                        rs.getString("Vehicle_name"),
                        rs.getString("Registration_no"),
                        rs.getString("Vehicle_type"),
                        rs.getDouble("Price"),
                        rs.getDouble("Average_rent"),
                        "Yes"
                });
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(parentFrame, "No available vehicles.", "Info", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                return;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parentFrame, "Error loading available vehicles: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            return;
        }

        // Footer panel with Close button
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

        frame.setVisible(true);
    }


    void searchRental()
    {
        JFrame frame = new JFrame("Search Rentals");
        frame.setSize(950, 520);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Search Rentals");
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

        JRadioButton rbCNIC = new JRadioButton("Customer CNIC");
        JRadioButton rbRegNo = new JRadioButton("Vehicle Registration No.");
        ButtonGroup group = new ButtonGroup();
        group.add(rbCNIC);
        group.add(rbRegNo);
        rbCNIC.setSelected(true);

        rbCNIC.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbRegNo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rbCNIC.setBackground(new Color(245, 246, 250));
        rbRegNo.setBackground(new Color(245, 246, 250));

        searchOptionsPanel.add(rbCNIC);
        searchOptionsPanel.add(rbRegNo);

        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchOptionsPanel.add(txtSearch);

        searchOptionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(searchOptionsPanel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Customer Name", "CNIC", "Vehicle ID", "name", "Reg#"};
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

        // Footer panel
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

        // Database Search Logic
        btnSearch.addActionListener(e -> {
            model.setRowCount(0);
            String input = txtSearch.getText().trim();

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter search value.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String query = "";
            if (rbCNIC.isSelected()) {
                query = """
                SELECT DISTINCT C.CustomerName, C.CustomerCNIC, V.VehicleID, V.Vehicle_name, V.Registration_no
                FROM Customers C
                JOIN Vehicles V ON V.VehicleID IN (
                    SELECT VehicleID FROM Rentals WHERE CustomerID = C.CustomerID
                    UNION
                    SELECT VehicleID FROM Records WHERE CustomerID = C.CustomerID
                )
                WHERE C.CustomerCNIC = ?
            """;
            } else {
                query = """
                SELECT DISTINCT C.CustomerName, C.CustomerCNIC, V.VehicleID, V.Vehicle_name, V.Registration_no
                FROM Vehicles V
                JOIN Customers C ON C.CustomerID IN (
                    SELECT CustomerID FROM Rentals WHERE VehicleID = V.VehicleID
                    UNION
                    SELECT CustomerID FROM Records WHERE VehicleID = V.VehicleID
                )
                WHERE V.Registration_no = ?
            """;
            }

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, input);
                ResultSet rs = stmt.executeQuery();

                boolean found = false;
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("CustomerName"),
                            rs.getString("CustomerCNIC"),
                            rs.getInt("VehicleID"),
                            rs.getString("Vehicle_name"),
                            rs.getString("Registration_no")
                    });
                    found = true;
                }

                if (!found) {
                    JOptionPane.showMessageDialog(frame, "No rentals found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }





    public void returnVehicle() {
        JFrame frame = new JFrame("Return Vehicle");
        frame.setSize(550, 420);
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

        JLabel titleLabel = new JLabel("Return Vehicle", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 15, 25));
        formPanel.setBackground(new Color(44, 62, 80));

        JLabel lblCnic = new JLabel("Customer CNIC:");
        lblCnic.setFont(labelFont);
        lblCnic.setForeground(Color.WHITE);
        JTextField txtCnic = new JTextField();
        txtCnic.setFont(fieldFont);

        JLabel lblRegNo = new JLabel("Vehicle Reg No:");
        lblRegNo.setFont(labelFont);
        lblRegNo.setForeground(Color.WHITE);
        JTextField txtRegNo = new JTextField();
        txtRegNo.setFont(fieldFont);

        formPanel.add(lblCnic); formPanel.add(txtCnic);
        formPanel.add(lblRegNo); formPanel.add(txtRegNo);
        mainPanel.add(formPanel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnPanel.setBackground(new Color(44, 62, 80));

        JButton btnReturn = new JButton("Return Vehicle");
        btnReturn.setFont(new Font("Arial", Font.BOLD, 16));
        btnReturn.setBackground(new Color(41, 128, 185));
        btnReturn.setForeground(Color.WHITE);
        btnReturn.setFocusPainted(false);
        btnReturn.setPreferredSize(new Dimension(160, 40));

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Arial", Font.BOLD, 16));
        btnClose.setBackground(new Color(192, 57, 43));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setPreferredSize(new Dimension(100, 40));
        btnClose.addActionListener(e -> frame.dispose());

        btnPanel.add(btnReturn);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel);
        frame.add(mainPanel);
        frame.setVisible(true);

        btnReturn.addActionListener(e -> {
            String cnic = txtCnic.getText().trim();
            String regNo = txtRegNo.getText().trim();

            if (cnic.isEmpty() || regNo.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both CNIC and Registration No.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!regNo.matches("[A-Z]{1,3}-\\d{1,4}")) {
                JOptionPane.showMessageDialog(frame, "Registration number must be in format ABC-1234.", "Invalid Format", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!cnic.matches("^[0-9]{5}-[0-9]{7}-[0-9]$")) {
                JOptionPane.showMessageDialog(frame, "Invalid CNIC format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection con = DatabaseConnection.getConnection()) {

                // Step 1: Get VehicleID and CustomerID
                String fetchQuery = """
                SELECT r.Rental_id, r.VehicleID, r.CustomerID, r.rental_date, r.rental_price,
                       c.CustomerName, v.Vehicle_name
                FROM Rentals r
                JOIN Customers c ON r.CustomerID = c.CustomerID
                JOIN Vehicles v ON r.VehicleID = v.VehicleID
                WHERE c.CustomerCNIC = ? AND v.Registration_no = ?
            """;

                try (PreparedStatement ps = con.prepareStatement(fetchQuery)) {
                    ps.setString(1, cnic);
                    ps.setString(2, regNo);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        int rentalId = rs.getInt("Rental_id");
                        int vehicleId = rs.getInt("VehicleID");
                        int customerId = rs.getInt("CustomerID");
                        Date rentalDate = rs.getDate("rental_date");
                        double rentalPrice = rs.getDouble("rental_price");
                        String customerName = rs.getString("CustomerName");
                        String vehicleModel = rs.getString("Vehicle_name");

                        // Step 2: Insert into Records
                        String insertRecord = """
                        INSERT INTO Records (VehicleID, CustomerID, record_date, return_date, price, Status)
                        VALUES (?, ?, ?, CURRENT_DATE, ?, 'Rented')
                    """;
                        try (PreparedStatement psInsert = con.prepareStatement(insertRecord)) {
                            psInsert.setInt(1, vehicleId);
                            psInsert.setInt(2, customerId);
                            psInsert.setDate(3, rentalDate);
                            psInsert.setDouble(4, rentalPrice);
                            psInsert.executeUpdate();
                        }

                        // Step 3: Insert into History
                        String insertHistory = """
                        INSERT INTO History (CustomerName, CustomerCNIC, Vehicle_name, Registration_no, record_date, return_date, price, Status)
                        VALUES (?, ?, ?, ?, ?, CURRENT_DATE, ?, 'Rented')
                    """;
                        try (PreparedStatement psHistory = con.prepareStatement(insertHistory)) {
                            psHistory.setString(1, customerName);
                            psHistory.setString(2, cnic);
                            psHistory.setString(3, vehicleModel);
                            psHistory.setString(4, regNo);
                            psHistory.setDate(5, rentalDate);
                            psHistory.setDouble(6, rentalPrice);
                            psHistory.executeUpdate();
                        }

                        // Step 4: Delete from Rentals
                        String deleteRental = "DELETE FROM Rentals WHERE Rental_id = ?";
                        try (PreparedStatement psDelete = con.prepareStatement(deleteRental)) {
                            psDelete.setInt(1, rentalId);
                            psDelete.executeUpdate();
                        }

                        // Step 5: Update Vehicle Status
                        String updateVehicle = "UPDATE Vehicles SET VehicleStatus = 'available' WHERE VehicleID = ?";
                        try (PreparedStatement psUpdate = con.prepareStatement(updateVehicle)) {
                            psUpdate.setInt(1, vehicleId);
                            psUpdate.executeUpdate();
                        }

                        JOptionPane.showMessageDialog(frame, "Vehicle successfully returned and record saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();

                    } else {
                        JOptionPane.showMessageDialog(frame, "No matching rental found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }



    public void sellVehicle(JFrame parentFrame)
    {
        JFrame sellFrame = new JFrame("Sell Vehicle");
        sellFrame.setSize(900, 800);
        sellFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sellFrame.setLayout(new BorderLayout());
        sellFrame.setLocationRelativeTo(null);

        Color backgroundColor = new Color(44, 62, 80);
        Font titleFont = new Font("Arial", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);


        JLabel titleLabel = new JLabel("Sell a Vehicle", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(backgroundColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(backgroundColor);
        JLabel typeLabel = new JLabel("Select Vehicle Type: ");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(Color.WHITE);

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

        topPanel.add(typeLabel);
        topPanel.add(comboType);

        String[] columnNames = {"ID", "Name", "Type", "Registration Number", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(800, 150));
        table.setRowHeight(25);
        table.setFont(fieldFont);
        table.getTableHeader().setFont(labelFont);
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(Color.GRAY);
        table.setSelectionForeground(Color.YELLOW);

        comboType.addActionListener(e -> {
            tableModel.setRowCount(0);
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT V.VehicleID, V.Vehicle_name, T.Vehicle_type, V.Registration_no, V.Price " +
                        "FROM Vehicles V JOIN VehicleType T ON V.TypeId = T.TypeId " +
                        "WHERE T.Vehicle_type = ? AND V.VehicleStatus = 'available'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, (String) comboType.getSelectedItem());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("VehicleID"),
                            rs.getString("Vehicle_name"),
                            rs.getString("Vehicle_type"),
                            rs.getString("Registration_no"),
                            rs.getDouble("Price")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(sellFrame, "Database error: " + ex.getMessage());
            }
        });
        comboType.setSelectedIndex(0);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 20));
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel[] labels = {
                new JLabel("Enter Vehicle ID:"), new JLabel("Customer Name:"), new JLabel("Phone Number:"),
                new JLabel("CNIC:"), new JLabel("Email:"), new JLabel("Sale Price:"), new JLabel("Price Option:")
        };
        for (JLabel label : labels) {
            label.setFont(labelFont);
            label.setForeground(Color.WHITE);
        }

        JTextField vehicleIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField cnicField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField priceField = new JTextField();
        priceField.setEnabled(false);

        JTextField[] fields = {vehicleIdField, nameField, phoneField, cnicField, emailField, priceField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
        }

        JRadioButton defaultPriceRadio = new JRadioButton("Default Price", true);
        defaultPriceRadio.setFont(labelFont);
        defaultPriceRadio.setForeground(Color.WHITE);
        defaultPriceRadio.setBackground(backgroundColor);
        JRadioButton customPriceRadio = new JRadioButton("Custom Price");
        customPriceRadio.setFont(labelFont);
        customPriceRadio.setForeground(Color.WHITE);
        customPriceRadio.setBackground(backgroundColor);

        ButtonGroup priceGroup = new ButtonGroup();
        priceGroup.add(defaultPriceRadio);
        priceGroup.add(customPriceRadio);

        JPanel priceOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priceOptionPanel.setBackground(backgroundColor);
        priceOptionPanel.add(defaultPriceRadio);
        priceOptionPanel.add(customPriceRadio);

        formPanel.add(labels[0]); formPanel.add(vehicleIdField);
        formPanel.add(labels[1]); formPanel.add(nameField);
        formPanel.add(labels[2]); formPanel.add(phoneField);
        formPanel.add(labels[3]); formPanel.add(cnicField);
        formPanel.add(labels[4]); formPanel.add(emailField);
        formPanel.add(labels[5]); formPanel.add(priceField);
        formPanel.add(labels[6]); formPanel.add(priceOptionPanel);

        defaultPriceRadio.addActionListener(e -> priceField.setEnabled(false));
        customPriceRadio.addActionListener(e -> priceField.setEnabled(true));

        JButton sellButton = new JButton("Sell Vehicle");
        sellButton.setFont(new Font("Arial", Font.BOLD, 18));
        sellButton.setBackground(new Color(70, 130, 180));
        sellButton.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        bottomPanel.add(sellButton);

        sellButton.addActionListener(e -> {
            String idText = vehicleIdField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String cnic = cnicField.getText().trim();
            String email = emailField.getText().trim();

            if (idText.isEmpty() || name.isEmpty() || phone.isEmpty() || cnic.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(sellFrame, "All fields are required.");
                return;
            }

            if (!cnic.matches("^[0-9]{5}-[0-9]{7}-[0-9]{1}$")) {
                JOptionPane.showMessageDialog(sellFrame, "Invalid CNIC format. Use 12345-1234567-1", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!phone.matches("^[0-9]{4} [0-9]{7}$")) {
                JOptionPane.showMessageDialog(sellFrame, "Invalid Phone format. Use 0300 1234567", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                int vehicleId = Integer.parseInt(idText);

                PreparedStatement findStmt = conn.prepareStatement(
                        "SELECT V.*, T.Vehicle_type FROM Vehicles V " +
                                "JOIN VehicleType T ON V.TypeId = T.TypeId " +
                                "WHERE V.VehicleID = ? AND V.VehicleStatus = 'available'"
                );
                findStmt.setInt(1, vehicleId);
                ResultSet rs = findStmt.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(sellFrame, "Vehicle not found or already sold.");
                    return;
                }

                double price = rs.getDouble("Price");
                String model = rs.getString("Vehicle_name");
                String reg = rs.getString("Registration_no");

                if (customPriceRadio.isSelected()) {
                    try {
                        double custom = Double.parseDouble(priceField.getText().trim());
                        if (custom <= 0) throw new Exception();
                        price = custom;
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(sellFrame, "Enter a valid custom price.");
                        return;
                    }
                }

                // Insert or find Customer
                int customerId = -1;
                PreparedStatement checkCustomer = conn.prepareStatement("SELECT CustomerID FROM Customers WHERE CustomerCNIC = ?");
                checkCustomer.setString(1, cnic);
                ResultSet custRs = checkCustomer.executeQuery();
                if (custRs.next()) {
                    customerId = custRs.getInt("CustomerID");
                } else {
                    PreparedStatement insertCustomer = conn.prepareStatement(
                            "INSERT INTO Customers (CustomerName, CustomerCNIC, Phone_no, Email) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    insertCustomer.setString(1, name);
                    insertCustomer.setString(2, cnic);
                    insertCustomer.setString(3, phone);
                    insertCustomer.setString(4, email);
                    insertCustomer.executeUpdate();
                    ResultSet genKeys = insertCustomer.getGeneratedKeys();
                    if (genKeys.next()) {
                        customerId = genKeys.getInt(1);
                    }
                }

               // Check for duplicate record in Records
                PreparedStatement checkDuplicate = conn.prepareStatement(
                        "SELECT * FROM Records WHERE VehicleID = ? AND CustomerID = ? AND record_date = CURRENT_DATE AND Status = 'Sold'"
                );
                checkDuplicate.setInt(1, vehicleId);
                checkDuplicate.setInt(2, customerId);
                ResultSet dupRs = checkDuplicate.executeQuery();

                if (dupRs.next()) {
                    JOptionPane.showMessageDialog(sellFrame, "This vehicle has already been sold to this customer today.");
                    return;
                }

// Proceed to insert only if not duplicated
                PreparedStatement insertRecord = conn.prepareStatement(
                        "INSERT INTO Records (VehicleID, CustomerID, record_date, price, Status) VALUES (?, ?, CURRENT_DATE, ?, 'Sold')"
                );
                insertRecord.setInt(1, vehicleId);
                insertRecord.setInt(2, customerId);
                insertRecord.setDouble(3, price);
                insertRecord.executeUpdate();

// Insert into History
                PreparedStatement insertHistory = conn.prepareStatement(
                        "INSERT INTO History (CustomerName, CustomerCNIC, Vehicle_name, Registration_no, record_date, return_date, price, Status) " +
                                "VALUES (?, ?, ?, ?, CURRENT_DATE, NULL, ?, 'Sold')"
                );
                insertHistory.setString(1, name);
                insertHistory.setString(2, cnic);
                insertHistory.setString(3, model);
                insertHistory.setString(4, reg);
                insertHistory.setDouble(5, price);
                insertHistory.executeUpdate();

                // Update vehicle status
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE Vehicles SET VehicleStatus = 'sold' WHERE VehicleID = ?");
                updateStmt.setInt(1, vehicleId);
                updateStmt.executeUpdate();
                // Update vehicle price
               PreparedStatement updateStmt2 = conn.prepareStatement("UPDATE Vehicles SET Price = ? WHERE VehicleID = ?");
                updateStmt2.setDouble(1, price);
               updateStmt2.setInt(2, vehicleId);
               updateStmt2.executeUpdate();


                JOptionPane.showMessageDialog(sellFrame,
                        "<html><b>Vehicle Sold Successfully!</b><br><br>" +
                                "<b>Vehicle:</b> " + model + "<br>" +
                                "<b>Reg No:</b> " + reg + "<br>" +
                                "<b>Price:</b> Rs. " + price + "<br>" +
                                "<b>Customer:</b> " + name + "</html>"
                );

                // Refresh
                comboType.setSelectedItem(comboType.getSelectedItem());
                vehicleIdField.setText("");
                nameField.setText("");
                phoneField.setText("");
                cnicField.setText("");
                emailField.setText("");
                priceField.setText("");
                defaultPriceRadio.setSelected(true);
                priceField.setEnabled(false);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(sellFrame, "SQL Error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(sellFrame, "Vehicle ID must be numeric.");
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(backgroundColor);
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(bottomPanel, BorderLayout.SOUTH);

        sellFrame.add(titleLabel, BorderLayout.NORTH);
        sellFrame.add(centerPanel, BorderLayout.CENTER);
        sellFrame.add(southPanel, BorderLayout.SOUTH);

        sellFrame.setVisible(true);
    }


    public void displaySoldVehicle(JFrame parentFrame) {
        JFrame frame = new JFrame("Sold Vehicles");
        frame.setSize(950, 520);
        frame.setLocationRelativeTo(parentFrame);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 246, 250));

        // Top panel for title
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Sold Vehicles");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Customer Name", "Phone", "CNIC", "Email", "Vehicle ID", "Vehicle name", "Registration Number", "Price", "Type"};
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

        // Database Fetch
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            String query = "SELECT c.CustomerName, c.Phone_no, c.CustomerCNIC, c.Email, " +
                    "v.VehicleID, v.Vehicle_name, v.Registration_no, v.Price, vt.Vehicle_type " +
                    "FROM Records r " +
                    "JOIN Customers c ON r.CustomerID = c.CustomerID " +
                    "JOIN Vehicles v ON r.VehicleID = v.VehicleID " +
                    "JOIN VehicleType vt ON v.TypeId = vt.TypeId " +
                    "WHERE r.Status = 'Sold'";

            ResultSet rs = stmt.executeQuery(query);

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                model.addRow(new Object[]{
                        rs.getString("CustomerName"),
                        rs.getString("Phone_no"),
                        rs.getString("CustomerCNIC"),
                        rs.getString("Email"),
                        rs.getInt("VehicleID"),
                        rs.getString("Vehicle_name"),
                        rs.getString("Registration_no"),
                        rs.getDouble("Price"),
                        rs.getString("Vehicle_type")
                });
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(parentFrame, "No sold vehicle records available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                return;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parentFrame, "Error loading sold vehicle data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            return;
        }

        // Footer panel with Close button
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

        frame.setVisible(true);
    }

}
