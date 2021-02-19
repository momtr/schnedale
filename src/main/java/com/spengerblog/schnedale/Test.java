package com.spengerblog.schnedale;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        while(true) {
            Socket socket = serverSocket.accept();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            printWriter.println(
                    "HTTP/1.1 200 OK\n" +
                    "\n"
            );
            printWriter.flush();
            printWriter.write("hello world");
            printWriter.flush();
            socket.close();
        }
    }

}
