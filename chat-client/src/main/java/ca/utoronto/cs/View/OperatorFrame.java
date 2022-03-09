package ca.utoronto.cs.View;
import ca.utoronto.cs.RequestManager;
import ca.utoronto.cs.entity.EventType;
import ca.utoronto.cs.entity.Role;
import ca.utoronto.cs.entity.Room;
import ca.utoronto.cs.exception.GenericResponseException;
import ca.utoronto.cs.message.*;
import ca.utoronto.cs.entity.Event;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

public class OperatorFrame extends JFrame {
    private final RequestManager reqMgr;
    private String username;

    private JPanel panel;
    private JPanel accountPanel;
    private JPanel eventPanel;
    private JPanel messagePanel;
    private JPanel roomPanel;
    private JPanel eventListPanel;

    private JTable eventTable;
    private JTable roomTable;

    final int FRAME_HEIGHT=650;
    final int FRAME_WIDTH=1030;

    public OperatorFrame(RequestManager reqMgr, String username){
        this.username = username;
        this.reqMgr = reqMgr;
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Log Out");
        menu.add(menuItem);

        panel = new JPanel();
        panel.setLayout(new GridLayout(2,3));
        accountPanel = new JPanel(new FlowLayout());
        JLabel userNameLabel = new JLabel("Username:");
        JTextField userNameInput = new JTextField(12);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField userPasswordInput = new JPasswordField(12);
        JComboBox<String> userTypeBox = new JComboBox<String>();
        userTypeBox.addItem("Operator");
        userTypeBox.addItem("User");
        userTypeBox.addItem("VIP User");
        userTypeBox.setSelectedIndex(0);
        JButton createUserButton = new JButton("Create User");
        JPanel accountInfoPanel = new JPanel(new GridLayout(2,2));
        accountInfoPanel.add(userNameLabel);
        accountInfoPanel.add(userNameInput);
        accountInfoPanel.add(passwordLabel);
        accountInfoPanel.add(userPasswordInput);
        accountPanel.add(accountInfoPanel);
        accountPanel.add(userTypeBox);
        accountPanel.add(createUserButton);

        roomPanel = new JPanel(new FlowLayout());
//        JLabel roomIDLabel = new JLabel("RoomId:");
//        JTextField roomIDInput = new JTextField(10);
        JLabel roomNameLabel = new JLabel("RoomName:");
        JTextField roomNameInput = new JTextField(10);
        JLabel roomCapacityLabel = new JLabel("Capacity:");
        JTextField roomCapacityInput = new JTextField(10);
        JButton addRoomButton = new JButton("Add Room");
        JButton deleteRoomButton = new JButton("Delete Room");

        Object[][] rowData = null;
        Object[] columnNames = { "RoomID", "RoomName", "Capacity" };
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableModel roomModel = new DefaultTableModel(rowData, columnNames);
        roomTable = new JTable(roomModel);
        roomTable.setPreferredScrollableViewportSize(new Dimension(250, 160));
        JScrollPane scrollPaneRoomTable = new JScrollPane(roomTable);
        getListAllRooms();
        roomTable.setDefaultRenderer(Object.class, r);
        JPanel roomGridPanel = new JPanel(new GridLayout(3,2));
        roomGridPanel.add(roomNameLabel);
        roomGridPanel.add(roomNameInput);
        roomGridPanel.add(roomCapacityLabel);
        roomGridPanel.add(roomCapacityInput);
        roomGridPanel.add(addRoomButton);
        roomGridPanel.add(deleteRoomButton);
        roomPanel.add(roomGridPanel);
        roomPanel.add(roomTable.getTableHeader(), BorderLayout.NORTH);
        roomPanel.add(scrollPaneRoomTable, BorderLayout.CENTER);

        messagePanel = new JPanel();
        JPanel messageGridPanel = new JPanel(new GridLayout(1,2));
        JLabel messageLabel = new JLabel("Message");
        JTextArea  messageInput = new JTextArea (8,20);
        JComboBox<String> messageSendBox = new JComboBox<>();
        messageSendBox.addItem("Send To All Speaker");
        messageSendBox.addItem("Send To All Attendees");
        messageSendBox.addItem("Send To Specific Event");
        messageSendBox.addItem("Send To Specific Speaker");
        messageSendBox.setSelectedIndex(0);

        JLabel eventIDLabel = new JLabel("EventID:");
        JTextField eventIDInput = new JTextField(12);
        eventIDInput.setEnabled(false);

        JButton sendButton = new JButton("Send");
        messagePanel.add(messageLabel);
        messagePanel.add(messageInput);
        messagePanel.add(messageSendBox);
        messageGridPanel.add(eventIDLabel);
        messageGridPanel.add(eventIDInput);
        messagePanel.add(messageGridPanel);
        messagePanel.add(sendButton);

        eventPanel = new JPanel();
        JPanel eventGridPanel = new JPanel(new GridLayout(7,2));
        JLabel eventNameLabel = new JLabel("EventName:");
        JTextField eventNameInput = new JTextField(10);
        JLabel speakerIDLabel = new JLabel("SpeakerID:");
        JTextField speakerIDInput = new JTextField(10);
        JLabel roomLabel = new JLabel("RoomName:");
        JTextField roomInput = new JTextField(10);
        JLabel startTimeLabel = new JLabel("StartTime:");
        JTextField startTimeInput = new JTextField(12);
        startTimeInput.setText(dateToString(new Date()));
        JLabel endTimeLabel = new JLabel("EndTime:");
        JTextField endTimeInput = new JTextField(12);
        endTimeInput.setText(dateToString(new Date()));
        JLabel capacityLabel = new JLabel("Capacity");
        JTextField capacityInput = new JTextField(10);
        JLabel eventTypeLabel = new JLabel("EventType:");
        JComboBox<String> eventTypeBox = new JComboBox<String>();
        eventTypeBox.addItem("SOLO");
        eventTypeBox.addItem("MULTIPLE");
        eventTypeBox.addItem("NONE");
        eventTypeBox.addItem("VIPEVENT");
        eventTypeBox.setSelectedIndex(2);
        JButton scheduleButton = new JButton("Schedule");

        eventGridPanel.add(speakerIDLabel);
        eventGridPanel.add(speakerIDInput);
        eventGridPanel.add(eventNameLabel);
        eventGridPanel.add(eventNameInput);
        eventGridPanel.add(roomLabel);
        eventGridPanel.add(roomInput);
        eventGridPanel.add(startTimeLabel);
        eventGridPanel.add(startTimeInput);
        eventGridPanel.add(endTimeLabel);
        eventGridPanel.add(endTimeInput);
        eventGridPanel.add(capacityLabel);
        eventGridPanel.add(capacityInput);
        eventGridPanel.add(eventTypeLabel);
        eventGridPanel.add(eventTypeBox);
        eventPanel.add(eventGridPanel);
        eventPanel.add(scheduleButton);

        eventListPanel = new JPanel();
        JButton refreshEventsButton = new JButton("Refresh");
        JButton deleteEventButton = new JButton("Delete");
        JButton modifyCapacityButton = new JButton("Modify Capacity");
        JButton changeSpeakerButton = new JButton("Change Speaker");
        JTextField changeSpeakerInput = new JTextField(10);
        Object[][] rowEvent = null;
        Object[] eventColumn = { "EventID", "EventName", "RoomID", "SpeakerID","MaxAttendee"};
        DefaultTableModel eventModel = new DefaultTableModel(rowEvent, eventColumn);
        eventTable = new JTable(eventModel);
        eventTable.setPreferredScrollableViewportSize(new Dimension(330, 180));
        JScrollPane scrollPaneEventTable = new JScrollPane(eventTable);
        eventTable.setDefaultRenderer(Object.class, r);
        eventListPanel.add(refreshEventsButton);
        eventListPanel.add(deleteEventButton);
        eventListPanel.add(modifyCapacityButton);
        eventListPanel.add(changeSpeakerButton);
        eventListPanel.add(changeSpeakerInput);
        eventListPanel.add(eventTable.getTableHeader(), BorderLayout.NORTH);
        eventListPanel.add(scrollPaneEventTable, BorderLayout.CENTER);
        getListAllEvents();

        panel.add(accountPanel);
        panel.add(eventPanel);
        panel.add(messagePanel);
        panel.add(roomPanel);
        panel.add(eventListPanel);

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userNameInput.getText();
                String password = userPasswordInput.getText();
                if(username.equals("")||password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Username or password Can not be null", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                Role role = Role.fromValue(userTypeBox.getSelectedIndex()+1);
                CreateUserAccountRequest  req = new CreateUserAccountRequest();
                req.username = username;
                req.password = password;
                req.role = role;
                reqMgr.sendAsync(req)
                        .thenAccept((resp) ->
                        {
                            if(userTypeBox.getSelectedIndex() == 0)
                                JOptionPane.showMessageDialog(null,"Create operator Successfully");
                            if(userTypeBox.getSelectedIndex() == 1)
                                JOptionPane.showMessageDialog(null,"Create user Successfully");
                            if(userTypeBox.getSelectedIndex() == 2)
                                JOptionPane.showMessageDialog(null,"Create VIP user Successfully");
                        })
                        .exceptionally(err -> {
                            JOptionPane.showMessageDialog(null,"Create User failed");
                            return null;
                        });

            }
        });

        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateRoomRequest createRoomReq = new CreateRoomRequest();
                if(roomNameInput.getText().equals("")||roomCapacityInput.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Values can not be null", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                createRoomReq.name = roomNameInput.getText();
                createRoomReq.capacity = Integer.parseInt(roomCapacityInput.getText());

                reqMgr.sendAsync(createRoomReq)
                        .thenAccept((resp) ->
                        {JOptionPane.showMessageDialog(null,"Add room Successfully");
                            getListAllRooms();
                        })
                        .exceptionally(err -> {
                            GenericResponseException exception = (GenericResponseException) err;
                            JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                            return null;
                        });

            }
        });

        deleteRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteRoomRequest deleteRoomRequest = new DeleteRoomRequest();
                int index = roomTable.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Must select a room!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                deleteRoomRequest.roomId = (int) roomTable.getValueAt(index, 0);
                reqMgr.sendAsync(deleteRoomRequest)
                        .thenAccept((resp) ->
                        {
                            JOptionPane.showMessageDialog(null,"Delete room Successfully");
                        })
                        .exceptionally(err -> {
                            GenericResponseException exception = (GenericResponseException) err;
                            JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                            return null;
                        });
                getListAllRooms();
            }
        });
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(capacityInput.getText().equals("")||roomInput.getText().equals("")||speakerIDInput.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Values can not be null", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                CreateEventRequest createEventReq = new CreateEventRequest();
                createEventReq.name = eventNameInput.getText();
                createEventReq.capacity = Integer.parseInt(capacityInput.getText());
                createEventReq.room = roomInput.getText();
                createEventReq.startTime = startTimeInput.getText();
                createEventReq.endTime = endTimeInput.getText();
                createEventReq.speaker = Arrays.asList(speakerIDInput.getText().split(","));
                createEventReq.type = EventType.fromValue(getSelectedEventType((String) eventTypeBox.getSelectedItem()));
                reqMgr.sendAsync(createEventReq)
                        .thenAccept((resp) ->
                        {
                            JOptionPane.showMessageDialog(null,"Schedule event Successfully");
                            getListAllEvents();
                        })
                        .exceptionally(err -> {
                            GenericResponseException exception = (GenericResponseException) err;
                            JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                            return null;
                        });

            }
        });

        messageSendBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = messageSendBox.getSelectedIndex();
                if (index == 0) {
                    eventIDInput.setEnabled(false);
                } else {
                    eventIDInput.setEnabled(true);
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(messageInput.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Pleas input message.", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int index = messageSendBox.getSelectedIndex();
                if(index == 0) {
                    MessageAllSpeakerRequest allSpeakerReq = new MessageAllSpeakerRequest();
                    allSpeakerReq.message = messageInput.getText();
                    reqMgr.sendAsync(allSpeakerReq)
                            .thenAccept((resp) ->
                            {
                                JOptionPane.showMessageDialog(null,"Message sent to all speakers Successfully");
                            })
                            .exceptionally(err -> {
                                GenericResponseException exception = (GenericResponseException) err;
                                JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                                return null;
                            });
                } else if (index == 1) {
                    if(eventIDInput.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter event ID.", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    BroadcastAudiencesRequest allAttendeesReq = new BroadcastAudiencesRequest();
                    allAttendeesReq.content = messageInput.getText();
                    allAttendeesReq.eventId = Integer.parseInt(eventIDInput.getText());
                    reqMgr.sendAsync(allAttendeesReq)
                            .thenAccept((resp) ->
                            {
                                JOptionPane.showMessageDialog(null,"Message sent to all attendees Successfully");
                            })
                            .exceptionally(err -> {
                                GenericResponseException exception = (GenericResponseException) err;
                                JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                                return null;
                            });
                } else if (index == 2) {
                    if(eventIDInput.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter event ID.", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    MessageEventRequest specificEventReq = new MessageEventRequest();
                    specificEventReq.message = messageInput.getText();
                    specificEventReq.eventId = Integer.parseInt(eventIDInput.getText());
                    reqMgr.sendAsync(specificEventReq)
                            .thenAccept((resp) ->
                            {
                                JOptionPane.showMessageDialog(null,"Message sent to specific event Successfully");
                            })
                            .exceptionally(err -> {
                                GenericResponseException exception = (GenericResponseException) err;
                                JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                                return null;
                            });
                } else if (index == 3) {
                    if(eventIDInput.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter event ID.", "CheckInput", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    MessageSpeakerRequest specificSpeakerReq = new MessageSpeakerRequest();
                    specificSpeakerReq.content = messageInput.getText();
                    specificSpeakerReq.eventId = Integer.parseInt(eventIDInput.getText());
                    reqMgr.sendAsync(specificSpeakerReq)
                            .thenAccept((resp) ->
                            {
                                JOptionPane.showMessageDialog(null,"Message sent to specific speaker Successfully");
                            })
                            .exceptionally(err -> {
                                GenericResponseException exception = (GenericResponseException) err;
                                JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                                return null;
                            });
                }


            }
        });

        refreshEventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getListAllEvents();
            }
        });

        deleteEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteEventRequest deleteEventReq = new DeleteEventRequest();
                int index = eventTable.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Must select a event!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                deleteEventReq.eventId = (int) eventTable.getValueAt(index, 0);
                reqMgr.sendAsync(deleteEventReq).thenAccept((resp) -> {
                    JOptionPane.showMessageDialog(null, "Delete event Successfully");
                    getListAllEvents();
                }).exceptionally(err -> {
                    GenericResponseException exception = (GenericResponseException) err;
                    JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                    return null;
                });
            }
        });

        modifyCapacityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeEventCapacityRequest changeEventCapacityRequest = new ChangeEventCapacityRequest();
                int index = eventTable.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Must select a event!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                changeEventCapacityRequest.eventid = (int) eventTable.getValueAt(index, 0);
                changeEventCapacityRequest.capacity = Integer.parseInt((String) eventTable.getValueAt(index, 4));
                reqMgr.sendAsync(changeEventCapacityRequest).thenAccept((resp) -> {
                    JOptionPane.showMessageDialog(null, "Modify capacity Successfully");
                    getListAllEvents();
                }).exceptionally(err -> {
                    GenericResponseException exception = (GenericResponseException) err;
                    JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                    return null;
                });
            }
        });

        changeSpeakerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeSpeakerRequest changeSpeakerReq = new ChangeSpeakerRequest();
                int index = eventTable.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Must select a event to change!", "Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                changeSpeakerReq.eventId = (int) eventTable.getValueAt(index, 0);
                changeSpeakerReq.speakerName = Arrays.asList(changeSpeakerInput.getText().split(","));
                reqMgr.sendAsync(changeSpeakerReq).thenAccept((resp) -> {
                    JOptionPane.showMessageDialog(null, "Change speaker Successfully");
                    getListAllEvents();
                }).exceptionally(err -> {
                    GenericResponseException exception = (GenericResponseException) err;
                    JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
                    return null;
                });
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
        this.add(panel, BorderLayout.CENTER);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Oraganizer Homepage - " + username);
    }

    private void getListAllEvents() {
        ListEventsRequest listEventsReq = new ListEventsRequest();

        reqMgr.sendAsync(listEventsReq).thenAccept((resp) -> {

            ListEventsResponse eventListResp = (ListEventsResponse) resp;
            ((DefaultTableModel)eventTable.getModel()).getDataVector().clear();
            for (int i = 0; i < eventListResp.events.size(); i++) {
                Event event = eventListResp.events.get(i);
                ((DefaultTableModel) eventTable.getModel())
                        .addRow(new Object[] { event.getId(), event.getName(), event.getRoomId(), event.getSpeakers(), String.valueOf(event.getCapacity()) });
            }
            eventTable.repaint();
        }).exceptionally(err -> {
            GenericResponseException exception = (GenericResponseException) err;
            JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
            return null;
        });
    }

    private void getListAllRooms() {
        ListRoomsRequest listRoomsReq = new ListRoomsRequest();

        reqMgr.sendAsync(listRoomsReq).thenAccept((resp) -> {

            ListRoomsResponse roomListResp = (ListRoomsResponse) resp;
            ((DefaultTableModel)roomTable.getModel()).getDataVector().clear();
            for (int i = 0; i < roomListResp.rooms.size(); i++) {
                Room room = roomListResp.rooms.get(i);
                    ((DefaultTableModel) roomTable.getModel())
                            .addRow(new Object[] { room.getId(), room.getName(), room.getCapacity() });
            }
            roomTable.repaint();
        }).exceptionally(err -> {
            GenericResponseException exception = (GenericResponseException) err;
            JOptionPane.showMessageDialog(null,((UnknownResponse)exception.getResponse()).content);
            return null;
        });
    }

    private int getSelectedEventType(String eventType) {
        switch (eventType) {
            case "SOLO":
                return 0;
            case "MULTIPLE":
                return 1;
            case "NONE":
                return 2;
            case "VIPEVENT":
                return 3;
        }
        return 0;
    }

    private String dateToString(Date date) {
        SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String time = sformat.format(date);
        return time;
    }
}

