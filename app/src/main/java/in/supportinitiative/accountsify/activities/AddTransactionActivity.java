package in.supportinitiative.accountsify.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.supportinitiative.accountsify.helpers.AccountsifySQLiteOpenHelper;
import in.supportinitiative.accountsify.R;
import in.supportinitiative.accountsify.entities.Transaction;


public class AddTransactionActivity extends ActionBarActivity {

    @InjectView(R.id.add_transaction_layout) LinearLayout addTransactionLayout;
    @InjectView(R.id.add_transaction_date) EditText transactionDate;
    @InjectView(R.id.add_transaction_time) EditText transactionTime;
    @InjectView(R.id.add_transaction_account) Spinner accountSpinner;
    @InjectView(R.id.transaction_list_toolbar) Toolbar transactionListToolbar;


    private static final String TAG = "AddTransactionActivity";
    private DatePickerDialog transactionDatePickerDialog;
    private TimePickerDialog transactionTimePickerDialog;


    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        ButterKnife.inject(this);

        //Load Toolbar
        setSupportActionBar(transactionListToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Accounts Spinner populate starts
        loadAccountSpinnerData();


        //Date picker dialog code starts
        //TODO
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        Log.v(TAG, "Calendar=>" + (Calendar.YEAR) + "=" + (Calendar.MONTH) + "=" + (Calendar.DAY_OF_MONTH));
        Log.v(TAG, "Calendar=>" + newCalendar.get(Calendar.YEAR) + "=" + newCalendar.get(Calendar.MONTH) + "=" + newCalendar.get(Calendar.DAY_OF_MONTH));
        transactionDate.setText(dateFormatter.format(newCalendar.getTime()));

        transactionDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                transactionDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //Date picker dialog code ends



        //Time picker dialog code starts

        transactionTime.setText(pad(newCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + pad(newCalendar.get(Calendar.MINUTE)));
        Log.v(TAG, "Calendar=>" + (Calendar.HOUR_OF_DAY) + "=" + (Calendar.MINUTE) );
        Log.v(TAG, "Calendar=>" + newCalendar.get(Calendar.HOUR_OF_DAY) + "=" + newCalendar.get(Calendar.MINUTE) );

        transactionTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Log.v(TAG,"onTimeSet="+hourOfDay + ":" + minute);
                transactionTime.setText(pad(hourOfDay) + ":" + pad(minute));
            }

        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        //Time picker dialog code ends

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveTransaction(addTransactionLayout);
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveTransaction(View v) {
        Log.v(TAG, "saveTransaction =>");
        Transaction newTransaction = new Transaction();

        Spinner account = (Spinner) findViewById(R.id.add_transaction_account);
        Log.v(TAG, "saveTransaction account=>" + account.getSelectedItem().toString());
        newTransaction.account = account.getSelectedItem().toString();
        Log.v(TAG, "saveTransaction account=>" + newTransaction.account);

        final EditText category = (EditText) findViewById(R.id.add_transaction_category);
        newTransaction.category = category.getText().toString();
        Log.v(TAG, "saveTransaction category=>" + newTransaction.category);

        final EditText subcategory = (EditText) findViewById(R.id.add_transaction_subcategory);
        newTransaction.subcategory = subcategory.getText().toString();
        Log.v(TAG, "saveTransaction subcategory=>" + newTransaction.subcategory);

        final EditText notes = (EditText) findViewById(R.id.add_transaction_notes);
        newTransaction.notes = category.getText().toString();
        Log.v(TAG, "saveTransaction notes=>" + newTransaction.notes);

        final EditText amount = (EditText) findViewById(R.id.add_transaction_amount);
        newTransaction.amount = Integer.parseInt(amount.getText().toString());

        Log.v(TAG, "Amount =>" + newTransaction.amount);


        try {
            AccountsifySQLiteOpenHelper sqliteHelper = new AccountsifySQLiteOpenHelper(this);
            sqliteHelper.insertTransaction(newTransaction);
            Toast.makeText(this, "Transaction Saved", Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }

    }


    private void loadAccountSpinnerData() {
        // database handler
        AccountsifySQLiteOpenHelper sqliteHelper = new AccountsifySQLiteOpenHelper(this);

        // Spinner Drop down elements
        List<String> lables = sqliteHelper.getAllAccounts();

        // Creating adapter for spinner
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        accountSpinner.setAdapter(accountAdapter);
    }

    public void showDatePickerDialog(View view) {
        if (view == transactionDate) {
            transactionDatePickerDialog.show();
        }
    }

    public void showTimePickerDialog(View view) {
        if (view == transactionTime) {
            transactionTimePickerDialog.show();
        }
    }

    public String pad(int input)
    {
        String str = "";
        if (input > 10) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
    }




}
