package com.bla.androidsqlitebasics.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bla.androidsqlitebasics.database.DBHelper;
import com.live.sinhalacoder.androidsqlitebasics.R;

public class HomeActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt;
    Button loginBt, registerBt;

    //to get access to database table
    DBHelper mHelper;

    //newly added user primary key
    long userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameEt = findViewById(R.id.idUsernameEt);
        passwordEt = findViewById(R.id.idPasswordEt);

        loginBt = findViewById(R.id.idLoginBt);
        registerBt = findViewById(R.id.idRegisterBt);

        //initialize db helper when app create
        mHelper = new DBHelper(this);

        //if user clicked login button
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login existing user
                login();
            }
        });

        //if user clicked register button
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new user to the database
                registerUser();
            }
        });
    }

    private void login() {
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();

        //if username and pass does not match -1 will return from checkUser function and if not -1 logged in
        userId = mHelper.checkUser(username, password);
        if (userId != -1) {
            Intent intent = new Intent(HomeActivity.this, ProfileManagementActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Err.. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerUser() {
        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();

        userId = mHelper.addInfo(username, password);
        if (userId == -1) {
            Toast.makeText(this, "Err.. Try again!", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Successfully registed!", Toast.LENGTH_SHORT).show();
            login();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();
    }
}
