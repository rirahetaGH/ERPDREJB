package com.erpdr.test;

import java.lang.reflect.InvocationTargetException;

import com.erpdr.client.HelloWorldEJBClient;

public class HelloWorldTest {
	private static HelloWorldEJBClient HelloWorldEJBService;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (HelloWorldEJBService == null)
			HelloWorldEJBService = new HelloWorldEJBClient();

		String v_method = args[0];

		/*
		 * List lstPeriods=new Vector();
		 * 
		 * lstPeriods=AccountingEJBService.getAccPeriods();
		 * 
		 * System.out.println(lstPeriods);
		 */
		try {
			HelloWorldTest.class.getMethod(args[0], null).invoke(null, null);
			// testPeriods();

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SayHello(){
		
		try {
			HelloWorldEJBService.SayHello("primer ejb");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
