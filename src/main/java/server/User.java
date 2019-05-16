package server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class User implements Comparable<User> {
  private String nickname;
  private final String uuid;
  private int puan = 0;

  public User(String nickname, String uuid) {
    this.nickname = nickname;
    this.uuid = uuid;
    USERS.put(uuid, this);
    userList.add(this);
  }

  private static final ConcurrentHashMap<String, User> USERS = new ConcurrentHashMap<>();
  private static final ArrayList<User> userList = new ArrayList<>();

  public int getPuan() {
    return puan;
  }

  public void setPuan(int puan) {
    this.puan = puan;
  }

  public Channel getChannel() {
    return GameServerHandler.getChannel(this);
  }

  public int compareTo(User another) {
    return this.nickname.compareTo(another.nickname);
  }

  public String getNickname() {
    return nickname;
  }

  public String getUuid() {
    return uuid;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public static User getUser(String uuid) {
    return USERS.get(uuid);
  }

  public static User getByNick(String nick) {
    return USERS.values().stream().filter(x -> x.nickname.equalsIgnoreCase(nick)).findFirst().get();
  }

  public static void removeUser(String uuid) {
    USERS.remove(uuid);
    for (User u : userList)
      if (u.uuid.equals(uuid)) {
        userList.remove(u);
      }
  }

  public void message(User sender, String msg) {
    msg += "\r\n";
    for (User u : userList) {
      if (!u.getUuid().equals(sender.getUuid()))
        u.getChannel().writeAndFlush(sender.getNickname() + " >> " + msg);
    }
    sender.getChannel().writeAndFlush(" You >> " + msg);
  }


}
