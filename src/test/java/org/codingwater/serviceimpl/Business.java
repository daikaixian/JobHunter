package org.codingwater.serviceimpl;

import static java.lang.Thread.sleep;

/**
 * Created by water on 10/28/16.
 */
public class Business {



	public static void main(String[] args) {
		Data data = new Data();
		Consumer consumer = new Consumer(data);
		Producer producer = new Producer(data);
		Thread t1 = new Thread(consumer);
		Thread t2 = new Thread(producer);
		t2.start();
		t1.start();


	}






}

class Data {
	int i = 0;

	public void add() {
		synchronized (this) {
			i++;

		if (i % 5 == 0 ) {
			this.notifyAll();
		}
		}
	}


	public void cut()  {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("before" + i);
		i--;
		System.out.println("after" + i);

	}
}

class Consumer implements Runnable {
	private Data data;

	public Consumer(Data data) {
		this.data = data;
	}

	@Override public void run() {
		while (true) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.data.cut();
		}
	}
}

class Producer implements Runnable {

	private Data data;

	public Producer(Data data){
		this.data = data;
	}

	@Override public void run() {
		while (true) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.data.add();
		}
	}
}

