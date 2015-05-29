package pl.piotrstaniow.organizeme;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import pl.piotrstaniow.organizeme.Models.NotificationAggregator;
import pl.piotrstaniow.organizeme.Models.NotificationItem;

public class NotificationFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    GridView notifGrid;
    ArrayAdapter<CharSequence> notifAdapter;
    long task_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notifAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.notif_types, R.layout.notif_grid_item);

        notifGrid = (GridView) view.findViewById(R.id.notif_grid);
        notifGrid.setAdapter(notifAdapter);
        notifGrid.setOnItemClickListener(this);

        Bundle args = getArguments();
        if (args != null) {
            task_id = args.getLong("TASK_ID");
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String[] types = getActivity().getResources().getStringArray(R.array.notif_types);
        int[] delays = getActivity().getResources().getIntArray(R.array.notif_delay);
        NotificationCreator creator = new NotificationCreator(getActivity());
        creator.setType(types[position]);
        creator.setTaskID(task_id);
        creator.delegate(delays[position]);
        NotificationItem notif = creator.create();
        NotificationAggregator.getInstance().add(notif);
        dismiss();
    }
}
