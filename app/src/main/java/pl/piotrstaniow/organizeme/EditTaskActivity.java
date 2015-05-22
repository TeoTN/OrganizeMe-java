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


public class EditTaskActivity extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    FloatingActionButton createBtn;
    EditText taskDescET, taskDateET, taskTimeET;
    Spinner categorySpinner;
    Task editedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        CategoryAggregator ca = CategoryAggregator.getInstance();

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
        List<Category> allCategories = ca.getAll();
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        String str = bundle.getString("task");

        editedTask = Task.deserializeTask(str);

        taskDescET.setText(editedTask.getTaskDesc());

        String[] spl = editedTask.getDisplayDate().split(" ");
        taskDateET.setText(spl[0]+" "+ spl[1]+" "+spl[2]);
        if (editedTask.isTimeSet())
            taskTimeET.setText(spl[3]);


        int selectedCat = adapter.getPosition(editedTask.getCategory());
        categorySpinner.setSelection(selectedCat);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == createBtn) {
            editTask();
        }
        if (view == taskDateET) {
            pickDate();
        }
        if (view == taskTimeET){
            pickTime();
        }
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, month, day);
        datePicker.show();

    }

    private void pickTime() {
        TimePickerDialog timePicker = new TimePickerDialog(this,this,9,0,true);
        timePicker.show();
    }

    private void editTask() {
        String taskDesc = String.valueOf(taskDescET.getText());
        editedTask.setTaskDesc(taskDesc);
        TaskAggregator.getInstance().edit(editedTask);
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
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        Date date = editedTask.getDate();
        date = DateTimeUtils.setTimeInDate(date,hour,min);
        editedTask.setDate(date, true);
        String displayDate = editedTask.getDisplayDate();
        String[] splitted = displayDate.split(" ");
        taskTimeET.setText(splitted[3]);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Date date = editedTask.getDate();
        date = DateTimeUtils.setDateInDate(date, year, month, day, editedTask.isTimeSet());
        editedTask.setDate(date, false);
        String displayDate = editedTask.getDisplayDate();
        String[] splitted = displayDate.split(" ");
        taskDateET.setText(day+" "+splitted[1]+" "+year);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Category category = (Category) adapterView.getAdapter().getItem(i);
        editedTask.setCategory(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
