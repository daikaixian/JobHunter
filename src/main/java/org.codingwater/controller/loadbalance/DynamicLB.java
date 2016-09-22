package org.codingwater.controller.loadbalance;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * DYNAMIC LOAD BALANCE
 * 
 * <pre>
 * 
 * 使用三个维度的指标去猜测该路由的结点
 * 1. 响应时间resp：以500ms为100%做归一化
 * 2. 失败率fail：成功为0.1，失败为1
 * 3. 负载load：以50个连接为100%做归一化
 * 
 * 以负ln求和作为activation函数：
 * E = (-1) * ( wResp * ln(resp) + wRespAvg * ln(respAvg) 
 *            + wLoad * ln(load) + wLoadAvg * ln(loadAvg)
 *            + wFail * ln(fail) + wFailAvg * ln(failAvg)
 *            )
 * 
 * 以activation函数作为各结点被路由的选择概率：
 * P_N1 = E_N1 / (E_N1 + E_N2 + ... + E_Nn)
 * 
 * 
 * 这三个维度的采样，分为两个统计窗口：
 * 1. 小窗：代表最近的微观审计
 * 2. 大窗：代表周期性的宏观审计
 * 
 * To be done:
 * 		增加小窗与大窗的梯度计算，根据梯度及动量变化，来修正activation中的wXXXX
 * 
 * </pre>
 * 
 * @author laurence
 *
 */
public class DynamicLB {

	static Logger log = LoggerFactory.getLogger(DynamicLB.class);

	protected double RESP_REF = 0.5d; // 500ms ==> 100%
	protected double LOAD_REF = 50d; // 50 connections ==> 100%

	protected double wResp = 0.2; // weight of response
	protected double wLoad = 0.15; // weight of Load
	protected double wFail = 0.4; // weight of Failure

	protected double wRespAvg = 0.1; // weight of response history
	protected double wLoadAvg = 0.05; // weight of Load history
	protected double wFailAvg = 0.1; // weight of Failure history

	protected int nodeCount;

	protected DescriptiveStatistics[] resp; // response in milliseconds
	protected DescriptiveStatistics[] load; // borrowed connection
	protected DescriptiveStatistics[] fail; // successful = 0, failure = 1

	protected DescriptiveStatistics[] respAvg; // response in microseconds
												// history
	protected DescriptiveStatistics[] loadAvg; // borrowed connection history
	protected DescriptiveStatistics[] failAvg; // successful = 0, failure = 1
												// history

	protected double[] choice; // choice probability


	// nodeCount为备选节点数量,最后要计算出一个对应的数组,记录选取每个节点的概率.
	public DynamicLB(int nodeCount, int latestWindowSize, int avgWindowSize) {
		this.nodeCount = nodeCount;
		this.choice = new double[nodeCount];
		log.info("initialized load balance for nodes = " + nodeCount);
		resp = new DescriptiveStatistics[nodeCount];
		load = new DescriptiveStatistics[nodeCount];
		fail = new DescriptiveStatistics[nodeCount];

		respAvg = new DescriptiveStatistics[nodeCount];
		loadAvg = new DescriptiveStatistics[nodeCount];
		failAvg = new DescriptiveStatistics[nodeCount];

		for (int i = 0; i < nodeCount; i++) {
			resp[i] = new DescriptiveStatistics();
			resp[i].setWindowSize(latestWindowSize);
			load[i] = new DescriptiveStatistics();
			load[i].setWindowSize(latestWindowSize);
			fail[i] = new DescriptiveStatistics();
			fail[i].setWindowSize(latestWindowSize);

			respAvg[i] = new DescriptiveStatistics();
			respAvg[i].setWindowSize(avgWindowSize);
			loadAvg[i] = new DescriptiveStatistics();
			loadAvg[i].setWindowSize(avgWindowSize);
			failAvg[i] = new DescriptiveStatistics();
			failAvg[i].setWindowSize(avgWindowSize);
		}
	}

	public int nodeCount() {
		return nodeCount;
	}

	protected void updateOneResp(int i, double resp1, boolean ok1) {
		synchronized (resp) {

			resp[i].addValue(resp1);
			fail[i].addValue(ok1 ? 0.1 : 1);

			respAvg[i].addValue(resp1);
			failAvg[i].addValue(ok1 ? 0.1 : 1);

		}
	}

	protected void updateActive(int[] active) {
		synchronized (load) { // ordering for write
			for (int i = 0; i < load.length; i++) {
				load[i].addValue(active[i]);

				loadAvg[i].addValue(active[i]);
			}
		}
	}

	protected synchronized double[] flashChoice() {
		double total = 0d;
		for (int i = 0; i < choice.length; i++) {
			double resp_snap = resp[i].getMean();
			double load_snap = load[i].getMean();
			double fail_snap = fail[i].getMean();

			double respAvg_snap = respAvg[i].getMean();
			double loadAvg_snap = loadAvg[i].getMean();
			double failAvg_snap = failAvg[i].getMean();

			double resp_rate = Math.min(resp_snap / RESP_REF, 1d);
			double load_rate = Math.min(load_snap / LOAD_REF, 1d);
			double fail_rate = fail_snap;

			double respAvg_rate = Math.min(respAvg_snap / RESP_REF, 1d);
			double loadAvg_rate = Math.min(loadAvg_snap / LOAD_REF, 1d);
			double failAvg_rate = failAvg_snap;

			/**
			 * E = (-1) * ( wResp * ln(resp) + wRespAvg * ln(respAvg)
			 *            + wLoad * ln(load) + wLoadAvg * ln(loadAvg)
			 *            + wFail * ln(fail) + wFailAvg * ln(failAvg)
			 *            )
			 * choice[i] = E
			 */
			choice[i] = (-1) * (wResp * Math.log(resp_rate) + wLoad * Math.log(load_rate)
					+ wRespAvg * Math.log(respAvg_rate) + wLoadAvg * Math.log(loadAvg_rate)
					+ wFail * Math.log(fail_rate) + wFailAvg * Math.log(failAvg_rate));
			total += choice[i];
		}

		for (int i = 0; i < choice.length; i++) {
			/**
			 * P_N1 = E_N1 / (E_N1 + E_N2 + ... + E_Nn)
			 * choice[i] = P
			 */
			choice[i] /= total;
			if (i > 0) {
				choice[i] += choice[i - 1];
			}
		}
		log.info("flash choice = " + Arrays.toString(choice));
		return choice;
	}

	/**
	 * multinomial sampling
	 * 
	 * @return
	 */
	public int sample() {
		double s = Math.random();
		for (int i = 0; i < choice.length; i++) {
			// 从小到大排列,比较随机出来的概率位于哪个区间.
			if (s < choice[i]) {
				return i;
			}
		}
		return choice.length - 1;
	}

}
