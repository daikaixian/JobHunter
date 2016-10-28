package org.codingwater.serviceimpl;

/**
 * Created by water on 10/28/16.
 */
public class TicketTest {

	public static void main(String[] args) {

		MyTickerRunnable myTickerRunnable = new MyTickerRunnable();

		Thread t1 = new Thread(myTickerRunnable);
		Thread t2 = new Thread(myTickerRunnable);
		Thread t3 = new Thread(myTickerRunnable);

		t1.start();
		t2.start();
		t3.start();

	}

}

class MyTickerRunnable implements Runnable {

	private int ticket = 5;
	private final Object lock = new Object();

	@Override public   void  run() {


	}


	public synchronized void tell() {
		for (int i = 0; i < 10 ; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (ticket >= 0) {
				System.out.println("剩余车票" + ticket --);
			}
		}
	}
}