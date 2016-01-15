package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WalkAdapter extends ArrayAdapter {

    private ArrayList<WalkObject> walkListItems;

    public WalkAdapter(Context context, int textViewResourceId, ArrayList<WalkObject> listItems) {
        super(context, textViewResourceId, listItems);
        this.walkListItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.listview_layout, parent, false);
        }

        WalkObject listItemObj = walkListItems.get(position);

        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.districtCode);

        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        String timeGoalExtraTotal = listItemObj.timeGoal + " + " + listItemObj.timeExtra + " = " + listItemObj.timeTotal;
        textViewDownLeft.setText(timeGoalExtraTotal);

        TextView textViewUpRight1 = (TextView) view.findViewById(R.id.listItemUpRight1);
        String timeBeginEnd1 = listItemObj.timeBegin1 + " - " + listItemObj.timeEnd1;
        textViewUpRight1.setText(timeBeginEnd1);

        TextView textViewUpRight2 = (TextView) view.findViewById(R.id.listItemUpRight2);
        String timeBeginEnd2 = listItemObj.timeBegin2 + " - " + listItemObj.timeEnd2;
        textViewUpRight2.setText(timeBeginEnd2);

        TextView textViewUpRight3 = (TextView) view.findViewById(R.id.listItemUpRight3);
        String timeBeginEnd3 = listItemObj.timeBegin3 + " - " + listItemObj.timeEnd3;
        textViewUpRight3.setText(timeBeginEnd3);

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idItem = Integer.toString(listItemObj.id3);
        textViewUpCenter.setText(idItem);

        return view;
    }
}
