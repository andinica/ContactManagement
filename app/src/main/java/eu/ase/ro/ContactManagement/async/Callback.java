package eu.ase.ro.ContactManagement.async;

public interface Callback<R> {

    void runResultOnUiThread(R result);
}