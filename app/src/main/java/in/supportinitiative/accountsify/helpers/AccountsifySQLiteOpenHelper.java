package in.supportinitiative.accountsify.helpers;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.supportinitiative.accountsify.utils.Constants;
import in.supportinitiative.accountsify.entities.Account;
import in.supportinitiative.accountsify.entities.Currency;
import in.supportinitiative.accountsify.entities.Transaction;

/**
 * Created by SI289869 on 3/19/2015.
 */
public class AccountsifySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "AccountsifySQLiteOpenHelper";
    private static final String DATABASE_NAME = Constants.DATABASE_NAME;
    private static final int DATABASE_VERSION = Constants.DATABASE_VERSION;

    static {
        // register our models
        cupboard().register(Transaction.class);
        cupboard().register(Account.class);
        cupboard().register(Currency.class);
    }

    public AccountsifySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this will ensure that all tables are created
        Log.v(TAG, "onCreate");
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks


        Account newaccount = new Account();
        Account newaccount1 = new Account();

        newaccount.account = "Siva";
        newaccount.currency = "INR";

        newaccount1.account = "Raman";
        newaccount1.currency = "USD";

        db.beginTransaction();
        try {
            cupboard().withDatabase(db).put(newaccount, newaccount1);

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // do migration work
    }


    public void insertTransaction(Transaction newTransacction) {
        //this will add the transaction

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            cupboard().withDatabase(db).put(newTransacction);

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }


    }


    public List<String> getAllAccounts() {

        Log.v(TAG, "getAllAccounts");

        List<String> labels = new ArrayList<String>();

        // Select All Query

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor accounts = cupboard().withDatabase(db).query(Account.class).getCursor();

        Log.v(TAG, "getAllAccounts =>" + accounts.getColumnCount());

        if (accounts.moveToFirst()) {
            do {
                Log.v(TAG, "getAllAccounts =>" + accounts.getString(1));
                labels.add(accounts.getString(1));
            } while (accounts.moveToNext());
        }

        // closing connection
        accounts.close();
        db.close();

        // returning lables
        return labels;
    }


}
