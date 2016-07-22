package org.codingwater.service;

import org.junit.Test;

import java.util.Date;

/**
 * Created by water on 7/21/16.
 */
public class JavaPerformanceTest {

	@Test
	public void testJavaLoop() {
		Date startDate = new Date();
		System.out.println("********start*****");
		long start = startDate.getTime();
		System.out.println(start);
		int j = 10;
		for (int i = 0; i < 10000000; i++) {
			j = j + i;
		}

		System.out.println("*********end********");
		Date endDate = new Date();
		long end = endDate.getTime();
		System.out.println(end);
		long cost = end - start;
		System.out.println("Java execute 10000000 times addition takes " + cost + " milliseconds");



	}






}
