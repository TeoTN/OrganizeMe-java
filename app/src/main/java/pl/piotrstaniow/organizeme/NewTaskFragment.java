package pl.piotrstaniow.organizeme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * OrganizeMe
 * <p/>
 * Author: Piotr Staniów
 * Email: staniowp@gmail.com
 * Created: 12.05.15
 */
public class NewTaskFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener, View.OnClickListener, View.OnFocusChangeListener {
    private OnNewTaskCreatedListener mListener;
    Button createBtn, dismissBtn;
    EditText taskDescET, taskDateET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context ctx = getActivity();
        final View thisView = inflater.inflate(R.layout.new_task_fragment, container, false);

        thisView.setMinimumWidth(500);
        thisView.setMinimumHeight(500);

        createBtn = (Button) thisView.findViewById(R.id.create_new_task);
        dismissBtn = (Button) thisView.findViewById(R.id.dismiss_new_task);

        taskDateET = (EditText) thisView.findViewById(R.id.task_date);
        taskDescET = (EditText) thisView.findViewById(R.id.task_desc);

        taskDateET.setOnClickListener(this);
        taskDateET.setOnFocusChangeListener(this);
        createBtn.setOnClickListener(this);
        dismissBtn.setOnClickListener(this);

        // Inflate the layout for this fragment
        return thisView;
    }

    public void onAttach(Activity activity) {
        try {
            this.mListener = (OnNewTaskCreatedListener) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNewTaskCreatedListener");
        }
        super.onAttach(activity);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        taskDateET.setText(day+"."+month+"."+year);
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
            dismiss();
        }
    }

    private void pickDate() {
        final Context ctx = getActivity();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePicker = new DatePickerDialog(ctx, this, year, month, day);
        datePicker.show();
    }

    private void createNewTask() {
        String taskDesc = String.valueOf(taskDescET.getText());
        String taskDate = String.valueOf(taskDateET.getText());
        Task task = new Task(taskDesc);
        task.setTaskDate(taskDate);
        mListener.onNewTaskCreated(task);
        dismiss();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == taskDateET && b == true) {
            pickDate();
        }
    }

    public interface OnNewTaskCreatedListener {
        public abstract void onNewTaskCreated(Task newTask);
    }
}
