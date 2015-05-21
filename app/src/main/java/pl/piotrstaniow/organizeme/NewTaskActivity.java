package pl.piotrstaniow.organizeme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.getbase.floatingactionbutton.FloatingActionButton;

import pl.piotrstaniow.organizeme.CategoryPredicates.CategoryAllPredicate;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskUtils;

import java.sql.Time;
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

        taskDateET = (EditText) findViewById(R.id.task_date);
        taskDescET = (EditText) findViewById(R.id.task_desc);
        taskTimeET = (EditText) findViewById(R.id.task_time);

        categorySpinner = (Spinner) findViewById(R.id.category);

        taskDateET.setOnClickListener(this);
        taskDateET.setOnFocusChangeListener(this);

        taskTimeET.setOnClickListener(this);
        taskTimeET.setOnFocusChangeListener(this);

        createBtn.setOnClickListener(this);
        List<Category> allCategories = ca.filter(new CategoryAllPredicate());
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_spinner_item,allCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
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
        Calendar calendar = Calendar.getInstance();
        Date date = createdTask.getDate();
        if(!createdTask.isTimeSet())
            date = TaskUtils.cutTime(date);
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        date = calendar.getTime();
        createdTask.setDate(date, false);
        taskDateET.setText(day+"."+(month+1)+"."+year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        Date date = createdTask.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        createdTask.setDate(date, true);
        String str = hour+":";
        if(min < 10)
            str += "0" + min;
        else
            str += "" + min;
        taskTimeET.setText(str);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Category category = (Category) adapterView.getAdapter().getItem(i);
        createdTask.setCategory(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
