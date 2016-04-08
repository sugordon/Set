package network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Collections;

public class Database {
	/** The name of the MySQL account to use (or empty for anonymous) */
	private static final String userName = "SetGuest";

	/** The password for the MySQL account (or empty for anonymous) */
	private static final String password = "guessme1";

	public static String hash(String s) {
		MessageDigest md;
		String ret = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			ret = Base64.getEncoder().encodeToString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static int newUser(Connection conn, String name, String hash) {
		String cmd =
				"INSERT INTO Users" +
				"(name, hash) VALUES ('" +
				name + "', '" + hash + "')";
		System.out.println(cmd);
		try {
			Database.executeUpdate(conn, cmd);
		} catch (SQLException e) {
			//Error code for duplicate Username
			if (e.getErrorCode() == 1062) {
				System.out.println(name + " already exists!");
				return 2;
			}
			System.err.println("Error executing " + cmd);
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	public static boolean auth(Connection conn, String name, String hash) {
		String cmd =
				"SELECT hash FROM Users WHERE name = '" +
				name + "'";
		System.out.println(cmd);
		ResultSet rs;
		String stored;
		try {
			rs = Database.executeQuery(conn, cmd);
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
		return hash.equals(stored);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://199.98.20.115/SetGame", Database.userName, Database.password);
	}

	private static void executeUpdate(Connection conn, String command) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(command);
	}
	
	private static ResultSet executeQuery(Connection conn, String command) throws SQLException {
	    Statement stmt = conn.createStatement();
        return stmt.executeQuery(command);
	}
	
}
