/**
 * MonthAdapter.java
 *
 * Custom adapter for adding content into listView of Months.java.
 *
 * Made by Remco Blom - mProg Project
 */

package nl.mprog.postnlwerktijdensalaris.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mprog.postnlwerktijdensalaris.modelclasses.Month;
import nl.mprog.postnlwerktijdensalaris.R;

public class MonthAdapter extends ArrayAdapter {

    private ArrayList<Month> monthListItems;

    /**
     * Constructor for MonthAdapter objects.
     */
    public MonthAdapter(Context context, int textViewResourceId, ArrayList<Month> listItems) {
        super(context, textViewResourceId, listItems);
        this.monthListItems = listItems;
    }

    /**
     * Sets layout of listview and sets values of list item object in right textviews.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate layout into view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_listitem_layout, parent, false);
        }

        Month listItemObj = monthListItems.get(position);

        // set list item object values in textviews
        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.month);

        // if days is 1, use 'day' instead of 'days'
        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        String daysStr;
        if (listItemObj.days == 1) {
            daysStr = listItemObj.days + " dag";
        }
        else {
            daysStr = listItemObj.days + " dagen";
        }
        textViewDownLeft.setText(daysStr);

        // if double has no two decimals, put a zero behind it
        TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight1);
        String salaryStr = "€ " + listItemObj.salary;
        if (salaryStr.charAt(salaryStr.length() - 2) == '.') {
            salaryStr = salaryStr + "0";
        }
        textViewUpRight.setText(salaryStr);

        TextView textViewDownRight = (TextView) view.findViewById(R.id.listItemDownRight);
        textViewDownRight.setText(listItemObj.time);

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idItem = Integer.toString(listItemObj.id);
        textViewUpCenter.setText(idItem);

        return view;
    }
}
