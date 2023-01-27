package com.rahul.phonebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.rahul.phonebook.contact.Contact;
import com.rahul.phonebook.handler.DbHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ImageButton addUser;

    public static final String KEY = "ContactName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handler object
        DbHandler db = new DbHandler(MainActivity.this);

        listView = findViewById(R.id.listView);
        addUser = findViewById(R.id.adduser);

        //populate list view
        ArrayList<String> contactList = new ArrayList<>();

        List<Contact> getContacts = db.getAllContacts();
        for(Contact contactObject : getContacts){
            contactList.add(contactObject.getName());
        }

        //array adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.custom_listview_items, contactList);
        listView.setAdapter(arrayAdapter);

        //onClickListener for listView item
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ContactDetails.class);
            intent.putExtra(KEY, ((TextView) view).getText().toString());
            startActivity(intent);
        });

        addUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContact.class);
            startActivity(intent);
        });
    }
}