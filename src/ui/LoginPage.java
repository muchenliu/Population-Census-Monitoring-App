package ui;

import controller.DataBaseConnectionHandler;

import javax.swing.*;

public class LoginPage extends JPanel{

    private JTextField loginName;
    private JPasswordField loginPassword;
    private JButton loginButton;

    public LoginPage(DataBaseConnectionHandler db) {
        JFrame loginFrame = new JFrame();
        loginFrame.setSize(300, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ends system when frame closed
        loginFrame.setTitle("Login");
        loginFrame.add(this);
        setLayout(null);
        JLabel loginNameLabel = new JLabel("User Name: ");
        JLabel loginPasswordLabel = new JLabel(("Password: "));
        loginNameLabel.setBounds(50, 50, 75, 20);
        loginPasswordLabel.setBounds(50, 100, 75, 20);
        loginName = new JTextField();
        loginName.setBounds(125, 50, 100, 20);
        loginPassword = new JPasswordField();
        loginPassword.setBounds(125, 100, 100, 20);
        loginPassword.setEchoChar('*');
        loginButton = new JButton("login");
        loginButton.setBounds(100, 150, 75, 20);
        loginButton.addActionListener(e -> {
            System.out.println("set up connection successful");
            boolean loginSucceeded = db.login(loginName.getText(), String.valueOf(loginPassword.getPassword()));
            if (loginSucceeded) {
                loginFrame.dispose();
                new WelcomePage(db); // TODO: go to main page, to be changed
            }
        });
        add(loginNameLabel);
        add(loginPasswordLabel);
        add(loginName);
        add(loginPassword);
        add(loginButton);
        repaint();
        loginFrame.setVisible(true);
    }
}
