package com.marshall.cafeproject.activities;

import com.google.android.gms.maps.GoogleMap;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.SettingsEnums;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private Spinner spinner;
	private RadioGroup radioGroup;
	private RadioGroup rgMap;
	private EditText number;
	private String column;
	private int checkedRadioButton;
	private String order;
	private int limit;
	private int mapType;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getControls();

		setSettings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		spinner = (Spinner) findViewById(R.id.spinnerSort);
		radioGroup = (RadioGroup) findViewById(R.id.rgSort);
		rgMap = (RadioGroup) findViewById(R.id.rgMap);
		number = (EditText) findViewById(R.id.etNumber);
	}

	private void setSettings() {
		SharedPreferences preferences = getSharedPreferences("Settings",
				Context.MODE_PRIVATE);
		column = preferences.getString("column", column);
		order = preferences.getString("order", order);
		limit = preferences.getInt("limit", limit);
		mapType = preferences.getInt("mapType", mapType);

		number.setText(String.valueOf(limit));

		if (column != null)
			spinner.setSelection(SettingsEnums.EnglishColumns.valueOf(column)
					.ordinal());

		if (order != null) {
			if (order.equals("ASC"))
				radioGroup.check(R.id.radioASC);
			else if (order.equals("DESC"))
				radioGroup.check(R.id.radioDESC);
		}

		switch (mapType) {
		case GoogleMap.MAP_TYPE_SATELLITE:
			rgMap.check(R.id.radioSatellite);
			break;
		case GoogleMap.MAP_TYPE_TERRAIN:
			rgMap.check(R.id.radioTerrain);
			break;
		case GoogleMap.MAP_TYPE_NORMAL:
			rgMap.check(R.id.radioStandard);
			break;
		default:
			rgMap.check(R.id.radioStandard);
			break;
		}

	}

	private void save() {
		try {
			limit = Integer.parseInt(number.getText().toString());

			if (limit > 0) {
				// pobranie wartoœci ze spinnera
				column = spinner.getSelectedItem().toString();

				column = SettingsEnums.EnglishColumns.values()[SettingsEnums.PolishColumns
						.valueOf(column).ordinal()].toString();

				// obs³uga RadioGroup
				checkedRadioButton = radioGroup.getCheckedRadioButtonId();
				switch (checkedRadioButton) {
				case R.id.radioASC:
					order = "ASC";
					break;
				case R.id.radioDESC:
					order = "DESC";
					break;
				}

				// rodzaj mapy
				checkedRadioButton = rgMap.getCheckedRadioButtonId();
				switch (checkedRadioButton) {
				case R.id.radioSatellite:
					mapType = GoogleMap.MAP_TYPE_SATELLITE;
					break;
				case R.id.radioTerrain:
					mapType = GoogleMap.MAP_TYPE_TERRAIN;
					break;
				case R.id.radioStandard:
					mapType = GoogleMap.MAP_TYPE_NORMAL;
					break;
				}

				sharedPreferences = getSharedPreferences("Settings", 0);
				editor = sharedPreferences.edit();
				editor.putString("column", column);
				editor.putString("order", order);
				editor.putInt("limit", limit);
				editor.putInt("mapType", mapType);
				editor.commit();

				setResult(100, getIntent());
				finish();
			} else {
				showToast();
			}
		} catch (Exception e) {
			showToast();
			Log.e("SettingsActivity ", e.toString());
		}
	}

	private void showToast() {
		Toast.makeText(SettingsActivity.this,
				getResources().getString(R.string.enter_valid_data),
				Toast.LENGTH_LONG).show();
	}
}
