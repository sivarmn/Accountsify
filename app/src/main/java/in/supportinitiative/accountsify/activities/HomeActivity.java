package in.supportinitiative.accountsify.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.supportinitiative.accountsify.R;
import in.supportinitiative.accountsify.providers.TransactionsContentProvider;
import in.supportinitiative.accountsify.adapters.TransactionsCursorAdapter;


public class HomeActivity extends ActionBarActivity  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "HomeActivity";
    private static final int LOADER_ID = 0;

    private TransactionsCursorAdapter mAdapter;

    @InjectView(R.id.transaction_list_toolbar) Toolbar transactionListToolbar;
    @InjectView(R.id.transaction_list) ListView mTransactionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate=>" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        setSupportActionBar(transactionListToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        transactionListToolbar.setLogo(R.drawable.ic_launcher);


        Log.v(TAG,"before initLoader");
        getLoaderManager().initLoader(LOADER_ID, null, this);
        Log.v(TAG,"after initLoader");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_transaction) {
            Log.v(TAG, "onOptionsItemSelected add_transaction=>" );
            Intent intent = new Intent(this, AddTransactionActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(TAG, "onCreateLoader=>" );
        CursorLoader cursorLoader = new CursorLoader(this, TransactionsContentProvider.TRANSACTION_URI, new String[]{ },
                null, null, null
        );
        //cursorLoader.setUpdateThrottle(Constants.UPDATE_THROTTLE_DELAY);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

            Log.v(TAG,"onLoadFinished");
            Log.v(TAG, "new mDrawerAdapter");
            mAdapter = new TransactionsCursorAdapter(this,R.layout.transactions_list, cursor);
            mTransactionListView.setAdapter(mAdapter);
            Log.v(TAG, "after setAdapter");

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.v(TAG,"onLoaderReset");
        mAdapter.swapCursor(null);
    }

}
