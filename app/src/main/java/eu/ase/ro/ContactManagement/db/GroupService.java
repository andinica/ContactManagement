package eu.ase.ro.ContactManagement.db;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import eu.ase.ro.ContactManagement.async.AsyncTaskRunner;
import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.model.Contact;
import eu.ase.ro.ContactManagement.model.Group;

public class GroupService {

    private final AsyncTaskRunner asyncTaskRunner;
    private final GroupDao groupDao;

    public GroupService(Context context) {
        asyncTaskRunner = new AsyncTaskRunner();
        groupDao = DatabaseManager.getInstance(context).getGroupDao();
    }

    public void insert(Group group, Callback<Group> insertActivityThread) {
        Callable<Group> insertOperation = new Callable<Group>() {
            @Override
            public Group call() throws Exception {
                if (group == null || group.getId() > 0) {
                    return null;
                }
                //aici ne aflam pe un alt thread...
                long id = groupDao.insert(group);
                if (id < 0) {
                    return null;
                }
                group.setId(id);
                return group;
            }
        };
        asyncTaskRunner.executeAsync(insertOperation, insertActivityThread);
    }

    public void getAll(Callback<List<Group>> getAllActivityThread) {
        Callable<List<Group>> getAllOperation = new Callable<List<Group>>() {
            @Override
            public List<Group> call() throws Exception {
                //ne aflam pe un alt thread.
                //ma conectez la baza de date
                return groupDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(getAllOperation, getAllActivityThread);
    }

    public void update(Group group, Callback<Group> updateActivityThread) {
        Callable<Group> updateOperation = new Callable<Group>() {
            @Override
            public Group call() throws Exception {
                if (group == null || group.getId() <= 0) {
                    return null;
                }
                int count = groupDao.update(group);
                if (count < 1) {
                    return null;
                }
                return group;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, updateActivityThread);
    }

    public void delete(Group group, Callback<Boolean> deleteActivityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int count = groupDao.delete(group);
                return count >= 1;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, deleteActivityThread);
    }

//    public void queryGroups(String searchQuery, Callback<List<Contact>> searchActivityThread) {
//        Callable<List<Contact>> searchOperation = new Callable<List<Contact>>() {
//            @Override
//            public List<Contact> call() throws Exception {
//                return groupDao.queryContacts('%' + searchQuery + '%');
//            }
//        };
//        asyncTaskRunner.executeAsync(searchOperation, searchActivityThread);
//    }
}