package server;

public class Element {
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getPuan() {
    return puan;
  }

  public void setPuan(int puan) {
    this.puan = puan;
  }

  String name;
  String color;
  int puan;

  public Element(String name, String color, int puan) {
    this.name = name;
    this.color = color;
    this.puan = puan;
  }
}
