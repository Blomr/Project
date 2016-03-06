/**
 * WalkAdapter.java
 *
 * Custom adapter for adding content into listView of Walks.java.
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

import nl.mprog.postnlwerktijdensalaris.R;
import nl.mprog.postnlwerktijdensalaris.modelclasses.Walk;

public class WalkAdapter extends ArrayAdapter {

    private ArrayList<Walk> walkListItems;

    /**
     * Constructor for WalkAdapter objects.
     */
    public WalkAdapter(Context context, int textViewResourceId, ArrayList<Walk> listItems) {
        super(context, textViewResourceId, listItems);
        this.walkListItems = listItems;
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

        Walk listItemObj = walkListItems.get(position);

        // set list item object values in textviews
        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.districtCode);

        // if time extra is negative, use minus instead of plus
        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        String timeGoalExtraTotal;
        if (listItemObj.timeExtra.charAt(0) == '-') {
            timeGoalExtraTotal = listItemObj.timeGoal + " - " + listItemObj.timeExtra.substring(1) + " = " + listItemObj.timeTotal;
        }
        else {
            timeGoalExtraTotal = listItemObj.timeGoal + " + " + listItemObj.timeExtra + " = " + listItemObj.timeTotal;
        }
        textViewDownLeft.setText(timeGoalExtraTotal);

        TextView textViewUpRight1 = (TextView) view.findViewById(R.id.listItemUpRight1);
        String timeBeginEnd1 = listItemObj.timeBegin1 + " - " + listItemObj.timeEnd1;
        textViewUpRight1.setText(timeBeginEnd1);

        // if time begin and time end are both 0, don't display them
        TextView textViewUpRight2 = (TextView) view.findViewById(R.id.listItemUpRight2);
        if (!listItemObj.timeBegin2.equals("00:00") && !listItemObj.timeEnd2.equals("00:00")) {
            String timeBeginEnd2 = listItemObj.timeBegin2 + " - " + listItemObj.timeEnd2;
            textViewUpRight2.setText(timeBeginEnd2);
        }
        else {
            textViewUpRight2.setText("");
        }

        TextView textViewUpRight3 = (TextView) view.findViewById(R.id.listItemUpRight3);
        if (!listItemObj.timeBegin3.equals("00:00") && !listItemObj.timeEnd3.equals("00:00")) {
            String timeBeginEnd3 = listItemObj.timeBegin3 + " - " + listItemObj.timeEnd3;
            textViewUpRight3.setText(timeBeginEnd3);
        }
        else {
            textViewUpRight3.setText("");
        }

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idItem = Integer.toString(listItemObj.id3);
        textViewUpCenter.setText(idItem);

        return view;
    }
}
