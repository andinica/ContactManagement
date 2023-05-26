package eu.ase.ro.ContactManagement.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.model.GroupWithMemberCount;

@Dao
public interface GroupDao {
    @Insert
    long insert(Group group); //id- daca totul este ok; sau -1 daca au fost probleme la insert
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
    int update(Group group); // reprezinta numarul de inregistrari afectate
    @Delete
    int delete(Group group);
}
