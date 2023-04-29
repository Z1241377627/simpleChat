package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServiceThread extends Thread{
    private Socket socket;

    public ClientConnectServiceThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("================当前在线用户==============");
                    for (int i = 0; i < onlineUsers.length; i++){
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println();
                    System.out.println(message.getSender() + "对" + message.getGetter() + "说：" + message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_EXIT)){
                    System.out.println();
                    System.out.println(message.getSender() + "对大家说：" + message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println(message.getSender() + "给" + message.getGetter() + "发文件：" +
                            message.getSrc() + "到我电脑的目录：" + message.getDest());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("文件保存成功");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }
}
