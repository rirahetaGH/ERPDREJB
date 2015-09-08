package com.erpdr.security.ejb;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import com.erpdr.object.security.dao.UserDAO;
import com.erpdr.object.security.to.ProfileOutTO;
import com.erpdr.object.security.to.UserAppInTO;
import com.erpdr.object.security.to.UserAppOutTO;

/**
 * Session Bean implementation class SecurityEJB
 */
@Stateless
public class SecurityEJB implements SecurityEJBRemote {

    /**
     * Default constructor. 
     */
    public SecurityEJB() {
        // TODO Auto-generated constructor stub
    }

    public UserAppOutTO UserValidate(UserAppInTO usr) {
		// TODO Auto-generated method stub
		UserAppOutTO usrValid = new UserAppOutTO();
		ProfileOutTO usrprofile = null;
		UserDAO userdao = new UserDAO();
		userdao.setIstransaccional(true);
		try {
			usrValid = userdao.getUserValid(usr);

			if (usrValid.getValidUser() == 0) {
				// usuario Valido
				System.out.println("validate " + usr.getIdUserApp());
				userdao.initCommon();
				usrprofile = userdao.getUsrProfileHeader(usr.getIdUserApp());
				usrValid.setUsrprofile(usrprofile);
			}
			// ToDo: forceconn
			if (userdao.getConn() != null) {

				userdao.getConn().close();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw (EJBException) new EJBException(e1);
		} finally {

			userdao.forceCloseConnection();
		}
		return usrValid;
	}
    
}
