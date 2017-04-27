package org.codingwater.serviceimpl;

import java.util.concurrent.Exchanger;

/**
 * Created by water on 11/4/16.
 */
public class ExchangerTest {
	public static void main(String[] args) {



		Exchanger<String> exchanger = new Exchanger<>();
		new A(exchanger, "A").start();
		new B(exchanger, "B").start();

	}
}

class A extends Thread{

	private Exchanger<String> exchanger;

	public A(Exchanger<String> exchanger, String name) {
		setName(name);
		this.exchanger = exchanger;
	}

	@Override public void run() {

		try {
			String string = exchanger.exchange("HI");
			System.out.println(getName() + ":" + string);

			string = exchanger.exchange("A");
			System.out.println(getName() + ":" + string);


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}


class B extends Thread{

	private Exchanger<String> exchanger;

	public B(Exchanger<String> exchanger, String name) {
		setName(name);
		this.exchanger = exchanger;
	}

	@Override public void run() {

		try {
			String string = exchanger.exchange("Hello");
			System.out.println(getName() + ":" + string);

			string = exchanger.exchange("B");
			System.out.println(getName() + ":" + string);


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}

