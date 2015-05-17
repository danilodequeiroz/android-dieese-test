package com.danilodequeiroz.contactsdefy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mInstance = null;
    private Context context;

/*
    private long mId;
    private String mName;
    public String mSurname;
    private Date mBirthDate;
    private String mCompanyName;
    private String mPhone;
    private String mMobilePhone;
    private String mWorkPhone;*/

    public static DatabaseHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private static final String CREATE_DATABASE[] = {
            "CREATE TABLE tb_contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name VARCHAR(75) NOT NULL, " + "surname VARCHAR(75), "
                    + "birthdate VARCHAR(8) , "
                    + "companyName VARCHAR(75) , "
                    + "phone VARCHAR(25), "
                    + "mobilePhone VARCHAR(25), "
                    + "workPhone VARCHAR(25));" ,
                    "INSERT INTO tb_contacts(name,surname,birthdate," +
                    "companyName,phone,mobilePhone,workPhone)VALUES(" +
                    "'Danilo','de Queiroz','1987-07-10','braz.io corp'," +
                    "'+55 13 98828-8265','+55 13 98110-5760','+55 13 3011-3546');",


    };

    public static final String DELETE_DATABASE[] = {
            "DROP TABLE IF EXISTS tb_contacts;"};

    public static final String DATABASE_NAME = "CONTACTSDEFY.sqlite";

    public static final String TB_CONTACTS = "tb_contacts";

    // Nomes das colunas
    public static final String[] CONTACTS_COLS = new String[] { "id",
            "name", "surname", "birthdate", "companyName", "phone", "mobilePhone",
            "workPhone"};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(TAG, CREATE_DATABASE[0]);
            db.execSQL(CREATE_DATABASE[0]);
            db.execSQL(CREATE_DATABASE[1]);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar as tabelas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading Database...");
        try {
            // Deletando tabelas
            Log.i(TAG, DELETE_DATABASE[0]);
            db.execSQL(DELETE_DATABASE[0]);
            // Criando tabelas
            Log.i(TAG, CREATE_DATABASE[0]);
            db.execSQL(CREATE_DATABASE[0]);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar o banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}