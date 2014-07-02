package com.marshall.cafeproject.adapters;

import java.util.ArrayList;
import java.util.List;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.Table;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TablesArrayAdapter extends ArrayAdapter<Table> {

	private LayoutInflater inflater;
	private TextView tvNumber;
	private TextView tvSeats;
	private CheckBox checkBox;
	private List<Integer> checkedTables;

	public TablesArrayAdapter(Context context, ArrayList<Table> tables) {
		super(context, R.layout.tables_item, tables);

		checkedTables = new ArrayList<Integer>();
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Table table = (Table) this.getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tables_item, null);

			getControls(convertView);

			tvNumber.setText(String.valueOf(table.getTableNumber()));
			tvSeats.setText(String.valueOf(table.getSeats()));

			if (isOnlineUser()) {
				checkBox.setId(position + 1);
				
				if (table.isReservedForOtherUser())
					checkBox.setEnabled(false);
				else {
					checkBox.setEnabled(true);
					
					if (table.isYourReservation()) { 
						checkBox.setChecked(true);
						fillCheckedTable(checkBox);
					} else 
						checkBox.setChecked(false);
				}
				
				checkBox.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						CheckBox checkBox = (CheckBox) v;
						fillCheckedTable(checkBox);
					}
				});
			} else {
				checkBox.setEnabled(false);
			}
		}

		return convertView;
	}

	private void getControls(View view) {
		tvNumber = (TextView) view.findViewById(R.id.tvNumber);
		tvSeats = (TextView) view.findViewById(R.id.tvSeats);
		checkBox = (CheckBox) view.findViewById(R.id.checkBox);
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	public List<Integer> getSelectedTables() {
		return checkedTables;
	}

	private boolean isOnlineUser() {
		SharedPreferences preferences = getContext().getSharedPreferences(
				"Settings", Context.MODE_PRIVATE);

		if (!preferences.getString("login", "").equals(""))
			return true;
		else
			return false;
	}
	
	private void fillCheckedTable(CheckBox checkBox) {
		if (checkBox.isChecked()) {
			checkedTables.add(checkBox.getId());
		} else {
			checkedTables.remove(checkedTables.indexOf(checkBox
					.getId()));
		}
	}
}
