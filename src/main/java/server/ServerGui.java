package server;

import client.ChatWindow;
import client.GameClient;
import client.GameWindow;
import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerGui {

  private JFrame frame;
  private JPanel panel;
  private JList<String> userList;
  private JButton startGameButton;
  private JLabel label;
  private JTextField boy;
  private JTextField en;
  private TextField time;
  private TextField counter;

  public ServerGui() {
    initWindow();
  }

  private void initWindow() {
    frame = new JFrame("Game Server");
    frame.setSize(350, 450);
    frame.setLayout(new GridLayout(1, 1));
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((int) ((dimension.getWidth() - frame.getWidth()) / 2),
        (int) ((dimension.getHeight() - frame.getHeight()) / 2));
    frame.setResizable(false);
//		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.ico")));
    frame.setType(Window.Type.UTILITY);


    WebLookAndFeel.install();

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    panel = new JPanel();
    panel.setLayout(null);

    frame.add(panel);
    startGameButton = new JButton("START");
    startGameButton.setBounds(50, 400, 200, 30);
    startGameButton.addActionListener(new ButtonClickListener());
    userList = new JList<>();
    userList.setBounds(50, 25, 200, 270);
    label= new JLabel("Users:",SwingConstants.CENTER);
    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
    label.setBounds(50, 2, 50, 30);
    JLabel enLabel = new JLabel("Width");
    enLabel.setBounds(10,370,40,30);
    JLabel boyLabel = new JLabel("Height");
    boyLabel.setBounds(130,370,45,30);
    JLabel timeLabel = new JLabel("Wait time for element:");
    timeLabel.setBounds(50,300,150,20);
    JLabel countLabel = new JLabel("How many element:");
    countLabel.setBounds(50,330,150,20);
    counter= new TextField("10");
    counter.setBounds(200,330,40,30);
    time= new TextField();
    time.setBounds(201,300,50,30);

    en=new JTextField();
    en.setBounds(50,370,40,30);
    boy=new JTextField();
    boy.setBounds(170,370,40,30);



    panel.add(startGameButton);
    panel.add(userList);
    panel.add(label);
    panel.add(timeLabel);
    panel.add(time);
    panel.add(countLabel);
    panel.add(counter);
    panel.add(boyLabel);
    panel.add(enLabel);
    panel.add(en);
    panel.add(boy);
    frame.setVisible(true);


  }

  public void addNewUserToList(String name,int puan) {
    DefaultListModel listModel = new DefaultListModel();

    for (int i = 0; i < userList.getModel().getSize(); i++) {
      listModel.addElement(userList.getModel().getElementAt(i));
    }

    listModel.addElement(name+ "--" +puan);

    userList.setModel(listModel);
  }




    public void removeUserToList(String name) {
    DefaultListModel listModel = new DefaultListModel();

    for (int i = 0; i < userList.getModel().getSize(); i++) {
      listModel.addElement(userList.getModel().getElementAt(i));
      if(userList.getModel().getElementAt(i).startsWith(name)){
        listModel.removeElement(userList.getModel().getElementAt(i));
      }
    }
    userList.setModel(listModel);
  }

  private class ButtonClickListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {


      new Thread() {
        @Override
        public void run() {
          GameServerHandler.sendMessage("//START "+en.getText()+" "+boy.getText());
          GameTable gameTable = new GameTable(Integer.parseInt(en.getText()),Integer.parseInt(boy.getText()),Integer.parseInt(time.getText()),Integer.parseInt(counter.getText()));
          gameTable.sendClickElement();

        }
      }.start();
    }
  }

}

