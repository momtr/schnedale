package com.spengerblog.schnedale.client;

import java.io.*;
import java.net.Socket;

public class Publisher {

    public static void main(String[] args) throws IOException {
        final String ip = "127.0.0.1";
        final int port = 2227;
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            printWriter.println(getEventMessage());
            printWriter.flush();
            System.out.println("sent message");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            char[] buffer = new char[200];
            while(true) {
                int anzahlZeichen = bufferedReader.read(buffer, 0, 200);
                String nachricht = new String(buffer, 0, anzahlZeichen);
                System.out.println("received message: " + nachricht);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: " + e.getLocalizedMessage());
        } finally {
            if(socket != null)
                socket.close();
        }
    }

    public static String getEventMessage() {
        return "PUSH schnedale testtest";
    }

}
