package org.codingwater.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * Created by water on 11/4/16.
 */
public class PhaserExample {

	public static void main(String[] args) throws InterruptedException {

		List<Runnable> tasks = new ArrayList<>();

		for (int i = 0; i < 2; i++) {

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					int a = 0, b = 1;
					for (int i = 0; i < 2000000000; i++) {
						a = a + b;
						b = a - b;
					}
				}
			};

			tasks.add(runnable);

		}

		new PhaserExample().runTasks(tasks);

	}

	void runTasks(List<Runnable> tasks) throws InterruptedException {

		final Phaser phaser = new Phaser(1); // 默认一个注册 register = 1

		for ( final Runnable task : tasks) {
			phaser.register(); // 增加了两个注册 register = 2 + 1 = 3
			new Thread() {
				public void run() {

					System.out.println("phaser.arriveAndAwaitAdvance();");
					phaser.arriveAndAwaitAdvance(); // 如果等待的和注册数一样多，则到下一个阶段
					// register = 3
					try {

						task.run();

					} finally {
						System.out.println("phaser.arriveAndDeregister();");
						phaser.arriveAndDeregister();
					}

				}
			}.start();
		}

		System.out.println("Main phaser.arriveAndAwaitAdvance();");
		phaser.arriveAndAwaitAdvance();// 如果等待的和注册数一样多，则到下一个阶段 register = 3

		System.out.println("Main phaser.arriveAndAwaitAdvance();");
		phaser.arriveAndAwaitAdvance();// 如果等待的和注册数一样多，则到下一个阶段 register = 1

		System.out.println("Main phaser.arriveAndDeregister();");
		phaser.arriveAndDeregister(); // 还有一个register, 减去1, 然后退出

		System.out.println(phaser.getRegisteredParties()); // 0
	}

}
