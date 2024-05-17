package com.samprama.mobileapp.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import com.samprama.mobileapp.viewmodel.RegistrationModel
import com.samprama.mobileapp.viewmodel.UserDataModel
import java.io.File

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users_db"
        private const val DATABASE_VERSION = 1

        // Define table 1 and column names
        private const val TABLE_NAME = "registration"
        private const val ID = "id"
        private const val MO_COMPANY_NAME = "mc_name"
        private const val MODEL_NUMBER = "model_no"
        private const val CUSTOMER_NAME = "customer_nm"
        private const val CUSTOMER_MO_NO = "customer_mo_no"
        private const val VILLAGE_NAME = "village_nm"
        private const val PROBLEM = "problem"
        private const val COST = "rate"
        private const val DEPOSIT = "deposit"
        private const val PENDING = "pending"
        private const val CUST_CREATED_DATE ="created_date"


        // Define table 2 and column names
        private const val TABLE_NAME_1 = "users"
        private const val UID = "id"
        private const val USERNAME = "user_name"
        private const val MOBILE_NO = "mobile_no"
        private const val MAIL = "mail_id"
        private const val PASSWORD = "password"
        private const val USER_CREATED_DATE="user_created_dt"

    }

    // Create the database table
    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $MO_COMPANY_NAME TEXT, $MODEL_NUMBER TEXT,$CUSTOMER_NAME TEXT, $CUSTOMER_MO_NO TEXT, $VILLAGE_NAME TEXT, $PROBLEM TEXT, $COST TEXT,$DEPOSIT TEXT,$PENDING TEXT,$CUST_CREATED_DATE TEXT)"
        db.execSQL(createTableSQL)

        val createUserTableSQL = "CREATE TABLE $TABLE_NAME_1 ($UID INTEGER PRIMARY KEY AUTOINCREMENT, $USERNAME TEXT, $MOBILE_NO TEXT,$MAIL TEXT, $PASSWORD TEXT,$USER_CREATED_DATE TEXT)"
        db.execSQL(createUserTableSQL)
    }

    // Upgrade the database (if needed)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if exists and recreate
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert data into the database
    fun insertData(mcName: String,modelNo: String,cName: String,cMoNo: String,vName: String,problem: String,rate: String,deposit:String,pending:String,createdDate: String) {
        val database = this.writableDatabase
//        val values = ContentValues()
        val values = ContentValues().apply {
            put(MO_COMPANY_NAME, mcName)
            put(MODEL_NUMBER, modelNo)
            put(CUSTOMER_NAME, cName)
            put(CUSTOMER_MO_NO, cMoNo)
            put(VILLAGE_NAME, vName)
            put(PROBLEM, problem)
            put(COST, rate)
            put(DEPOSIT,deposit)
            put(PENDING,pending)
            put(CUST_CREATED_DATE, createdDate)
        }
        database.insert(TABLE_NAME, null, values)
       // database.close()
    }

    fun insertUserData(username: String,mobileNO: String,mail: String,pass: String,date:String) {
        val database = this.writableDatabase
//        val values = ContentValues()
        val values = ContentValues().apply {
            put(USERNAME, username)
            put(MOBILE_NO, mobileNO)
            put(MAIL, mail)
            put(PASSWORD, pass)
            put(USER_CREATED_DATE,date)
        }
        database.insert(TABLE_NAME_1, null, values)
        System.out.println(values)
        // database.close()
    }

    // Read data from the database
    @SuppressLint("Range")
    fun readData(): ArrayList<RegistrationModel> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val data = ArrayList<RegistrationModel>()

        if (cursor.moveToFirst()) {
            do {
                val id =cursor.getInt(cursor.getColumnIndex(ID))
                val mc_name = cursor.getString(cursor.getColumnIndex(MO_COMPANY_NAME))
                val modelno = cursor.getString(cursor.getColumnIndex(MODEL_NUMBER))
                val cname = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME))
                val custmono = cursor.getString(cursor.getColumnIndex(CUSTOMER_MO_NO))
                val vname = cursor.getString(cursor.getColumnIndex(VILLAGE_NAME))
                val problem = cursor.getString(cursor.getColumnIndex(PROBLEM))
                val rate = cursor.getString(cursor.getColumnIndex(COST))
                val deposit = cursor.getString(cursor.getColumnIndex(DEPOSIT))
                val pending = cursor.getString(cursor.getColumnIndex(PENDING))
                val date = cursor.getString(cursor.getColumnIndex(CUST_CREATED_DATE))
                // Create an instance of RegistrationModel and add it to the list
                val registrationModel = RegistrationModel(id,mc_name, modelno, cname, custmono, vname, problem, rate,deposit,pending,date)
                data.add(registrationModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
      //  db.close()
        return data
    }

    // Read data from the database
    @SuppressLint("Range")
    fun readUserData(): ArrayList<UserDataModel> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_1", null)
        val data = ArrayList<UserDataModel>()

        if (cursor.moveToFirst()) {
            do {
                val id =cursor.getInt(cursor.getColumnIndex(UID))
                val userName = cursor.getString(cursor.getColumnIndex(USERNAME))
                val mobileNo = cursor.getString(cursor.getColumnIndex(MOBILE_NO))
                val mail = cursor.getString(cursor.getColumnIndex(MAIL))
                val pass = cursor.getString(cursor.getColumnIndex(PASSWORD))
                val date = cursor.getString(cursor.getColumnIndex(USER_CREATED_DATE))
                // Create an instance of RegistrationModel and add it to the list
                val userData = UserDataModel(id,userName, mobileNo, mail,pass,date)
                data.add(userData)
            } while (cursor.moveToNext())
        }
        cursor.close()
       // db.close()
        return data
    }

    // Update data in the database
    fun updateData(id: Int, mcName: String,modelNo: String,cName: String,cMoNo: String,vName: String,problem: String,rate: String,date:String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(MO_COMPANY_NAME, mcName)
            put(MODEL_NUMBER, modelNo)
            put(CUSTOMER_NAME, cName)
            put(CUSTOMER_MO_NO, cMoNo)
            put(VILLAGE_NAME, vName)
            put(PROBLEM, problem)
            put(COST, rate)
            put(CUST_CREATED_DATE,date)
        }
        db.update(TABLE_NAME, values, "$ID = ?", arrayOf(id.toString()))
       // db.close()
    }

    // Delete data from the database
    fun deleteData(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf(id.toString()))
      //  db.close()
    }

    // Change the database file location to external storage
    override fun getDatabaseName(): String {
        val folder = Environment.getExternalStorageDirectory()
        val dbFile = File(folder, DATABASE_NAME)
        return dbFile.absolutePath
    }
}