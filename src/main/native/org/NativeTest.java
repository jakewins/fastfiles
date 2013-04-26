package org;


public class NativeTest
{

    public native String sayHello();

    static
    {
        System.loadLibrary( "NativeTest" );
    }

    public static void main(String ... args)
    {

        NativeTest t = new NativeTest();
        System.out.println(t.sayHello());
        System.out.println("Hello, world!");

    }



}
