package org.codingwater.serviceimpl;

import java.util.concurrent.CountDownLatch;

/**
 * Created by water on 10/28/16.
 */
public class CountDownLatchTest {
	public static void main(String[] args) {

		CountDownLatch countDownLatch = new CountDownLatch(3);
		Runner r1 = new Runner(countDownLatch, "A");
		Runner r2 = new Runner(countDownLatch, "B");
		Runner r3 = new Runner(countDownLatch, "C");

		r1.start();
		r2.start();
		r3.start();
		for (int i = 0; i < 3 ; i++) {
			System.out.println(3 - i);
			countDownLatch.countDown();
		}

	}





}

class Runner extends Thread {
	private CountDownLatch countDownLatch;

	public Runner(CountDownLatch countDownLatch, String name) {
		setName(name);
		this.countDownLatch = countDownLatch;
	}

	@Override public void run() {

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(getName() + "跑");
	}



}