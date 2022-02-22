package junaid.aslam.laindain;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity implements OnItemClickListener{

	Context context;
	ListView list;
	List<MyCostumRow> data;
	int listCount;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);
        context = this;
        SharedPreferences myPref = getSharedPreferences("pref", MODE_PRIVATE);
        listCount = myPref.getInt("listcount", 0);
        data = new ArrayList<MyCostumRow>();
        for(int i=0; i<listCount; i++)
        	data.add(new MyCostumRow(myPref.getString("name"+(i+1), "Empty"),
        			myPref.getString("amount"+(i+1), "Empty"),
        			myPref.getString("date"+(i+1), "Empty")));
        MyListAdapter adapter = new MyListAdapter(context, R.layout.activity_my_list_adapter, data);
        list.setAdapter(adapter);
        
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		
	}
}
