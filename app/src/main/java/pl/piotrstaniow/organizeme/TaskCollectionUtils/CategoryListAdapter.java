package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pl.piotrstaniow.organizeme.ItemAggregator;
import pl.piotrstaniow.organizeme.ItemListAdapter;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;
import pl.piotrstaniow.organizeme.R;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created: 12.05.15
 */
public class CategoryListAdapter extends BaseAdapter implements ItemListAdapter<Category> {
    private Context ctx;
    private ItemAggregator<Category> aggregator;

    public CategoryListAdapter(Context ctx, CategoryAggregator categoryAggregator) {
        this.ctx = ctx;
        aggregator = categoryAggregator;
    }

    @Override
    public int getCount() {
        return aggregator.getSize();
    }

    @Override
    public Category getItem(int id) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View returnView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            returnView =  inflater.inflate(R.layout.category_layout, viewGroup, false);
        } else {
            returnView = view;
        }

        TextView text1 = (TextView) returnView.findViewById(R.id.text1);
        TextView text2 = (TextView) returnView.findViewById(R.id.text2);

        Category task = getItem(i);
        TextView separator = (TextView) returnView.findViewById(R.id.separator);
        separator.setVisibility(View.GONE);

        text1.setText(task.getName());
        text2.setText(task.getColor());

        return returnView;
    }

    @Override
    public void remove(Category item) {
        aggregator.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void add(Category item) {
        aggregator.add(item);
        notifyDataSetChanged();
    }
}
