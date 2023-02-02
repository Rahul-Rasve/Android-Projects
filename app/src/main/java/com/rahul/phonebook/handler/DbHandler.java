package com.rahul.phonebook.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rahul.phonebook.contact.Contact;
import com.rahul.phonebook.parameters.Parameters;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, Parameters.DB_NAME, null, Parameters.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + Parameters.TABLE_NAME + " (" +
                        Parameters.KEY_NAME + " TEXT PRIMARY KEY," +
                        Parameters.KEY_MOBILE + " TEXT," +
                        Parameters.KEY_EMAIL + " TEXT);" ;
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP TABLE " + Parameters.TABLE_NAME + ";";
        db.execSQL(drop);
        onCreate(db);
    }

    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Parameters.KEY_NAME, contact.getName());
        values.put(Parameters.KEY_MOBILE, contact.getMobile());
        values.put(Parameters.KEY_EMAIL, contact.getEmail());

        db.insert(Parameters.TABLE_NAME, null, values);
        db.close();
    }

    public List<Contact> getAllContacts(){
        //query to order by ascending order
        String select = "SELECT * FROM " + Parameters.TABLE_NAME + " ORDER BY " + Parameters.KEY_NAME + " ASC";

        List<Contact> contactsList = new ArrayList<>();

        SQLiteDatabase db =this.getReadableDatabase();

        Cursor cursor =db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do {
                Contact contact = new Contact();
                contact.setName(cursor.getString(0));
                contact.setMobile(cursor.getString(1));
                contact.setEmail(cursor.getString(2));

                //push on list
                contactsList.add(contact);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return contactsList;
    }

    public Contact searchContact(String name){
        // '?' is used like in string format we use %d,s...
        String select = "SELECT * FROM " + Parameters.TABLE_NAME + " WHERE " + Parameters.KEY_NAME + " = ?";
        Contact contactObject = new Contact();

        SQLiteDatabase db = this.getReadableDatabase();

        //here 'name' will be replaced with ? in query
        Cursor cursor = db.rawQuery(select, new String[]{name});

/*      >>>>>>>METHOD 2<<<<<<<<
        Cursor cursor = db.query(Parameters.TABLE_NAME,
                                new String[]{Parameters.KEY_NAME, Parameters.KEY_MOBILE, Parameters.KEY_EMAIL},
                                Parameters.KEY_NAME + "=?",
                                new String[]{name}, null, null, null);
*/

        if(cursor.moveToFirst()) {
            contactObject.setName(cursor.getString(0));
            contactObject.setMobile(cursor.getString(1));
            contactObject.setEmail(cursor.getString(2));
        }

        cursor.close();
        return contactObject;
    }

    public void updateContact(Contact contact, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Parameters.KEY_NAME, contact.getName());
        values.put(Parameters.KEY_MOBILE, contact.getMobile());
        values.put(Parameters.KEY_EMAIL, contact.getEmail());

        //delete previous contact
        db.delete(Parameters.TABLE_NAME, Parameters.KEY_NAME + " =? ",
                new String[]{name});

        //insert updated contact
        db.insert(Parameters.TABLE_NAME, null, values);
    }

    public void deleteContact(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete query
        db.delete(Parameters.TABLE_NAME, Parameters.KEY_NAME + " =? ",
                new String[]{name});
    }
}
