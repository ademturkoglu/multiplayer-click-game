
package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import server.ServerGui;

import java.io.IOException;
import java.util.function.Predicate;

@ChannelHandler.Sharable
public class GameServerHandler extends SimpleChannelInboundHandler<String> {

  private ServerGui gui;

  public GameServerHandler(ServerGui gui) {
    this.gui = gui;
  }

  private static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
    Channel incoming = channelHandlerContext.channel();

    User u = User.getUser(incoming.remoteAddress().toString());
    if (u == null && !msg.startsWith("/"))
      return;
    if (msg.startsWith("//")) {
      System.out.println("[_CMD] " + incoming.remoteAddress() + ": " + msg);
      String[] parse = msg.split(" ");
      if (parse[0].equals("//JOIN")) {
        User user = new User(parse[1], incoming.remoteAddress().toString());
        gui.addNewUserToList(parse[1], user.getPuan());
        for (Channel channel : CHANNELS) {
          if (channel != incoming) channel.writeAndFlush("//JOINED " + parse[1] + "\r\n");
        }
      } else if (parse[0].equals("//CLICK")) {
        Predicate<ActiveElement> p1 = activeElement -> activeElement.getX() == Integer.parseInt(parse[3]) && activeElement.getY() == Integer.parseInt(parse[4]);
        if (GameTable.activeElements.stream().anyMatch(p1)) {
          u.setPuan(u.getPuan() + GameTable.getScore(parse[1], parse[2]));
          gui.removeUserToList(u.getNickname());
          gui.addNewUserToList(u.getNickname(), u.getPuan());
          GameTable.deleteActiveElements(GameTable.activeElements.stream().filter(p1).findAny().get());
          sendMessage("//CLICKED "+parse[1]+" "+parse[2]+" "+parse[3]+" "+parse[4]);
        }
      }
    } else {
      u.message(u, msg);
    }


  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("[INFO] " + incoming.remoteAddress() + " is starting to connect");
    CHANNELS.add(incoming);
  }

  public static Channel getChannel(User u) {
    for (Channel c : CHANNELS) {
      if (c.remoteAddress().toString().equals(u.getUuid()))
        return c;
    }
    return null;
  }

  public static void sendMessage(String msg) {
    CHANNELS.writeAndFlush(msg + "\r\n");
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("[INFO] " + incoming.remoteAddress() + " is starting to disconnnect");
    incoming.writeAndFlush("[SERVER] Bye!\r\n");
    CHANNELS.remove(incoming);

    User u = User.getUser(incoming.remoteAddress().toString());
    if (u == null) {
      System.out.println("[INFO] Disconnect from " + incoming.remoteAddress().toString());
    } else {
      gui.removeUserToList(u.getNickname());
      User.removeUser(incoming.remoteAddress().toString());
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    if (cause instanceof IOException) {
      System.out.println(
          "[INFO] " + ctx.channel().remoteAddress().toString() + ": Client forcibly closed the connection");
      if (System.getProperty("debug", "false").equalsIgnoreCase("true"))
        cause.printStackTrace();
    } else {
      System.err.println("[ERROR] " + ctx.channel().remoteAddress().toString() + ": Unknown exception -> "
          + cause.getMessage());
      if (System.getProperty("debug", "false").equalsIgnoreCase("true"))
        cause.printStackTrace();
    }
    ctx.close();
  }
}
