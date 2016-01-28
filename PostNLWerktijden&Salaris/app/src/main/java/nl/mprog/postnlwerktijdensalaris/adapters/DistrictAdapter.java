/**
 * DistrictAdapter.java
 *
 * Custom adapter for adding content into listView of Districts.java.
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

import nl.mprog.postnlwerktijdensalaris.modelclasses.District;
import nl.mprog.postnlwerktijdensalaris.R;

public class DistrictAdapter extends ArrayAdapter {

    private ArrayList<District> districtListItems;

    /**
     * Constructor for DistrictAdapter objects.
     */
    public DistrictAdapter(Context context, int textViewResourceId, ArrayList<District> listItems) {
        super(context, textViewResourceId, listItems);
        this.districtListItems = listItems;
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

        District listItemObj = districtListItems.get(position);

        // set list item object values in textviews
        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.districtCode);

        if (!listItemObj.timeGoalBusy.equals("0:00")) {
            TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight1);
            String timeGoalBusy = "Piekdag: " + listItemObj.timeGoalBusy;
            textViewUpRight.setText(timeGoalBusy);
        }

        if (!listItemObj.timeGoalCalm.equals("0:00")) {
            TextView textViewDownRight = (TextView) view.findViewById(R.id.listItemDownRight);
            String timeGoalCalm = "Daldag: " + listItemObj.timeGoalCalm;
            textViewDownRight.setText(timeGoalCalm);
        }

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idStr = Integer.toString(listItemObj.id);
        textViewUpCenter.setText(idStr);

        return view;
    }
}
