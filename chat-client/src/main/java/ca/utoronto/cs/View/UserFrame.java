package ca.utoronto.cs.View;

import ca.utoronto.cs.RequestManager;
import ca.utoronto.cs.entity.*;
import ca.utoronto.cs.entity.Event;
import ca.utoronto.cs.exception.GenericResponseException;
import ca.utoronto.cs.message.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserFrame extends JFrame {
    private final RequestManager reqMgr;

    private String username;
    private User user;
    private JPanel panel;
    private JPanel eventListPanel;
    private JPanel talkListPanel;
    private JPanel messagePanel;
    private JPanel chatPanel;

    private JTable eventTable;
    private JTable talkTable;

    private JTextArea receiveMessage;
    private JCheckBox attendeeCheckBox;
    final int FRAME_HEIGHT = 700;
    final int FRAME_WIDTH = 900;

    private List<Event> listEvents = new ArrayList<>();
    public UserFrame(RequestManager reqMgr, User user) {
        this.user = user;
        this.reqMgr = reqMgr;
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Log Out");
        menu.add(menuItem);

        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        eventListPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        JButton signUpButton = new JButton("SignUp");

        Object[][] rowEvent = null;
        Object[] eventColumn = { "EventID", "EventName", "RoomID", "SpeakerID", "Type" };
        DefaultTableModel eventModel = new DefaultTableModel(rowEvent, eventColumn);
        eventTable = new JTable(eventModel);
        eventListPanel.add(refreshButton);
        eventListPanel.add(signUpButton);
        eventListPanel.add(eventTable.getTableHeader(), BorderLayout.NORTH);
        eventListPanel.add(eventTable, BorderLayout.CENTER);
        eventListPanel.add(eventTable);
        this.getListAllEvents();

        talkListPanel = new JPanel();
        Object[][] rowTalk = null;
        Object[] talkColumn = { "EventID", "RoomID", "SpeakerID", "StatTime", "EndTime" };
        DefaultTableModel talkModel = new DefaultTableModel(rowTalk, talkColumn);
        JButton refreshTalkButton = new JButton("Refresh");
        JButton cancelButton = new JButton("Cancel");
        talkTable = new JTable(talkModel);
        talkListPanel.add(refreshTalkButton);
        talkListPanel.add(cancelButton);
        talkListPanel.add(talkTable.getTableHeader(), BorderLayout.NORTH);
        talkListPanel.add(talkTable, BorderLayout.CENTER);
        talkListPanel.add(talkTable);

        messagePanel = new JPanel();
        JLabel messageLabel = new JLabel("Message");
        JTextArea messageInput = new JTextArea(15, 30);
        JLabel receiverLabel = new JLabel("ReceiverName");
        JTextField receiverInput = new JTextField(10);
        attendeeCheckBox = new JCheckBox("All attendees");
        attendeeCheckBox.setEnabled(false);
        JButton sendButton = new JButton("Send");

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setViewportView(messageInput);

        messagePanel.add(messageLabel);
        messagePanel.add(scrollPane_2);
        messagePanel.add(receiverLabel);
        messagePanel.add(receiverInput);
        messagePanel.add(attendeeCheckBox);
        messagePanel.add(sendButton);

        chatPanel = new JPanel();
        JLabel chatRoomLabel = new JLabel("Chat:");
        JScrollPane scrollPane_1 = new JScrollPane();
        receiveMessage = new JTextArea(15, 30);
        scrollPane_1.setViewportView(receiveMessage);
        receiveMessage.setLineWrap(true);

        chatPanel.add(chatRoomLabel);
        chatPanel.add(scrollPane_1);
        panel.add(eventListPanel);
        panel.add(talkListPanel);
        panel.add(messagePanel);
        panel.add(chatPanel);

        this.add(panel, BorderLayout.CENTER);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("User Homepage - " + user.getUsername() + " ID: " + user.getId());

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getListAllEvents();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JoinEventRequest joinEventReq = new JoinEventRequest();
                int index = eventTable.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Must select a event!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                joinEventReq.eventId = (int) eventTable.getValueAt(index, 0);
                reqMgr.sendAsync(joinEventReq).thenAccept((resp) -> {

                    JOptionPane.showMessageDialog(null, "Join event Successfully");
                    getListAllEvents();

                }).exceptionally(err -> {
                    GenericResponseException exception = (GenericResponseException) err;
                    JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                    return null;
                });
            }
        });

        refreshTalkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getListAllEvents();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LeaveEventRequest leaveEventReq = new LeaveEventRequest();
                int index = talkTable.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Must select a talk!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                List<Integer>listSpeaker = (List<Integer>) talkTable.getValueAt(index, 2);
                if(listSpeaker.contains(user.getId())) {
                    JOptionPane.showMessageDialog(null, "You can't' cancel the event, because you are the speaker!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                leaveEventReq.eventId = (int) talkTable.getValueAt(index, 0);
                reqMgr.sendAsync(leaveEventReq).thenAccept((resp) -> {
                    JOptionPane.showMessageDialog(null, "Leave event Successfully");
                    getListAllEvents();
                }).exceptionally(err -> {
                    GenericResponseException exception = (GenericResponseException) err;
                    JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                    return null;
                });
            }
        });

        attendeeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (attendeeCheckBox.isSelected()) {
                    receiverInput.setEnabled(false);
                } else {
                    receiverInput.setEnabled(true);
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (messageInput.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "No message to send", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (receiverInput.getText().equals("") && attendeeCheckBox.isSelected() == false) {
                    JOptionPane.showMessageDialog(null, "Check input", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if(attendeeCheckBox.isSelected()) {
                    BroadcastAudiencesRequest toAllAttendeesReq = new BroadcastAudiencesRequest();
                    toAllAttendeesReq.content = messageInput.getText();
                    toAllAttendeesReq.eventId = getEventByUserID();
                    reqMgr.sendAsync(toAllAttendeesReq)
                            .thenAccept((resp) ->
                            {
                                receiveMessage.append(user.getUsername() + ":" + messageInput.getText());
                                receiveMessage.append("\n");
                            })
                            .exceptionally(err -> {
                                GenericResponseException exception = (GenericResponseException) err;
                                JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                                return null;
                            });
                } else {
                    MessageFriendRequest messageFriendRequest = new MessageFriendRequest();
                    messageFriendRequest.msg = messageInput.getText();
                    messageFriendRequest.receiver = receiverInput.getText();
                    reqMgr.sendAsync(messageFriendRequest)
                            .thenAccept((resp) ->
                            {
                                receiveMessage.append(user.getUsername() + ":" + messageInput.getText());
                                receiveMessage.append("\n");
                            })
                            .exceptionally(err -> {
                                GenericResponseException exception = (GenericResponseException) err;
                                JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                                return null;
                            });
                }

            }
        });

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame login= new LoginFrame(reqMgr);
                setVisible(false);
                login.setVisible(true);
            }
        });

        reqMgr.subscribe(MessageResponse.class).subscribe(
                resp ->handleMessageResponse(resp.content)
        );

    }

    private void handleMessageResponse(Message content) {
        receiveMessage.append(content.toString());
        receiveMessage.append("\r\n");
        System.out.println(String.format("Message: %s", content.toString()));
    }

    private void getListAllEvents() {
        ListEventsRequest listEventsReq = new ListEventsRequest();

        reqMgr.sendAsync(listEventsReq).thenAccept((resp) -> {

            ListEventsResponse eventListResp = (ListEventsResponse) resp;
            ((DefaultTableModel)eventTable.getModel()).getDataVector().clear();
            ((DefaultTableModel)talkTable.getModel()).getDataVector().clear();
            listEvents = new ArrayList<>();
            for (int i = 0; i < eventListResp.events.size(); i++) {
                Event event = eventListResp.events.get(i);
                listEvents.add(event);
                if (event.getType().equals(EventType.VIPEVENT)) {
                    if(user.getRole().equals(Role.VIP) || event.getSpeakers().contains(user.getId())) {
                        ((DefaultTableModel) eventTable.getModel())
                                .addRow(new Object[] { event.getId(), event.getName(), event.getRoomId(), event.getSpeakers(), event.getType() });
                    }
                } else {
                    ((DefaultTableModel) eventTable.getModel())
                                .addRow(new Object[] { event.getId(), event.getName(), event.getRoomId(), event.getSpeakers(), event.getType() });
                }
                for (int j = 0; j < event.getSpeakers().size(); j++) {
                    if (event.getSpeakers().get(j) == user.getId()) {
                        attendeeCheckBox.setEnabled(true);
                    }
                    if (user.getId() == event.getSpeakers().get(j)) {
                        ((DefaultTableModel) talkTable.getModel())
                                .addRow(new Object[] { event.getId(), event.getRoomId(), event.getSpeakers(), dateToString(event.getStart()), dateToString(event.getEnd()) });
                    }
                }
                for (int j = 0; j < event.getParticipants().size(); j++) {
                    if (user.getId() == event.getParticipants().get(j)) {
                        ((DefaultTableModel) talkTable.getModel())
                                .addRow(new Object[] { event.getId(), event.getRoomId(), event.getSpeakers(), dateToString(event.getStart()), dateToString(event.getEnd()) });
                    }
                }
            }
            eventTable.repaint();
            talkTable.repaint();
        }).exceptionally(err -> {
            GenericResponseException exception = (GenericResponseException) err;
            JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
            return null;
        });
    }

    private void getListAllTalks() {
        getListAllEvents();
        ((DefaultTableModel)talkTable.getModel()).getDataVector().clear();
        for (int i = 0; i < listEvents.size(); i++) {
            Event event = listEvents.get(i);
            for (int j = 0; j < event.getSpeakers().size(); j++) {
                if (user.getId() == event.getSpeakers().get(j)) {
                    ((DefaultTableModel) talkTable.getModel())
                            .addRow(new Object[] { event.getId(), event.getRoomId(), event.getSpeakers(), dateToString(event.getStart()), dateToString(event.getEnd()) });
                }
            }
            for (int j = 0; j < event.getParticipants().size(); j++) {
                if (user.getId() == event.getParticipants().get(j)) {
                    ((DefaultTableModel) talkTable.getModel())
                            .addRow(new Object[] { event.getId(), event.getRoomId(), event.getSpeakers(), dateToString(event.getStart()), dateToString(event.getEnd()) });
                }
            }
        }
        talkTable.repaint();
    }

    private int getEventByUserID() {
        getListAllEvents();
        int eventID = 0;
        for (int i = 0; i < listEvents.size(); i++) {
            for (int j = 0; j < listEvents.get(i).getSpeakers().size(); j++) {
                if (user.getId() == listEvents.get(i).getSpeakers().get(j)) {
                    eventID = listEvents.get(i).getId();
                }
            }
        }
        return eventID;
    }

    private String dateToString(Date date) {
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String time = sformat.format(date);
        return time;
    }
}
