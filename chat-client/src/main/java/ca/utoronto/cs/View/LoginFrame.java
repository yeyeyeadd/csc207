package ca.utoronto.cs.View;

import ca.utoronto.cs.RequestManager;
import ca.utoronto.cs.message.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import io.netty.channel.Channel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class LoginFrame extends JFrame {
    private final RequestManager reqMgr;
    private JLabel user;
    private JLabel password;
    private JLabel role;
    private JTextField user_input;
    private JPasswordField password_input;
    private JTextField role_input;
    private JButton login_button;
    private JButton register_button;
    private JPanel panel;
    final int FRAME_HEIGHT=500;
    final int FRAME_WIDTH=500;


    public LoginFrame(RequestManager reqMgr) {
        this.reqMgr = reqMgr;

        panel = new JPanel();

        JLabel user = new JLabel("Username:");
        panel.add(user);

        JTextField user_input = new JTextField(20);
        panel.add(user_input);

        JLabel password = new JLabel("Password:");
        panel.add(password);

        JPasswordField password_input = new JPasswordField(20);
        panel.add(password_input);

        JButton register_button = new JButton("Register");
        panel.add(register_button);

        JButton login_button = new JButton("Login");
        panel.add(login_button);

        //Debug
        user_input.setText("admin");
        password_input.setText("admin");

        register_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.this.setVisible(false);
                JFrame register_frame = new registerFrame(reqMgr);
                register_frame.setVisible(true);

            }
        });

        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = user_input.getText();
                String pw = password_input.getText();
                LoginRequest req = new LoginRequest();
                req.username = id;
                req.password = pw;
                reqMgr.sendAsync(req)
                        .thenAccept((resp) ->
                        {JOptionPane.showMessageDialog(null,"Login Successfully");
                            LoginFrame.this.setVisible(false);
                            LoginResponse loginResp = (LoginResponse) resp;

                            if(loginResp.user.getRole().getValue() == 1) {
                                JFrame organizer = new OperatorFrame(reqMgr, user_input.getText());
                                organizer.setVisible(true);
                            } else if (loginResp.user.getRole().getValue() == 2 || loginResp.user.getRole().getValue() == 3) {
                                JFrame user = new UserFrame(reqMgr, loginResp.user);
                                user.setVisible(true);
                            } else if (loginResp.user.getRole().getValue() == 0) {
                                //ANONYMOUS
                            }

                        })
                        .exceptionally(err -> {
                            JOptionPane.showMessageDialog(null,"Login failed");
                            return null;
                        });

            }
        });
        this.setLayout(null);
        panel.setSize(250, 250);
        panel.setLocation((FRAME_HEIGHT - 250) / 2, (FRAME_WIDTH-250)/2) ;
        this.add(panel);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("User Login");

    }


}