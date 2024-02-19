package controller;

import ui.LoginPage;

public class Main {
    public static void main(String[] arg) {
        DataBaseConnectionHandler db = new DataBaseConnectionHandler();
        new LoginPage(db);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            db.close();
            System.out.println("db closed");
        }));
    }
}
