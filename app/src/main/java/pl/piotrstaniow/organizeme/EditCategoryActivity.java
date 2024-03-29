package pl.piotrstaniow.organizeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.larswerkman.holocolorpicker.ColorPicker;

import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;


public class EditCategoryActivity extends ActionBarActivity implements View.OnClickListener {
    FloatingActionButton saveBtn;
    EditText categoryName;
    ColorPicker categoryColor;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        Intent intent = getIntent();

        category = new Category();
        category.setId(intent.getLongExtra("id", 0));

        saveBtn = (FloatingActionButton) findViewById(R.id.create_new_task);
        categoryName = (EditText) findViewById(R.id.category_name);
        categoryName.setEnabled(false);
        categoryName.setText(intent.getStringExtra("name"));
        categoryColor = (ColorPicker) findViewById(R.id.category_color);
        categoryColor.setColor(Integer.parseInt(intent.getStringExtra("color")));

        saveBtn.setOnClickListener(this);
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
        if (view == saveBtn) {
            if(categoryName.getText().toString().matches("")) {
                Toast.makeText(this, "Enter description", Toast.LENGTH_SHORT).show();
                return;
            }
            save();
        }
    }

    private void save() {
        String name = String.valueOf(categoryName.getText());
        String color = String.valueOf(categoryColor.getColor());
        category.setName(name);
        category.setColor(color);
        CategoryAggregator.getInstance().update(category);
        finish();
    }
}
