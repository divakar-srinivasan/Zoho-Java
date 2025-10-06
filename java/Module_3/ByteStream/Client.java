package ByteStream;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String host = "localhost"; 
        int port = 5000;           
        String fileToSend = "ByteStream/send.txt";

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to server!");

            
            try (BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                 FileInputStream fis = new FileInputStream(fileToSend);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.flush();
                System.out.println("File sent successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
