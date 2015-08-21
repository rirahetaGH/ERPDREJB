package com.erpdr.client;

import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;



import com.erpdr.bussinesslogic.ejb.HelloWorldEJBRemote;
import com.erpdr.client.ClientUtility;

public class HelloWorldEJBClient {
	private static final String LOOKUP_STRING = "java:global/ERPDREAR/ERPDR/HelloWorldEJB!com.erpdr.bussinesslogic.ejb.HelloWorldEJBRemote";
	private static HelloWorldEJBRemote bean;
	private static Context context = null;

	public HelloWorldEJBClient() {

		// 2. Lookup and cast
		try {
			context = ClientUtility.getInitialContext();
			bean = (HelloWorldEJBRemote) context.lookup(LOOKUP_STRING);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String SayHello(String Message) throws Exception {
		// TODO Auto-generated method stub
		String _return;

		_return=bean.SayHello(Message);

		return _return;
	}
}
