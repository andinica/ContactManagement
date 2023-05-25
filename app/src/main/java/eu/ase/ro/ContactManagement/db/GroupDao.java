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
    long insert(Group group); //id- daca totul este ok; sau -1 daca au fost probleme la insert
    @Query("select * from groups")
    List<Group> getAll();
    @Query("SELECT name from groups WHERE id = :id")
    String getGroupNameById(double id);
    @Update
    int update(Group group); // reprezinta numarul de inregistrari afectate
    @Delete
    int delete(Group group);
}
