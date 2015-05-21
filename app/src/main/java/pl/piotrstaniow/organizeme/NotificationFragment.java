package pl.piotrstaniow.organizeme;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class NotificationFragment extends DialogFragment {
    GridView notifGrid;
    ArrayAdapter<CharSequence> notifAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notifAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.notif_types, android.R.layout.simple_list_item_1);

        notifGrid = (GridView) view.findViewById(R.id.notif_grid);
        notifGrid.setAdapter(notifAdapter);
        return view;
    }


}
