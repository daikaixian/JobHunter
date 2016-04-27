package org.codingwater.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by water on 4/27/16.
 */
public class MultiThreadTest {

    public static void main(final String[] argv) {
      final ExecutorService service;
      final Future<String> task;

      service = Executors.newFixedThreadPool(1);
      task    = service.submit(new Foo());

      try {
        final String str;

        // waits the 10 seconds for the Callable.call to finish.
        str = task.get(10, TimeUnit.SECONDS); // this raises ExecutionException if thread dies
        System.out.println(str);
      } catch(final InterruptedException ex) {
        ex.printStackTrace();
      } catch(final ExecutionException ex) {
        ex.printStackTrace();
      } catch (TimeoutException e) {
        e.printStackTrace();
      }

      service.shutdownNow();
    }
  }

  class Foo implements Callable<String> {
    public String call() {
      try {
        // sleep for 10 seconds
        Thread.sleep(24 * 1000);
      } catch(final InterruptedException ex) {
        ex.printStackTrace();
      }

      return ("Hello, World!");
    }

  }

