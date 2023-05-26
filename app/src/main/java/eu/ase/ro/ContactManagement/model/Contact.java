package eu.ase.ro.ContactManagement.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "contacts",
        foreignKeys = @ForeignKey(
        entity = Group.class,
        parentColumns = "id",
        childColumns = "groupId",
        onDelete = ForeignKey.SET_NULL),
        indices = @Index("groupId"))
public class Contact implements Serializable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private long id;
    @ColumnInfo
    private String firstName;
    @ColumnInfo
    private String lastName;
    @ColumnInfo
    private Long groupId;
    @ColumnInfo
    private String phoneNumber;
    @ColumnInfo
    private String address;
    @ColumnInfo
    private Date birthday;


    public Contact(long id, String firstName, String lastName, Long groupId, String phoneNumber, String address, Date birthday) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = birthday;
    }

    @Ignore
    public Contact(String firstName, String lastName, Long group, String phoneNumber, String address, Date birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = group;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Long getGroupId() {
        return groupId;
    }

    public Contact(){

    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Contact: Name:" + firstName + " " + lastName + ", group " + groupId + ", phone number " + phoneNumber + ", address:  " + address + ", birthday: " + birthday;
    }


}
