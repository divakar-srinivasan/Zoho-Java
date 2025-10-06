package JavaIO;

import java.io.*;

public class CharStream {
    public static void main(String[] args) {
        String s = "Hello Java";
        File fileio = new File("JavaIO/io.txt");
        fileio.getParentFile().mkdirs(); 

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileio))) {
            bw.write(s);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileio))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
