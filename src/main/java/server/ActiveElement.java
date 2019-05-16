package server;

public class ActiveElement{
  String name;
  String color;
  int x;
  int y;

  ActiveElement(String name,String color,int x, int y){
    this.name=name;
    this.color=color;
    this.x=x;
    this.y=y;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
