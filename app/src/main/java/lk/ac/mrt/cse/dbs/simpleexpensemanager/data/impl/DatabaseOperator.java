package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public abstract class DatabaseOperator extends SQLiteOpenHelper implements Serializable{

    public static final String DB_NAME = "190310J_expenseManager.db";
    public static final String TABLE1 = "account_info";
    public static final String TABLE2 = "transaction_info";
    private static int version = 1;



    public DatabaseOperator(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String statement1 = "CREATE TABLE " + TABLE1 + "(account_num TEXT PRIMARY KEY, acc_holder TEXT, bank TEXT, balance REAL)";
            String statement2 = "CREATE TABLE " + TABLE2 + "(trans_id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, account_num TEXT, amount REAL, ex_type TEXT, FOREIGN KEY(account_num) REFERENCES " + TABLE2 + "(account_num))";
            db.execSQL(statement1);
            db.execSQL(statement2);

        } catch (Exception er) {
            //error
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            version = newVersion;
            db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
            onCreate(db);
        } catch (Exception er) {
            //error
        }
    }
    // get readable/writable database
    public SQLiteDatabase openR() throws SQLException {
        return this.getReadableDatabase();
    }
    public SQLiteDatabase openW() throws SQLException {
        return this.getWritableDatabase();
    }

}

/*

    TEST METHODS


    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return database;
    }

    // method to insert a record in Table
    public void insertAccount(String account_num, String acc_holder, String bank, double balance) {

        try {

            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("account_num", account_num);
            newValues.put("acc_holder", acc_holder);
            newValues.put("bank", bank);
            newValues.put("balance", balance);

            // Insert the row into your table
            database = this.getWritableDatabase();
            long result = database.insert(TABLE1, null, newValues);
            Log.i("Row Insert Result ", String.valueOf(result));
//            toast("User Info Saved!");
            database.close();

        } catch (Exception ex) {
            //error
        }
    }

    // method to Update an Existing Row in Table
    public void updateAccount(String account_num, String acc_holder, String bank, double balance) {
        try {
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("account_num", account_num);
            updatedValues.put("acc_holder", acc_holder);
            updatedValues.put("bank", bank);
            updatedValues.put("balance", balance);
            database = this.getReadableDatabase();
            database.update(TABLE1, updatedValues, "account_num = ?", new String[]{account_num});
            database.close();
//        toast("Row Updated!");
        } catch (Exception ex) {
            //error
        }
    }


    // method to delete a Record in Table using Primary Key Here it is ID
    public void deleteAccount(String account_num) {
        try {
            database = this.getReadableDatabase();
            int numberOFEntriesDeleted = database.delete(TABLE1, "account_num=?", new String[]{account_num}) ;
//        toast("Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted);
        } catch (Exception e) {
            //error
        }

    }

    // method to get all Rows Saved in Table
    public Map<String, Account> getAccounts() {

        Map<String, Account> accs = new HashMap<>();

        try {
            Account acc;
            database=this.getReadableDatabase();
            Cursor projCursor = database.query(TABLE1, null, null,null, null, null, null,null);
            while (projCursor.moveToNext()) {
                String account_num = projCursor.getString(projCursor.getColumnIndex("account_num"));
                String acc_holder = projCursor.getString(projCursor.getColumnIndex("acc_holder"));
                String bank = projCursor.getString(projCursor.getColumnIndex("bank"));
                double balance = projCursor.getDouble(projCursor.getColumnIndex("balance"));
                acc=new Account(account_num, acc_holder, bank, balance);
                accs.put(account_num, acc);
            }
            projCursor.close();

        } catch (Exception e) {
            //error
        }
        return accs;
    }


    //T R A N S A C T I O N
    // method to insert a record in Table
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_ = dateFormat.format(date);

            String expenseType_ = expenseType.toString();

            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("date", date_);
            newValues.put("accountNo", accountNo);
            newValues.put("expenseType", expenseType_);
            newValues.put("amount", amount);

            // Insert the row into your table
            database = this.getWritableDatabase();
            database.insert(TABLE2, null, newValues);
            database.close();

        } catch (Exception ex) {
            //error
        }
    }

    public List<Transaction> getTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        try {
            Transaction transaction;
            database=this.getReadableDatabase();
            Cursor projCursor = database.query(TABLE2, null, null,null, null, null, null,null);
            while (projCursor.moveToNext()) {
                String date_ = projCursor.getString(projCursor.getColumnIndex("date"));
                String account_num = projCursor.getString(projCursor.getColumnIndex("account_num"));
                double amount = projCursor.getDouble(projCursor.getColumnIndex("amount"));
                String type = projCursor.getString(projCursor.getColumnIndex("type"));

                //STRING DATE  TO DATE CONVERSION
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(date_);

                ExpenseType expenseType = type.equals("INCOME") ? ExpenseType.INCOME : ExpenseType.EXPENSE;

                transaction=new Transaction(date, account_num, expenseType, amount);
                transactions.add(transaction);
            }
            projCursor.close();
            database.close();

        } catch (Exception e) {
            //error
        }
        return transactions;
    }
    */