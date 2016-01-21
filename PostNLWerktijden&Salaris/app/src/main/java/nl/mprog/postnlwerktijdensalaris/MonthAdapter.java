package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MonthAdapter extends ArrayAdapter {

    private ArrayList<MonthObject> monthListItems;

    public MonthAdapter(Context context, int textViewResourceId, ArrayList<MonthObject> listItems) {
        super(context, textViewResourceId, listItems);
        this.monthListItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.listview_layout, parent, false);
        }

        MonthObject listItemObj = monthListItems.get(position);

        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.month);

        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        String daysStr;
        if (listItemObj.days == 1) {
            daysStr = listItemObj.days + " dag";
        }
        else {
            daysStr = listItemObj.days + " dagen";
        }
        textViewDownLeft.setText(daysStr);

        TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight1);
        String salaryStr = "€" + listItemObj.salary;
        textViewUpRight.setText(salaryStr);

        TextView textViewDownRight = (TextView) view.findViewById(R.id.listItemDownRight);
        textViewDownRight.setText(listItemObj.time);

        TextView textViewUpCenter = (TextView) view.findViewById(R.id.listItemUpCenter);
        String idItem = Integer.toString(listItemObj.id);
        textViewUpCenter.setText(idItem);

        return view;
    }
}
