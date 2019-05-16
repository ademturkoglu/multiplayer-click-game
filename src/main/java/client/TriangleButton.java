package client;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class TriangleButton extends JButton {
  private Shape triangle = createTriangle();

  public void paintBorder( Graphics g ) {
    ((Graphics2D)g).draw(triangle);
  }
  public void paintComponent( Graphics g ) {
    ((Graphics2D)g).fill(triangle);
  }
  public Dimension getPreferredSize() {
    return new Dimension(100,50);
  }
  public boolean contains(int x, int y) {
    return triangle.contains(x, y);
  }


  private Shape createTriangle() {
    Polygon p = new Polygon();
    p.addPoint( 0   , 50 );
    p.addPoint( 50 , 0   );
    p.addPoint( 100 ,50  );
    return p;
  }
}


