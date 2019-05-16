package server;



public class ServerMain {

  public static void main(String[] args) {
    ServerGui serverGui = new ServerGui();
    GameServer server = new GameServer(4000,serverGui);
    server.run();
  }


}
