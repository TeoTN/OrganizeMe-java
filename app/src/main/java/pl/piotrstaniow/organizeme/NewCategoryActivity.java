package pl.piotrstaniow.organizeme;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.larswerkman.holocolorpicker.ColorPicker;

import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;


public class NewCategoryActivity extends ActionBarActivity implements View.OnClickListener {
    FloatingActionButton createBtn;
    EditText categoryName;
    ColorPicker categoryColor;
    Category createdCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        createdCategory = new Category();

        createBtn = (FloatingActionButton) findViewById(R.id.create_new_task);
        categoryName = (EditText) findViewById(R.id.category_name);
        categoryColor = (ColorPicker) findViewById(R.id.category_color);

        createBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == createBtn) {
            createNewCategory();
        }
    }

    private void createNewCategory() {
        String name = String.valueOf(categoryName.getText());
        String color = String.valueOf(categoryColor.getColor());
        createdCategory.setName(name);
        createdCategory.setColor(color);
        CategoryAggregator.getInstance().add(createdCategory);
        finish();
    }
}
