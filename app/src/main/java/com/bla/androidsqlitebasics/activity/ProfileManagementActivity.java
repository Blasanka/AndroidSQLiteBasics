package com.bla.androidsqlitebasics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.bla.androidsqlitebasics.database.DBHelper;
import com.bla.androidsqlitebasics.model.User;
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

        //get logged in or registered user data user object that we retrive using readAllInfor()
        User u = mHelper.readAllInfor(userId);
        usernameEt.setText(u.getUsername());
        passwordEt.setText(u.getPassword());
        dobEt.setText(u.getDob());
        if (u.getGender() != null) {
            if (u.getGender().equals("Male")) {
                genderRadio.check(R.id.idMaleRadio);
            } else if (u.getGender().equals("Female")) {
                genderRadio.check(R.id.idFemaleRadio);
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
