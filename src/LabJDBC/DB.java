package LabJDBC;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import lab.Group;
import lab.Children;
import IO.JSON_IO;
import IO.XML_IO;
import lab.*;

public class DB {

	private static MySqlManager sql;

	public static void initialize() {

		sql = new MySqlManager();
		sql.connectDatabase();
	}

	public static void createChildrenTable() {

		String query = "CREATE TABLE chidren(" + "children_id INT AUTO_INCREMENT," + "firstName VARCHAR(20),"
				+ "lastName VARCHAR(20)," + "dateOfBirthday DATE," + "address VARCHAR(20),"
				+ "phoneNumber VARCHAR(20),group_id INT," + "PRIMARY KEY(children_id),"
				+ "FOREIGN KEY(group_id) REFERENCES groups(group_id));";
		sql.executeUpdate(query);
		//System.out.println(123);
	}

	public static void createGroupTable() {

		String query = "CREATE TABLE groups(" + "group_id INT AUTO_INCREMENT," + "name VARCHAR(10),"
				+ "mentor VARCHAR(20)," + "PRIMARY KEY(group_id));";
		sql.executeUpdate(query);
		//System.out.println(123);
	}

	public static void addChildren(Children s, int group) {

		String query = "INSERT INTO children(firstname, lastname, "
				+ "dateOfBirthday, address, phonenumber, group_id) VALUES (?,?,?,?,?,?);";
		PreparedStatement prepStmt = sql.createPreparedStatement(query);
		try {
			prepStmt.setString(1, s.getFirstName());
			prepStmt.setString(2, s.getLastName());
			prepStmt.setDate(3, java.sql.Date.valueOf(s.getDateOfBirthday()));
			prepStmt.setString(4, s.getAddress());
			prepStmt.setString(5, s.getPhoneNumber());
			prepStmt.setInt(6, group);
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public static void addChildren(Children s, Group g) {

		String tmp = "SELECT group_id FROM groups WHERE name='" + g.getName() + "';";
		ResultSet res = sql.executeQuery(tmp);
		int groupId = 0;
		try {
			while (res.next()) {
				groupId = res.getInt("group_id");
			}
		} catch (SQLException e) {
		}

		String query = "INSERT INTO children(firstname, lastname, "
				+ "dateOfBirthday, address, phonenumber) VALUES (?,?,?,?,?);";
		PreparedStatement prepStmt = sql.createPreparedStatement(query);
		try {
			prepStmt.setString(1, s.getFirstName());
			prepStmt.setString(2, s.getLastName());
			prepStmt.setDate(3, java.sql.Date.valueOf(s.getDateOfBirthday()));
			prepStmt.setString(4, s.getAddress());
			prepStmt.setString(5, s.getPhoneNumber());
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		}
		String tmp2 = "UPDATE children SET group_id=" + groupId + " WHERE phoneNumber='" + s.getPhoneNumber() + "';";
		sql.executeUpdate(tmp2);
	}

	public static void addGroup(Group g) {

		String query = "INSERT INTO groups(name, mentor) VALUES (?,?);";
		PreparedStatement prepStmt = sql.createPreparedStatement(query);
		try {
			prepStmt.setString(1, g.getName());
			prepStmt.setString(2, g.getMentor());
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public static void transferToNextGroup(Children s, String newGroup) {

		String query = "UPDATE children " + "SET group_id = (SELECT group_id FROM groups WHERE name = ?)"
				+ "WHERE phonenumber = ?;";

		PreparedStatement prepStmt = sql.createPreparedStatement(query);
		try {
			prepStmt.setString(1, newGroup);
			prepStmt.setString(2, s.getPhoneNumber());
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	// output group
	public static void outputAllGroups() {

		String query = "SELECT firstname, lastname, dateofbirthday, address, phonenumber, name"
				+ "FROM children s,groups g WHERE s.group_id = g.group_id;";
		ResultSet res = sql.executeQuery(query);
		try {
			while (res.next()) {
				String firstname = res.getString("firstname");    // обращаемся по имени колонки
				String lastname = res.getString("lastname");
				String dateOfBirthday = res.getString("dateofbirthday");
				String address = res.getString("address");
				String phonenum = res.getString("phonenumber");
				String name = res.getString("name");

				System.out.println(
						"\nFirst name: " + firstname + "\nLast name: " + lastname + "\nBirthday: " + dateOfBirthday
								+ "\nAddress: " + address + "\nPhone number: " + phonenum + "\nGroup: " + name + "\n");
			}
		} catch (SQLException e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}

	public static ArrayList<Group> getGroups() {

		ArrayList<Group> groups = new ArrayList<Group>();

		String query = "SELECT * FROM groups";
		ResultSet res = sql.executeQuery(query);
		try {
			while (res.next()) {

				String name = res.getString("name");
				String mentor = res.getString("mentor");

				Group g = new Group();
				g.setName(name);
				g.setMentor(mentor);
				groups.add(g);
			}
		} catch (SQLException e) {
			// System.out.println(e);
			Group tmp = new Group();
			tmp.setName("tmp group");
			groups.add(tmp);
		} finally {
			return groups;
		}
	}

	public static ArrayList<Children> getChildrens(String title) {

		ArrayList<Children> child = new ArrayList<Children>();

		String query = "SELECT * FROM children WHERE group_id=(SELECT group_id FROM groups WHERE name='" + title
				+ "');";
		ResultSet res = sql.executeQuery(query);
		try {
			while (res.next()) {

				String firstname = res.getString("firstname");
				String lastname = res.getString("lastname");
				String dateOfBirthday = res.getString("dateofbirthday");
				String address = res.getString("address");
				String phonenum = res.getString("phonenumber");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

				child.add(new Children.Builder().setFirstName(firstname).setLastName(lastname).setAddress(address)
						.setDateOfBirthday(LocalDate.parse(dateOfBirthday, formatter)).setPhoneNumber(phonenum)
						.createChildren());
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return child;
	}

	public static void deleteGroup(Group group) throws Exception {

		String queryDelGroups = "DELETE FROM groups WHERE name='" + group.getName() + "';";
		int affectedRows = sql.executeUpdate(queryDelGroups);
		if (affectedRows == 0) {
			throw new Exception("You cannot delete from 'Groups' table because of dependencies.");
		}
	}

	public static void deleteGroupWithStudents(Group group) {

		String tmp = "SELECT COUNT(*) FROM groups g, children s WHERE g.group_id=s.group_id AND g.name='"
				+ group.getName() + "';";
		ResultSet res = sql.executeQuery(tmp);
		int count = 0;
		try {
			if (res.next())
				count = res.getInt("COUNT(*)");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (count != 0) {
			String query = "DELETE FROM children WHERE group_id = (SELECT group_id FROM groups WHERE name='"
					+ group.getName() + "');";
			sql.executeUpdate(query);
			String queryDelGroups = "DELETE FROM groups WHERE name='" + group.getName() + "';";
			sql.executeUpdate(queryDelGroups);
		} else {
			String queryDelGroups = "DELETE FROM groups WHERE name='" + group.getName() + "';";
			sql.executeUpdate(queryDelGroups);
		}
	}

	public static void close() {
		sql.disconnectDatabase();
	}

	public static void main(String[] args) {
		Group[] g = new Group[3];
		DB.initialize();
		// ArrayList<Group> groupsList = DB.getGroups();
		DB.createGroupTable();
		DB.createChildrenTable();
		g = generateGroup();
		DB.addGroup(g[0]);
		JSON_IO js = new JSON_IO();
		File file1 = new File("Group.json");
		System.out.println(g[1]);
		js.serialize(g[1], file1);
		XML_IO xm = new XML_IO();
		File file = new File("Group.xml");
		;
		xm.serialize(g[1], file);
		DB.close();
	}

	private static Group[] generateGroup() {

		Children c1, c2, c3, c4, c5, c6;

		c1 = new Children.Builder().setFirstName("Damian").setLastName("Johnson")
				.setDateOfBirthday(LocalDate.of(2010, Month.JANUARY, 18)).setAddress("Polytaeva 7")
				.setPhoneNumber("380981234567").createChildren();

		c2 = new Children.Builder().setFirstName("Alex").setLastName("Austen")
				.setDateOfBirthday(LocalDate.of(2015, Month.MARCH, 12)).setAddress("Zelana 27")
				.setPhoneNumber("380981651567").createChildren();

		c3 = new Children.Builder().setFirstName("Kim").setLastName("Loss")
				.setDateOfBirthday(LocalDate.of(2013, Month.JULY, 10)).setAddress("Dovgogo 123")
				.setPhoneNumber("380978934567").createChildren();

		c4 = new Children.Builder().setFirstName("Joe").setLastName("Clark")
				.setDateOfBirthday(LocalDate.of(2013, Month.JANUARY, 22)).setAddress("Morska 7")
				.setPhoneNumber("380964234567").createChildren();

		c5 = new Children.Builder().setFirstName("Alice").setLastName("Cooper")
				.setDateOfBirthday(LocalDate.of(2014, Month.DECEMBER, 13)).setAddress("Holovna 7")
				.setPhoneNumber("380999024567").createChildren();

		c6 = new Children.Builder().setFirstName("Linda").setLastName("Tyler")
				.setDateOfBirthday(LocalDate.of(2015, Month.JULY, 25)).setAddress("Polytaeva 251")
				.setPhoneNumber("380981296367").createChildren();

		Group[] g = new Group[3];
		g[0] = new Group();
		g[0].setName("g345");
		g[0].setMentor("Allie Fox");
		g[1] = new Group();
		g[1].setName("g365");
		g[1].setMentor("Randy Fisher");
		g[2] = new Group();
		g[2].setName("g285");
		g[2].setMentor("James Hilton");

		TreeSet<Children> st1 = new TreeSet<>();
		st1.add(c3);
		st1.add(c5);
		TreeSet<Children> st2 = new TreeSet<>();
		st2.add(c1);
		st2.add(c6);
		TreeSet<Children> st3 = new TreeSet<>();
		st3.add(c2);
		st3.add(c4);

		g[0].setChildrens(st1);
		g[1].setChildrens(st2);
		g[2].setChildrens(st3);
		return g;
	}
}
