package com.example.demo.middleware;

public class ActionService {

    ServerToMock serverToMock;

    public ActionService(ServerToMock serverToMock){
        this.serverToMock = serverToMock;
    }

    public int test(int a, int b) {
        if(serverToMock.result()){
            return a+b;
        } else {
            return a*b;
        }
    }
}
