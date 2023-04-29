package com.hspedu.qqclient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread {
    private static HashMap<String, ClientConnectServiceThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String userId, ClientConnectServiceThread clientConnectServiceThread){
        hm.put(userId, clientConnectServiceThread);
    }

    public static ClientConnectServiceThread getClientConnectServiceThread(String userId){
        return hm.get(userId);
    }
}
