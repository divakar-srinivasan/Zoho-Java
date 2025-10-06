package Proxy.Dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface DynamicService{
    void perform();
}

class DynamicRealService implements DynamicService{
    public void perform(){
        System.out.println("performing...");
    }
}

class ProxyHandler1 implements InvocationHandler{
    private Object target;

    public ProxyHandler1(Object target){
        this.target=target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        System.out.println("LOG : Calling "+method.getName());
        Object result = method.invoke(target, args);
        System.out.println("LOG : "+method.getName()+" Finished...");
        return result;
    }
}

public class DynamicProxy1 {
    public static void main(String args[]){
        DynamicService proxyInstance = (DynamicService) Proxy.newProxyInstance(DynamicRealService.class.getClassLoader(), new Class[]{DynamicService.class}, new ProxyHandler1(new DynamicRealService()));
        proxyInstance.perform();
    }
}
