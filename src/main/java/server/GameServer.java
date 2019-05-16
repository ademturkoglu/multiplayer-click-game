package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.ServerGui;
import server.ServerInitializer;

public class GameServer {
  private final int port;
  private ServerGui gui;

  public GameServer(int port,ServerGui gui) {
    this.port = port;
    this.gui= gui;
  }

  public void run() {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      System.out.println("[INFO] Setting up bootstrap");
      ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ServerInitializer(new GameServerHandler(gui)));


      System.out.println("[INFO] Listening on port: " + port);
      bootstrap.bind(port).sync().channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();

    }
  }
}