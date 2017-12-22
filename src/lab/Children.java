package lab;

import java.lang.Object;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;
import java.time.LocalDate;
import java.util.regex.Pattern;
import com.alibaba.fastjson.annotation.*;
@XmlRootElement(name = "Childrens")
@XmlAccessorType(XmlAccessType.FIELD)
public class Children implements Comparable{

    // constants
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final String PATTERN = "^[A-Z][a-z ]+";
   // private static final String PATTERN1 = "[A-Z] {1}[a-z]*(\\s)[0-9]*";
  //  private static final String PATTERN1 = "^[A-Z][a-z]+";

    public enum PaidFree {
        PAID, FREE
    }

    // fields
   // @JSONField //(ordinal = 1)
    private String firstName;

  //  @JSONField //(ordinal = 2)
    private String lastName;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
  //  @JSONField //(ordinal = 3)
    private LocalDate dateOfBirthday;

 //   @JSONField //(ordinal = 4)
    private String address;


  //  @JSONField //(ordinal = 5)
    private String phoneNumber;

    // constructors
    private Children(){}
    private Children(Builder b){
        firstName = b.firstName;
        lastName = b.lastName;
        dateOfBirthday = b.dateOfBirthday;
        address= b.address;
        phoneNumber = b.phoneNumber;
    }

    // getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }


    // setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    // builder
    public static class Builder {

        // fields
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirthday;
        private String address;
        private String phoneNumber;


        // setters
        public Builder setFirstName(String firstName) {
            Pattern p = Pattern.compile(PATTERN);
            if (p.matcher(firstName).matches())
                this.firstName = firstName;
            else
                throw new IllegalArgumentException("Illegal firstName");
            return this;
        }
        public Builder setLastName(String lastName) {
            Pattern p = Pattern.compile(PATTERN);
            if (p.matcher(lastName).matches())
                this.lastName = lastName;
            else
                throw new IllegalArgumentException("Illegal name");
            return this;
        }
        public Builder setDateOfBirthday(LocalDate date) {
            if (date.getYear() > CURRENT_YEAR - 9 && date.getYear() < CURRENT_YEAR - 1)
                this.dateOfBirthday = date;
            else
                throw new IllegalArgumentException("Illegal Date");
            return this;
        }
        public Builder setAddress(String address) {
         /*   Pattern p = Pattern.compile(PATTERN1);
            if (p.matcher(address).matches())
                this.address = address;
            else
                throw new IllegalArgumentException("Illegal Address");
            return this; */
        	this.address = address;
        	return this;
        }
        public Builder setPhoneNumber(String phoneNumber) {
            Pattern p = Pattern.compile("^[\\+]*(380)[0-9]{9}");
            if (p.matcher(phoneNumber).matches())
                this.phoneNumber = phoneNumber;
            else
                throw new IllegalArgumentException("Illegal Phone");
            return this;
        }

        public Children createChildren(){
            return new Children(this);
        }
    }

    @Override
    public int compareTo (Object o){
        Children s = (Children) o;
        int c = this.getFirstName().compareTo(s.getFirstName());
        return c == 0? this.getLastName().compareTo(s.getLastName()) : c;
    }
    @Override
    public String toString(){
        return "\nFirst name: " + firstName + "\nLast name: " + lastName +
                "\nBirthday: " + dateOfBirthday + "\nAddress: " + address +
                "\nPhone number: " + phoneNumber + "\n";
    }
    @Override
    public boolean equals (Object obj){

        return  firstName.equals(((Children) obj).firstName) &&
                lastName.equals(((Children) obj).lastName) &&
                phoneNumber.equals(((Children) obj).phoneNumber) &&
                address.equals(((Children) obj).address);
    }
}

class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
}

