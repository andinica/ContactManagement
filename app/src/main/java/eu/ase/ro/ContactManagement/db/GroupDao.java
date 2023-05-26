package eu.ase.ro.ContactManagement.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;

@Dao
public interface GroupDao {
    @Insert
    long insert(Group group);
    @Query("SELECT * FROM groups")
    List<Group> getAll();
    @Query("SELECT * FROM groups WHERE name LIKE :searchQuery")
    List<Group> queryGroups(String searchQuery);
    @Query("SELECT name FROM groups")
    List<String> getAllGroupNames();
    @Query("SELECT * FROM groups WHERE id = :id")
    Group getGroupById(Long id);
    @Query("SELECT name FROM groups WHERE id = :id")
    String getGroupNameById(Long id);
    @Query("SELECT id FROM groups WHERE name = :name")
    Long getGroupIdByName(String name);
    @Update
    int update(Group group);
    @Delete
    int delete(Group group);
}
