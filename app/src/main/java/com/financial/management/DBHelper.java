package com.financial.management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Test.db";
    public static final String TABLE_NAME = "userinfo";
    public static final String COLUMN_USERID = "uid";
    public static final String COLUMN_USERPWD = "upwd";

    public static final String RECORD_TABLE_NAME = "record";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STATE = "state";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MONEY = "money";

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " (" + COLUMN_USERID + " TEXT NOT NULL PRIMARY KEY, "
            + COLUMN_USERPWD + " TEXT NOT NULL)";

    private static final String CREATE_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS "
            + RECORD_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_TYPE + " TEXT, "
            + COLUMN_MONEY + " FLOAT, "
            + COLUMN_STATE + " TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 3); // 更新版本号到 3
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_RECORD_TABLE);
            Log.d("DBHelper", "Tables created successfully.");
        } catch (SQLException e) {
            Log.e("DBHelper", "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d("DBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
            if (oldVersion < 3) { // 仅当需要更新时才执行
                db.execSQL("DROP TABLE IF EXISTS " + RECORD_TABLE_NAME);
                db.execSQL(CREATE_RECORD_TABLE);
                Log.d("DBHelper", "Record table recreated successfully.");
            }
        } catch (SQLException e) {
            Log.e("DBHelper", "Error upgrading tables: " + e.getMessage());
        }
    }

    // 登录方法
    public User userLogin(String userId, String userPwd) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        try (Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_USERID, COLUMN_USERPWD},
                COLUMN_USERID + "=? AND " + COLUMN_USERPWD + "=?",
                new String[]{userId, userPwd},
                null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERID)));
                user.setUserPwd(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERPWD)));
            }
        } catch (SQLException e) {
            Log.e("DBHelper", "Error during user login: " + e.getMessage());
        }
        return user;
    }

    // 注册方法
    public long registerUser(User user) {
        long result = -1;
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_USERID, user.getUserId());
            contentValues.put(COLUMN_USERPWD, user.getUserPwd());
            result = db.insert(TABLE_NAME, null, contentValues);
        } catch (SQLException e) {
            Log.e("DBHelper", "Error registering user: " + e.getMessage());
        }
        return result;
    }

    // 获取所有记录的方法
    public List<Record> getAllRecords() {
        List<Record> recordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(RECORD_TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                    String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                    float money = cursor.getFloat(cursor.getColumnIndex(COLUMN_MONEY));
                    recordList.add(new Record(type, date, money));
                }
            }
        } catch (SQLException e) {
            Log.e("DBHelper", "Error fetching records: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return recordList;
    }
}
