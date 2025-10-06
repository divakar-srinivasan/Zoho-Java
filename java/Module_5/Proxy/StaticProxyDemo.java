package Proxy;

interface Service{
    void doWork();
}

class RealService implements Service{
    public void doWork(){
        System.out.println("Doing the real work...");
    }
}

class ServiceProxy implements Service{
    private Service realService;

    public ServiceProxy(Service realService){
        this.realService=realService;
    }

    public void doWork(){
        System.out.println("[proxy] Before work");
        realService.doWork();
        System.out.println("[proxy] After work");
    }
}

public class StaticProxyDemo {
    public static void main(String args[]){
        Service service=new ServiceProxy(new RealService());
        service.doWork();
    }
}
