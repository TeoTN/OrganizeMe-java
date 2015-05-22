package pl.piotrstaniow.organizeme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.CategoryListAdapter;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskListAdapter;


public class CategoriesFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemLongClickListener {
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
        categoryListView.setOnItemLongClickListener(this);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        final CategoryListAdapter categoryListAdapter = (CategoryListAdapter) adapterView.getAdapter();
        final Category category = categoryListAdapter.getItem(position);
        final CategoryAggregator ca = CategoryAggregator.getInstance();

        new BottomSheet.Builder(getActivity(), R.style.BottomSheet_Dialog)
                .title("Actions")
                .grid() // <-- important part
                .sheet(R.menu.menu_category_action)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == R.id.category_edit) {
                            Intent i = new Intent(getActivity(), EditCategoryActivity.class);
                            i.putExtra("id", category.getId());
                            i.putExtra("name", category.getName());
                            i.putExtra("color", category.getColor());
                            startActivity(i);
                        } else if (which == R.id.category_delete) {
                            ca.remove(category);
                            categoryListAdapter.notifyDataSetChanged();
                        }
                    }
                }).show();
        return true;
    }
}
