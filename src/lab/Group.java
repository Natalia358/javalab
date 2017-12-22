package lab;

import com.alibaba.fastjson.annotation.*;
import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@XmlRootElement
@XmlType(propOrder = {"name", "mentor", "childrens"})
public class Group {

    // fields
    @JSONField //(ordinal = 1)
    private String name;
    @JSONField //(ordinal = 2)
    private String mentor;
    @JSONField //(ordinal = 3)
    private TreeSet<Children> childrens = new TreeSet<>();

    // constructor
    public Group(){ }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
    @XmlElementWrapper(name = "childrens")
    @XmlElement(name = "children")
    public void setChildrens(TreeSet<Children> childrens){
        this.childrens = childrens;
    }

    // getters
    public String getName() {
        return name;
    }
    public String getMentor() {
        return mentor;
    }

    public TreeSet<Children> getChildrens() {
        return childrens;
    }

    // methods
    public void addChildrenToGroup(Children st){
        childrens.add(st);
    }

    public void transferToAnotherGroup(Children st, Group g){
        g.childrens.add(st);
        childrens.remove(st);
    }
  
    public void outputGroup(){
        childrens.stream().forEach(s->System.out.println(s));
}

    @Override
    public String toString() {
        String res = "Name: " +name +
                "\nMentor: " + mentor +
                "\nChildrens: " + "\n";
        for (Children st : childrens){
            res += st.toString();
        }
        return res;
    }
    @Override
    public boolean equals(Object obj){
        return  (name.equals(((Group) obj).name) &&
                mentor.equals(((Group) obj).mentor));    }
}

