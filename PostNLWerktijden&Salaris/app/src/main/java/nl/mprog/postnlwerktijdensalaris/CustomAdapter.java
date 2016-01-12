package nl.mprog.postnlwerktijdensalaris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    private ArrayList<Months.ListItemObj> monthListItems;
    private ArrayList<Days.ListItemObj> daysListItems;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<Months.ListItemObj> listItems, Class motherClass) {
        super(context, textViewResourceId, listItems);
        if (motherClass == Months.class) {
            this.monthListItems = listItems;
        }
        else if (motherClass == Days.class)

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.listview_layout_1, parent, false);
        }

        Months.ListItemObj listItemObj = monthListItems.get(position);

        TextView textViewUpLeft = (TextView) view.findViewById(R.id.listItemUpLeft);
        textViewUpLeft.setText(listItemObj.upLeft);

        TextView textViewDownLeft = (TextView) view.findViewById(R.id.listItemDownLeft);
        textViewDownLeft.setText(listItemObj.downLeft);

        TextView textViewUpRight = (TextView) view.findViewById(R.id.listItemUpRight);
        textViewUpRight.setText(listItemObj.upRight);

        TextView textViewDownRight = (TextView) view.findViewById(R.id.listItemDownRight);
        textViewDownRight.setText(listItemObj.downRight);

        return view;
    }
}
