package org.codingwater.controller.loadbalance;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestDynamicLB {

	final static String[] caption = { "fast", "common", "slow" };

	 static double[][] respSample = new double[][] {
			{ 0.060d, 0.001d }, // 60ms, fast
			{ 0.200d, 0.01d }, // 200ms, common
			{ 0.500d, 0.001d }// 500ms, slow
	};

	 static double[] opOKSample = new double[] {
			0.9999d, // ten-thousandth
			0.999d, // thousandth
			0.05d, // 5%
	};

	 static double[][] loadSample = new double[][] {
			{ 20d, 0.01d }, // lightweight
			{ 40d, 0.01d }, // normal
			{ 60d, 0.01d }// overload
	};

	public static void main(String[] args) {
		ConsoleAppender ca = new ConsoleAppender();
		ca.setWriter(new OutputStreamWriter(System.out));
		ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
		ca.setName("myfoobar");
		LogManager.getRootLogger().addAppender(ca);
		LogManager.getRootLogger().setLevel(Level.INFO);

//		test(5, 1000, new int[] { 0, 1, 2, 0, 1 });
//		test(5, 1000, new int[] { 0, 2, 2, 2, 1 });
//		test(5, 1000, new int[] { 0, 1, 2, 1, 1 });

		System.out.println("****************************************************************************************");

//		test(5, 1000, new int[] { 0, 0, 0, 0, 0 });
//		test(5, 1000, new int[] { 1, 1, 1, 1, 1 });
		test(10, 1000, new int[] { 2, 2, 2, 2, 2 ,2 ,2 ,2 ,2 ,2 });
//		test(5, 1000, new int[] { 0, 0, 0, 0, 2 });


	}

	public static List<Nodes> test(int loop, int count, int[] profiles) {
		DynamicLB lb = new DynamicLB(profiles.length, 100, 5000);
//		for (int i = 0; i < loop; i++) { // turn
			List<Integer> samples = new ArrayList<Integer>();
			//计算出概率
			simulation(profiles, count, lb);

			//根据生成的累加概率积分进行随机采样，统计10000次中各个节点被选择的次数
			for (int j = 0; j < 10000; j++) {
				int pos = lb.sample();
				//做一万次采样
				samples.add(pos);
			}
			//对样品进行统计.
			//对采样数据整理
			Map<Integer, Long> ret = samples.stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			String[] title = new String[profiles.length];

			List<Nodes> nodesList = new ArrayList<>();
			for (int j = 0; j < profiles.length; j++) {

				Nodes nodes = new Nodes();
				nodes.setCaption(caption[profiles[j]]);
				System.out.println(ret.get(j).intValue());
				nodes.setCount(String.valueOf(ret.get(j)));
				nodesList.add(nodes);

				title[j] = caption[profiles[j]];
			}




			System.out.println(Arrays.toString(title) + " ==> " + ret);

		return nodesList;
//		}
	}

	/**
	 * 更新响应时间，失败率，负载load，更新后重新计算每个节点的选择概率
	 * @param profile
	 * @param count
	 * @param lb
	 */
	static double[] simulation(int[] profile, int count, DynamicLB lb) {
		Random r = new Random();
		//生成正态分布数组
		NormalDistribution[] profiles = new NormalDistribution[profile.length];

		for (int i = 0; i < profile.length; i++) {
			//根据sample中的均值和标准差为每个节点生成对应的正态分布
			profiles[i] = new NormalDistribution(respSample[profile[i]][0], respSample[profile[i]][1]);
		}
		NormalDistribution[] loads = new NormalDistribution[profile.length];
		for (int i = 0; i < profile.length; i++) {
			loads[i] = new NormalDistribution(loadSample[profile[i]][0], loadSample[profile[i]][1]);
		}

		int[] load = new int[lb.nodeCount()];
		for (int j = 0; j < lb.nodeCount(); j++) {
			for (int i = 0; i < count; i++) { // how many records
				//根据标准正态分布来随机生成响应时间，接近均值的值生成可能性大
				double resp = profiles[j].sample();
				boolean ok = r.nextDouble() < opOKSample[profile[j]] ? true : false;
				lb.updateOneResp(j, resp, ok);
			}
			load[j] = Double.valueOf(loads[profile[j]].sample()).intValue();
		}
		lb.updateActive(load);

		return lb.flashChoice();
	}
}
