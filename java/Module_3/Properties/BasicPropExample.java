package Properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class BasicPropExample {
    public static void main(String args[])throws IOException{
        File file=new File("Properties/App.properties");
        file.getParentFile().mkdir();
        Properties prop=new Properties();
        prop.setProperty("app.name","Eventix");
        prop.setProperty("app.version", "1.0");
        //FileOutputStream write=null;
        //FileInputStream read=null;
        FileReader read=null;
        FileWriter write=null;
        try{
            write=new FileWriter(file);
            prop.store(write,"writeOperation");
            System.out.println("Successfull...");
            read=new FileReader(file);
            prop.load(read);
            for(String key : prop.stringPropertyNames()){
                System.out.println(key+" = "+prop.getProperty(key));
            }
        }catch(IOException e){
            e.printStackTrace();
            write.close();
        }

    }
}
