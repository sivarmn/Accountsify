package in.supportinitiative.accountsify.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import in.supportinitiative.accountsify.R;

/**
 * Created by SI289869 on 3/20/2015.
 */
public class TransactionsCursorAdapter extends ResourceCursorAdapter {
    private static final String TAG = "TransactionsCursorAdapter";
    private int mIdPos, mAmountPos, mCategoryPos, mDatePos;

    public TransactionsCursorAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor, 0);
        reinit(cursor);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        Log.v(TAG,"bindView");

        if (view.getTag(R.id.holder) == null) {
            ViewHolder holder = new ViewHolder();
            holder.amountTextView = (TextView) view.findViewById(R.id.amount);
            holder.dateTextView = (TextView) view.findViewById(R.id.date);
            holder.categoryTextView = (TextView) view.findViewById(R.id.category);
            view.setTag(R.id.holder, holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag(R.id.holder);

        String amountText = cursor.getString(mAmountPos);
        holder.amountTextView.setText(amountText);

        String dateText = cursor.getString(mDatePos);
        holder.dateTextView.setText(dateText);

        String categoryText = cursor.getString(mCategoryPos);
        Log.v(TAG,"bindView categoryText"+ categoryText);
        holder.categoryTextView.setText(categoryText);

    }

    private void reinit(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            mIdPos = cursor.getColumnIndex("_id");
            mAmountPos = cursor.getColumnIndex("amount");
            mCategoryPos = cursor.getColumnIndex("category");
            mDatePos = cursor.getColumnIndex("date");
        }
    }

    private static class ViewHolder {
        public TextView amountTextView;
        public TextView dateTextView;
        public TextView categoryTextView;
    }

}
