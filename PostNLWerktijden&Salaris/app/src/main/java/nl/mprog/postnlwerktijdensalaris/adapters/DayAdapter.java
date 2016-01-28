/**
 * DayAdapter.java
 *
 * Custom adapter for adding content into listView of Days.java.
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

import nl.mprog.postnlwerktijdensalaris.modelclasses.Day;
import nl.mprog.postnlwerktijdensalaris.R;

public class DayAdapter extends ArrayAdapter {

    private ArrayList<Day> dayListItems;

    /**
     * Constructor for DayAdapter objects.
     */
    public DayAdapter(Context context, int textViewResourceId, ArrayList<Day> listItems) {
        super(context, textViewResourceId, listItems);
        this.dayListItems = listItems;
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

        Day listItemObj = dayListItems.get(position);

        // set list item object values in textviews
        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.day);

        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        textViewDownLeft.setText(listItemObj.districts);

        TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight1);
        textViewUpRight.setText(listItemObj.timeTotal);

        // if time extra is negative, use minus instead of plus
        TextView textViewDownRight = (TextView) view.findViewById(R.id.listItemDownRight);
        String timeGoalExtra;
        if (listItemObj.timeExtra.charAt(0) == '-') {
            timeGoalExtra = listItemObj.timeGoal + " - " + listItemObj.timeExtra.substring(1);
        }
        else {
            timeGoalExtra = listItemObj.timeGoal + " + " + listItemObj.timeExtra;
        }
        textViewDownRight.setText(timeGoalExtra);

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idItem = Integer.toString(listItemObj.id2);
        textViewUpCenter.setText(idItem);

        return view;
    }
}
