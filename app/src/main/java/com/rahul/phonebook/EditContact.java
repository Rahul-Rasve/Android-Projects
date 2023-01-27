package com.rahul.phonebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rahul.phonebook.contact.Contact;
import com.rahul.phonebook.handler.DbHandler;

public class EditContact extends AppCompatActivity {

    EditText name, mobile, email;
    Button saveContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        //handler object
        DbHandler db = new DbHandler(EditContact.this);

        //views
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        saveContact = findViewById(R.id.saveContact);

        Intent intent = getIntent();

        //get the strings
        String contactName = intent.getStringExtra(ContactDetails.NAME);
        String contactMobile = intent.getStringExtra(ContactDetails.MOBILE);
        String contactEmail = intent.getStringExtra(ContactDetails.EMAIL);

        //set the strings
        name.setText(contactName);
        mobile.setText(contactMobile);
        email.setText(contactEmail);

        saveContact.setOnClickListener(v -> {
            if(!isNullText(name.getText().toString()) && !isNullText(mobile.getText().toString()) && !isNullText(email.getText().toString())) {
                Contact contactObject = new Contact();
                contactObject.setName(name.getText().toString());
                contactObject.setMobile(mobile.getText().toString());
                contactObject.setEmail(email.getText().toString());
                db.updateContact(contactObject, contactName);

                //give conformation
                Toast.makeText(EditContact.this, "Contact Updated", Toast.LENGTH_SHORT).show();

                //go back to main activity
                Intent intent1 = new Intent(EditContact.this, MainActivity.class);
                startActivity(intent1);
            }
            else {
                Toast.makeText(EditContact.this, "Something is Missing..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //to check for empty inputs
    public boolean isNullText(String text){
        return text.matches("");
    }
}