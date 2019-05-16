package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameClient {
  private final String host;
  private final int port;
  private final String userName;

  public static GameClient getInstance() {
    return instance;
  }

  private static GameClient instance;
  private ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

  public GameClient(String host, int port,String userName) {
    this.host = host;
    this.port = port;
    this.userName = userName;
    instance = this;
  }

  public void initGui(ChatWindow chatWindow, GameWindow gameWindow) {
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .handler(new GameClientInitialize(new GameClientHandler(chatWindow,gameWindow)));
      Channel channel = bootstrap.connect(host, port).sync().channel();
      channel.writeAndFlush("//JOIN "+userName+"\r\n");


      for(;;) {
        if(!deque.isEmpty()) {
          String msg = deque.removeFirst();
          channel.writeAndFlush(msg + "\r\n");
        }
      }


    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
    }
  }

  public void sendMessage(String message) {
    if(message.length() > 0)
      deque.addLast(message);
  }
}
