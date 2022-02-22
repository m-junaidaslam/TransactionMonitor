package junaid.aslam.laindain;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<MyCostumRow> {

	Activity context;
	int resource;
	List<MyCostumRow> data;

	public MyListAdapter(Context context, int resource, List<MyCostumRow> data) {
		super(context, resource, data);

		this.context = (Activity) context;
		this.resource = resource;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(resource, parent, false);

			holder= new Holder();
			holder.name = (TextView) row.findViewById(R.id.name);
			holder.amount = (TextView) row.findViewById(R.id.amount);
			holder.date = (TextView) row.findViewById(R.id.date);
			
			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		String str1 = data.get(position).getName();
		holder.name.setText(str1.toString());
		String str2= data.get(position).getAmount();
		holder.amount.setText(str2.toString());
		String str3 = data.get(position).getDate();
		holder.date.setText(str3.toString());

		return row;
	}

	static class Holder {
		TextView name;
		TextView amount;
		TextView date;
	}
}
