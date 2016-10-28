package org.codingwater.serviceimpl;

import java.util.concurrent.Semaphore;

/**
 * Created by water on 10/28/16.
 */
public class SemaphoreTest {

	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(2);
		Person p1 = new Person("A", semaphore);
		Person p2 = new Person("B", semaphore);
		Person p3 = new Person("C", semaphore);

		p1.start();
		p2.start();
		p3.start();


	}



}

class Person extends Thread {
	private Semaphore semaphore;

	public Person (String name , Semaphore semaphore) {
		setName(name);
		this.semaphore = semaphore;
	}

	@Override public void run() {
		System.out.println(getName() + " is waiting...");

		try {
			semaphore.acquire();
			System.out.println(getName() + " is servicing");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(getName() + " is done");
		semaphore.release();
	}

}