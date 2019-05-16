package client;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;

public class ChatWindow {
  private JFrame frame;
  private JPanel panel;
  private JButton registerButton;
  private JList<String> userList;
  private JTextField userNameText;
  private JTextField hostText;
  private JTextField field;
  private volatile JTextArea area;
  private boolean first = true;



  public ChatWindow() {
    initWindow();
  }

  private void initWindow() {
    frame = new JFrame("Chat ");
    frame.setSize(400, 438);
    frame.setLayout(new GridLayout(1, 1));
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((int) ((dimension.getWidth() - frame.getWidth()) / 2),
        (int) ((dimension.getHeight() - frame.getHeight()) / 2));
    frame.setResizable(false);
//		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.ico")));
    frame.setType(Window.Type.UTILITY);


    /*
     * try {
     * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
     * MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme()); } catch
     * (Exception e) { e.printStackTrace(); }
     */
    WebLookAndFeel.install();

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {

        System.exit(0);
      }
    });
    panel = new JPanel();
    panel.setLayout(null);
    frame.add(panel);

    JLabel loginLabel = new JLabel("Register:", SwingConstants.CENTER);
    loginLabel.setFont(new Font(loginLabel.getFont().getFontName(), Font.BOLD, loginLabel.getFont().getSize()));
    loginLabel.setBounds(10, 40, 70, 30);

    userNameText = new JTextField(6);
    hostText = new JTextField(6);
    hostText.setText("127.0.0.1");
    hostText.setBounds(80, 10, 200, 30);
    userNameText.setBounds(80, 40, 200, 30);
    registerButton = new JButton("Register");
    registerButton.setBounds(180, 72, 100, 30);
    registerButton.addActionListener(new ButtonClickListener());
    userList = new JList<>();
    userList.setBounds(290, 10, 100, 90);
    area = new JTextArea();
    area.setBounds(10, 102, 380, 250);
    area.setBackground(new Color(0, 0, 0));
    area.setForeground(new Color(200, 200, 200));
    area.setFont(new Font("Courier New", Font.PLAIN, 17));
    area.setBorder(null);
    area.setEditable(false);
    area.setLineWrap(true);
    area.setBorder(null);
    DefaultCaret caret = (DefaultCaret) area.getCaret();
    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

    JScrollPane areaScroll = new JScrollPane();
    areaScroll.setViewportView(area);
    areaScroll.setBorder(null);
    field = new JTextField();
    field.setFont(new Font("Courier New", Font.PLAIN, 17));
    field.setBounds(10,360,380,30);
    field.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {}

      @Override
      public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 10) {
          GameClient.getInstance().sendMessage(field.getText());
          field.setText("");
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {}
    });
    field.setBorder(null);



    panel.add(hostText);
    panel.add(userNameText);
    panel.add(registerButton);
    panel.add(loginLabel);
    panel.add(userList);
    panel.add(area);
    panel.add(field);
    frame.setVisible(true);


  }

  public void addNewUserToList(String name) {
    DefaultListModel listModel = new DefaultListModel();

    for (int i = 0; i < userList.getModel().getSize(); i++) {
      listModel.addElement(userList.getModel().getElementAt(i));
    }

    listModel.addElement(name);

    userList.setModel(listModel);
  }

  public void removeUserToList(String name) {
    DefaultListModel listModel = new DefaultListModel();

    for (int i = 0; i < userList.getModel().getSize(); i++) {
      listModel.addElement(userList.getModel().getElementAt(i));
      if (userList.getModel().getElementAt(i) == name) {
        listModel.removeElement(userList.getModel().getElementAt(i));
      }
    }
    userList.setModel(listModel);
  }

  public void addLine(String msg) {
    if(first) {
      area.setText(area.getText()+msg);
      first = false;
    } else {
      area.setText(area.getText()+"\r\n"+msg);
    }
  }

  private class ButtonClickListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String host;
      int port = 4000;
      host = hostText.getText();
      GameWindow gameWindow = new GameWindow();

      new Thread() {
        @Override
        public void run() {
          GameClient gameClient = new GameClient(host, port,
              userNameText.getText());
          gameClient.initGui(ChatWindow.this, gameWindow);
        }
      }.start();

      registerButton.setEnabled(false);
    }
  }

  public JFrame getFrame() {
    return frame;
  }

}


