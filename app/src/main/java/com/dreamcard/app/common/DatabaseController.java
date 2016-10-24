package com.dreamcard.app.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.NotificationDB;
import com.dreamcard.app.entity.SearchCriteria;
import com.dreamcard.app.entity.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Moayed on 9/27/2014.
 */
public class DatabaseController extends SQLiteOpenHelper{

    private static DatabaseController db;

    public static DatabaseController getInstance(Context context) {
        if (db == null) {
            db = new DatabaseController(context);
        }
        return db;
    }

    public DatabaseController(Context context) {
        super(context, Params.SYSTEM_DB, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGIN_CREATE_TABLE);
        db.execSQL(SEARCH_CRITERIA_CREATE_TABLE);
        db.execSQL(NOTIFICATION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int position, int position2) {
        db.execSQL(LOGIN_CREATE_TABLE);
        db.execSQL(SEARCH_CRITERIA_CREATE_TABLE);
        db.execSQL(NOTIFICATION_TABLE_CREATE);
    }

    /**
     * Login type
     */
    public static final String TABLE_LOGIN="LOGIN_tbl";
    public static final String TABLE_SEARCH_CRITERIA="SEARCH_CRITERIA_TBL";
    public static final String TABLE_NOTIFICATIONS = "NOTIFICATION_TABLE";

    public static final String LOGIN_COLUMN_EMAIL="TXT_EMAIL";
    public static final String LOGIN_COLUMN_PASSWORD="TXT_PASSWORD";

    public static final String SEARCH_CRITERIA_CITY="TXT_CITY";
    public static final String SEARCH_CRITERIA_CITY_NAME="TXT_CITY_NAME";
    public static final String SEARCH_CRITERIA_CATEGORIES="TXT_CATEGORIES";
    public static final String SEARCH_CRITERIA_DISC_RATE="TXT_DISC_RATE";
    public static final String SEARCH_CRITERIA_RATE="TXT_RATE";

    private static final String NOTIFICATION_TYPE_COLUMN = "Type";
    private static final String NOTIFICATION_ID_COLUMN = "id";
    private static final String NOTIFICATION_TIMESTAMP = "TimeStamp";
    private static final String NOTIFICATION_POINTS_VALUE = "PointsValue";

    private String LOGIN_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN
            + "("
            + LOGIN_COLUMN_EMAIL + " VARCHAR ,"
            + LOGIN_COLUMN_PASSWORD + " VARCHAR"
            + ")";

    private String SEARCH_CRITERIA_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SEARCH_CRITERIA
            + "("
            + SEARCH_CRITERIA_CITY + " VARCHAR ,"
            + SEARCH_CRITERIA_CITY_NAME+" VARCHAR,"
            + SEARCH_CRITERIA_CATEGORIES + " VARCHAR,"
            + SEARCH_CRITERIA_DISC_RATE + " VARCHAR,"
            + SEARCH_CRITERIA_RATE + " VARCHAR"
            + ")";

    private String NOTIFICATION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTIFICATIONS
            + "("
            + NOTIFICATION_TYPE_COLUMN + " VARCHAR, "
            + NOTIFICATION_ID_COLUMN + " VARCHAR, "
            + NOTIFICATION_TIMESTAMP + " NUMBER, "
            + NOTIFICATION_POINTS_VALUE + " VARCHAR"
            + ")";

    public long saveNotificaiton(NotificationDB notification){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_TYPE_COLUMN, notification.type);
        values.put(NOTIFICATION_ID_COLUMN, notification.id);
        values.put(NOTIFICATION_TIMESTAMP, Calendar.getInstance().getTimeInMillis());
        values.put(NOTIFICATION_POINTS_VALUE, notification.pointsValue);

        return db.insert(TABLE_NOTIFICATIONS, null, values);
    }

