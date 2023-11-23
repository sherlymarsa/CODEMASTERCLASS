package com.example.codemasterclass

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper (var context: Context): SQLiteOpenHelper(
    context,DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private val DATABASE_NAME = "codemasterclass"
        private val DATABASE_VERSION = 2

        //table name
        private val TABLE_ACCOUNT = "account"

        //column account table
        private val COLUMN_EMAIL = "email"
        private val COLUMN_NAME = "name"
        private val COLUMN_PASSWORD = "password"
    }
    //create table account sql query
    private val CREATE_ACCOUNT_TABLE =("CREATE TABLE " + TABLE_ACCOUNT + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY, "+ COLUMN_NAME +" TEXT, "
            + COLUMN_PASSWORD + " TEXT)")

    //DROP TABLE ACOUNT SQL QUERY
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_ACCOUNT_TABLE)
        onCreate(p0)
    }

    //LOGIN CHECK
    fun checkLogin(username:String, password:String):Boolean{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        //selection criteria
        val selection = "$COLUMN_NAME = ? AND $COLUMN_PASSWORD = ?"
        //selection arguments
        val selectionArgs = arrayOf(username,password)

        val cursor = db.query(TABLE_ACCOUNT, //table query
            colums, //colums to return
            selection, //colums for WHERE clause
            selectionArgs, //the values for the WHERE clause
            null, //group the rows
            null, //
            null )

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        //check data available or not
        if(cursorCount > 0)
            return true
        else
            return false
    }

    //add User
    fun addAcount(email: String, name:String, password: String){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_ACCOUNT, null, values)
        //show message
        if (result==(0).toLong()){
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Register Succes, " +
                    "please login using your new account", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    @SuppressLint("Range")
    fun checkData(email: String):String{
        val columns = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        var name:String = ""

        val  cursor = db.query(TABLE_ACCOUNT, //table to query
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)
        if (cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return name
    }





}