package org.codingwater.serviceimpl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by water on 11/10/16.
 */
public class LockTest {

	public static void main(String[] args) {
		Data2 data2 = new Data2();

		new DMT(data2).start();
		new DMT(data2).start();
		new DMT(data2).start();
		new DMT(data2).start();

	}



}

class Data2 {
	int i = 0;
	Lock lock = new ReentrantLock();
	AtomicInteger ai = new AtomicInteger(0);


	void operate() {
		lock.lock();
		int p = ai.incrementAndGet();
		System.out.println(p);
		lock.unlock();
//		lock.lock();
//		i++;
//		System.out.println(i);
//		lock.unlock();
	}

}

class DMT extends Thread {
	private Data2 data2;
	public DMT(Data2 data2) {
		this.data2 = data2;
	}

	@Override public void run() {

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			data2.operate();

		}
	}
}