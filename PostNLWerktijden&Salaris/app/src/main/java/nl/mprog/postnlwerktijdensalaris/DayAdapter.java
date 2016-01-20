package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DayAdapter extends ArrayAdapter {

    private ArrayList<DayObject> dayListItems;

    public DayAdapter(Context context, int textViewResourceId, ArrayList<DayObject> listItems) {
        super(context, textViewResourceId, listItems);
        this.dayListItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.listview_layout, parent, false);
        }

        DayObject listItemObj = dayListItems.get(position);

        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.day);

        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        textViewDownLeft.setText(listItemObj.districts);

        TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight1);
        textViewUpRight.setText(listItemObj.timeTotal);

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
