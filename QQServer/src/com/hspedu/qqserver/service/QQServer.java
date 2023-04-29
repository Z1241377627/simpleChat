package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.HashMap;

public class QQServer {
    private ServerSocket ss = null;
    private static HashMap<String, User> validUsers = new HashMap<>();
    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("1241377", new User("1241377", "123456"));
        validUsers.put("凉凉", new User("凉凉", "123456"));
        validUsers.put("火火", new User("火火", "123456"));
    }

    private boolean checkUser(String userId, String pwd){
        User user = validUsers.get(userId);
        if (user == null){
            return false;
        }
        if (!user.getPwd().equals(pwd)){
            return false;
        }
        return true;
    }

    public QQServer(){
        try {
            System.out.println("服务器在9999端口监听");
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                Message message = new Message();
                if (checkUser(user.getUserId(), user.getPwd())){
                    System.out.println("登录成功，欢迎" + user.getUserId() + "用户");
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    ManageClientThreads.addClientThread(user.getUserId(), serverConnectClientThread);
                }else {
                    System.out.println("登录失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
