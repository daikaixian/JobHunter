package org.codingwater.serviceimpl;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by water on 10/28/16.
 */

//循环屏障
public class CyclicBarrierTest {

	public static void main(String[] args) throws InterruptedException {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
			@Override public void run() {
				System.out.println("start Game");
			}
		});

		Player p1 = new Player(cyclicBarrier, "A");
		Player p2 = new Player(cyclicBarrier, "B");
		Player p3 = new Player(cyclicBarrier, "C");

		p1.start();
		Thread.sleep(1000);
		p2.start();
		Thread.sleep(1000);
		p3.start();
	}
}

class Player extends Thread {
	private CyclicBarrier barrier;

	public Player(CyclicBarrier cyclicBarrier, String name) {
		this.barrier = cyclicBarrier;
		setName(name);
	}

	@Override public void run() {
		try {
			System.out.println(getName() + "等待" + (barrier.getParties() - barrier.getNumberWaiting() - 1) + "位玩家");
			if((barrier.getParties() - barrier.getNumberWaiting() - 1) == 0) {
				System.out.println("人数到齐,准备开始");
			}
			barrier.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

	}
}