package com.erpdr.bussinesslogic.ejb;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class HelloWorldEJB
 */
@Stateless
public class HelloWorldEJB implements HelloWorldEJBRemote {

    /**
     * Default constructor. 
     */
    public HelloWorldEJB() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public String SayHello(String Message) {
		// TODO Auto-generated method stub
		String _return;
		//System.out.println(Message);
		_return=Message + " From EJB";
		return _return;
	}

}
