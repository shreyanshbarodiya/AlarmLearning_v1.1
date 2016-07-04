package shreyansh.com.alarmlearning;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog fromDatePickerDialog;
    private EditText etDate;
    private EditText etHours;
    private EditText etMinutes;
    private EditText etInterval;
    private Button btnSet;
    private Button btnStop;
    private Button btnStopAll;
    private SimpleDateFormat dateFormatter;
    public ArrayList<PendingIntent> intentArray = new ArrayList<>();
    public int Year;
    public int Month;
    public int Date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        etDate = (EditText) findViewById(R.id.etxt_fromdate);
        etHours = (EditText) findViewById(R.id.etHours);
        etMinutes = (EditText) findViewById(R.id.etMinutes);
        etInterval = (EditText) findViewById(R.id.etInterval);
        btnSet = (Button) findViewById(R.id.btnSet);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStopAll = (Button) findViewById(R.id.btnStopAll);

        etDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
                Year = year;
                Month = monthOfYear;
                Date = dayOfMonth;
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        btnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlert();
                Toast.makeText(MainActivity.this,"Alarms have been set", Toast.LENGTH_LONG).show();
            }
        });

        btnStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
            }
        });

        btnStopAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAllAlarms();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startAlert() {

        int interval = Integer.parseInt(etInterval.getText().toString());
        int hours = Integer.parseInt(etHours.getText().toString());
        int minutes = Integer.parseInt(etMinutes.getText().toString());

        Intent intent = new Intent(this, MyBroadcastReceiver.class);

        Calendar cal=Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        cal.set(Calendar.MONTH,Month);
        cal.set(Calendar.YEAR,Year);
        cal.set(Calendar.DAY_OF_MONTH,Date);
        cal.set(Calendar.HOUR_OF_DAY,hours);
        cal.set(Calendar.MINUTE,minutes);
        cal.set(Calendar.SECOND,0);

        if(cal.getTimeInMillis()>(System.currentTimeMillis())){
            Log.i("myTag","cal.getTimeInMillis()>(System.currentTimeMillis())");
        }else{
            Log.i("myTag","ulta");
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        long milli = cal.getTimeInMillis();
        for(int i = 0; i < 6; i++)
        {
            if(MyBroadcastReceiver.mp!=null){
                if(MyBroadcastReceiver.mp.isPlaying()){
                    MyBroadcastReceiver.mp.stop();
                }
            }

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i*44, intent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        (milli + (i*interval*60000)),
                        pendingIntent);
            }
            Log.i("myTag","alarms set");

            intentArray.add(pendingIntent);
        }
    }

    public void stopAlarm(){
        MyBroadcastReceiver.mp.stop();

    }

    public void stopAllAlarms(){
        PendingIntent pendingIntent;
        if(MyBroadcastReceiver.mp != null){
            if(MyBroadcastReceiver.mp.isPlaying()){
                MyBroadcastReceiver.mp.stop();
            }
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        for(int i =0; i < intentArray.size(); i++){
            pendingIntent = intentArray.get(i);
            alarmManager.cancel(pendingIntent);
        }
    }
}