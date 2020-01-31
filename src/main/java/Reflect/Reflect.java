package Reflect;

import Reflect.Test.Parent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Reflect implements Callable {
    public synchronized static void main(String[] args) throws ExecutionException, InterruptedException {
        Reflect reflect = new Reflect();
        FutureTask futureTask = new FutureTask(reflect);
        new Thread(futureTask,"t").start();
        Object o = futureTask.get();
        System.out.println(o.toString());
    }

    public synchronized Object call() throws Exception {
        return "hello";
    }
}
