package com.erpdr.object.dao.common;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

import com.erpdr.client.ClientUtility;
import com.erpdr.object.catalogos.Common;
import com.erpdr.object.common.to.DetailParameter;
import com.sun.rowset.CachedRowSetImpl;

public class CommonDAO {
	public Connection conn;
	private static final String DB_JDBC = "jdbc/erpdrJDBC";
	private static final String CALLABLETYPE = "CALLABLE";
	private static final String PREPAREDTYPE = "PREPARED";
	private static Context context = null;
	private boolean istransaccional = false;
	private String typeStatement = null;
	private Hashtable inParameters;
	private Hashtable outParameters;
	private String dbObject;

	private String typeReturn;

	public CommonDAO() {

		this.initCommon();

	}

	/*
	 * Reutilizando conexion
	 */
	public CommonDAO(Connection _conn) {

		this.initCommon(_conn);

	}

	public void closeConnection() {
		try {
			if (this.getIstransaccional() == false && this.conn != null) {
				this.conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void forceCloseConnection() {
		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rollBackConnection() {
		try {
			if (this.conn != null) {
				this.conn.rollback();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Forza el commit para operaciones transaccionales
	 */
	public void forceCommit() {
		try {
			if (this.conn != null) {
				this.conn.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

	private void insertParam(int position, String colName, Object colValue,
			String colType) {

		DetailParameter detailParameter = new DetailParameter(position,
				colName, colValue, colType);

		this.inParameters.put(new Integer(position), detailParameter);

	}

	/*
	 * 
	 */
	public void setString(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPESTRING);
	}

	/*
	 * 
	 */
	public void setArrayString(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPESARRAYTRING);
	}

	/*
	 * 
	 */
	public void setInt(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPEINT);
	}

	/*
	 * 
	 */
	public void setBigDecimal(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPEBIGDECIMAL);
	}

	/*
	 * 
	 */
	public void setLong(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPELONG);
	}

	/*
	 * 
	 * */
	public void setFloat(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPEFLOAT);
	}

	/*
	 * 
	 * 
	 */
	public void setDouble(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPEDOUBLE);
	}

	public void setDate(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPEDATE);
	}

	public void setBool(int position, String colName, Object colValue) {
		this.insertParam(position, colName, colValue, Common.TYPEBOOL);
	}

	/*
	 * 
	 */
	public Connection getConn() {
		return conn;
	}

	/*
	 * Ejecuta sps de insercion, update o delete
	 */
	public int runUpdate() throws Exception {

		CallableStatement statementToExecute = null;
		int v_result = 0;

		if (this.getTypeReturn() == null) {
			this.setTypeReturn(Common.TYPERETURN_INT);
		}

		try {
			statementToExecute = this.getConn().prepareCall(this.getDbObject());
			// this.getConn().setAutoCommit(!this.istransaccional);
			if (this.istransaccional) {
				this.getConn().setAutoCommit(false);
			}
			// this.getConn().commit(); afuera en dao
			// asignamos parametros

			Enumeration enParameters = this.inParameters.keys();
			DetailParameter dtParameterTmp = null;
			Integer v_position = null;
			// Recorremos hash con parametros previamente seteados
			while (enParameters.hasMoreElements()) {

				v_position = (Integer) enParameters.nextElement();

				dtParameterTmp = (DetailParameter) this.inParameters
						.get(v_position);

				System.out.println("detail: position " + v_position);
				System.out.println("detail: getColName "
						+ dtParameterTmp.getColName());
				System.out.println("detail: getColValue "
						+ dtParameterTmp.getColValue());
				System.out.println("detail: getColType ");
				System.out.println(dtParameterTmp.getColType());

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPESTRING)) {
					statementToExecute.setString(v_position.intValue(),
							(String) dtParameterTmp.getColValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPESARRAYTRING)) {
					statementToExecute.setArray(v_position.intValue(),
							(Array) dtParameterTmp.getColValue());
				}

				if (dtParameterTmp.getColType()
						.equalsIgnoreCase(Common.TYPEINT)) {
					statementToExecute
							.setInt(v_position.intValue(),
									((Integer) dtParameterTmp.getColValue())
											.intValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPELONG)) {
					statementToExecute.setLong(v_position.intValue(),
							((Long) dtParameterTmp.getColValue()).longValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEFLOAT)) {
					statementToExecute
							.setFloat(v_position.intValue(),
									((Float) dtParameterTmp.getColValue())
											.floatValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDOUBLE)) {
					statementToExecute.setDouble(v_position.intValue(),
							((Double) dtParameterTmp.getColValue())
									.doubleValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDATE)) {
					statementToExecute.setDate(v_position.intValue(),
							((Date) dtParameterTmp.getColValue()));
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEBOOL)) {
					statementToExecute.setBoolean(v_position.intValue(),
							(Boolean) (dtParameterTmp.getColValue()));
				}
			}

			/*
			 * if (this.getTypeReturn().equals(Common.TYPERETURN_INT)){
			 * statementToExecute.registerOutParameter(1,Types.INTEGER); }
			 */

			v_result = statementToExecute.executeUpdate();
			// System.out.println("resultado");
			// System.out.println(statementToExecute.getInt(1));
			/*
			 * if (this.getTypeReturn().equals(Common.TYPERETURN_INT)){
			 * this.outParameters.put(new Integer(1), new
			 * Integer(statementToExecute.getInt(1))); }
			 */

			// Cerramos el Statement
			if (statementToExecute != null) {
				statementToExecute.close();
			}
			// Cerramos la conexi�n
			this.closeConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {

		}
		return v_result;
	}

