package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {

    private User user = new User();
    private Socket socket;

    public boolean checkUser(String userId, String pwd){
        boolean b = false;
        user.setUserId(userId);
        user.setPwd(pwd);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){
                ClientConnectServiceThread clientConnectServiceThread = new ClientConnectServiceThread(socket);
                clientConnectServiceThread.start();
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServiceThread);
                b = true;
            }else {
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());

        try {
            ClientConnectServiceThread clientConnectServiceThread = ManageClientConnectServerThread.getClientConnectServiceThread(user.getUserId());
            Socket socket = clientConnectServiceThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServiceThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println("退出程序");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
