package ca.utoronto.cs.View;
import ca.utoronto.cs.RequestManager;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.message.Request;
import ca.utoronto.cs.message.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import io.netty.channel.Channel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class registerFrame extends JFrame {
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




    public registerFrame(RequestManager reqMgr) {
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

        JLabel role = new JLabel("Role:");
        panel.add(role);


        JTextField role_input = new JTextField(20);
        panel.add(role_input);

        JButton register_button = new JButton("Register");
        panel.add(register_button);

        JButton login_button = new JButton("Login");
        panel.add(login_button);

        register_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = user_input.getText();
                String pw = password_input.getText();
                Role role = Role.fromValue( Integer.valueOf(role_input.getText()));
                CreateUserAccountRequest  req = new CreateUserAccountRequest();
                req.username = id;
                req.password = pw;
                req.role = role;
                reqMgr.sendAsync(req)
                        .thenAccept((resp) ->
                        {JOptionPane.showMessageDialog(null,"Register Successfully");
                        registerFrame.this.setVisible(false);
                        Frame login= new LoginFrame(reqMgr);
                        login.setVisible(true);})

                        .exceptionally(err -> {
                            JOptionPane.showMessageDialog(null,"Register failed");
                            return null;
                        });

            }
        });

        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame.this.setVisible(false);
                Frame login= new LoginFrame(reqMgr);
                login.setVisible(true);
                }



        });



        this.setLayout(null);
        panel.setSize(250, 250);
        panel.setLocation((FRAME_HEIGHT - 250) / 2, (FRAME_WIDTH-250)/2) ;
        this.add(panel);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("User Register");




    }


}