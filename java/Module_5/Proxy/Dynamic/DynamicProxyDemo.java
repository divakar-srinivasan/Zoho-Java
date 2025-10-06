package Proxy.Dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Service{
    void execute();
}

class MeetingService implements Service{
    public void execute(){
        System.out.println("Meeting Service....");
    }
}

class ShoppingService implements Service{
    public void execute(){
        System.out.println("Shopping Servce");
    }
}

class Booking implements Service{
    public void execute(){
        System.out.println("Booking Service...");
    }
}

class LoggingHandler implements InvocationHandler{
    private Object target;

    LoggingHandler(Object target){
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        System.out.println("Loggin Before : "+target.getClass().getSimpleName());
        Object result = method.invoke(target, args);
        System.out.println("After Loggin : " + target.getClass().getSimpleName());
        return result;
    }
}

public class DynamicProxyDemo {
    public static void main(String args[]){
        Service meeting = createProxy(new MeetingService());
        Service shopping = createProxy(new ShoppingService());
        Service booking = createProxy(new Booking());

        meeting.execute();
        shopping.execute();
        booking.execute();
    }
    private static Service createProxy(Service service){
        return (Service) Proxy.newProxyInstance(service.getClass().getClassLoader(), new Class[]{Service.class}, new LoggingHandler(service));
    }
}
