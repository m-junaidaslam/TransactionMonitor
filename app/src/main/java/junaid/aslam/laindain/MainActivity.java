package junaid.aslam.laindain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity implements OnItemClickListener, AdapterView.OnItemLongClickListener {

    Button adder;
    Context context;
    ListView list;
    List<MyCostumRow> data;
    MyDatabaseHandler db_Person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        context = this;
        db_Person = new MyDatabaseHandler(context);
        data = db_Person.getAllPersons();
        adder = (Button) findViewById(R.id.adder);
        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPerson();
            }
        });
        MyListAdapter adapter = new MyListAdapter(context, R.layout.activity_my_list_adapter, data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add) {
            addPerson();
            return true;
        }

        if (id == R.id.about)
            aboutUs();

        if(id == R.id.exportdb)
            exportDB();
        return super.onOptionsItemSelected(item);
    }

    private void exportDB() {
        Toast.makeText(context, "Exporting!", Toast.LENGTH_SHORT).show();
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "/data/" + "junaid.aslam.laindain" + "/databases/" + "PersonDB";
        String backupDBPath = "Download/SampleDB.db";

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "DB Exported! because"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent myIntent = new Intent(context, EditPerson.class);
        myIntent.putExtra("name", data.get(position).getName());
        startActivity(myIntent);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
       // final AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
        final AlertDialog myAlert = new AlertDialog.Builder(context).create();
        myAlert.setTitle("Options");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final Button delete = new Button(context);
        delete.setText("Delete");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_Person.deletePerson(data.get(position).getName());
                mHandler.sendEmptyMessage(1);
                myAlert.dismiss();
            }
        });

        layout.addView(delete);
        final Button deleteAll = new Button(context);
        deleteAll.setText("Delete All");
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_Person.deleteAll();
                mHandler.sendEmptyMessage(1);
                myAlert.dismiss();
            }
        });
        layout.addView(deleteAll);
        myAlert.setView(layout);
        myAlert.show();
        return true;
    }

    public void aboutUs() {
        AlertDialog.Builder abt = new AlertDialog.Builder(context);
        abt.setTitle("About Us");
        abt.setMessage("Muhammad Junaid Aslam\nElectronics Engineer\nAir University Islamabad");
        abt.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        abt.create().show();
    }

    public void addPerson() {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
        myAlert.setTitle("Add Person");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(context);
        name.setHint("Name");
        name.requestFocus();
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});
        layout.addView(name);
        myAlert.setView(layout);
        myAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Person person = new Person();
                List<String> names = db_Person.getAllPersonsList();
                if (!names.contains(name.getText().toString())) {
                    person.setName(name.getText().toString());
                    person.setDate(getCurrentDate());
                    person.setAmount("0");
                    db_Person.addPerson(person);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                } else
                    Toast.makeText(context, "Name already present", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(1);
            }
        });
        myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myAlert.create().show();
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
        int hours = Integer.valueOf(sdf1.format(c.getTime()));
        String hour = new String();
        String x = new String();
        if (hours > 12) {
            x = "PM";
            hours = hours - 12;
            if (hours < 10)
                hour = "0" + String.valueOf(hours);
            else
                hour = String.valueOf(hours);
        } else if (hours == 0) {
            x = "AM";
            hour = "12";
        } else {
            x = "AM";
            if (hours < 10)
                hour = "0" + String.valueOf(hours);
            else
                hour = String.valueOf(hours);
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("mm");
        String total = sdf2.format(c.getTime()) + "\n" + hour + ":" + sdf3.format(c.getTime()) + " " + x;
        return total;
    }

    Handler mHandler= new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {

            data = db_Person.getAllPersons();
            MyListAdapter adapter = new MyListAdapter(context, R.layout.activity_my_list_adapter, data);
            list.setAdapter(adapter);

        };

    };
}