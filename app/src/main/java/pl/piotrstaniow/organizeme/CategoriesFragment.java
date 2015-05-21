package pl.piotrstaniow.organizeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.CategoryListAdapter;


public class CategoriesFragment extends Fragment
        implements View.OnClickListener {
    private CategoryListAdapter categoryListAdapater;
    private FloatingActionButton newCategoryBtn;
    private FloatingActionsMenu floatingMenu;
    private ListView categoryListView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        LocalDbHelper.createInstance(getActivity());

        floatingMenu = (FloatingActionsMenu) view.findViewById(R.id.floating_menu);

        categoryListView = (ListView) view.findViewById(R.id.categoryList);
        categoryListAdapater = new CategoryListAdapter(getActivity(), CategoryAggregator.getInstance());

        newCategoryBtn = (FloatingActionButton) view.findViewById(R.id.new_category_btn);
        newCategoryBtn.setOnClickListener(this);

        categoryListView.setAdapter(categoryListAdapater);
        return view;
    }

	@Override
    public void onResume() {
        super.onResume();
        categoryListAdapater.notifyDataSetChanged();
        categoryListView.deferNotifyDataSetChanged();
    }


	private void createNewCategoryActivity(){
        Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
        startActivity(intent);
    }

	@Override
	public void onClick(View view) {
		if (view == newCategoryBtn) {
			createNewCategoryActivity();
			floatingMenu.collapse();
		}
	}
}
