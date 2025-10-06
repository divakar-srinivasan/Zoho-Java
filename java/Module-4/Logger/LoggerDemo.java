package Logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class User{
    private String name;
    private final Logger logger;

    User(String name,Logger logger){
        this.name=name;
        this.logger=logger;
    }

    public void login(){
        logger.info(name + " User logged in");
    }
    public void logout(){
        logger.info(name + "User Logout ");
    }
}

public class LoggerDemo {
    public static void main(String args[]) throws Exception{

        LogManager.getLogManager().reset();

        Logger logger=Logger.getLogger("AppLogger");
        FileHandler file=new FileHandler("Logger/app.log",true);
        file.setFormatter(new SimpleFormatter());
        logger.addHandler(file);
        logger.setLevel(Level.ALL);

        ConsoleHandler console=new ConsoleHandler();
        console.setFormatter(new SimpleFormatter());
        console.setLevel(Level.ALL);
        logger.addHandler(console);

        User user1 = new User("Divakar",logger);
        User user2=new User("Arul",logger);


        user1.login();
        user2.login();
        user1.logout();
        user2.logout();

        logger.warning("Application going to sleep");

    }
}
