package pl.piotrstaniow.organizeme;

import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskUtils;


public class NewTaskActivity extends ActionBarActivity
        implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    Button createBtn, dismissBtn;
    EditText taskDescET, taskDateET;
    Task createdTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        createdTask = new Task();

        createBtn = (Button) findViewById(R.id.create_new_task);
        dismissBtn = (Button) findViewById(R.id.dismiss_new_task);

        taskDateET = (EditText) findViewById(R.id.task_date);
        taskDescET = (EditText) findViewById(R.id.task_desc);

        taskDateET.setOnClickListener(this);
        taskDateET.setOnFocusChangeListener(this);
        createBtn.setOnClickListener(this);
        dismissBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
        if (view == dismissBtn) {
            finish();
           // dismiss();
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

    private void createNewTask() {
        String taskDesc = String.valueOf(taskDescET.getText());
        String taskDate = String.valueOf(taskDateET.getText());
        createdTask.setTaskDesc(taskDesc);
        TaskAggregator.getInstance().addTask(createdTask);
        finish();
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == taskDateET && b) {
            pickDate();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = TaskUtils.cutTime(calendar.getTime());
        createdTask.setDate(date);
        taskDateET.setText(day+"."+(month+1)+"."+year);
    }
}
