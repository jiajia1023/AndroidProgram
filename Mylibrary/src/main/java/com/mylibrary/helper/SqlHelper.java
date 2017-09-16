package com.mylibrary.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mylibrary.R;
import com.mylibrary.utils.FileUtils;

/**
 * Created by zoulx on 2016/3/24.
 */
public class SqlHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DBBeatName="Beta.db";
    public static final String DBName="city.db";
    public SqlHelper ( Context context, String dbName ) {
        this(context, dbName, 1);
    }

    public SqlHelper ( Context context, String dbName, int version ) {
        this(context, dbName, null, version);
    }

    public SqlHelper ( Context context, String name, SQLiteDatabase.CursorFactory factory, int version ) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createExitDB();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<newVersion){
            upgradeDB();
        }
    }

    private void createExitDB(){
//        boolean flag= FileUtils.getInstance ().writeFile ( context.getResources ().openRawResource ( R.raw.city ), "/data/data/" + context.getPackageName () + "/databases", DBName, true );
//        Log.e("TEST", "createDB: "+flag);
    }

    private void upgradeDB(){
//        FileUtils.getInstance().writeFile ( context.getResources().openRawResource ( R.raw.city ), "/data/data/" + context.getPackageName () + "/databases", DBName, true );
    }

}
