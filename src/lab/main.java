package lab;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.TreeSet;
//import LabJDBC.UniversityDB;
import lab.Group;
import lab.Children;
import IO.*;

public class main {

    public static void main(String[] args) {
    	 Group[] g = new Group[3];
    	 g=generateGroup();
    	//write/read to/from xml/json files
        JSON_IO js = new JSON_IO();
        File file1 = new File("Group.json");
        System.out.println(g[1]);
        js.serialize(g[1], file1);
        //Group tmp =(Group) (js.deserialize(Group.class,file1));
        //System.out.println(tmp);

        XML_IO xm= new XML_IO();
        File file = new File("Group.xml");;
        xm.serialize(g[1], file); 
     //   Group tmpw = (Group) (js.readObjectFromXMLfile(Group.class, file));
      //  System.out.println(tmpw);

        // deserializing groups from json file

      /*  File file1 = new File("group1.json");
        File file2 = new File("group2.json");
        File file3 = new File("group3.json");

        InputOutput fastjson = new JSON_IO();

        Group[] groups = new Group[3];
        groups[0] = (Group) (fastjson.deserialize(Group.class, file1));
        groups[1] = (Group) (fastjson.deserialize(Group.class, file2));
        groups[2] = (Group) (fastjson.deserialize(Group.class, file3));

        Children[] st1 = groups[0].getChildrens().toArray(new Children[groups[0].getChildrens().size()]);
        Children[] st2 = groups[1].getChildrens().toArray(new Children[groups[1].getChildrens().size()]);
        Children[] st3 = groups[2].getChildrens().toArray(new Children[groups[2].getChildrens().size()]);
*/
    	
        // working with database

      //  DB.initialize();
        /*
       DB.createGroupTable();
        DB.createStudentTable();
        */
        /*
        for (int i = 0; i < groups.length; i++)
            DB.addGroup(groups[i]);

        for(int i = 0; i < st1.length; i++)
           	DB.addStudent(st1[i], 1);
        for(int i = 0; i < st2.length; i++)
            DB.addStudent(st2[i], 2);
        for(int i = 0; i < st3.length; i++)
            DB.addChildern(st3[i], 3);
        */

        //DB.transferToNextGroup(st1[0], "g345");
        //System.out.println("Course: " + DB.getCourse(st1[0]));

        //DB.outputAllGroups();
        /*ArrayList<Group> g = DB.getGroups();
        for (Group gr: g) {

            System.out.println(gr);
        }*/
        /*
        ArrayList<Children> stud = DB.getChildrens("g285");
        for (Children st: stud){
            System.out.println(st);
        }*/
//        DB.outputGrantApplicants(50);

        /*
        try {
            DB.deleteGroup(groups[0]);
        } catch (Exception e){
            System.out.println("There are students in this group.");
            DB.deleteGroupWithStudents(groups[0]);
        }
        */
        /*Children st = new Children.Builder().setFirstName("Alina").setLastName("Foe").
                setAddress("Morska 17").setPhoneNumber("380981234511").
                setDateOfBirthday(LocalDate.of(1996, 11, 12)).
                setPaidOrFreeGroup(Children.PaidFree.FREE).createChildren();

     	DB.addChildren(st, groups[0]);
        */

     //   DB.close();

    }
    private static Group[] generateGroup(){

        Children c1, c2, c3, c4, c5, c6;

        c1 = new Children.Builder().setFirstName("Damian").setLastName("Johnson").
                setDateOfBirthday(LocalDate.of(2010, Month.JANUARY, 18)).
                setAddress("Polytaeva 7").setPhoneNumber("380981234567").createChildren();

        c2 = new Children.Builder().setFirstName("Alex").setLastName("Austen").
                setDateOfBirthday(LocalDate.of(2015, Month.MARCH, 12)).
                setAddress("Zelana 27").setPhoneNumber("380981651567").createChildren();

        c3 = new Children.Builder().setFirstName("Kim").setLastName("Loss").
                setDateOfBirthday(LocalDate.of(2013, Month.JULY, 10)).
                setAddress("Dovgogo 123").setPhoneNumber("380978934567").createChildren();

        c4 = new Children.Builder().setFirstName("Joe").setLastName("Clark").
                setDateOfBirthday(LocalDate.of(2013, Month.JANUARY, 22)).
                setAddress("Morska 7").setPhoneNumber("380964234567").createChildren();

        c5 = new Children.Builder().setFirstName("Alice").setLastName("Cooper").
                setDateOfBirthday(LocalDate.of(2014, Month.DECEMBER, 13)).
                setAddress("Holovna 7").setPhoneNumber("380999024567").createChildren();

        c6 = new Children.Builder().setFirstName("Linda").setLastName("Tyler").
                setDateOfBirthday(LocalDate.of(2015, Month.JULY, 25)).
                setAddress("Polytaeva 251").setPhoneNumber("380981296367").createChildren();

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

