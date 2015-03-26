package in.supportinitiative.accountsify.providers;

/**
 * Created by SI289869 on 3/20/2015.
 */
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


import in.supportinitiative.accountsify.helpers.AccountsifySQLiteOpenHelper;
import in.supportinitiative.accountsify.entities.Transaction;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class TransactionsContentProvider extends ContentProvider {
    private static final String TAG = "TransactionsContentProvider";
    public static String AUTHORITY = "in.supportinitiative.provider.accountsify";

    public static final Uri TRANSACTION_URI = Uri.parse("content://"+AUTHORITY+"/transaction");

    private AccountsifySQLiteOpenHelper mDatabaseHelper;
    private static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int TRANSACTION = 0;
    private static final int TRANSACTIONS = 1;


    static {
        sMatcher.addURI(AUTHORITY, "transaction/#", TRANSACTION);
        sMatcher.addURI(AUTHORITY, "transaction", TRANSACTIONS);
    }

    @Override
    public int delete(Uri uri, String selection, String[] args) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.v(TAG, "onCreate");
        mDatabaseHelper = new AccountsifySQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(TAG, "query");
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        switch (sMatcher.match(uri)) {
            case TRANSACTIONS:
                // this is the full query syntax, most of the time you can leave out projection etc
                // if the content provider returns a fixed set of data
                return cupboard().withDatabase(db).query(Transaction.class).
                        withProjection(projection).
                        withSelection(selection, selectionArgs).
                        orderBy(sortOrder).
                        getCursor();
            case TRANSACTION:
                return cupboard().withDatabase(db).query(Transaction.class).
                        byId(ContentUris.parseId(uri)).
                        getCursor();

        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
