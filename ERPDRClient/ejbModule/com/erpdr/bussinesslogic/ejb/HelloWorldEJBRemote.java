package com.erpdr.bussinesslogic.ejb;

import javax.ejb.Remote;

@Remote
public interface HelloWorldEJBRemote {
	public String SayHello(String Message);
}
