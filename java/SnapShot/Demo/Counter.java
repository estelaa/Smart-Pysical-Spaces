package SnapShot.Demo;

public class Counter {

    private static int counter=0;

    public static int getCounter(){
        return counter;
    }

    public static void plusCounter(){
        counter++;
    }

    public static void resetCounter(){
        counter=0;
    }

}
