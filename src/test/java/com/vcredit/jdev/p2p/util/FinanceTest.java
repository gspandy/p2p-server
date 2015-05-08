package com.vcredit.jdev.p2p.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.Finance;
import org.junit.Test;

/**
 * 测试财务算法
 * 
 * @author ChenChang:
 */
public class FinanceTest {

	//年利率
	double rate = 0.115;
	//第几期
	int per = 2;
	//总期数
	int nper = 24;
	//本金
	int pv = 32000;

	@Test
	public void testCase20150323() {
		double pv = 50000;
		int nper = 12;
		double r = 0.13/12;

		//		应还本息
		//		应还本金
		//		应还利息

		for (int i = 1; i <= nper; i++) {
			System.out.print(FinanceUtil.PMT(r, i, nper, pv) + "       ");
			System.out.print(FinanceUtil.PPMT(r, i, nper, pv) + "       ");
			System.out.println(FinanceUtil.IPMT(r, i, nper, pv));
		}
	}

	/**
	 * 
	 */
	@Test
	public void testPMT() {
		System.out.println(FinanceUtil.PMT(rate / 12, 12, nper, pv));
	}

	@Test
	public void testIPMT() {
		System.out.println(FinanceUtil.IPMT(rate / 12, per, nper, pv));
	}

	@Test
	public void testPPMT() {
		System.out.println(FinanceUtil.PPMT(rate / 12, per, nper, pv));
	}

	@Test
	public void testLP() {
		System.out.println(FinanceUtil.LP(rate / 12, per, nper, pv));
	}

	@Test
	public void testCalcYield() {
		System.out.println(FinanceUtil.calcYieldBase(rate / 12, nper, pv));
		System.out.println(FinanceUtil.calcYield4P2pPmt(rate / 12, nper, pv));
	}

	@Test
	public void testCalcWeightedAvgYieldRate() {
		//1000,0.11
		//2000,0.105
		//5000,0.12
		//3000,0.11
		//（1000*0.11+2000*0.105+5000*0.12+3000*0.11）/（1000+2000+5000+3000）=11.36%

		Map<Integer, Double> hm = new HashMap<Integer, Double>();
		hm.put(1000, 0.11);
		hm.put(2000, 0.105);
		hm.put(5000, 0.12);
		hm.put(3000, 0.11);

		System.out.println(FinanceUtil.calcWeightedAvgYieldRate(hm));
	}
}
