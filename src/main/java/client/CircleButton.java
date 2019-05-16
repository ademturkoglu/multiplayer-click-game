package client;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.FontMetrics;

public class CircleButton extends JButton{


  public CircleButton(){

    setOpaque(false);
    setFocusPainted(false);
    setBorderPainted(false);

  }

  private int getDiameter(){
    int diameter = Math.min(getWidth(), getHeight());
    return diameter;
  }

  @Override
  public Dimension getPreferredSize(){
    FontMetrics metrics = getGraphics().getFontMetrics(getFont());
    int minDiameter = 10 + Math.max(metrics.stringWidth(getText()), metrics.getHeight());
    return new Dimension(minDiameter, minDiameter);
  }

  @Override
  public boolean contains(int x, int y){
    int radius = getDiameter()/2;
    return Point2D.distance(x, y, getWidth()/2, getHeight()/2) < radius;
  }

  @Override
  public void paintComponent(Graphics g){

    int diameter = getDiameter();
    int radius = diameter/2;


    g.fillOval(getWidth()/2 - radius, getHeight()/2 - radius, diameter, diameter);



  }
}