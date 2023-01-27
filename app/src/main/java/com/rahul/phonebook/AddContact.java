package com.rahul.phonebook;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rahul.phonebook.contact.Contact;
import com.rahul.phonebook.handler.DbHandler;

public class AddContact extends AppCompatActivity {

    EditText name,mobile,email;
    Button addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        //handler object
        DbHandler db = new DbHandler(AddContact.this);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        addContact = findViewById(R.id.addContact);

        try {
            addContact.setOnClickListener(v -> {
                new CountDownTimer(100, 50) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        addContact.setBackgroundColor(Color.WHITE);
                    }

                    @Override
                    public void onFinish() {
                        addContact.setBackgroundColor(Color.BLACK);
                    }
                }.start();

                if(!isNullText(name.getText().toString()) && !isNullText(mobile.getText().toString()) && !isNullText(email.getText().toString())) {
                    //contact object
                    Contact contactObject = new Contact();
                    contactObject.setName(name.getText().toString());
                    contactObject.setMobile(mobile.getText().toString());
                    contactObject.setEmail(email.getText().toString());

                    db.addContact(contactObject);
                    Toast.makeText(AddContact.this, "Contact Saved", Toast.LENGTH_SHORT).show();

                    //go back to main page
                    Intent intent = new Intent(AddContact.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AddContact.this, "Something is Missing..", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //to check for empty inputs
    public boolean isNullText(String text){
        return text.matches("");
    }
}