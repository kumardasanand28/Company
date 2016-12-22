package appointmentclaim.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.PreparedStatement;




public class AppointmentClaimDAO {


	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/claims";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "root";



	public Map<String,String> fetchAllAppointMents(){
		Map<String,String> appointmentClaimMap = new HashMap<String,String>();

		try {
			Connection connection = getDBConnection();  
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from claims_appointments");  
			while(rs.next()) {
				appointmentClaimMap.put(rs.getString("CLAIM_NUMBER"),rs.getString("CLAIM_TYPE"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return appointmentClaimMap;
	}
	
	
	public  Map<String,String>  fetchAppointMentResponse(String claimNo){
		Map<String,String> responseTextMap = new HashMap<String,String>();
		try {
			Connection connection = getDBConnection();  
			PreparedStatement statement = connection.prepareStatement("select * from claims_appointments where CLAIM_NUMBER = ?");
			statement.setString(1, claimNo);
			ResultSet rs = statement.executeQuery();  
			while(rs.next()) {
				responseTextMap.put(rs.getString("STATUS"),rs.getString("ALEXA_TEXT"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return responseTextMap;
	}
	
	
	public  Map<String,String>  fetchAppointMentResponseOnType(String claimType){
		Map<String,String> responseTextMap = new HashMap<String,String>();
		try {
			Connection connection = getDBConnection();  
			PreparedStatement statement = connection.prepareStatement("select * from claims_appointments where CLAIM_TYPE = ?");
			statement.setString(1, claimType);
			ResultSet rs = statement.executeQuery();  
			while(rs.next()) {
				responseTextMap.put(rs.getString("STATUS"),rs.getString("ALEXA_TEXT"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return responseTextMap;
	}



	private Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DB_DRIVER);
		Connection con=DriverManager.getConnection(DB_CONNECTION,DB_USER,DB_PASSWORD);
		return con;
	}

}
