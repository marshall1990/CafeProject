package com.marshall.cafeproject.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.marshall.cafeproject.MenuCafe;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.database.DatabaseHandler;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MenuActivity extends ListActivity {

	private ProgressDialog pDialog;
	private ArrayList<HashMap<String, String>> menuList;
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_QUANTITY = "quantity";
	private static final String TAG_PRICE = "price";
	private int id;
	private String name;
	private TextView tvMenuName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_cafe);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		tvMenuName = (TextView) findViewById(R.id.tvMenuName);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("CafeId");
		name = bundle.getString("Name");
		tvMenuName.setText(name + " - Menu");

		new LoadMenu().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private class LoadMenu extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			menuList = new ArrayList<HashMap<String, String>>();

			pDialog = new ProgressDialog(MenuActivity.this);
			pDialog.setMessage("Wczytywanie menu. \nProszê czekaæ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHandler db = new DatabaseHandler(MenuActivity.this);

			List<MenuCafe> menuCafeList = db.getMenu(id);

			for (MenuCafe menuCafe : menuCafeList) {
				String product = menuCafe.getProduct();
				String quantity = menuCafe.getQuantity();
				String price = menuCafe.getPrice();

				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_PRODUCT, product);
				map.put(TAG_QUANTITY, quantity);
				map.put(TAG_PRICE, price);

				menuList.add(map);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			runOnUiThread(new Runnable() {
				public void run() {
					ListAdapter adapter = new SimpleAdapter(
							MenuActivity.this,
							menuList,
							R.layout.menu_item,
							new String[] { TAG_PRODUCT, TAG_QUANTITY, TAG_PRICE },
							new int[] { R.id.product, R.id.quantity, R.id.price });

					setListAdapter(adapter);
				}
			});
		}
	}
}
