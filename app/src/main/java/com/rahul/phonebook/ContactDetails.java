package com.rahul.phonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rahul.phonebook.contact.Contact;
import com.rahul.phonebook.handler.DbHandler;

public class ContactDetails extends AppCompatActivity {

    TextView nameText, mobileText, emailText;
    ImageButton editUser, deleteUser, callingBtn;

    public static final String NAME = "ContactName";
    public static final String MOBILE = "ContactMobile";
    public static final String EMAIL = "ContactEmail";
    public static final int CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //handler object
        DbHandler db = new DbHandler(ContactDetails.this );

        nameText = findViewById(R.id.nameText);
        mobileText = findViewById(R.id.mobileText);
        emailText = findViewById(R.id.emailText);

        editUser = findViewById(R.id.editUser);
        deleteUser = findViewById(R.id.deleteUser);
        callingBtn = findViewById(R.id.callingBtn);

        //get intent object
        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.KEY);
        Contact contactObject = db.searchContact(name);

        //set the textViews
        nameText.setText(contactObject.getName());
        mobileText.setText(contactObject.getMobile());
        emailText.setText(contactObject.getEmail());

        //edit listener
        editUser.setOnClickListener(view -> {
            Intent intent1 = new Intent(ContactDetails.this, EditContact.class);
            intent1.putExtra(NAME, nameText.getText().toString());
            intent1.putExtra(MOBILE, mobileText.getText().toString());
            intent1.putExtra(EMAIL, emailText.getText().toString());
            startActivity(intent1);
        });

        //delete listener
        deleteUser.setOnClickListener(v -> {
            //create an alert
            AlertDialog.Builder alert = new AlertDialog.Builder(ContactDetails.this);
            alert.setTitle("Delete Contact");
            alert.setMessage("Are you sure you want to DELETE this Contact?");

            //positive button
            alert.setPositiveButton(R.string.positive, (dialog, which) -> {
                db.deleteContact(nameText.getText().toString());
                Toast.makeText(ContactDetails.this, "Contact Deleted", Toast.LENGTH_SHORT).show();

                //return to main activity
                Intent intent12 = new Intent(ContactDetails.this, MainActivity.class);
                startActivity(intent12);
            });

            //negative button
            alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());

            //popup the alert
            alert.show();
        });

        callingBtn.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CODE);
            }
            else{
                String phoneNumber = mobileText.getText().toString();
                Intent intent3 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent3);
            }
        });
    }
}