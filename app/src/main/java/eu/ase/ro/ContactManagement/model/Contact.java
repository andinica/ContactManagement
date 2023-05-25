package eu.ase.ro.ContactManagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Contact {



    private String firstName;
    private String lastName;
    private String group;



    private String phoneNumber;
    private String address;
    private Date birthday;



    public Contact(String firstName, String lastName, String group, String phoneNumber, String address, Date birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = birthday;
    }





    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Contact: Name:" + firstName + " " + lastName + ", group " + group + ", phone number " + phoneNumber + ", address:  " + address + ", birthday: " + birthday;
    }


}
