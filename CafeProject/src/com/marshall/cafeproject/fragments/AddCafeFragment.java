package com.marshall.cafeproject.fragments;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.marshall.cafeproject.CheckConnection;
import com.marshall.cafeproject.JSONParser;
import com.marshall.cafeproject.MenuCollections;
import com.marshall.cafeproject.MenuCafe;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.Table;
import com.marshall.cafeproject.adapters.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddCafeFragment extends Fragment {

	private EditText etName, etStreet, etBuilding, etLocal, etDistrict, etCity,
			etPhone, etWebsite, etDescription, etImage, etMonday, etTuesday,
			etWednesday, etThursday, etFriday, etSaturday, etSunday, etProduct,
			etQuantity, etPrice;
	private String email;
	private ProgressDialog progressDialog;
	private MenuCollections menuCollections;
	private List<NameValuePair> params;
	private JSONParser jsonParser;
	private CheckConnection checkConnection;
	private static String url_add_cafe = "http://marshall.ct8.pl/cafeproject/add_cafe.php";
	private ViewPager viewPager;
	private TabsPagerAdapter adapter;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_pager_layout, container,
				false);

		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		adapter = new TabsPagerAdapter(getActivity(), getChildFragmentManager());
		
		viewPager.setAdapter(adapter);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		boolean isCreated = false;

		for (int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getTitle() == getResources().getString(
					R.string.send))
				isCreated = true;
		}

		if (!isCreated)
			getActivity().getMenuInflater().inflate(R.menu.save_menu, menu);

		DrawerLayout drawerMenu = (DrawerLayout) getActivity().findViewById(
				R.id.drawer_layout);
		ListView menuListView = (ListView) getActivity().findViewById(
				R.id.list_slidermenu);

		if (drawerMenu.isDrawerOpen(menuListView))
			menu.findItem(R.id.save).setVisible(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			save();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("childFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			Log.e("AddCafeFragment ", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("AddCafeFragment ", e.getMessage());
		}
	}

	private void save() {
		getControls();
		checkConnection = new CheckConnection();
		viewPager.setCurrentItem(1);
		
		final EditText input = new EditText(getActivity());
		new AlertDialog.Builder(getActivity())
				.setTitle(
						getResources().getString(R.string.add_cafe_alert_title))
				.setView(input)
				.setPositiveButton(getResources().getString(R.string.ok),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								boolean isValid = Patterns.EMAIL_ADDRESS
										.matcher(input.getText().toString())
										.matches();

								if (!input.getText().equals("") && isValid) {
									email = input.getText().toString();

									if (checkConnection.internet(getActivity())
											|| checkConnection
													.wifi(getActivity())) {
										new CreateCafe().execute();
										dialog.dismiss();
									} else {
										Intent intent = new Intent(
												"com.example.cafeclub.EXCEPTIONACTIVITY");
										startActivity(intent);
									}

								} else {
									Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
								}

							}
						}).create().show();
	}

	private void getControls() {
		etName = (EditText) getActivity().findViewById(R.id.etName);
		etStreet = (EditText) getActivity().findViewById(R.id.etStreet);
		etBuilding = (EditText) getActivity().findViewById(R.id.etBuilding);
		etLocal = (EditText) getActivity().findViewById(R.id.etLocal);
		etDistrict = (EditText) getActivity().findViewById(R.id.etDistrict);
		etCity = (EditText) getActivity().findViewById(R.id.etCity);
		etPhone = (EditText) getActivity().findViewById(R.id.etPhone);
		etWebsite = (EditText) getActivity().findViewById(R.id.etWebsite);
		etDescription = (EditText) getActivity().findViewById(
				R.id.etDescription);
		etImage = (EditText) getActivity().findViewById(R.id.etImage);
		etMonday = (EditText) getView().findViewById(R.id.etMonday);
		etTuesday = (EditText) getView().findViewById(R.id.etTuesday);
		etWednesday = (EditText) getView().findViewById(R.id.etWednesday);
		etThursday = (EditText) getView().findViewById(R.id.etThursday);
		etFriday = (EditText) getView().findViewById(R.id.etFriday);
		etSaturday = (EditText) getView().findViewById(R.id.etSaturday);
		etSunday = (EditText) getView().findViewById(R.id.etSunday);
		etProduct = (EditText) getActivity().findViewById(R.id.etProduct);
		etQuantity = (EditText) getActivity().findViewById(R.id.etQuantity);
		etPrice = (EditText) getActivity().findViewById(R.id.etPrice);
	}

	private class CreateCafe extends AsyncTask<String, String, String> {

		private int dialogFlag;
		private int success;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Przesy³anie danych...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

			menuCollections = new MenuCollections();
			params = new ArrayList<NameValuePair>();
			jsonParser = new JSONParser();
		}

		@Override
		protected String doInBackground(String... args) {

			checkField("Name", etName.getText().toString());
			checkField("Street", etStreet.getText().toString());
			checkField("Building", etBuilding.getText().toString());
			checkField("Local", etLocal.getText().toString());
			checkField("District", etDistrict.getText().toString());
			checkField("City", etCity.getText().toString());
			checkField("Phone", etPhone.getText().toString());
			checkField("Website", etWebsite.getText().toString());
			checkField("Description", etDescription.getText().toString());
			checkField("Image", etImage.getText().toString());
			checkField("Monday", etMonday.getText().toString());
			checkField("Tuesday", etTuesday.getText().toString());
			checkField("Wednesday", etWednesday.getText().toString());
			checkField("Thursday", etThursday.getText().toString());
			checkField("Friday", etFriday.getText().toString());
			checkField("Saturday", etSaturday.getText().toString());
			checkField("Sunday", etSunday.getText().toString());
			checkField("Email", email);

			if (menuCollections.getProducts().size() > 0)
				addProducts();

			params.add(new BasicNameValuePair("Number", Integer
					.toString(menuCollections.getProducts().size())));

			if (menuCollections.getTables().size() > 0)
				addTables();
			
			params.add(new BasicNameValuePair("TablesCount", Integer
					.toString(menuCollections.getTables().size())));

			Log.d("params", params.toString());

			JSONObject json = jsonParser.makeHttpRequest(url_add_cafe, "POST",
					params);

			try {
				success = json.getInt("success");

				if (success == 1) {
					dialogFlag = 1;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (dialogFlag == 1) {
				setAlertDialog(
						getString(R.string.success_add_cafe_title).toString(),
						getResources().getString(
								R.string.success_add_cafe_message).toString(),
						dialogFlag);
			} else {
				setAlertDialog(
						getString(R.string.check_entered_data_title).toString(),
						getResources().getString(
								R.string.check_entered_data_message).toString(),
						0);
			}
		}
	}

	public void checkField(String name, String value) {
		if (!value.isEmpty()) {
			Log.d(name + ": ", value);
			params.add(new BasicNameValuePair(name, value));
		} else {
			Log.d(name + ": ", value);
		}
	}

	private void setAlertDialog(String title, String message,
			final int dialogFlag) {
		new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(getResources().getString(R.string.ok),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (dialogFlag == 1) {
									clearData();
								} else {
									dialog.dismiss();
								}

							}
						}).show();
	}

	private void clearData() {
		etName.setText("");
		etStreet.setText("");
		etBuilding.setText("");
		etLocal.setText("");
		etDistrict.setText("");
		etCity.setText("");
		etPhone.setText("");
		etWebsite.setText("");
		etDescription.setText("");
		etImage.setText("");
		etMonday.setText("");
		etTuesday.setText("");
		etWednesday.setText("");
		etThursday.setText("");
		etFriday.setText("");
		etSaturday.setText("");
		etSunday.setText("");
		etProduct.setText("");
		etQuantity.setText("");
		etPrice.setText("");
	}

	private void addProducts() {
		ArrayList<MenuCafe> products = menuCollections.getProducts();

		for (int i = 0; i < products.size(); i++) {
			checkField("Product" + i, products.get(i).getProduct());
			checkField("Quantity" + i, products.get(i).getQuantity());
			checkField("Price" + i, products.get(i).getPrice());
		}
	}
	
	private void addTables() {
		ArrayList<Table> tables = menuCollections.getTables();

		for (int i = 0; i < tables.size(); i++) {
			checkField("TableNumber" + i, String.valueOf(tables.get(i).getTableNumber()));
			checkField("Seats" + i, String.valueOf(tables.get(i).getSeats()));
		}
	}
}
