package org.codingwater.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by water on 5/23/16.
 */
public class ThreadPoolDeadLockAvoidance {

  private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
      1,
      2,
      60,
      TimeUnit.SECONDS,
      new SynchronousQueue<Runnable>(),
      new ThreadPoolExecutor.CallerRunsPolicy());

  public static void main(String[] args) {
    ThreadPoolDeadLockAvoidance me = new ThreadPoolDeadLockAvoidance();
    me.test("<this will not deadlock>");
  }

  private void test(final String message) {
    Runnable taskA = new Runnable() {

      @Override
      public void run() {
        System.out.println("Executing TaskA...");

        Runnable taskB = new Runnable() {
          @Override
          public void run() {
            System.out.println("TaskB processes " + message);
          }
        };

        Future<?> result = threadPool.submit(taskB);

        try {
          result.get();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }

        System.out.println("TaskA  Done.");

      }
    };
    threadPool.submit(taskA);

  }


}
