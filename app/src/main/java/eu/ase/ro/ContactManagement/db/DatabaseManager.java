package eu.ase.ro.ContactManagement.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;
import eu.ase.ro.ContactManagement.utils.DateConverter;

@Database(entities = {Contact.class, Group.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class DatabaseManager extends RoomDatabase {

    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if(databaseManager == null){
            synchronized (DatabaseManager.class){
                if(databaseManager == null){
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, "contact_management_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return databaseManager;
    }

    public abstract ContactDao getContactDao();
    public abstract GroupDao getGroupDao();

}