	/*
	 * 
	 */
	public List runQuery() throws Exception {

		// Primero creamos el prepareStatement

		CallableStatement statementToExecute = null;
		boolean v_haveResultsets = false;
		ResultSet rsData = null;
		CachedRowSet crsData = null;
		List rsDataList = new Vector();

		if (this.getTypeReturn() == null) {
			this.setTypeReturn(Common.TYPERETURN_INT);
		}

		try {
			statementToExecute = this.getConn().prepareCall(this.getDbObject());

			// asignamos parametros

			Enumeration enParameters = this.inParameters.keys();
			DetailParameter dtParameterTmp = null;
			Integer v_position = null;
			// Recorremos hash con parametros previamente seteados
			while (enParameters.hasMoreElements()) {

				v_position = (Integer) enParameters.nextElement();

				dtParameterTmp = (DetailParameter) this.inParameters
						.get(v_position);

				System.out.println("detail: position " + v_position);
				System.out.println("detail: getColName "
						+ dtParameterTmp.getColName());
				System.out.println("detail: getColValue "
						+ dtParameterTmp.getColValue());
				System.out.println("detail: getColType ");
				System.out.println(dtParameterTmp.getColType());

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPESTRING)) {
					statementToExecute.setString(v_position.intValue(),
							(String) dtParameterTmp.getColValue());
				}

				if (dtParameterTmp.getColType()
						.equalsIgnoreCase(Common.TYPEINT)) {
					statementToExecute
							.setInt(v_position.intValue(),
									((Integer) dtParameterTmp.getColValue())
											.intValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPELONG)) {
					statementToExecute.setLong(v_position.intValue(),
							((Long) dtParameterTmp.getColValue()).longValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEFLOAT)) {
					statementToExecute
							.setFloat(v_position.intValue(),
									((Float) dtParameterTmp.getColValue())
											.floatValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDOUBLE)) {
					statementToExecute.setDouble(v_position.intValue(),
							((Double) dtParameterTmp.getColValue())
									.doubleValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDATE)) {
					statementToExecute.setDate(v_position.intValue(),
							(Date) dtParameterTmp.getColValue());
				}

			}
			if (this.getTypeReturn().equals(Common.TYPERETURN_INT)) {
				statementToExecute.registerOutParameter(1, Types.INTEGER);
			}

			if (this.getTypeReturn().equals(Common.TYPERETURN_CURSOR)) {
				statementToExecute.registerOutParameter(1, Types.OTHER);
				this.getConn().setAutoCommit(false);
			}

			v_haveResultsets = statementToExecute.execute();
			// System.out.println("resultado");
			// System.out.println(statementToExecute.getInt(1));
			if (this.getTypeReturn().equals(Common.TYPERETURN_INT)) {
				this.outParameters.put(new Integer(1), new Integer(
						statementToExecute.getInt(1)));
			}

