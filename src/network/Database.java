package network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class Database {
	/** The name of the MySQL account to use (or empty for anonymous) */
	private static final String userName = "SetGuest";

	/** The password for the MySQL account (or empty for anonymous) */
	private static final String password = "guessme1";

	private static String hash(String s) {
		MessageDigest md;
		String ret = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			ret = Base64.getEncoder().encodeToString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean newUser(String name, String pwd) {
		String s = Database.hash(pwd);
		String cmd =
				"INSERT INTO Users" +
				"(name, hash) VALUES ('" +
				name + "', '" + s + "')";
		System.out.println(cmd);
		try {
			Database.executeUpdate(Database.getConnection(), cmd);
		} catch (SQLException e) {
			//Error code for duplicate Username
			if (e.getErrorCode() == 1062) {
				System.out.println(name + " already exists!");
				return false;
			}
			System.err.println("Error executing " + cmd);
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean auth(String name, String pwd) {
		String hash = Database.hash(pwd);
		String cmd =
				"SELECT hash FROM Users WHERE name = '" +
				name + "'";
		System.out.println(cmd);
		ResultSet rs;
		String stored;
		try {
			rs = Database.executeQuery(Database.getConnection(), cmd);
			if (rs != null && rs.first()) {
				stored = rs.getString("hash");
			} else {
				System.out.println("User Not Found");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println(hash);
		System.out.println(stored);
		System.out.println(hash.equals(stored));
		return hash.equals(stored);
	}

	private static Connection getConnection() throws SQLException {
//		Connection conn = null;
//		Properties connectionProps = new Properties();
//		connectionProps.put("user", Database.userName);
//		connectionProps.put("password", Database.password);

//		conn = DriverManager.getConnection("jdbc:mysql://"
//				+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
//				connectionProps);
		return DriverManager.getConnection("jdbc:mysql://199.98.20.115/SetGame", Database.userName, Database.password);

//		return conn;
	}

	private static void executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt;
		stmt = conn.createStatement();
		stmt.executeUpdate(command);
	}
	
	private static ResultSet executeQuery(Connection conn, String command) {
	    Statement stmt;
		try {
			stmt = conn.createStatement();
			return stmt.executeQuery(command);
		} catch (SQLException e) {
			System.err.println("Error executing " + command);
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Database.newUser("HIHI", "asdf");
		Database.auth("HIHI", "f");
	}
}
