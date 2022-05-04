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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * A SQLite database is used to store transaction information in the table transaction_info
 */
public class PersistentTransactionDAO extends DatabaseOperator implements TransactionDAO {

    public PersistentTransactionDAO(Context context) {
        super(context);
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_ = dateFormat.format(date);

            String expenseType_ = expenseType.toString();

            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("date", date_);
            newValues.put("account_num", accountNo);
            newValues.put("ex_type", expenseType_);
            newValues.put("amount", amount);

            // Insert the row into your table
            openW().insert(TABLE2, null, newValues);
            openW().close();

        } catch (Exception ex) {
            //error
        }

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();


        Cursor cursor = openR().rawQuery("SELECT * FROM " + TABLE2, null);
        if (cursor.moveToFirst()) {
            do {
                String date_ = cursor.getString(1);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(date_);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String accountNo = cursor.getString(2);
                String type = cursor.getString(4);

                ExpenseType expenseType = type.equals("INCOME") ? ExpenseType.INCOME : ExpenseType.EXPENSE;

                Double amount = cursor.getDouble(3);
                transactions.add(new Transaction(date, accountNo, expenseType, amount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> t = getAllTransactionLogs();
        int size = t.size();
        if (size <= limit) {
            return t;
        }
        // return the last <code>limit</code> number of transaction logs
        return t.subList(size - limit, size);

    }

}
