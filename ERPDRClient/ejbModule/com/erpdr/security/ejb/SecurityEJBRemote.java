package com.erpdr.security.ejb;

import javax.ejb.Remote;

import com.erpdr.object.security.to.UserAppInTO;
import com.erpdr.object.security.to.UserAppOutTO;

@Remote
public interface SecurityEJBRemote {
	 public UserAppOutTO UserValidate(UserAppInTO usr);
}
