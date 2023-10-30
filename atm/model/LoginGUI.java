import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginGUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        // Set the JFrame to appear in the center of the screen
        setLocationRelativeTo(null);

        setResizable(false);

        setLayout(new GridLayout(3, 2));
        
        usernameLabel = new JLabel("Account ID:");
        passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label as a placeholder
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                // Check the username and password
                if ("your_username".equals(username) && "your_password".equals(new String(password))) {
                    // Successful login, perform actions here
                    JOptionPane.showMessageDialog(null, "Login successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
                }
                // Clear the fields
                usernameField.setText("");
                passwordField.setText("");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}