    public ArrayList<NotificationDB> getNotification(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.query(TABLE_NOTIFICATIONS, new String[]{NOTIFICATION_TYPE_COLUMN, NOTIFICATION_ID_COLUMN}
                , NOTIFICATION_ID_COLUMN, new String[] {id}, null, null, null);

        ArrayList<NotificationDB> notificationArray = new ArrayList<NotificationDB>();

        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                NotificationDB notification = new NotificationDB();
                notification.type = rs.getString(rs.getColumnIndex(NOTIFICATION_TYPE_COLUMN));
                notification.id = rs.getString(rs.getColumnIndex(NOTIFICATION_ID_COLUMN));
                notification.pointsValue = rs.getString(rs.getColumnIndex(NOTIFICATION_POINTS_VALUE));
                notificationArray.add(notification);
                rs.moveToNext();
            }
        }

        return notificationArray;
    }

    public ArrayList<NotificationDB> getAllNotifications() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.query(TABLE_NOTIFICATIONS, new String[]{NOTIFICATION_TYPE_COLUMN, NOTIFICATION_ID_COLUMN, NOTIFICATION_POINTS_VALUE}
                , null, null, null, null, NOTIFICATION_TIMESTAMP + " DESC");

        ArrayList<NotificationDB> notificationArray = new ArrayList<NotificationDB>();

        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                NotificationDB notification = new NotificationDB();
                notification.type = rs.getString(rs.getColumnIndex(NOTIFICATION_TYPE_COLUMN));
                notification.id = rs.getString(rs.getColumnIndex(NOTIFICATION_ID_COLUMN));
                notification.pointsValue = rs.getString(rs.getColumnIndex(NOTIFICATION_POINTS_VALUE));
                notificationArray.add(notification);
                rs.moveToNext();
            }
        }

        return notificationArray;
    }

    public int deleteNotification(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NOTIFICATIONS, NOTIFICATION_ID_COLUMN + " like " + id, null);
    }

    public int saveCriteria(SearchCriteria bo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SEARCH_CRITERIA_CATEGORIES, bo.getCategories());
        values.put(SEARCH_CRITERIA_CITY,bo.getCityId());
        values.put(SEARCH_CRITERIA_DISC_RATE,bo.getDiscRate());
        values.put(SEARCH_CRITERIA_RATE, bo.getRate());
        values.put(SEARCH_CRITERIA_CITY_NAME, bo.getCityName());

        if(getCriteria()==null){
            db.insert(TABLE_SEARCH_CRITERIA, null, values);
        }else{
            db.update(TABLE_SEARCH_CRITERIA, values, null, null);
        }

        db.close();
        return 1;
    }

    public int saveLogin(String email,String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOGIN_COLUMN_EMAIL, email);
        values.put(LOGIN_COLUMN_PASSWORD, password);
        db.insert(TABLE_LOGIN, null, values);
        return 1;
    }

    public UserInfo getLoginInfo(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.query(TABLE_LOGIN, new String[]{LOGIN_COLUMN_EMAIL, LOGIN_COLUMN_PASSWORD}
                , null, null, null, null, null);
        String loginType=null;
        UserInfo bean=null;
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                bean=new UserInfo();
                bean.setEmail(rs.getString(rs.getColumnIndex(LOGIN_COLUMN_EMAIL)));
                bean.setPassword(rs.getString(rs.getColumnIndex(LOGIN_COLUMN_PASSWORD)));
                rs.moveToNext();
            }
        }
        return bean;
    }

    public SearchCriteria getCriteria(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.query(TABLE_SEARCH_CRITERIA, new String[]{SEARCH_CRITERIA_CATEGORIES, SEARCH_CRITERIA_CITY
                ,SEARCH_CRITERIA_DISC_RATE,SEARCH_CRITERIA_RATE,SEARCH_CRITERIA_CITY_NAME}
                , null, null, null, null, null);
        SearchCriteria bean=null;
        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                bean=new SearchCriteria();
                bean.setCategories(rs.getString(rs.getColumnIndex(SEARCH_CRITERIA_CATEGORIES)));
                bean.setCityId(rs.getString(rs.getColumnIndex(SEARCH_CRITERIA_CITY)));
                bean.setCityName(rs.getString(rs.getColumnIndex(SEARCH_CRITERIA_CITY_NAME)));
                bean.setDiscRate(rs.getString(rs.getColumnIndex(SEARCH_CRITERIA_DISC_RATE)));

                if(rs.getString(rs.getColumnIndex(SEARCH_CRITERIA_RATE))!=null
                        && rs.getString(rs.getColumnIndex(SEARCH_CRITERIA_RATE)).length() > 0) {
                    bean.setRate(rs.getInt(rs.getColumnIndex(SEARCH_CRITERIA_RATE)));
                }

                rs.moveToNext();
            }
        }
        return bean;
    }
    public int deleteLogin() {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_LOGIN, null, null);
        return result;
    }

    public int deleteCriteria() {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_SEARCH_CRITERIA, null, null);
        return result;
    }
}
