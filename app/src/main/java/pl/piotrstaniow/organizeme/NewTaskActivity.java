package pl.piotrstaniow.organizeme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.*;
import com.getbase.floatingactionbutton.FloatingActionButton;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NewTaskActivity extends ActionBarActivity
        implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, Spinner.OnItemSelectedListener{

    FloatingActionButton createBtn;
    EditText taskDescET, taskDateET, taskTimeET;
    Spinner categorySpinner;
    Task createdTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CategoryAggregator ca = CategoryAggregator.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        createdTask = new Task();

        createBtn = (FloatingActionButton) findViewById(R.id.create_new_task);
        createBtn.setOnClickListener(this);

        taskDateET = (EditText) findViewById(R.id.task_date);
        taskDescET = (EditText) findViewById(R.id.task_desc);
        taskTimeET = (EditText) findViewById(R.id.task_time);

        categorySpinner = (Spinner) findViewById(R.id.category);

        taskDateET.setOnClickListener(this);
        taskDateET.setOnFocusChangeListener(this);

        taskTimeET.setOnClickListener(this);
        taskTimeET.setOnFocusChangeListener(this);

        List<Category> allCategories = ca.getAll();
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == createBtn) {
            createNewTask();
        }
        if (view == taskDateET) {
            pickDate();
        }
        if (view == taskTimeET){
            pickTime();
        }

    }

    private void pickTime() {
        TimePickerDialog timePicker = new TimePickerDialog(this,this,9,0,true);
        timePicker.show();
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, month, day);
        datePicker.show();
    }

    private void createNewTask() {
        String taskDesc = String.valueOf(taskDescET.getText());
        createdTask.setTaskDesc(taskDesc);
        TaskAggregator.getInstance().add(createdTask);
        finish();
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == taskDateET && b) {
            pickDate();
        }
        if (view == taskTimeET && b) {
            pickTime();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Date date = createdTask.getDate();
        date = DateTimeUtils.setDateInDate(date, year, month, day, createdTask.isTimeSet());
        createdTask.setDate(date, false);
        String displayDate = createdTask.getDisplayDate();
        String[] splitted = displayDate.split(" ");
        taskDateET.setText(day+" "+splitted[1]+" "+year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        Date date = createdTask.getDate();
        date = DateTimeUtils.setTimeInDate(date,hour,min);
        createdTask.setDate(date, true);
        String displayDate = createdTask.getDisplayDate();
        String[] splitted = displayDate.split(" ");
        taskTimeET.setText(splitted[3]);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Category category = (Category) adapterView.getAdapter().getItem(i);
        createdTask.setCategory(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String unassigned_name = getResources().getString(R.string.unassigned_category_name);
        int unassigned_color = getResources().getColor(R.color.unassigned_category_color);
        Category cat = new Category();
        cat.setName(unassigned_name);
        cat.setColor(String.valueOf(unassigned_color));
        createdTask.setCategory(cat);
    }
}
