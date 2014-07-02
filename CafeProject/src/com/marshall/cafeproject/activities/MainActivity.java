package com.marshall.cafeproject.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.marshall.cafeproject.Cafe;
import com.marshall.cafeproject.CheckConnection;
import com.marshall.cafeproject.DateConverter;
import com.marshall.cafeproject.Event;
import com.marshall.cafeproject.JSONParser;
import com.marshall.cafeproject.MenuCafe;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.Reservation;
import com.marshall.cafeproject.Table;
import com.marshall.cafeproject.User;
import com.marshall.cafeproject.database.DatabaseHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private static final String col_id = "Id";

	// Table Cafe
	private static final String col_name = "Name";
	private static final String col_street = "Street";
	private static final String col_building = "Building";
	private static final String col_local = "Local";
	private static final String col_district = "District";
	private static final String col_city = "City";

	// Table Details
	private static final String col_phone = "Phone";
	private static final String col_website = "Website";
	private static final String col_description = "Description";
	private static final String col_image = "Image";

	// Table Hours
	private static final String col_pn = "Monday";
	private static final String col_wt = "Tuesday";
	private static final String col_sr = "Wednesday";
	private static final String col_cz = "Thursday";
	private static final String col_pt = "Friday";
	private static final String col_sb = "Saturday";
	private static final String col_nd = "Sunday";

	// Table Menu
	private static final String col_menu = "Menu";
	private static final String col_product = "Product";
	private static final String col_quantity = "Quantity";
	private static final String col_price = "Price";

	// Table Rating
	private static final String col_points = "Points";

	// Table LatLng
	private static final String col_lat = "Lat";
	private static final String col_lng = "Lng";
	
	// Table Users
	private static final String col_login = "Login";
	private static final String col_password = "Password";
	
	// Table Tables
	private static final String col_tables = "Tables";
	private static final String col_number = "Number";
	private static final String col_seats = "Seats";
	private static final String col_cafeid = "CafeId";
	
	// Table Events
	private static final String col_start = "Start";
	private static final String col_end = "End";
	
	//Table Reservations
	private static final String col_tableid = "TableId";
	private static final String col_eventid = "EventId";
	private static final String col_userid = "UserId";
	private static final String col_dateandtime = "DateAndTime";
	
	// Table Informations
	private static final String col_lastupdate = "LastUpdate";

	private static String url_get_last_update = "http://marshall.ct8.pl/cafeproject/get_last_update.php";
	private static String url_all_cafes = "http://marshall.ct8.pl/cafeproject/get_all_cafes.php";
	private static String url_get_menu = "http://marshall.ct8.pl/cafeproject/get_menu.php";
	private static String url_get_users = "http://marshall.ct8.pl/cafeproject/get_users.php";
	private static String url_get_tables = "http://marshall.ct8.pl/cafeproject/get_tables.php";
	private static String url_get_events = "http://marshall.ct8.pl/cafeproject/get_events.php";
	private static String url_get_reservations = "http://marshall.ct8.pl/cafeproject/get_reservations.php";
	private JSONObject json, jsonMenu, jsonUsers, jsonTables, jsonEvents, jsonReservations;
	private JSONParser jParser;
	private JSONArray cafes, menu, users, tables, events, reservations;
	private DatabaseHandler db;
	private Cafe cafe;
	private Date lastUpdate;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.introduction);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		new CheckDatabases().execute();
	}
	
	private void startHomeActiity() {
		finish();
		Intent intent = new Intent("com.marshall.cafeproject.activities.HOMEACTIVITY");
		startActivity(intent);
	}

	private Date getLastUpdate() {
		try {
			jParser = new JSONParser();
			json = jParser.makeHttpRequest(url_get_last_update, "POST", null);
			lastUpdate = DateConverter.stringToDate(json.getString(col_lastupdate));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return lastUpdate;
	}
	
	private class CheckDatabases extends AsyncTask<Void, Void, Boolean> {
		boolean download = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new DatabaseHandler(MainActivity.this);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			CheckConnection checkConnection = new CheckConnection();
			if (checkConnection.internet(MainActivity.this) || checkConnection.wifi(MainActivity.this)) {
				
				if (!db.getLastUpdate().equals(getLastUpdate())) {
					download = true;
				} else {
					download = false;
				}
			} else {
				download = false;
			}
			
			return download;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if (download) 
				new LoadAllCafes().execute();
			else {
				if (db == null) {
					finish();
					Intent intent = new Intent("com.marshall.cafeproject.activities.EXCEPTIONACTIVITY");
					startActivity(intent);
				} else {
					startHomeActiity();
				}
			}  
		}
		
	}
	
	private class LoadAllCafes extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Trwa pobieranie danych. \nProszê czekaæ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			db.addLastUpdate(lastUpdate);
			
			cafe = new Cafe();
			json = jParser.makeHttpRequest(url_all_cafes, "POST", null);

			try {
				int success = json.getInt("success");

				if (success == 1) {
					db.clearTables();
					cafes = json.getJSONArray("Cafes");

					for (int i = 0; i < cafes.length(); i++) {
						JSONObject c = cafes.getJSONObject(i);

						cafe.setId(c.getInt(col_id));
						cafe.setName(c.getString(col_name));
						cafe.setStreet(c.getString(col_street));
						cafe.setBuilding(c.getString(col_building));
						cafe.setLocal(c.getString(col_local));
						cafe.setDistrict(c.getString(col_district));
						cafe.setCity(c.getString(col_city));
						cafe.setPhone(c.getString(col_phone));
						cafe.setWebsite(c.getString(col_website));
						cafe.setDescription(c.getString(col_description));
						cafe.setImage(c.getString(col_image));
						cafe.setPoints(c.getInt(col_points));
						cafe.setMonday(c.getString(col_pn));
						cafe.setTuesday(c.getString(col_wt));
						cafe.setWednesday(c.getString(col_sr));
						cafe.setThursday(c.getString(col_cz));
						cafe.setFriday(c.getString(col_pt));
						cafe.setSaturday(c.getString(col_sb));
						cafe.setSunday(c.getString(col_nd));

						if (c.getString(col_menu).equals("true")) 
							getMenu(cafe.getId());
						
						if (c.getString(col_tables).equals("true"))
							cafe.setIsMenu(true);
						else
							cafe.setIsMenu(false);

						cafe.setLat(c.getString(col_lat));
						cafe.setLng(c.getString(col_lng));
						cafe.setIcon("icon1");
						cafe.setSnippet(cafe.getStreet() + " " + cafe.getBuilding() +", " + cafe.getCity());

						db.addCafe(cafe);
					}
				} else {
					Log.i("Downloading date frob db - ",
							"Problem with connection");
				}
				
				getUsers();
				
				getTables();
				
				getReservations();
				
				getEvents();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			pDialog.dismiss();
			startHomeActiity();

		}

		private void getMenu(int cafeId) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			List<MenuCafe> menuCafeList = new ArrayList<MenuCafe>();
			MenuCafe menuCafe;
			params.add(new BasicNameValuePair("Id", Integer.toString(cafeId)));
			jsonMenu = jParser.makeHttpRequest(url_get_menu, "GET", params);

			try {
				int successMenu = jsonMenu.getInt("success");
				if (successMenu == 1) {
					menu = jsonMenu.getJSONArray("Cafes");

					for (int i = 0; i < menu.length(); i++) {
						JSONObject m = menu.getJSONObject(i);

						menuCafe = new MenuCafe();

						menuCafe.setProduct(m.getString(col_product));
						menuCafe.setQuantity(m.getString(col_quantity));
						menuCafe.setPrice(m.getString(col_price));
						menuCafe.setCafeId(cafeId);

						menuCafeList.add(menuCafe);
					}
					cafe.setMenu(menuCafeList);
					db.addMenu(cafe);
				}
			} catch (Exception e) {
				Log.e("Main Activity - getMenu() ", e.toString());
			}
		}
		
		private void getUsers() {
			List<User> usersList = new ArrayList<User>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			jsonUsers = jParser.makeHttpRequest(url_get_users, "GET", params);
			
			try {
				int successUsers = jsonUsers.getInt("success");
				if (successUsers == 1) {
					users = jsonUsers.getJSONArray("Users");
					
					for (int i = 0; i < users.length(); i++) {
						JSONObject u = users.getJSONObject(i);
						
						User user = new User();
						user.setId(u.getInt(col_id));
						user.setLogin(u.getString(col_login));
						user.setPassword(u.getString(col_password));
						
						usersList.add(user);
					}
					db.addUsers(usersList);
				}
			} catch (Exception e) {
				Log.e("Main Activity - getUsers() ", e.toString());
			}
		}

		private void getTables() {
			List<Table> tablesList = new ArrayList<Table>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			jsonTables = jParser.makeHttpRequest(url_get_tables, "GET", params);
			
			try {
				int successUsers = jsonTables.getInt("success");
				if (successUsers == 1) {
					tables = jsonTables.getJSONArray("Tables");
					
					for (int i = 0; i < tables.length(); i++) {
						JSONObject t = tables.getJSONObject(i);
						
						Table table = new Table();
						table.setTableNumber(t.getInt(col_number));
						table.setSeats(t.getInt(col_seats));
						table.setCafeId(t.getInt(col_cafeid));
						
						tablesList.add(table);
					}
					db.addTables(tablesList);
				}
			} catch (Exception e) {
				Log.e("Main Activity - getTables() ", e.toString());
			}
		}
		
		private void getReservations() {
			List<Reservation> reservationsList = new ArrayList<Reservation>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			jsonReservations = jParser.makeHttpRequest(url_get_reservations, "GET", params);
			
			try {
				int success = jsonReservations.getInt("success");
				if (success == 1) {
					reservations = jsonReservations.getJSONArray("Reservations");
					
					for (int i = 0; i < reservations.length(); i++) {
						JSONObject r = reservations.getJSONObject(i);
						
						Reservation reservation = new Reservation();
						
						reservation.setTableId(r.getInt(col_tableid));
						reservation.setEventId(r.getInt(col_eventid));
						reservation.setCafeId(r.getInt(col_cafeid));
						reservation.setUserId(r.getInt(col_userid));
						reservation.setDateAndTime(r.getString(col_dateandtime));
						
						reservationsList.add(reservation);
					}
					db.addReservations(reservationsList);
				}
			} catch (Exception e) {
				Log.e("Main Activity - getTables() ", e.toString());
			}
		}
		
		private void getEvents() {
			List<Event> eventsList = new ArrayList<Event>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			jsonEvents = jParser.makeHttpRequest(url_get_events, "GET", params);
			
			try {
				int successUsers = jsonEvents.getInt("success");
				if (successUsers == 1) {
					events = jsonEvents.getJSONArray("Events");
					
					for (int i = 0; i < events.length(); i++) {
						JSONObject e = events.getJSONObject(i);
						
						Event event = new Event();
						event.setName(e.getString(col_name));
						event.setDescription(e.getString(col_description));
						event.setStart(DateConverter.stringToDate(e.getString(col_start)));
						event.setEnd(DateConverter.stringToDate(e.getString(col_end)));
						event.setCafeId(e.getInt(col_cafeid));
						
						eventsList.add(event);
					}
					db.addEvents(eventsList);
				}
			} catch (Exception e) {
				Log.e("Main Activity - getEvents() ", e.toString());
			}
		}
	}

}
