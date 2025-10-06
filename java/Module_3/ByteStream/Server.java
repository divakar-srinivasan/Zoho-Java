package ByteStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 5000; 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server waiting for connection...");

            Socket socket = serverSocket.accept(); 
            System.out.println("Client connected!");

            try (BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                 FileOutputStream fos = new FileOutputStream("ByteStream/received.txt");
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.flush();
                System.out.println("File received successfully!");
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

