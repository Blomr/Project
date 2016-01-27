package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DistrictAdapter extends ArrayAdapter {

    private ArrayList<DistrictObject> districtListItems;

    public DistrictAdapter(Context context, int textViewResourceId, ArrayList<DistrictObject> listItems) {
        super(context, textViewResourceId, listItems);
        this.districtListItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.listview_layout, parent, false);
        }

        DistrictObject listItemObj = districtListItems.get(position);

        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.districtCode);

        TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight1);
        String timeGoalBusy = "Piekdag: " + listItemObj.timeGoalBusy;
        textViewUpRight.setText(timeGoalBusy);

        TextView textViewDownRight = (TextView) view.findViewById(R.id.listItemDownRight);
        String timeGoalCalm = "Daldag: " + listItemObj.timeGoalCalm;
        textViewDownRight.setText(timeGoalCalm);

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idStr = Integer.toString(listItemObj.id);
        textViewUpCenter.setText(idStr);

        return view;
    }
}
