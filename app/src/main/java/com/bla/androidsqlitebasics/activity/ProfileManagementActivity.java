package com.bla.androidsqlitebasics.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bla.androidsqlitebasics.database.DBHelper;
import com.live.sinhalacoder.androidsqlitebasics.R;

public class ProfileManagementActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt, dobEt;
    RadioGroup genderRadio;
    Button updateProfileBt;

    //to get access to database table
    DBHelper mHelper;

    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        usernameEt = findViewById(R.id.idUsernameEt);
        passwordEt = findViewById(R.id.idPasswordEt);
        dobEt = findViewById(R.id.idBirthdayEt);

        genderRadio = findViewById(R.id.radioGroup);

        updateProfileBt = findViewById(R.id.idUpdateBt);

        //initialize db helper when app create
        mHelper = new DBHelper(this);

        //get userId that is coming from the home activity
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getLongExtra("userId", -1);
        }

        //get logged in or registered user data from table and bind to editTexts
        Cursor cursor = mHelper.readAllInfor(userId);
        if (cursor.moveToFirst()) {
            usernameEt.setText(cursor.getString(1));
            passwordEt.setText(cursor.getString(2));
            dobEt.setText(cursor.getString(3));
            if (cursor.getString(4) != null) {
                if (cursor.getString(4).equals("Male")) {
                    genderRadio.check(R.id.idMaleRadio);
                } else {
                    genderRadio.check(R.id.idFemaleRadio);
                }
            }
        }
        //if user clicked edit button
        updateProfileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open update profile
                Intent intent = new Intent(ProfileManagementActivity.this, EditProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();
    }
}
