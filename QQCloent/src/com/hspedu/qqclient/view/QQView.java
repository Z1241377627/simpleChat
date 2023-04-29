package com.hspedu.qqclient.view;

import com.hspedu.qqclient.service.FileClientService;
import com.hspedu.qqclient.service.MessageClientService;
import com.hspedu.qqclient.service.UserClientService;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Scanner;

public class QQView {
    private boolean loop = true;
    private UserClientService userClientService = new UserClientService();
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();
    public static void main(String[] args) {
        new QQView().mainMenu();
    }
    private void mainMenu() {

        Scanner scanner = new Scanner(System.in);
        while (loop) {
            System.out.println("============欢迎登录网络通信系统============");
            System.out.println("\t\t1:登录系统");
            System.out.println("\t\t9:退出系统");
            System.out.print("请输入您的选择：");
            String key = scanner.next();

            switch (key){
                case "1":
                    System.out.println("请输入用户名");
                    String userId = scanner.next();
                    System.out.println("请输入密  码");
                    String pwd = scanner.next();
                    if (userClientService.checkUser(userId, pwd)) {
                        while (loop) {
                            System.out.println("=================欢迎" + userId + "用户=============");
                            System.out.println("============网络通信系统二级菜单============");
                            System.out.println("\t\t1:显示在线用户列表");
                            System.out.println("\t\t2:群发消息");
                            System.out.println("\t\t3:私聊消息");
                            System.out.println("\t\t4:发送文件");
                            System.out.println("\t\t9:退出系统");
                            System.out.print("请输入您的选择：");
                            String key2 = scanner.next();
                            switch (key2) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "2":
                                    System.out.println("输入想对大家说的话:");
                                    String str = scanner.next();
                                    messageClientService.sendMessageToAll(str, userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号(在线)");
                                    String getterId = scanner.next();
                                    System.out.println("请输入想说的话: ");
                                    String content = scanner.next();
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    break;
                                case "4":
                                    System.out.print("请输入你想发送文件给的用户：");
                                     getterId = scanner.next();
                                    System.out.print("请输入发送文件的路径");
                                    String src = scanner.next();
                                    System.out.print("请输入发送文件到对方对应的路径");
                                    String dest = scanner.next();
                                    fileClientService.sendFileToOne(src, dest, userId, getterId);
                                    break;
                                case "9":
                                    System.out.println("\t\t9:退出系统");
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }
                    break;
                case "9":
                    userClientService.logout();
                    break;
            }
        }
    }
}
