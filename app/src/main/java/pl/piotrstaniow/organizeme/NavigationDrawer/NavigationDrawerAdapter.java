package pl.piotrstaniow.organizeme.NavigationDrawer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pl.piotrstaniow.organizeme.R;

import java.util.ArrayList;

/**
 * Created by piotr on 11.09.15.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    private final Activity context;
    private final ArrayList<NavigationDrawerItem> items;

    public NavigationDrawerAdapter(Activity context, ArrayList items) {
        this.context = context;
        this.items = items;
    }

    static class ItemViewHolder {
        public ImageView icon;
        public TextView text;
        public TextView value;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NavigationDrawerItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.navigation_drawer_item, null);
            ItemViewHolder viewHolder = new ItemViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.drawer_item_text);
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.drawer_item_icon);
            viewHolder.value = (TextView) rowView.findViewById(R.id.drawer_item_value);
            rowView.setTag(viewHolder);
        }

        ItemViewHolder holder = (ItemViewHolder) rowView.getTag();
        NavigationDrawerItem item = items.get(position);
        holder.text.setText(item.text);
        holder.icon.setImageResource(item.icon);
        if (item.hasValue()) {
            holder.value.setText(String.valueOf(item.value));
            holder.value.setVisibility(View.VISIBLE);
        }
        return rowView;
    }
}
