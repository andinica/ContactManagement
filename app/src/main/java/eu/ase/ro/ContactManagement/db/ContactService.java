package eu.ase.ro.ContactManagement.db;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import eu.ase.ro.ContactManagement.async.AsyncTaskRunner;
import eu.ase.ro.ContactManagement.async.Callback;
import eu.ase.ro.ContactManagement.model.Contact;

public class ContactService {
    private final AsyncTaskRunner asyncTaskRunner;
    private final ContactDao contactDao;

    public ContactService(Context context) {
        asyncTaskRunner = new AsyncTaskRunner();
        contactDao = DatabaseManager.getInstance(context).getContactDao();
    }

    public void insert(Contact contact, Callback<Contact> insertActivityThread) {
        Callable<Contact> insertOperation = new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                Log.i("MainActivityDrawerHome", "Group name in ContactService.insert" + contact.toString());
                if (contact == null || contact.getId() > 0) {
                    return null;
                }
                if (contact.getGroupId() == null) {
                    contact.setGroupId(-1L);
                }
                long id = contactDao.insert(contact);
                if (id < 0) {
                    return null;
                }
                contact.setId(id);
                return contact;
            }
        };
        asyncTaskRunner.executeAsync(insertOperation, insertActivityThread);
    }

    public void getAll(Callback<List<Contact>> getAllActivityThread) {
        Callable<List<Contact>> getAllOperation = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                return contactDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(getAllOperation, getAllActivityThread);
    }

    public void update(Contact contact, Callback<Contact> updateActivityThread) {
        Callable<Contact> updateOperation = new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                Log.i("MainActivityDrawerHome", "Contact in update method: + " + contact.toString());
                if (contact == null || contact.getId() <= 0) {
                    return null;
                }
                int count = contactDao.update(contact);
                if (count < 1) {
                    return null;
                }
                return contact;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, updateActivityThread);
    }

    public void updateContacts(List<Contact> contacts, Callback<Integer> updateActivityThread) {
        Callable<Integer> updateOperation = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int count = contactDao.updateContacts(contacts);
                return count;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, updateActivityThread);
    }

    public void delete(Contact contact, Callback<Boolean> deleteActivityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int count = contactDao.delete(contact);
                return count >= 1;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, deleteActivityThread);
    }

    public void queryContacts(String searchQuery, Callback<List<Contact>> searchActivityThread) {
        Callable<List<Contact>> searchOperation = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                return contactDao.queryContacts('%' + searchQuery + '%');
            }
        };
        asyncTaskRunner.executeAsync(searchOperation, searchActivityThread);
    }
    public void getContactsByGroupId(long groupId, Callback<List<Contact>> getContactsByGroupActivityThread) {
        Callable<List<Contact>> getContactsByGroupOperation = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                return contactDao.getContactsByGroupId(groupId);
            }
        };
        asyncTaskRunner.executeAsync(getContactsByGroupOperation, getContactsByGroupActivityThread);
    }

    public void getGroupCountForEach(Long id, Callback<Long> getContactsByGroupActivityThread) {
        Callable<Long> getContactsByGroupOperation = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return contactDao.getGroupCountForEach(id);
            }
        };

        asyncTaskRunner.executeAsync(getContactsByGroupOperation, getContactsByGroupActivityThread);
    }
}