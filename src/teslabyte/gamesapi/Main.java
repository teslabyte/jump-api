package teslabyte.gamesapi;

import teslabyte.gamesapi.line.LineAuthentication;

public class Main {

    private static String userToken = "";

    public static void main(String[] args) {
        LineAuthentication lineAuthentication = new LineAuthentication(userToken);
        lineAuthentication.authenticate();
    }
}