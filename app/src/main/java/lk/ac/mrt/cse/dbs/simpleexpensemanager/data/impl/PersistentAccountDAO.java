/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * A SQLite database is used to store account information in the table account_info
 */
public class PersistentAccountDAO extends DatabaseOperator implements AccountDAO {

    public PersistentAccountDAO(Context context) {
        super(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> acc_nums = new ArrayList<>();

        Cursor cursor = openR().rawQuery("SELECT "+ "account_num" + " FROM " + TABLE1 , null);

        if (cursor.moveToFirst()) {
            do {
                acc_nums.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return acc_nums;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        Cursor cursor = openR().rawQuery("SELECT * FROM " + TABLE1, null);
        if (cursor.moveToFirst()) {
            do {
                accounts.add(new Account(cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(1),
                        cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;
        String query = "SELECT * FROM " + TABLE1 + " WHERE account_num = " + "'" + accountNo + "'";
        Cursor cursor = openR().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(1),
                        cursor.getDouble(3));

        }
        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        try {

            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("account_num", account.getAccountNo());
            newValues.put("acc_holder", account.getAccountHolderName());
            newValues.put("bank", account.getBankName());
            newValues.put("balance", account.getBalance());

            // Insert the row into table
            openW().insert(TABLE1, null, newValues);
            openW().close();
        } catch (Exception ex) {
            //error
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        try {
            openW().delete(TABLE1, "account_num=?", new String[]{accountNo}) ;
        } catch (Exception e) {
            //error
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account ac = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                ac.setBalance(ac.getBalance() - amount);
                break;
            case INCOME:
                ac.setBalance(ac.getBalance() + amount);
                break;
        }
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("account_num", ac.getAccountNo());
        updatedValues.put("acc_holder", ac.getAccountHolderName());
        updatedValues.put("bank", ac.getBankName());
        updatedValues.put("balance", ac.getBalance());

        openW().update(TABLE1, updatedValues, "account_num = ?", new String[]{accountNo});
        openW().close();
    }
}
