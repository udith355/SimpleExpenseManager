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

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;


import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private ExpenseManager expenseManager;

    @Before
    public void setup() {
        Context c = ApplicationProvider.getApplicationContext();
        this.expenseManager = new PersistentExpenseManager(c);
    }

    @Test
    public void testAddingAcount() {

        //test adding
        String accNo = "355";
        expenseManager.addAccount(accNo, "BOC", "Udith", 1000);
        boolean res = expenseManager.getAccountNumbersList().contains(accNo);

        //test removing
        try {
            expenseManager.getAccountsDAO().removeAccount(accNo);
            boolean res_ = !(expenseManager.getAccountNumbersList().contains(accNo));
            assertTrue(res && res_);
        } catch (InvalidAccountException e) {
            assertTrue(false);
        }
    }


    @Test
    public void testTransactions() {

        //test transaction logging
        String accNo = "355";
        try {

            expenseManager.addAccount(accNo, "BOC", "Udith", 1000);
            expenseManager.updateAccountBalance(accNo, 17, 5, 2022, ExpenseType.INCOME, "1000");
            List<Transaction> transactions = expenseManager.getTransactionLogs();
            boolean res = false;
            for (Transaction t : transactions) {
                if (t.getAccountNo().equals(accNo)) {
                    res = res || true;
                }
            }
            assertTrue(res);
        } catch (InvalidAccountException e) {
            assertTrue(false);
        }




    }
}