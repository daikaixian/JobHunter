package org.codingwater.service;


import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by water on 7/21/16.
 */
public class JavaPerformanceTest {

	@Test
	public void threeinone() {
		System.out.println("耗时有波动");
		testJavaGenerateObjRate();
		System.out.println("test two time");
		testJavaGenerateObjRate();
		System.out.println("test three time");
		testJavaGenerateObjRate();

	}

	@Test
	public void testJavaGenerateObjRate() {
		Date startDate = new Date();
		long start = startDate.getTime();
		for (int i = 0; i < 100000000; i++) {
			int j = 3;
		}
		Date firtDate = new Date();
		long first = firtDate.getTime();
		System.out.println("Java execute 1000000000 times generate (int) takes " + (first - start) + " milliseconds");
		for (int i = 0; i < 100000000; i++) {
			new String("test");
		}
		Date secondDate = new Date();
		long second =  secondDate.getTime();
		System.out.println("Java execute 1000000000 times generate (String) takes " + (second - first) + " "
				+ "milliseconds");


		for (int i = 0; i < 100000000; i++) {
			ArrayList<String> list = new ArrayList<String>();
		}
		Date thirdDate = new Date();
		long  third = thirdDate.getTime();
		System.out.println("Java execute 1000000000 times generate (ArrayList<String>) takes " + (third - second) + " "
				+ "milliseconds");

		for (int i = 0; i < 100000000; i++) {
			String[] stringArray = new String[3];
		}
		Date endDate = new Date();
		long end = endDate.getTime();

		System.out.println("Java execute 1000000000 times generate (String[]) takes " + (end - third) + " milliseconds");

	}

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

	@Test
	public void testMethodInvoke() {

		System.out.println("start");
		Date dateOne = new Date();
		System.out.println(dateOne.getTime());
		invokeOne();
		Date dateTwo= new Date();
		System.out.println("invokeOne takes " + (dateTwo.getTime() - dateOne.getTime()) + " milliseconds");

		System.out.println(dateTwo.getTime());
		invokeTwo();
		Date dateThree = new Date();
		System.out.println("invokeTwo takes " + (dateThree.getTime() - dateTwo.getTime()) + " milliseconds");

		System.out.println(dateThree.getTime());
		invokeThree();
		Date dateFour = new Date();
		System.out.println("invokeThree takes " + (dateFour.getTime() - dateThree.getTime()) + " milliseconds");

		System.out.println(dateFour.getTime());
	}

	private void invokeOne() {
		System.out.println("invoke1");
	}

	private void invokeTwo() {
		System.out.println("invoke2");
	}

	private void invokeThree() {
		System.out.println("invoke3");
	}










}
