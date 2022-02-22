package junaid.aslam.laindain;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Switch;
import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditPerson extends Activity implements OnClickListener {

    Button[] btn = new Button[10];
    Button ok, cancel, back, dot;
    TextView name, amount, date;
    Switch status;
    Context context;
    Person person;
    MyDatabaseHandler db_Person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);
        context = this;
        Intent myIntent = getIntent();
        db_Person = new MyDatabaseHandler(this);
        name = (TextView) findViewById(R.id.name);
        amount = (TextView) findViewById(R.id.amount);
        status = (Switch) findViewById(R.id.switch1);
        ok = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        dot = (Button) findViewById(R.id.bdot);
        back = (Button) findViewById(R.id.back);
        btn[0] = (Button) findViewById(R.id.b0);
        btn[1] = (Button) findViewById(R.id.b1);
        btn[2] = (Button) findViewById(R.id.b2);
        btn[3] = (Button) findViewById(R.id.b3);
        btn[4] = (Button) findViewById(R.id.b4);
        btn[5] = (Button) findViewById(R.id.b5);
        btn[6] = (Button) findViewById(R.id.b6);
        btn[7] = (Button) findViewById(R.id.b7);
        btn[8] = (Button) findViewById(R.id.b8);
        btn[9] = (Button) findViewById(R.id.b9);
        date = (TextView) findViewById(R.id.date);
        name.setText(myIntent.getStringExtra("name"));
        person = db_Person.getPerson(myIntent.getStringExtra("name"));
        amount.setText(String.valueOf(person.getAmount()));
        date.setText(person.getDate().toString());
        if(person.getStatus().equals("Take")) {
            status.setChecked(true);
            name.setBackgroundColor(Color.parseColor("#7e03ff91"));
        }
        else if(person.getStatus().toString().equals("Give")) {
            status.setChecked(false);
            name.setBackgroundColor(Color.parseColor("#7eff0091"));
        }
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    name.setBackgroundColor(Color.parseColor("#7e03ff91"));
                else
                    name.setBackgroundColor(Color.parseColor("#7eff0091"));
            }
        });
        ok.setOnClickListener((OnClickListener) context);
        cancel.setOnClickListener((OnClickListener) context);
        dot.setOnClickListener((OnClickListener) context);
        back.setOnClickListener((OnClickListener) context);
        for(int i=0; i<10; i++)
            btn[i].setOnClickListener((OnClickListener) context);

    }

    @Override
    public void onClick(View v) {
        for(int i=0; i<10; i++)
            if(v.getId()==btn[i].getId())
                updateAmount(String.valueOf(i), 'a');
        if(v.getId()==dot.getId())
            updateAmount(".", 'd');
        if(v.getId()==back.getId()) {
            updateAmount("", 'r');
        }
        if(v.getId()==ok.getId()) {
            if (status.isChecked())
                person.setStatus("Take");
            else
                person.setStatus("Give");
            if(person.getAmount().endsWith(".")) {
                String x = person.getAmount()+"0";
                person.setAmount(x);
            }
            person.setDate(getCurrentDate());
            db_Person.updatePerson(person);
            Intent myIntent = new Intent(context, MainActivity.class);
            startActivity(myIntent);
            finish();
        }
        if(v.getId()==cancel.getId()) {
            onBackPressed();
        }

    }

    public void updateAmount(String plus, char func) {
        String temp = amount.getText().toString();
        switch(func) {
            case 'a':
                if(Double.valueOf(temp+plus)>1000000000 || temp.length()>=10) {
                    Toast.makeText(context, "Limit reached", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(Double.valueOf(temp)<=0 && Double.valueOf(plus)==0) {
                    Toast.makeText(context, "Invalid Selection", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(Double.valueOf(temp)==0)
                    temp = plus;
                else
                    temp = temp+plus;
                amount.setText(temp);
                person.setAmount(temp);
                break;
            case 'd':
                if(temp.contains(".")) {
                    Toast.makeText(context, "Point already present", Toast.LENGTH_SHORT).show();
                    break;
                }
                temp = temp + plus;
                amount.setText(temp);
                person.setAmount(temp);
                break;
            case 'r':
                amount.setText("0");
                person.setAmount("0");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(context, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
        int hours = Integer.valueOf(sdf1.format(c.getTime()));
        String hour = new String();
        String x = new String();
        if(hours>12) {
            x = "PM";
            hours = hours-12;
            if(hours<10)
                hour = "0"+String.valueOf(hours);
            else
                hour = String.valueOf(hours);
        } else if(hours == 0) {
            x = "AM";
            hour = "12";
        } else {
            x = "AM";
            if(hours<10)
                hour = "0"+String.valueOf(hours);
            else
                hour = String.valueOf(hours);
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("mm");
        String total = sdf2.format(c.getTime())+"\n"+hour+":"+sdf3.format(c.getTime())+" "+x;
        return total;
    }
}
