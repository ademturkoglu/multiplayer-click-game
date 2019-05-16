package server;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameTable {
  static int width;
  static int height;
  static int time;
  static int count;
  public static ArrayList<Element> elements = new ArrayList<>();
  public static ArrayList<ActiveElement> activeElements = new ArrayList<>();
  String[] elementNames = {"triangle", "square", "circle"};
  String[] colors = {"red", "green", "blue"};

  public GameTable(int width, int height, int time, int count) {
    this.width = width;
    this.height = height;
    this.time = time;
    this.count = count;
    for (String element : elementNames) {
      for (String color : colors) {
        int i = (int) (Math.random() * 10);
        elements.add(new Element(element, color, i));
        GameServerHandler.sendMessage("//LIST " + element + " " + color + " " + i);
      }
    }
  }


  void sendClickElement() {
    Timer myTimer = new Timer();
    TimerTask timerTask = new TimerTask() {
      Random random = new Random();

      @Override
      public void run() {
        int i = random.nextInt(elements.size());
        String name = elements.get(i).getName();
        String color = elements.get(i).getColor();
        int x = random.nextInt(width - 100);
        int y = random.nextInt(height - 70);
        GameServerHandler.sendMessage("//ELEMENT " + name + " " + color + " " + x + " " + y);
        activeElements.add(new ActiveElement(name, color, x, y));
        count--;
        if (count == 0)
          myTimer.cancel();
      }
    };

    myTimer.schedule(timerTask, 0, time);
  }

  public static int getScore(String name, String color) {
    return elements.stream()
        .filter(element -> element.getName().equals(name))
        .filter(element -> element.getColor().equals(color))
        .findAny().get().getPuan();
  }

  public static void deleteActiveElements(ActiveElement element) {
    activeElements.remove(element);

  }


}







