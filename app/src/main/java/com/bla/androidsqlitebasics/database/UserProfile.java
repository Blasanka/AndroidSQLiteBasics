package com.bla.androidsqlitebasics.database;

import android.provider.BaseColumns;

public final class UserProfile {

    private UserProfile() { }

    public class User implements BaseColumns {
        public static final String TABLE_NAME = "UserInfo";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_DOB = "dob";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_GENDER = "gender";
    }
}
