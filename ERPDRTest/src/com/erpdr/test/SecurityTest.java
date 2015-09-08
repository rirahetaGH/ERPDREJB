package com.erpdr.test;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//import com.erpdr.client.SecurityEJBClient;
import com.erpdr.object.catalogos.Common;
import com.erpdr.object.common.to.ResultOutTO;
import com.erpdr.object.security.to.AdmProfileTO;
import com.erpdr.object.security.to.ProfileDetOutTO;
import com.erpdr.object.security.to.ProfileInTO;
import com.erpdr.object.security.to.ProfileTO;
import com.erpdr.object.security.to.UserAppInTO;
import com.erpdr.object.security.to.UserAppOutTO;
import com.erpdr.object.security.to.UserTO;
import com.erpdr.object.utilities.PasswordService;
//import com.erpdrapp.test.SecurityTest;

public class SecurityTest {
	private static SecurityEJBClient SecurityEJBService = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (SecurityEJBService == null)
			SecurityEJBService = new SecurityEJBClient();

		String v_method = args[0];

		/*
		 * List lstPeriods=new Vector();
		 * 
		 * lstPeriods=AccountingEJBService.getAccPeriods();
		 * 
		 * System.out.println(lstPeriods);
		 */

		try {
			SecurityTest.class.getMethod(args[0], null).invoke(null, null);
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

	public static void GetUserProfile() {
		// TODO Auto-generated method stub
		if (SecurityEJBService == null)
			SecurityEJBService = new SecurityEJBClient();

		UserAppInTO usr = new UserAppInTO();
		UserAppOutTO usrRes = new UserAppOutTO();

		usr.setIdUserApp("admin");
		usr.setPasswordUserApp("+a6GBN4BXm/BKh69vnLyYrmBovY=");

		usrRes = SecurityEJBService.UserValidate(usr);

		// TODO Auto-generated method stub
		System.out.println("Usuario Valido");
		System.out.println(usrRes.getValidUser());

		System.out.println("Datos perfil");
		System.out.println("id" + usrRes.getUsrprofile().getId_perfil());
		System.out.println("desc" + usrRes.getUsrprofile().getDesc_perfil());

		ProfileInTO profileInTO = new ProfileInTO();
		List profileOutTO = new Vector();

		profileOutTO = SecurityEJBService.GetUserProfile_Mtto(1);

		//System.out.println(profileOutTO.getDesc_perfil());
		while (true) {
			ProfileDetOutTO profileDetOutTO = new ProfileDetOutTO();
			List profileDetOutTOLst = new Vector();
			profileDetOutTOLst = profileOutTO;
			// System.out.println(profileDetOutTO.getDesc_perfil_det());

			Iterator<ProfileDetOutTO> iterator = profileDetOutTOLst.iterator();
			while (iterator.hasNext()) {
				// System.out.println(iterator.next());
				ProfileDetOutTO profileDetOutTO1 = (ProfileDetOutTO) iterator
						.next();

				System.out
						.println("->" + profileDetOutTO1.getDesc_perfil_det()+profileDetOutTO1.getStatus());
				if (profileDetOutTO1.getNodeDetail() != null) {

					// ProfileDetOutTO
					// profileDetOutTO2=(ProfileDetOutTO)iterator.next();
					List profileDetOutTOLst1 = profileDetOutTO1.getNodeDetail();
					Iterator<ProfileDetOutTO> iterator1 = profileDetOutTOLst1
							.iterator();

					try {
						while (iterator1.hasNext()) {
							ProfileDetOutTO profileDetOutTO3 = (ProfileDetOutTO) iterator1
									.next();

							System.out.println("-->"
									+ profileDetOutTO3.getDesc_perfil_det());
							System.out.println("-->"
									+ profileDetOutTO3.getUrl_perfil_det());
							if (profileDetOutTO3.getNodeDetail() != null) {

								// ProfileDetOutTO
								// profileDetOutTO2=(ProfileDetOutTO)iterator.next();
								List profileDetOutTOLst2 = profileDetOutTO3
										.getNodeDetail();
								Iterator<ProfileDetOutTO> iterator2 = profileDetOutTOLst2
										.iterator();
								try {

									while (iterator2.hasNext()) {
										ProfileDetOutTO profileDetOutTO4 = (ProfileDetOutTO) iterator2
												.next();
										System.out.println("--->"
												+ profileDetOutTO4
														.getDesc_perfil_det());
										System.out.println("--->"
												+ profileDetOutTO4
														.getUrl_perfil_det());
									}

								} catch (Exception ex) {

								}

							}
						}

					} catch (Exception ex) {

					}

				}
			}

			break;
		}
		// UserDAO userdao = new UserDAO();
		// System.out.println("Conexion");
		// userdao.getUserValid();
	}

	public static void user_mtto() throws Exception {

		int _result;
		UserTO parameters = new UserTO();

		// parameters.setUsersign();
		parameters.setNickname("admin3");
		parameters.setUsername("Juan Carlos");
		parameters.setLastname("Castro");
		parameters.setPassword(PasswordService.getInstance().encrypt("jc"));
		parameters.setProfilecode(1);
		parameters.setLocked("N");
		parameters.setCusersign(0);

		// Agregar

		_result = SecurityEJBService.cat_users_mtto(parameters,
				Common.MTTOINSERT);

		// Actualizar
		/*
		 * parameters.setUsersign(11);
		 * parameters.setPassword(PasswordService.getInstance().encrypt("JC"));
		 * _result=SecurityEJBService.cat_users_mtto(parameters,
		 * Common.MTTOUPDATE);
		 */

		// Borrar
		/*
		 * parameters.setUsersign(13);
		 * _result=SecurityEJBService.cat_users_mtto(parameters,
		 * Common.MTTODELETE);
		 */

		System.out.println("luego de servicio");
		System.out.println(_result);

	}

	public static void getUser() {
		List resp = null;

		String name = null;
		String code = "art-001";

		resp = SecurityEJBService.getUser();

		Iterator<UserTO> iterator = resp.iterator();
		while (iterator.hasNext()) {
			UserTO user = (UserTO) iterator.next();
			System.out.println(user.getNickname() + " - " + user.getUsername()
					+ " - " + user.getUserdate());
		}
	}
	
	public static void getProfile() throws Exception {
		List resp = null;

		String name = null;
		String code = "art-001";

		resp = SecurityEJBService.getProfile(name);

		Iterator<ProfileTO> iterator = resp.iterator();
		while (iterator.hasNext()) {
			ProfileTO profile = (ProfileTO) iterator.next();
			System.out.println(profile.getProfilecode() + " - " + profile.getProfilename());
		}
	}
	

	public static void getUserByNickname() throws Exception {
		UserTO user = new UserTO();

		String name = "jccc";
		String code = "art-001";

		user = SecurityEJBService.getUserByNickname(name);

		System.out.println(user.getNickname() + " - " + user.getUsername()
				+ " - " + user.getUserdate());

	}
	
	
	public static void user1_profile_mtto() throws Exception {

		ResultOutTO _result= new ResultOutTO();
		ProfileTO parameters = new ProfileTO();

		// parameters.setUsersign();
		parameters.setActive("Y");
		parameters.setProfilecode(5);
		parameters.setProfilename("administrador");

		// Agregar

		_result = SecurityEJBService.Usr1_profile_mtto(parameters,
				3);

		System.out.println("luego de servicio");
		System.out.println(_result.getCodigoError()+"-- "+_result.getMensaje());

	}
	public static void getUser1Profile() throws Exception {
		//ProfileTO user = new ProfileTO();
		ProfileTO user2 = new ProfileTO();
		List resul= new Vector();
		String name = "jccc";
		String code = "art-001";

		//resul = SecurityEJBService.getUsr1Profile();
		user2=SecurityEJBService.getUsr1Profile_by_key(1);
		System.out.println(user2.getActive() + " - " + user2.getProfilecode());

		//System.out.println(user.getNickname() + " - " + user.getUsername()
			//	+ " - " + user.getUserdate());
		/*Iterator<ProfileTO> iterator = resul.iterator();
		while (iterator.hasNext()) {
			ProfileTO user = (ProfileTO) iterator.next();
			System.out.println(user.getActive() + " - " + user.getProfilecode());
		}*/

	}
	
	
	public static void adm_profile_mtto() throws Exception {

		ResultOutTO _result= new ResultOutTO();
		AdmProfileTO parameters = new AdmProfileTO();

		// parameters.setUsersign();
	
		parameters.setProfilecode(2);
		parameters.setDoccode("30");

		// Agregar

		_result = SecurityEJBService.adm_profile_mtto(parameters,
				3);

		System.out.println("luego de servicio");
		System.out.println(_result.getCodigoError()+"-- "+_result.getMensaje());

	}
	
	public static void getadmProfile() throws Exception {
		//ProfileTO user = new ProfileTO();
		ProfileTO user2 = new ProfileTO();
		List resul= new Vector();
		String name = "jccc";
		String code = "art-001";

		//resul = SecurityEJBService.getUsr1Profile();
		resul=SecurityEJBService.getAdmProfile_by_key(1);
		System.out.println(user2.getActive() + " - " + user2.getProfilecode());

		//System.out.println(user.getNickname() + " - " + user.getUsername()
			//	+ " - " + user.getUserdate());
		Iterator<AdmProfileTO> iterator = resul.iterator();
		while (iterator.hasNext()) {
			AdmProfileTO user = (AdmProfileTO) iterator.next();
			System.out.println(user.getDoccode() + " - " + user.getProfilecode());
		}

	}

}
