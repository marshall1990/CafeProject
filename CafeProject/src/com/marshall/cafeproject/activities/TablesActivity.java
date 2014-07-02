package com.marshall.cafeproject.activities;

import java.util.ArrayList;
import java.util.List;

import com.marshall.cafeproject.CheckConnection;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.ReservationType;
import com.marshall.cafeproject.SendDataToServer;
import com.marshall.cafeproject.Table;
import com.marshall.cafeproject.adapters.TablesArrayAdapter;
import com.marshall.cafeproject.database.DatabaseHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TablesActivity extends Activity {

	private ProgressDialog pDialog;
	private int id;
	private String name;
	private TextView tvTablesName, tvBookInfo;
	private ArrayList<Table> tablesList;
	private ListView listView;
	private TablesArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tables_layout);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getControls();

		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("CafeId");
		name = bundle.getString("Name");
		tvTablesName.setText(name + " - Stoliki");

		if (isOnlineUser()) {
			tvBookInfo.setText(getResources().getString(
					R.string.book_table_info_online));
		} else {
			tvBookInfo.setText(getResources().getString(
					R.string.book_table_info_offline));
		}

		new LoadTables().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isOnlineUser())
			getMenuInflater().inflate(R.menu.save_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.save:
			save();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void getControls() {
		tvTablesName = (TextView) findViewById(R.id.tvTablesName);
		tvBookInfo = (TextView) findViewById(R.id.tvBookInfo);
		listView = (ListView) findViewById(R.id.list_view);
	}

	private class LoadTables extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tablesList = new ArrayList<Table>();

			pDialog = new ProgressDialog(TablesActivity.this);
			pDialog.setMessage("Wczytywanie stolików. \nProszê czekaæ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHandler db = new DatabaseHandler(TablesActivity.this);

			List<Table> tables = db.getTables(id);

			for (int i = 0; i < tables.size(); i++) {
				Table table = tables.get(i);

				tablesList.add(table);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			runOnUiThread(new Runnable() {
				public void run() {

					adapter = new TablesArrayAdapter(TablesActivity.this,
							tablesList);
					listView.setAdapter(adapter);
				}
			});
		}
	}

	private void save() {
		DatabaseHandler db = new DatabaseHandler(TablesActivity.this);
		SharedPreferences preferences = getSharedPreferences("Settings",
				Context.MODE_PRIVATE);
		int userId = db.getUserId(preferences.getString("login", ""));

		CheckConnection checkConnection = new CheckConnection();
		if (checkConnection.internet(this) || checkConnection.wifi(this)) {
			SendDataToServer sendData = new SendDataToServer();
			if (sendData.setReservation(adapter.getSelectedTables(),
					ReservationType.TABLE, id, userId)) {
				finish();
				db.setReservation(adapter.getSelectedTables(), ReservationType.TABLE, id, userId);
			} else
				Toast.makeText(this,
						getResources().getText(R.string.error_while_send_data),
						Toast.LENGTH_LONG).show();
		} else {
			finish();
			Intent intent = new Intent(
					"com.marshall.cafeproject.activities.EXCEPTIONACTIVITY");
			startActivity(intent);
		}
		finish();
	}

	private boolean isOnlineUser() {
		SharedPreferences preferences = getSharedPreferences("Settings",
				Context.MODE_PRIVATE);

		if (!preferences.getString("login", "").equals(""))
			return true;
		else
			return false;
	}
}
