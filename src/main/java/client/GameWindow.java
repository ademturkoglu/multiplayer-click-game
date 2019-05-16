package client;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GameWindow {
  private JFrame frame;
  private JPanel panel;
  private JList<String> elementList;

  ArrayList<JButton> arrayList = new ArrayList<>();

  public void initWindow(int width, int height) {
    frame = new JFrame("Click Game");
    frame.setSize(width + 150, height);
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

    elementList = new JList<>();
    elementList.setBounds(width, 0, 150, height);
    panel.add(elementList);


  }

  public void addElementToList(String name) {
    DefaultListModel listModel = new DefaultListModel();

    for (int i = 0; i < elementList.getModel().getSize(); i++) {
      listModel.addElement(elementList.getModel().getElementAt(i));
    }

    listModel.addElement(name);

    elementList.setModel(listModel);
  }

  public void setVisible() {
    frame.setVisible(true);
    frame.repaint();
  }

  public void addTriangle(String color, int x, int y) {
    JButton b = new TriangleButton();
    b.setForeground(getColor(color));
    b.setBounds(x, y, 100, 50);
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GameClient.getInstance().sendMessage("//CLICK triangle "+color+" "+x+" "+y);
        b.setVisible(false);
      }
    });
    arrayList.add(b);
    panel.add(b);
    panel.repaint();


  }

  public void addSquare(String color, int x, int y) {
    JButton b = new SquareButton();
    b.setForeground(getColor(color));
    b.setBounds(x, y, 50, 50);
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GameClient.getInstance().sendMessage("//CLICK square "+color+" "+x+" "+y);
        b.setVisible(false);
      }
    });
    arrayList.add(b);
    panel.add(b);
    panel.repaint();
  }

  public void addCircle(String color, int x, int y) {
    JButton b = new CircleButton();
    b.setForeground(getColor(color));
    b.setBounds(x, y, 50, 50);
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GameClient.getInstance().sendMessage("//CLICK circle "+color+" "+x+" "+y);
        b.setVisible(false);
      }
    });
    arrayList.add(b);
    panel.add(b);
    panel.repaint();
  }

  public void deleteElement(int x,int y){
    arrayList.stream()
        .filter(jButton -> jButton.getBounds().getX()==x)
        .filter(jButton -> jButton.getBounds().getY()==y)
        .findFirst().get().setVisible(false);
  }

  static Color getColor(String col) {
    Color color = Color.WHITE;

    switch (col.toLowerCase()) {
      case "black":
        color = Color.BLACK;
        break;
      case "blue":
        color = Color.BLUE;
        break;
      case "cyan":
        color = Color.CYAN;
        break;
      case "darkgray":
        color = Color.DARK_GRAY;
        break;
      case "gray":
        color = Color.GRAY;
        break;
      case "green":
        color = Color.GREEN;
        break;

      case "yellow":
        color = Color.YELLOW;
        break;
      case "lightgray":
        color = Color.LIGHT_GRAY;
        break;
      case "magneta":
        color = Color.MAGENTA;
        break;
      case "orange":
        color = Color.ORANGE;
        break;
      case "pink":
        color = Color.PINK;
        break;
      case "red":
        color = Color.RED;
        break;
      case "white":
        color = Color.WHITE;
        break;
    }
    return color;
  }


}
