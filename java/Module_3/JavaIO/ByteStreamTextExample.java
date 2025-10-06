package JavaIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteStreamTextExample {
    public static void main(String[] args) {
        String text = "Hello € 😊"; 
        String filename = "file.txt";

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            byte[] bytes = text.getBytes("UTF-8");
            fos.write(bytes);
            System.out.println("Text written to file successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] data = fis.readAllBytes();
            String readText = new String(data, "UTF-8");
            System.out.println("Text read from file: " + readText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
