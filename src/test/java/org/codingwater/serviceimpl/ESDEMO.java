package org.codingwater.serviceimpl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by water on 11/4/16.
 */
public class ESDEMO {

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		ExecutorService es = Executors.newFixedThreadPool(2);
		Future<Integer> f1= es.submit(new DM(1, 100));
		Future<Integer> f2= es.submit(new DM(1, 1000));

		System.out.println(f1.get() + ":" + f2.get());
	}


}

class DM implements Callable<Integer> {
	int begin , end;
	public DM(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	@Override public Integer call() throws Exception {
		int sum = 0 ;
		for (int i = begin; i < end; i++) {
			sum ++;
		}
		return sum;
	}
}