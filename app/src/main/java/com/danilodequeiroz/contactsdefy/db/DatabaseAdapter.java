package com.danilodequeiroz.contactsdefy.db;

/**
 * Created by Danilo on 16/05/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.danilodequeiroz.contactsdefy.model.DefyContact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DatabaseAdapter {
    private static final String TAG = "DatabaseAdapter";
    private final Context context;

    private DatabaseHelper dbh;
    private SQLiteDatabase db;

    /**
     * Constructor for DatabaseAdapter
     *
     * @param context
     *            The application's Context
     */
    public DatabaseAdapter(Context context) {
        dbh = DatabaseHelper.getInstance(context);
        this.context = context;
    }


    /*
    private long mId;
    private String mName;
    public String mSurname;
    private Date mBirthDate;
    private String mCompanyName;
    private String mPhone;
    private String mMobilePhone;
    private String mWorkPhone;*/

    public long insert(DefyContact contact) {
        try {
            ContentValues values = new ContentValues();
            db = DatabaseHelper.getInstance(context).getWritableDatabase();
            values.put(DatabaseHelper.CONTACTS_COLS[1], contact.getName());
            values.put(DatabaseHelper.CONTACTS_COLS[2], contact.getmSurname());
            values.put(DatabaseHelper.CONTACTS_COLS[3], contact.getBirthYyyyMMdd());
            values.put(DatabaseHelper.CONTACTS_COLS[4], contact.getCompanyName());
            values.put(DatabaseHelper.CONTACTS_COLS[5], contact.getPhone());
            values.put(DatabaseHelper.CONTACTS_COLS[6], contact.getMobilePhone());
            values.put(DatabaseHelper.CONTACTS_COLS[7], contact.getWorkPhone());
            long id = db.insert(DatabaseHelper.TB_CONTACTS, null, values);
            dbh.close();
            return id;
        } catch (Exception e) {
            Log.e(TAG, "SQL Statement error: " + e.getMessage());
            e.printStackTrace();
            dbh.close();
            return -1;
        }
    }

    public long deleteContact(int id) {
        try {
            Log.d(TAG, "Deleting a contac.");
            db = dbh.getReadableDatabase();
            long count = db.delete(DatabaseHelper.TB_CONTACTS, "id=?", new String[]{String.valueOf(id)});
            Log.d(TAG, "Deleted "+count+" contact.");
            dbh.close();
            return count;
        } catch (Exception e) {
            Log.e(TAG, "SQL Statement error: " + e.getMessage());
            e.printStackTrace();
            dbh.close();
            return 0;
        }
    }

    public int updContact(int id, DefyContact.ColummValue columnValue,String toWriteValue) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CONTACTS_COLS[columnValue.getValue()], toWriteValue);
        String where = "id=?";
        String[] whereArgs = { String.valueOf(id) };
        db = dbh.getWritableDatabase();
        int count = db.update(DatabaseHelper.TB_CONTACTS, values, where,
                whereArgs);
        Log.i(TAG, "Updated [" + count + "] contact.");
        dbh.close();
        return count;
    }

    public DefyContact getContact(long id) {
        DefyContact contact;
        db = dbh.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TB_CONTACTS,
                DatabaseHelper.CONTACTS_COLS, "id=?",
                new String[] { String.valueOf(id) }, null, null, null);
        try {

            if (c.getCount() > 0) {
                c.moveToFirst();
                contact = new DefyContact(c.getInt(0), c.getString(1),
                        c.getString(2), c.getString(3), c.getString(4),
                        c.getString(5),c.getString(6),c.getString(7));
                c.close();
                dbh.close();
                return contact;
            } else {
                c.close();
                dbh.close();
                Log.e(TAG, "Coundlt find any tuple");
                return null;
            }
        } catch (Exception e) {
            c.close();
            dbh.close();
            Log.e(TAG, "Sql error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<DefyContact> getAllClientes() {
        db = dbh.getReadableDatabase();
        ArrayList<DefyContact> allContacts = new ArrayList<DefyContact>();
        Cursor c = db.query(DatabaseHelper.TB_CONTACTS,
                DatabaseHelper.CONTACTS_COLS, "", null, null, null,
                "name");
        try {
            if (c.getCount() != 0) {
                while (c.moveToNext()) {
                    DefyContact contact = new DefyContact(c.getInt(0), c.getString(1),
                            c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5),c.getString(6),c.getString(7));
                    allContacts.add(contact);
                }
            } else {
                Log.e(TAG, "Nenhum registro encontrado.");
            }
            c.close();
            dbh.close();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao recuperar todos: " + e.getMessage());
            e.printStackTrace();
            c.close();
            dbh.close();
        }
        return allContacts;
    }


}
