package org.codingwater.service;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.yield;

/**
 * Created by water on 4/28/16.
 */
public class NoVisibility {

  private static boolean ready;
  private static int number;
  private static class ReaderThread extends Thread {
    public void run() {
      while (!ready)
        Thread.yield();
      System.out.println(number);
    }
  }
  public static void main(String[] args) {
//    new ReaderThread().start();
//    number = 42;
//    ready = true;

    MyRunnnerable m1 = new MyRunnnerable();
    MyRunnnerable m2 = new MyRunnnerable();
    Thread t1 = new Thread(m1);
    Thread t2 = new Thread(m2);
    t1.start();
    t2.start();


  }
}


class MyRunnnerable implements Runnable {

  @Override public void run() {

    for (int i = 0; i < 100 ; i++) {
      if (i % 10 == 0) {
        System.out.println("yield");
        yield();
      }
      System.out.println(currentThread().getName());
    }


  }
}