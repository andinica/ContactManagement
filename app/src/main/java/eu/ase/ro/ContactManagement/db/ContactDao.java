package eu.ase.ro.ContactManagement.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import eu.ase.ro.ContactManagement.model.Contact;

@Dao
public interface ContactDao {
    @Insert
    long insert(Contact contact); //id- daca totul este ok; sau -1 daca au fost probleme la insert
    @Query("select * from contacts")
    List<Contact> getAll();
    @Update
    int update(Contact contact); // reprezinta numarul de inregistrari afectate
    @Delete
    int delete(Contact contact);

}
