import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import java.util.ArrayList;

class menueFrames {


        menueFrames( ){        }

    void LoginGUI() {
        JFrame loginFrame = new JFrame("Login");
        Container cont = loginFrame.getContentPane();
        cont.setLayout(null); // Using null layout for absolute positioning

        // Title Label
        JLabel titleLabel = new JLabel("Car Rental and Selling Facility");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBounds(100, 20, 600, 50); // x, y, width, height
        cont.add(titleLabel);

        // Username Label
        JLabel userLabel = new JLabel("Email/Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userLabel.setBounds(150, 179, 200, 30); // Positioning and size x y width hieght
        cont.add(userLabel);

        // Username Text Field
        JTextField userText = new JTextField(20);
        userText.setFont(new Font("Arial", Font.PLAIN, 18));
        userText.setBounds(350, 170, 250, 30); // Positioning and size
        cont.add(userText);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setBounds(150, 250, 200, 30); // Positioning and size
        cont.add(passwordLabel);

        // Password Text Field
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordText.setBounds(350, 250, 250, 30); // Positioning and size
        cont.add(passwordText);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBounds(300, 370, 200, 40); // Positioning and size
        cont.add(loginButton);

        // Frame settings
        loginFrame.setSize(800, 500);
        loginFrame.setResizable(false);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        loginButton.addActionListener(e -> {
            String user = userText.getText().trim();
            String pass = new String(passwordText.getPassword()).trim();

            loginFrame.dispose(); // Close login window

            if (user.equalsIgnoreCase("admin") && pass.equals("admin")) {
                showAdminMenu();
            } else if ((user.isEmpty() && pass.isEmpty()) || (user.equalsIgnoreCase("guest") && pass.equalsIgnoreCase("guest"))) {
                showCustomerMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                LoginGUI(); // Reopen login
            }
        });
    }



    void showAdminMenu()
    {
        JFrame frame = new JFrame("Car Rental and Selling Facility");
        Container cont = frame.getContentPane();
        cont.setLayout(new BorderLayout());
        //cont.setBackground(new Color(44, 62, 80));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 30, 10));

        JLabel welcomeLabel = new JLabel("Welcome To Our Car Rental and Selling Facility", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Increased font size
        welcomeLabel.setForeground(Color.WHITE); // Set a Blue Colour)
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        

        JPanel mainPanel = new JPanel(new GridBagLayout());
        //mainPanel.setPreferredSize(new Dimension(800, 600)); // width, height
        mainPanel.setBackground(new Color(44, 62, 80));
        GridBagConstraints gbc = new GridBagConstraints();
       // gbc.insets = new Insets(10, 10, 10, 10);

        JButton btnVehicle = createActionButton("Vehicle");
        JButton btnCustomer = createActionButton("Customer");
        JButton btnRentals = createActionButton("Rentals");
        JButton btnSelling = createActionButton("Selling");

        gbc.insets = new Insets(20, 50, 20, 50); // Equal gap between buttons

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
       // gbc.insets = new Insets(10, 50, 10, 50);
        mainPanel.add(btnVehicle, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(btnCustomer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnRentals, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(btnSelling, gbc);


        cont.add(mainPanel, BorderLayout.CENTER);
        cont.add(topPanel, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.setSize(1000, 600); // Reduced frame size   width,height
        frame.setLocationRelativeTo(null);
        // Enable default buttons and lock the frame size
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);


        btnVehicle.addActionListener(e -> {
                frame.setVisible(false);         // Hide main frame
                VehicleMenu(frame);              // Pass main frame reference
            });

        btnCustomer.addActionListener(e -> {
                 frame.setVisible(false);
                CustomerMenu(frame);
            });
        btnRentals.addActionListener(e -> {
                 frame.setVisible(false);
                RentalMenu(frame);
            });
        btnSelling.addActionListener(e -> {
                 frame.setVisible(false);
                SellMenu(frame);
            });
        

         frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }


    void VehicleMenu(JFrame mainFrame)
    {
        vehicleManager vm = new vehicleManager();

        JFrame frame = new JFrame("Vehicle Management panel");
        
        frame.setLayout(new BorderLayout());
       // frame.getContentPane().setBackground(new Color(45, 52, 54));

        
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        //mainPanel.setPreferredSize(new Dimension(800, 600)); // width, height
        mainPanel.setBackground(new Color(44, 62, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Adjusted gaps between buttons

        // Create welcome label with a beautiful color
        JLabel VehicleLabel = new JLabel("Vehicle Control Panel", JLabel.CENTER);
        VehicleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Increased font size
        VehicleLabel.setForeground(Color.WHITE); // Set a Blue Colour)

        // Place label with gap from top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        // gbc.anchor = GridBagConstraints.CENTER;
        // gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 50, 10); // Only top gap   (top, left,bottom,right )
        mainPanel.add( VehicleLabel, gbc);

        //frame.add( VehicleLabel, BorderLayout.North)

        // Create buttons
        JButton btnAdd = createActionButton("Add Vehicle");
        JButton btnEdit = createActionButton("Edit Vehicle");
        JButton btnDelete = createActionButton("Delete Vehicle");
        JButton btnSearch = createActionButton("Search Vehicle");
        JButton btnDisplay = createActionButton("Display Vehicles");
        JButton backButton = createActionButton("Back to Main Menu");


        btnAdd.addActionListener(e -> {
        //frame.dispose();           // Close vehicle window
        //mainFrame.setVisible(true);       // Show main menu again
            vm.addVehicle();
         });

        btnSearch.addActionListener(e -> {
            vm.searchVehicle();
        }); 

        btnDisplay.addActionListener(e -> {
            vm.displayVehicle();
        }); 

        btnEdit.addActionListener(e -> {
            vm.updateVehicle(frame);
        });

        btnDelete.addActionListener(e -> {
            vm.deleteVehicle(frame);
        }); 

        backButton.addActionListener(e -> {
             frame.dispose();           // Close vehicle window
             mainFrame.setVisible(true);       // Show main menu again
         });

        // Add buttons to the panel
        gbc.insets = new Insets(10, 50, 10, 50); // Equal gap between buttons

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
       // gbc.insets = new Insets(10, 50, 10, 50);
        mainPanel.add(btnAdd, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(btnEdit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnDelete, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(btnSearch, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(btnDisplay, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(backButton, gbc);

        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(1000, 600); // Reduced frame size   width,height
        frame.setLocationRelativeTo(null);
        // Enable default buttons and lock the frame size
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        // Add window listener for exit confirmation
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
   
    }

    void CustomerMenu(JFrame mainFrame)
    {
        customerManagerGUI cm = new customerManagerGUI();

        JFrame frame = new JFrame("Customer Management Panel");

        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Gap between elements

        // Create title label
        JLabel customerLabel = new JLabel("Customer Control Panel", JLabel.CENTER);
        customerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        customerLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 50, 10); // Padding under the title
        mainPanel.add(customerLabel, gbc);

        // Create buttons
        JButton btnAdd = createActionButton("Add Customer");
        JButton btnEdit = createActionButton("Edit Customer");
        JButton btnDelete = createActionButton("Delete Customer");
        JButton btnSearch = createActionButton("Search Customer");
        JButton btnDisplay = createActionButton("Display Customers");
        JButton backButton = createActionButton("Back to Main Menu");

        btnAdd.addActionListener(e -> {
        //frame.dispose();           // Close vehicle window
        //mainFrame.setVisible(true);       // Show main menu again
            cm.addCustomer(frame);
         });

        btnSearch.addActionListener(e -> {
            cm.searchCustomer(frame);
        }); 

        btnDisplay.addActionListener(e -> {
            cm.displayCustomers(frame);
        }); 

        btnEdit.addActionListener(e -> {
            cm.updateCustomer(frame);
        });

        btnDelete.addActionListener(e -> {
            cm.deleteCustomer(frame);
        }); 

        // Back button logic
        backButton.addActionListener(e -> {
            frame.dispose();              // Close customer window
            mainFrame.setVisible(true);  // Show main menu again
        });

        // Add buttons to panel
        gbc.insets = new Insets(10, 50, 10, 50); // Equal spacing

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(btnAdd, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(btnEdit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnDelete, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(btnSearch, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(btnDisplay, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(backButton, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    void RentalMenu(JFrame mainFrame) 
    {
        dealingsManager dm = new dealingsManager();
       
        JFrame frame = new JFrame("Car Rental and Selling Facility - Rentals");

        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50); // Equal gap between buttons

        // Rental Panel Header
        JLabel rentalLabel = new JLabel("Rental Control Panel", JLabel.CENTER);
        rentalLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        rentalLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 50, 10); // Top gap
        mainPanel.add(rentalLabel, gbc);

        // Rental Buttons
        JButton btnDisplayAll = createActionButton("Available Vehicles");
        JButton btnDisplayOngoing = createActionButton("Display Ongoing Rentals");
        JButton btnNewRental = createActionButton("Rent a Vehicle");
        JButton btnReturn = createActionButton("Return Vehicle");
        JButton btnSearchRental = createActionButton("Search Rental");
        JButton backButton = createActionButton("Back to Main Menu");

        // Reset padding for remaining buttons
        gbc.insets = new Insets(10, 50, 10, 50);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(btnDisplayAll, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(btnDisplayOngoing, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnNewRental, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(btnReturn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(btnSearchRental, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(backButton, gbc);

        btnNewRental.addActionListener(e -> {
           dm.rentVehicle(frame);
        });
        btnDisplayOngoing.addActionListener(e -> {
            dm.DisplayOngoing(frame);
        });
        btnDisplayAll.addActionListener(e -> {
            dm.displayAvailableVehicles(frame);
        });
        btnSearchRental.addActionListener(e -> {
            dm.searchRental();
        });
        btnReturn.addActionListener(e -> {
            dm.returnVehicle();
        });
        // Back button logic
        backButton.addActionListener(e -> {
            frame.dispose();             // Close current rental window
            mainFrame.setVisible(true);  // Show main menu again
        });

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        // Exit confirmation on window close
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    void SellMenu(JFrame mainFrame) 
    {
        dealingsManager dm = new dealingsManager();
       
        JFrame frame = new JFrame("Car Rental and Selling Facility - Sales");

        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50); // Equal gap between buttons

        // Header label
        JLabel sellLabel = new JLabel("Sales Control Panel", JLabel.CENTER);
        sellLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        sellLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 50, 10); // Top spacing
        mainPanel.add(sellLabel, gbc);

        // Create buttons
        JButton btnDisplaySales = createActionButton("Display All Sales");
        JButton btnSellVehicle = createActionButton("Sell a Vehicle");
        JButton btnDisplayAll = createActionButton("Available Vehicles");
        JButton backButton = createActionButton("Back to Main Menu");

        btnDisplayAll.addActionListener(e -> {
            dm.displayAvailableVehicles(frame);
        });
        btnSellVehicle.addActionListener(e -> {
            dm.sellVehicle(frame);
        });
        btnDisplaySales.addActionListener(e -> {
            dm.displaySoldVehicle(frame);
        });

        // Back button functionality
        backButton.addActionListener(e -> {
            frame.dispose();           // Close sales window
            mainFrame.setVisible(true); // Show main menu
        });

        // Add buttons to the panel
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 50);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(btnDisplaySales, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(btnSellVehicle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnDisplayAll, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(backButton, gbc);

        // Add to frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        // Confirm on close
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }


    private JButton createActionButton(String text) {
    JButton button = new JButton(text);
    Color normalColor = new Color(41, 128, 185); // Steel Blue
    Color hoverBorderColor = new Color(52, 152, 219); // Light Blue
    Color clickColor = new Color(241, 196, 15); // Gold

    styleButton(button, normalColor);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBorder(new LineBorder(hoverBorderColor, 2)); // Light blue border
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBorder(null); // Remove border when mouse exits
        }

        @Override
        public void mousePressed(MouseEvent e) {
            button.setBackground(clickColor); // Gold on click
            button.setOpaque(true);
            button.setBorder(new LineBorder(new Color(46, 204, 113), 2)); // Green border on press
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            button.setBackground(normalColor); // Return to normal color
        }
    });

    return button;
}

private void styleButton(JButton button, Color normalColor) {
    button.setPreferredSize(new Dimension(300, 90));
    button.setFont(new Font("Segoe UI", Font.BOLD, 22));
    button.setForeground(Color.WHITE);
    button.setBackground(normalColor);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorder(null);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}

    void showCustomerMenu()
    {

    }

}

class Menue {
    public static void main(String[] args) 
    {
        menueFrames mf = new menueFrames();
        mf.LoginGUI();
    }
}