			// analizando resultsets recibidos
			System.out.println("v_haveResultsets");
			System.out.println(v_haveResultsets);
			if (v_haveResultsets
					|| this.getTypeReturn().equals(Common.TYPERETURN_CURSOR)) {
				System.out.println("tiene resultsets");
				if (this.getTypeReturn().equals(Common.TYPERETURN_RESULTSET)) {
					rsData = statementToExecute.getResultSet();
				}
				if (this.getTypeReturn().equals(Common.TYPERETURN_CURSOR)) {
					rsData = (ResultSet) statementToExecute.getObject(1);
				}
				if (rsData != null) {
					rsData.clearWarnings();
					crsData = new CachedRowSetImpl();
					crsData.populate(rsData);
					rsDataList.add(crsData);
					// closeToDB (rsData);
					rsData = null;
					crsData = null;
				}

			}

			// Cerramos la conexion
			if (statementToExecute != null) {
				statementToExecute.close();
			}

			this.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {

		}
		return rsDataList;

	}

	/*
	 * Ejecuta un query string directamente a la base de datos sin sp de por
	 * medio Rutilio Iraheta Marzo 2015
	 */
	public List runQueryPrepared() throws Exception {

		// Primero creamos el prepareStatement

		PreparedStatement statementToExecute = null;
		boolean v_haveResultsets = false;
		ResultSet rsData = null;
		CachedRowSet crsData = null;
		List rsDataList = new Vector();

		if (this.getTypeReturn() == null) {
			this.setTypeReturn(Common.TYPERETURN_INT);
		}

		try {
			statementToExecute = this.getConn().prepareStatement(
					this.getDbObject());

			// asignamos parametros

			Enumeration enParameters = this.inParameters.keys();
			DetailParameter dtParameterTmp = null;
			Integer v_position = null;
			// Recorremos hash con parametros previamente seteados
			while (enParameters.hasMoreElements()) {

				v_position = (Integer) enParameters.nextElement();

				dtParameterTmp = (DetailParameter) this.inParameters
						.get(v_position);

				System.out.println("detail: position " + v_position);
				System.out.println("detail: getColName "
						+ dtParameterTmp.getColName());
				System.out.println("detail: getColValue "
						+ dtParameterTmp.getColValue());
				System.out.println("detail: getColType ");
				System.out.println(dtParameterTmp.getColType());

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPESTRING)) {
					statementToExecute.setString(v_position.intValue(),
							(String) dtParameterTmp.getColValue());
				}

				if (dtParameterTmp.getColType()
						.equalsIgnoreCase(Common.TYPEINT)) {
					statementToExecute
							.setInt(v_position.intValue(),
									((Integer) dtParameterTmp.getColValue())
											.intValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPELONG)) {
					statementToExecute.setLong(v_position.intValue(),
							((Long) dtParameterTmp.getColValue()).longValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEFLOAT)) {
					statementToExecute
							.setFloat(v_position.intValue(),
									((Float) dtParameterTmp.getColValue())
											.floatValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDOUBLE)) {
					statementToExecute.setDouble(v_position.intValue(),
							((Double) dtParameterTmp.getColValue())
									.doubleValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDATE)) {
					statementToExecute.setDate(v_position.intValue(),
							(Date) dtParameterTmp.getColValue());
				}

			}

			rsData = statementToExecute.executeQuery();
			// System.out.println("resultado");
			// System.out.println(statementToExecute.getInt(1));

			if (rsData != null) {
				System.out.println("encontr� datos");
				rsData.clearWarnings();
				crsData = new CachedRowSetImpl();
				crsData.populate(rsData);
				rsDataList.add(crsData);
				// closeToDB (rsData);
				rsData = null;
				crsData = null;
			}

			// Cerramos la conexion
			if (statementToExecute != null) {
				statementToExecute.close();
			}

			this.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {

		}
		return rsDataList;

	}

	public List runQueryUpdate() throws Exception {

		// Primero creamos el prepareStatement

		PreparedStatement statementToExecute = null;
		boolean v_haveResultsets = false;
		int rsData = 0;
		CachedRowSet crsData = null;
		List rsDataList = new Vector();

		if (this.getTypeReturn() == null) {
			this.setTypeReturn(Common.TYPERETURN_INT);
		}

		try {
			statementToExecute = this.getConn().prepareStatement(
					this.getDbObject());

			// asignamos parametros

			Enumeration enParameters = this.inParameters.keys();
			DetailParameter dtParameterTmp = null;
			Integer v_position = null;
			// Recorremos hash con parametros previamente seteados
			while (enParameters.hasMoreElements()) {

				v_position = (Integer) enParameters.nextElement();

				dtParameterTmp = (DetailParameter) this.inParameters
						.get(v_position);

				System.out.println("detail: position " + v_position);
				System.out.println("detail: getColName "
						+ dtParameterTmp.getColName());
				System.out.println("detail: getColValue "
						+ dtParameterTmp.getColValue());
				System.out.println("detail: getColType ");
				System.out.println(dtParameterTmp.getColType());

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPESTRING)) {
					statementToExecute.setString(v_position.intValue(),
							(String) dtParameterTmp.getColValue());
				}

				if (dtParameterTmp.getColType()
						.equalsIgnoreCase(Common.TYPEINT)) {
					statementToExecute
							.setInt(v_position.intValue(),
									((Integer) dtParameterTmp.getColValue())
											.intValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPELONG)) {
					statementToExecute.setLong(v_position.intValue(),
							((Long) dtParameterTmp.getColValue()).longValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEFLOAT)) {
					statementToExecute
							.setFloat(v_position.intValue(),
									((Float) dtParameterTmp.getColValue())
											.floatValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDOUBLE)) {
					statementToExecute.setDouble(v_position.intValue(),
							((Double) dtParameterTmp.getColValue())
									.doubleValue());
				}

				if (dtParameterTmp.getColType().equalsIgnoreCase(
						Common.TYPEDATE)) {
					statementToExecute.setDate(v_position.intValue(),
							(Date) dtParameterTmp.getColValue());
				}

			}
			 rsData = statementToExecute.executeUpdate();
			// System.out.println("resultado");
			// System.out.println(statementToExecute.getInt(1));

			/*if (rsData != null) {
				System.out.println("encontr� datos");
				rsData.clearWarnings();
				crsData = new CachedRowSetImpl();
				crsData.populate(rsData);
				rsDataList.add(crsData);
				// closeToDB (rsData);
				rsData = null;
				crsData = null;
			}*/

			// Cerramos la conexion
			if (statementToExecute != null) {
				statementToExecute.close();
			}

			this.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		} finally {

		}
		return rsDataList;

	}

	private void getConnectionDB() {
		if (conn == null) {
			try {

				// Class.forName(DB_DRIVER);
				context = ClientUtility.getInitialContext();
				DataSource datasource = (DataSource) context.lookup(DB_JDBC);
				this.conn = datasource.getConnection();

			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public boolean getIstransaccional() {
		return istransaccional;
	}

	public void setIstransaccional(boolean istransaccional) {
		this.istransaccional = istransaccional;
	}

	public String getDbObject() {
		return dbObject;
	}

	public void setDbObject(String dbObject) {
		this.dbObject = dbObject;
	}

	public int getInt() {
		int returnCode = 0;

		returnCode = ((Integer) this.outParameters.get(new Integer(1)))
				.intValue();

		return returnCode;
	}

	public String getTypeReturn() {
		return typeReturn;
	}

	public void setTypeReturn(String typeReturn) {
		this.typeReturn = typeReturn;
	}

	public void initCommon() {

		/*
		 * this.getConnectionDB(); this.inParameters = new Hashtable();
		 * this.outParameters = new Hashtable();
		 */
		this.initCommon(null);

	}

	/**
	 * Reutilizando conexion
	 * 
	 * @param _conn
	 */
	public void initCommon(Connection _conn) {
		if (_conn == null) {
			this.getConnectionDB();
		} else {
			this.setConn(_conn);
		}

		this.inParameters = new Hashtable();
		this.outParameters = new Hashtable();

	}
}
