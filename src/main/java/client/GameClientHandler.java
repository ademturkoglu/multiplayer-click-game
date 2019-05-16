package client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GameClientHandler extends SimpleChannelInboundHandler<String> {
  private ChatWindow chatWindow;
  private GameWindow gameWindow;

  public GameClientHandler(ChatWindow chatWindow, GameWindow gameWindow) {
    this.chatWindow = chatWindow;
    this.gameWindow = gameWindow;
  }

  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
    System.out.println(msg);
    if (msg.startsWith("//")) {
      String[] parse = msg.split(" ");
      if (parse[0].equals("//JOINED")) {
        chatWindow.addNewUserToList(parse[1]);
      } else if (parse[0].equals("//START")) {
        chatWindow.getFrame().dispose();
        chatWindow = null;
        gameWindow.initWindow(Integer.parseInt(parse[1]), Integer.parseInt(parse[2]));
        gameWindow.setVisible();
      } else if (parse[0].equals("//LIST")) {
        gameWindow.addElementToList(parse[1] + "-" + parse[2] + "-" + parse[3]);
        Thread.sleep(100);
      } else if (parse[0].equals("//ELEMENT")) {
        if (parse[1].equals("triangle")) {
          gameWindow.addTriangle(parse[2], Integer.parseInt(parse[3]), Integer.parseInt(parse[4]));
        } else if (parse[1].equals("square")) {
          gameWindow.addSquare(parse[2], Integer.parseInt(parse[3]), Integer.parseInt(parse[4]));
        } else if (parse[1].equals("circle")) {
          gameWindow.addCircle(parse[2], Integer.parseInt(parse[3]), Integer.parseInt(parse[4]));
        }

      }else if (parse[0].equals("//CLICKED")){
        gameWindow.deleteElement(Integer.parseInt(parse[3]), Integer.parseInt(parse[4]));
      }


    } else {
      chatWindow.addLine(msg);
    }

  }
}
