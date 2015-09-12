package pl.piotrstaniow.organizeme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;
import pl.piotrstaniow.organizeme.Models.*;
import pl.piotrstaniow.organizeme.NavigationDrawer.NavigationDrawerBuilder;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NewTaskActivity extends ActionBarActivity
        implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, Spinner.OnItemSelectedListener, TokenCompleteTextView.TokenListener, NumberPicker.OnValueChangeListener, CompoundButton.OnCheckedChangeListener {

    public static final int GET_LOCATION_REQUEST_CODE = 1;
    LabelsCompletionView completionView;
    FloatingActionButton createBtn;
    EditText taskDescET, taskDateET, taskTimeET, taskLocationET;
    Spinner categorySpinner;
    Task createdTask;
    boolean isEdit = false;
    ArrayAdapter<Category> adapter;
    ArrayAdapter<Label> labelAdapter;
    List<Label> task_labels;
    LabelAggregator la;
    //private Button addNotifBtn;
    private NumberPicker taskLocationPrecisionNP;
    private CheckBox taskLocationNotifyChB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        CategoryAggregator ca = CategoryAggregator.getInstance();
        la = LabelAggregator.getInstance();
        task_labels = new ArrayList<>();
        List<Category> allCategories = ca.getAll();
        List<Label> labels = la.getAll();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allCategories);

        createdTask = new Task();

        setVariables();
        manageEdit();
        setLabelAdapter(labels);

        completionView.setAdapter(labelAdapter);
        completionView.setTokenListener(this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        completionView.performBestGuess(false);
        completionView.allowDuplicates(false);
    }

    private void setLabelAdapter( List<Label> labels){
        labelAdapter = new FilteredArrayAdapter<Label>(this, R.layout.suggestion_layout, labels) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.suggestion_layout, parent, false);
                }

                Label p = getItem(position);
                ((TextView)convertView.findViewById(R.id.name)).setText(p.getName());

                return convertView;
            }
            @Override
            protected boolean keepObject(Label label, String mask) {
                mask = mask.toLowerCase();
                return label.getName().toLowerCase().startsWith(mask);
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (view == createBtn && !isEdit) {
            if(isNameEmpty()){
                return;
            }
            createNewTask();
        }
        if (view == createBtn && isEdit) {
            if(isNameEmpty()){
                return;
            }
            editTask();
        }

        /*
        if (view == addNotifBtn) {
            if (taskTimeET.getText().length() == 0 ||
                    taskDateET.getText().length() == 0)
            {
                Toast.makeText(this, "First select time and date", Toast.LENGTH_SHORT).show();
            }
            else {
                pickNotif(createdTask.getID());
            }
        }
        */
        if (view == taskDateET) {
            pickDate();
        }
        if (view == taskTimeET){
            pickTime();
        }
        if (view == taskLocationET) {
            setLocation();
        }
    }

    private boolean isNameEmpty() {
        if(taskDescET.getText().toString().matches("")) {
            Toast.makeText(this,"Enter description", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void manageEdit(){
        Intent i = getIntent();
        if(!i.hasExtra("task"))
            return;

        isEdit = true;
        Bundle bundle = getIntent().getExtras();
        createdTask = bundle.getParcelable("task");

        taskDescET.setText(createdTask.getTaskDesc());
        String[] spl = createdTask.getDisplayDate().split(" ");
        taskDateET.setText(spl[0]+" "+ spl[1]+" "+spl[2]);
        if (createdTask.isTimeSet())
            taskTimeET.setText(spl[3]);

        List<Label> temp_labels = createdTask.getLabels();
        if(!temp_labels.isEmpty()){
            for(Label l: temp_labels){
                completionView.addObject(l);
            }
        }

        taskLocationNotifyChB.setChecked(createdTask.isLocationNotify());
        taskLocationPrecisionNP.setValue(createdTask.getLocationPrecision());
        if (createdTask.getLocation() != null) {
            taskLocationET.setText(createdTask.getLocation().latitude + ", "
                    + createdTask.getLocation().longitude);
        }

        int selectedCat = adapter.getPosition(createdTask.getCategory());
        categorySpinner.setSelection(selectedCat);
        adapter.notifyDataSetChanged();
    }

    private void manageCategorySpinner(){
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(this);
    }

    private void setVariables(){
        createBtn = (FloatingActionButton) findViewById(R.id.create_new_task);
        createBtn.setOnClickListener(this);
        //addNotifBtn = (Button) findViewById(R.id.add_notif_btn);
        //addNotifBtn.setOnClickListener(this);

        taskDateET = (EditText) findViewById(R.id.task_date);
        taskDescET = (EditText) findViewById(R.id.task_desc);
        taskTimeET = (EditText) findViewById(R.id.task_time);
        taskLocationET = (EditText) findViewById(R.id.task_location);

        categorySpinner = (Spinner) findViewById(R.id.category);

        taskDateET.setOnClickListener(this);
        taskDateET.setOnFocusChangeListener(this);

        taskTimeET.setOnClickListener(this);
        taskTimeET.setOnFocusChangeListener(this);

        taskLocationET.setOnClickListener(this);
        taskLocationET.setOnFocusChangeListener(this);

        taskLocationPrecisionNP = (NumberPicker)findViewById(R.id.task_location_precision);
        taskLocationPrecisionNP.setMinValue(50);
        taskLocationPrecisionNP.setMaxValue(2000);
        taskLocationPrecisionNP.setWrapSelectorWheel(true);
        taskLocationPrecisionNP.setOnValueChangedListener(this);

        //Trick to make work NumberPicker inside ScrollView on my Android
        taskLocationPrecisionNP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }

                v.onTouchEvent(event);
                return true;
            }
        });

        taskLocationNotifyChB = (CheckBox)findViewById(R.id.task_location_notify);
        taskLocationNotifyChB.setOnCheckedChangeListener(this);

        completionView = (LabelsCompletionView)findViewById(R.id.searchView);
        manageCategorySpinner();
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

    private void editTask() {
        String taskDesc = String.valueOf(taskDescET.getText());
        createdTask.setTaskDesc(taskDesc);
        createdTask.setLabels(getLabelsFromView());
        TaskAggregator.getInstance().edit(createdTask);
        finish();
    }

    private void createNewTask() {

        String taskDesc = String.valueOf(taskDescET.getText());
        createdTask.setTaskDesc(taskDesc);
        createdTask.setLabels(getLabelsFromView());
        TaskAggregator.getInstance().add(createdTask);
        NavigationDrawerBuilder.getInstance(this).notifyValuesChanged();
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
        date = DateTimeUtils.setTimeInDate(date, hour, min);
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

    private List<Label> getLabelsFromView(){
        List<Label> labels = new ArrayList<>();
        LabelAggregator la = LabelAggregator.getInstance();
        Label l;
        for (Object token: completionView.getObjects()) {
            l = (Label) token;
            if(!labels.contains(l)){
                labels.add(l);
                labels.add(l);
                la.add(l);
            }
        }
        la.addLabelsToDB();
        return labels;
    }

    @Override
    public void onTokenAdded(Object token) {

    }

    @Override
    public void onTokenRemoved(Object o) {

    }

    private void setLocation() {
        Intent getLocationIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(getLocationIntent, GET_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                createdTask.setLocation((LatLng) bundle.getParcelable("location"));
                taskLocationET.setText(createdTask.getLocation().latitude + ", " + createdTask.getLocation().longitude);
            }
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if(picker == taskLocationPrecisionNP) {
            createdTask.setLocationPrecision(newVal);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == taskLocationNotifyChB) {
            createdTask.setLocationNotify(isChecked);
        }
    }
    /*
    private void pickNotif(long task_id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("picknotif");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        Bundle args = new Bundle();
        args.putLong("TASK_ID", task_id);

        DialogFragment newFragment = new NotificationFragment();
        newFragment.setArguments(args);
        newFragment.show(ft, "picknotif");
    }
    */
}
