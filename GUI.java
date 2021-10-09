import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GUI extends JFrame {
    private final ClientHandler clientHandler;

    public GUI() {
        setTitle("CP372 Assignment 1 - Group 10");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        clientHandler = new ClientHandler();
        initComponents();
    }

    private void btnDisconnectHandler(ActionEvent e) {
        try {
            btnDisconnect.setEnabled(false);
            clientHandler.disconnect();
            connectDialog();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void comboBoxRequestsHandler(ActionEvent e) {
        if (comboBoxRequests.getSelectedItem() == Request.GET) {
            checkboxAll.setEnabled(true);
            checkboxBibtex.setEnabled(true);
        } else {
            checkboxAll.setEnabled(false);
            checkboxAll.setSelected(false);
            checkboxBibtex.setEnabled(false);
            checkboxBibtex.setSelected(false);
            checkboxAllHandler(e);
        }
    }

    private void checkboxAllHandler(ActionEvent e) {
        txtSTATUS.setEnabled(!checkboxAll.isSelected());
        txtMESSAGE.setEnabled(!checkboxAll.isSelected());
        txtCOLOR.setEnabled(!checkboxAll.isSelected());
        txtWIDTH.setEnabled(!checkboxAll.isSelected());
        txtHEIGHT.setEnabled(!checkboxAll.isSelected());
        txtCoordinateX.setEnabled(!checkboxAll.isSelected());
        txtCoordinateY.setEnabled(!checkboxAll.isSelected());
    }

    private void btnClearFieldsHandler(ActionEvent e) {
        txtSTATUS.setText("");
        txtMESSAGE.setText("");
        txtCOLOR.setText("");
        txtHEIGHT.setText("");
        txtWIDTH.setText("");
        txtCoordinateX.setText("");
        txtCoordinateY.setText("");
    }

    private void btnClearOutputHandler(ActionEvent e) {
        txtOutput.setText("");
    }

    private void btnSubmitHandler(ActionEvent e) {
        if (clientHandler.isConnected()) {
            try {
                String STATUS;
                // Handle Title
                String MESSAGE = txtMESSAGE.getText().trim();
                // Handle Author
                String COLOR = txtCOLOR.getText().trim();
                // Handle Year
                Double HEIGHT = 0.0;
                if (txtHEIGHT.getText().length() > 0.0)
                    try {
                        HEIGHT = Double.parseDouble(txtHEIGHT.getText());
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(this, "Invalid Height", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                Double WIDTH = 0.0;
                if (txtWIDTH.getText().length() > 0.0)
                    try {
                        WIDTH = Double.parseDouble(txtWIDTH.getText());
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(this, "Invalid Width", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                Double COORDINATELLC = 0.0;
                if (txtCoordinateX.getText().length() > 0.0)
                    try {
                        COORDINATELLC = Double.parseDouble(txtCoordinateX.getText());
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(this, "Invalid Coordinate X", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                // Handle All Get request
                if (comboBoxRequests.getSelectedItem() == Request.GET && checkboxAll.isSelected()) {
                    txtOutput.setText(clientHandler.sendMessage(Request.GET, "", 0.0, 0.0, 0.0, "", ""));
                    return;
                }

                STATUS = txtSTATUS.getText().replace("-", "").trim();

                if (comboBoxRequests.getSelectedItem() == Request.POST)
                    if (STATUS.length() == 0) {
                        JOptionPane.showMessageDialog(this, "Please enter a Status (Pinned/Unpinned)", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                // Check if GET request and empty fields
                if (comboBoxRequests.getSelectedItem() == Request.GET
                        || comboBoxRequests.getSelectedItem() == Request.CLEAR)
                    if (STATUS.length() == 0 && MESSAGE.length() == 0 && COLOR.length() == 0 && HEIGHT == 0
                            && WIDTH == 0 && COORDINATELLC == 0) {
                        JOptionPane.showMessageDialog(this, "Please enter a field to search ", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                if (comboBoxRequests.getSelectedItem() == Request.CLEAR) {
                    int response = JOptionPane.showConfirmDialog(this, "Board Cleared Confirmation");
                    if (response == JOptionPane.NO_OPTION || response == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }

                if (STATUS.equalsIgnoreCase("Pinned") || STATUS.equalsIgnoreCase("Unpinned")) {
                    txtOutput.setText(clientHandler.sendMessage((Request) comboBoxRequests.getSelectedItem(), STATUS,
                            COORDINATELLC, HEIGHT, WIDTH, WIDTH, COLOR, MESSAGE));
                } else
                    JOptionPane.showMessageDialog(this, "Invalid Status", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            connectDialog();
        }
    }

    private void connectDialog() {
        txtIPAddress = new JTextField("127.0.0.1"); // TODO Remove default
        txtPort = new JTextField("3000"); // TODO Remove default
        Object[] fields = { "IP Address", txtIPAddress, "Port Number", txtPort };
        Object[] options = { "Connect", "Exit" };
        int option = JOptionPane.showOptionDialog(this, fields, "Connect to Server", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (option) {
            case JOptionPane.YES_OPTION:
                try {
                    clientHandler.connect(txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
                    btnDisconnect.setEnabled(true);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(this, "Invalid Port Number", "Error", JOptionPane.ERROR_MESSAGE);
                    connectDialog();
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this, "Unable to connect please check IP/Port", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    connectDialog();
                }
                break;
            case JOptionPane.NO_OPTION:
                System.exit(0);
                break;
        }
    }

    private void initComponents() {
        panelParent = new JPanel();
        panelParent.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelParent);
        panelParent.setLayout(new BorderLayout(5, 5));

        panelHeader = new JPanel();
        panelHeader.setBorder(new EmptyBorder(5, 10, 20, 10));
        panelParent.add(panelHeader, BorderLayout.NORTH);
        panelHeader.setLayout(new BorderLayout(0, 0));

        lblTitle = new JLabel("CP372 Assignment 1 - Group 10");
        lblTitle.setFont(new Font("Serif", Font.PLAIN, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelHeader.add(lblTitle, BorderLayout.CENTER);

        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.addActionListener(this::btnDisconnectHandler);
        btnDisconnect.setEnabled(false);
        panelHeader.add(btnDisconnect, BorderLayout.EAST);

        panelContent = new JPanel();
        panelParent.add(panelContent, BorderLayout.CENTER);
        panelContent.setLayout(new GridLayout(1, 0, 0, 0));

        panelLeft = new JPanel();
        panelLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelContent.add(panelLeft);
        panelLeft.setLayout(new BorderLayout(0, 0));

        panelRequest = new JPanel();
        panelRequest.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelLeft.add(panelRequest, BorderLayout.NORTH);
        panelRequest.setLayout(new GridLayout(1, 0, 0, 0));

        lblRequest = new JLabel("Request:");
        panelRequest.add(lblRequest);

        comboBoxRequests = new JComboBox<>(Request.values());
        comboBoxRequests.addActionListener(this::comboBoxRequestsHandler);
        panelRequest.add(comboBoxRequests);

        panelFields = new JPanel();
        panelFields.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelLeft.add(panelFields, BorderLayout.CENTER);
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.Y_AXIS));

        panelSTATUS = new JPanel();
        panelFields.add(panelSTATUS);
        panelSTATUS.setLayout(new GridLayout(2, 0, 0, 0));

        lblSTATUS = new JLabel("STATUS:");
        panelSTATUS.add(lblSTATUS);

        txtSTATUS = new JTextField();
        panelSTATUS.add(txtSTATUS);
        txtSTATUS.setColumns(10);

        panelMESSAGE = new JPanel();
        panelFields.add(panelMESSAGE);
        panelMESSAGE.setLayout(new GridLayout(2, 0, 0, 0));

        lblMESSAGE = new JLabel("MESSAGE:");
        panelMESSAGE.add(lblMESSAGE);

        txtMESSAGE = new JTextField();
        panelMESSAGE.add(txtMESSAGE);
        txtMESSAGE.setColumns(10);

        panelCOLOR = new JPanel();
        panelFields.add(panelCOLOR);
        panelCOLOR.setLayout(new GridLayout(2, 0, 0, 0));

        lblCOLOR = new JLabel("COLOR:");
        panelCOLOR.add(lblCOLOR);

        txtCOLOR = new JTextField();
        panelCOLOR.add(txtCOLOR);
        txtCOLOR.setColumns(10);

        panelHEIGHT = new JPanel();
        panelFields.add(panelHEIGHT);
        panelHEIGHT.setLayout(new GridLayout(2, 0, 0, 0));

        lblHEIGHT = new JLabel("HEIGHT:");
        panelHEIGHT.add(lblHEIGHT);

        txtHEIGHT = new JTextField();
        panelHEIGHT.add(txtHEIGHT);
        txtHEIGHT.setColumns(10);

        panelWIDTH = new JPanel();
        panelFields.add(panelWIDTH);
        panelWIDTH.setLayout(new GridLayout(2, 0, 0, 0));

        lblWIDTH = new JLabel("WIDTH:");
        panelWIDTH.add(lblWIDTH);

        txtWIDTH = new JTextField();
        panelWIDTH.add(txtWIDTH);
        txtWIDTH.setColumns(10);

        panelCOORDINATELLC = new JPanel();
        panelFields.add(panelCOORDINATELLC);
        panelCOORDINATELLC.setLayout(new GridLayout(2, 0, 0, 0));

        lblCoordinateX = new JLabel("Coordinate X:");
        panelCoordinateX.add(lblCoordinateX);

        txtCoordinateX = new JTextField();
        panelCoordinateX.add(txtCoordinateX);
        txtCoordinateX.setColumns(10);

        panelCoordinateY = new JPanel();
        panelFields.add(panelCoordinateY);
        panelCoordinateY.setLayout(new GridLayout(2, 0, 0, 0));

        lblCoordinateY = new JLabel("Coordinate Y:");
        panelCoordinateY.add(lblCoordinateY);

        txtCoordinateY = new JTextField();
        panelCoordinateY.add(txtCoordinateY);
        txtCoordinateY.setColumns(10);

        panelExtra = new JPanel();
        FlowLayout fl_panelExtra = (FlowLayout) panelExtra.getLayout();
        fl_panelExtra.setAlignment(FlowLayout.RIGHT);
        panelFields.add(panelExtra);

        checkboxAll = new JCheckBox("All");
        checkboxAll.setEnabled(false);
        checkboxAll.addActionListener(this::checkboxAllHandler);
        panelExtra.add(checkboxAll);

        // checkboxBibtex = new JCheckBox("Bibtex");
        // checkboxBibtex.setEnabled(false);
        // panelExtra.add(checkboxBibtex);

        panelButtons = new JPanel();
        panelLeft.add(panelButtons, BorderLayout.SOUTH);
        panelButtons.setLayout(new GridLayout(1, 0, 0, 0));

        panelClear = new JPanel();
        panelButtons.add(panelClear);
        panelClear.setLayout(new GridLayout(1, 0, 0, 0));

        btnClearFields = new JButton("Clear Fields");
        btnClearFields.addActionListener(this::btnClearFieldsHandler);
        panelClear.add(btnClearFields);

        btnClearOutput = new JButton("Clear Output");
        btnClearOutput.addActionListener(this::btnClearOutputHandler);
        panelClear.add(btnClearOutput);

        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(this::btnSubmitHandler);
        panelButtons.add(btnSubmit);

        panelRight = new JScrollPane();
        panelRight.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelContent.add(panelRight);

        lblOutput = new JLabel("Bulletin Board Output: ");
        lblOutput.setBorder(new EmptyBorder(0, 0, 10, 0));
        panelRight.setColumnHeaderView(lblOutput);

        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setLineWrap(true);
        txtOutput.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panelRight.setViewportView(txtOutput);

        setVisible(true);

        connectDialog();
    }

    JPanel panelParent;
    JPanel panelHeader;
    JLabel lblNames;
    JLabel lblTitle;
    JButton btnDisconnect;
    JPanel panelContent;
    JPanel panelLeft;
    JPanel panelRequest;
    JLabel lblRequest;
    JComboBox<Request> comboBoxRequests;
    JPanel panelFields;
    JPanel panelSTATUS;
    JLabel lblSTATUS;
    JTextField txtSTATUS;
    JPanel panelMESSAGE;
    JLabel lblMESSAGE;
    JTextField txtMESSAGE;
    JPanel panelCOLOR;
    JLabel lblCOLOR;
    JTextField txtCOLOR;
    JPanel panelHEIGHT;
    JLabel lblHEIGHT;
    JTextField txtHEIGHT;
    JPanel panelWIDTH;
    JLabel lblWIDTH;
    JTextField txtWIDTH;
    JPanel panelCOORDINATELLC;
    JLabel lblCOORDINATELLC;
    JTextField txtCOORDINATELLC;
    JPanel panelExtra;
    JCheckBox checkboxAll;
    JCheckBox checkboxBibtex;
    JPanel panelButtons;
    JPanel panelClear;
    JButton btnClearFields;
    JButton btnClearOutput;
    JButton btnSubmit;
    JScrollPane panelRight;
    JLabel lblOutput;
    JTextArea txtOutput;
    JTextField txtIPAddress;
    JTextField txtPort;

}
