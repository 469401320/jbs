package me.hao0.jbs.common.util;


public class Sleeps {

    private Sleeps(){}

    public static void sleep(int secs){
        try {
            Thread.sleep(secs * 1000L);
        } catch (InterruptedException e) {

        }
    }
}
