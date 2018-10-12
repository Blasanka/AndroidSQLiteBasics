package com.bla.androidsqlitebasics.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bla.androidsqlitebasics.database.DBHelper;
import com.live.sinhalacoder.androidsqlitebasics.R;

public class EditProfileActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt, dobEt;
    RadioGroup genderRadio;
    Button editBt, searchBt, deleteBt;

    //to get access to database table
    DBHelper mHelper;

    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        usernameEt = findViewById(R.id.idUsernameEt);
        passwordEt = findViewById(R.id.idPasswordEt);
        dobEt = findViewById(R.id.idBirthdayEt);

        genderRadio = findViewById(R.id.radioGroup);

        editBt = findViewById(R.id.idEditBt);
        searchBt = findViewById(R.id.idSearchBt);
        deleteBt = findViewById(R.id.idDeleteBt);

        //initialize db helper when app create
        mHelper = new DBHelper(this);

        //TODO: get userId that is coming from the home activity to search using user Id(not sure this way or using search username)
        //If press update or delete without searching user id coming from the home activity will be deleted
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getLongExtra("userId", -1);
        }

        //if user clicked edit button
        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = usernameEt.getText().toString();
                String pass = passwordEt.getText().toString();
                String dob = dobEt.getText().toString();
                String gender = "";
                if (genderRadio.getCheckedRadioButtonId() == R.id.idMaleRadio) {
                    gender = "Male";
                } else if (genderRadio.getCheckedRadioButtonId() == R.id.idFemaleRadio) {
                    gender = "Female";
                }

                //edit logged in user
                if (mHelper.updateInfo(userId, uname, pass, dob, gender)) {
                    Toast.makeText(EditProfileActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cannot update!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //if user clicked search button
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get typed username to search
                String username = usernameEt.getText().toString();

                //get current user values to textFields from readInfo
                Cursor cursor = mHelper.readAllInfor();

                //TODO: I think this way is not the perfect since, we can directly search using the query
                while (cursor.moveToNext()) {
                    //if typed username equals with table value
                    if (username.equals(cursor.getString(1))) {
                        //get the user id to update and delete
                        userId = cursor.getLong(0);
                        //if there is any matching username with db user table get those values and place into textfields
                        passwordEt.setText(cursor.getString(2));
                        dobEt.setText(cursor.getString(3));

                        if (cursor.getString(4) != null) {
                            if (cursor.getString(4).equals("Male")) {
                                genderRadio.check(R.id.idMaleRadio);
                            } else genderRadio.check(R.id.idFemaleRadio);
                        }
                    }
                }
                cursor.close();

                //dumb trick to display if user not exists
                if (passwordEt.getText().toString().equals("")) {
                    //if searched user not exists
                    Toast.makeText(EditProfileActivity.this, "No user found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //if user clicked delete button
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete user from table and if deleted count is greater than 0 display delete message
                if (mHelper.deleteInfo(userId)) {
                    Toast.makeText(EditProfileActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "User not in the table!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();
    }
}
