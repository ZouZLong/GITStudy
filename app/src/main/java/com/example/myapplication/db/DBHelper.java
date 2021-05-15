package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库的
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";  //数据库名字
    private static final int DATABASE_VERSION = 1;         //数据库版本号
    private static  SQLiteDatabase sqLiteDatabase = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * 创建数据库表：chat
     * _id为主键，自增
     * <p>
     * primary key 主键定义约束，唯一且非空
     * auto_increment 自增长
     * not null 非空
     * unique 唯一
     * varchar类型的长度不固定，占用更少的存储空间。
     **/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("123:", "创建聊天的大表");
        this.sqLiteDatabase=sqLiteDatabase;
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS chat(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userid VARCHAR," +
                "info TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        super.onOpen(sqLiteDatabase);
    }

    /**
     * 一个聊天对象 一个表
     *
     * @param tableName
     */
    public void createTable(String tableName){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userid VARCHAR," +
                "info TEXT)");
    }
}
