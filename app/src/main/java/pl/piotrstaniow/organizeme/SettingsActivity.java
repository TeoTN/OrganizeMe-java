package pl.piotrstaniow.organizeme;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateTimeUtils;


public class SettingsActivity extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener{
    public static final String DAILY_NOTIFICATIONS_SET = "DailyNotificationsSet";
    public static final String DAILY_NOTIFICATIONS_HOUR = "DailyNotificationsHour";
    public static final String DAILY_NOTIFICATIONS_MINUTE = "DailyNotificationsMinute";
    FloatingActionButton saveBtn;
    EditText dailyNotifTimeET;
    int dailyNotificationsHour;
    int dailyNotificationsMinute;
    boolean dailyNotificationsSet;
    private CheckBox notificationsSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dailyNotificationsHour = preferences.getInt(DAILY_NOTIFICATIONS_HOUR, 9);
        dailyNotificationsMinute = preferences.getInt(DAILY_NOTIFICATIONS_MINUTE, 0);
        dailyNotificationsSet = preferences.getBoolean(DAILY_NOTIFICATIONS_SET, false);

        dailyNotifTimeET = (EditText) findViewById(R.id.task_time);
        dailyNotifTimeET.setOnClickListener(this);
        dailyNotifTimeET.setOnFocusChangeListener(this);
        onTimeSet(null, dailyNotificationsHour, dailyNotificationsMinute);

        saveBtn = (FloatingActionButton) findViewById(R.id.create_new_task);
        saveBtn.setOnClickListener(this);

        notificationsSet = (CheckBox) findViewById(R.id.daily_notifications_set);
        notificationsSet.setChecked(dailyNotificationsSet);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_action, menu);
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
        if (view == saveBtn) {
            save();
        }
        else if (view == dailyNotifTimeET){
            pickTime();
        }
    }

    private void pickTime() {
        TimePickerDialog timePicker = new TimePickerDialog(this, this,
                dailyNotificationsHour, dailyNotificationsMinute, true);
        timePicker.show();
    }

    private void save() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DAILY_NOTIFICATIONS_SET, dailyNotificationsSet);
        editor.putInt(DAILY_NOTIFICATIONS_HOUR, dailyNotificationsHour);
        editor.putInt(DAILY_NOTIFICATIONS_MINUTE, dailyNotificationsMinute);
        editor.commit();
        finish();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == dailyNotifTimeET && b) {
            pickTime();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        dailyNotificationsHour = hour;
        dailyNotificationsMinute = min;
        String[] splitted = DateTimeUtils.dateToDisplay(
                DateTimeUtils.setTimeInDate(new Date(), hour, min), true).split(" ");
        dailyNotifTimeET.setText(splitted[3]);
    }

    public void onCheckboxClicked(View view) {
        dailyNotificationsSet = ((CheckBox) view).isChecked();
    }
}
