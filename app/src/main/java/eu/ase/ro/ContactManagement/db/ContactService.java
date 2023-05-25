package eu.ase.ro.ContactManagement.db;

import android.content.Context;

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
                if (contact == null || contact.getId() > 0) {
                    return null;
                }
                //aici ne aflam pe un alt thread...
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
                //ne aflam pe un alt thread.
                //ma conectez la baza de date
                return contactDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(getAllOperation, getAllActivityThread);
    }

    public void update(Contact contact, Callback<Contact> updateActivityThread) {
        Callable<Contact> updateOperation = new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
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
}