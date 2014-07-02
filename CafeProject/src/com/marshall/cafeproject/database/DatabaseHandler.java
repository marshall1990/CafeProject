package com.marshall.cafeproject.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.marshall.cafeproject.Cafe;
import com.marshall.cafeproject.DateConverter;
import com.marshall.cafeproject.Event;
import com.marshall.cafeproject.MenuCafe;
import com.marshall.cafeproject.Reservation;
import com.marshall.cafeproject.ReservationType;
import com.marshall.cafeproject.Table;
import com.marshall.cafeproject.User;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int db_version = 1;
	private static final String db_name = "Cafes";

	private static final String table_cafe = "Cafe";
	private static final String table_details = "Details";
	private static final String table_hours = "Hours";
	private static final String table_menu = "Menu";
	private static final String table_rating = "Rating";
	private static final String table_locations = "Locations";
	private static final String table_lastlocation = "LastLocation";
	private static final String table_users = "Users";
	private static final String table_tables = "Tables";
	private static final String table_events = "Events";
	private static final String table_informations = "Informations";
	private static final String table_reservations = "Reservatins";

	private static final String col_id = "Id";
	private static final String col_cafeid = "CafeId";

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
	private static final String col_product = "Product";
	private static final String col_quantity = "Quantity";
	private static final String col_price = "Price";

	// Table Rating
	private static final String col_points = "Points";

	// Table Locations
	private static final String col_lat = "Lat";
	private static final String col_lng = "Lng";
	private static final String col_icon = "Icon";

	// Table Users
	private static final String col_login = "Login";
	private static final String col_password = "Password";

	// Table Tables
	private static final String col_tablenumbers = "Numbers";
	private static final String col_seats = "Seats";

	// Table Events
	private static final String col_start = "Start";
	private static final String col_end = "End";

	// Table Reservations
	private static final String col_tableid = "TableId";
	private static final String col_eventid = "EventId";
	private static final String col_userid = "UserId";
	private static final String col_dateandtime = "DateAndTime";

	// Table Informations
	private static final String col_lastupdate = "LastUpdate";

	private int limit = 20;
	private String order = "ASC";
	private String column = "Points";

	private String name, street, district, city, findCity, product;

	private SharedPreferences sharedPreferences;

	private Context context;

	public DatabaseHandler(Context context) {
		super(context, db_name, null, db_version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + table_cafe + "(" + col_id + " INTEGER, "
				+ col_name + " TEXT, " + col_street + " TEXT, " + col_building
				+ " TEXT, " + col_local + " TEXT, " + col_district + " TEXT, "
				+ col_city + " TEXT " + ")");

		db.execSQL("CREATE TABLE " + table_details + "(" + col_id
				+ " INTEGER, " + col_phone + " TEXT, " + col_website
				+ " TEXT, " + col_description + " TEXT, " + col_image + " TEXT"
				+ ")");

		db.execSQL("CREATE TABLE " + table_hours + "(" + col_id + " INTEGER, "
				+ col_pn + " TEXT, " + col_wt + " TEXT, " + col_sr + " TEXT, "
				+ col_cz + " TEXT, " + col_pt + " TEXT, " + col_sb + " TEXT, "
				+ col_nd + " TEXT" + ")");

		db.execSQL("CREATE TABLE " + table_menu + "(" + col_cafeid
				+ " INTEGER, " + col_product + " TEXT, " + col_quantity
				+ " TEXT, " + col_price + " TEXT" + ")");

		db.execSQL("CREATE TABLE " + table_rating + "(" + col_cafeid
				+ " TEXT, " + col_points + " INTEGER" + ")");

		db.execSQL("CREATE TABLE " + table_locations + "(" + col_id
				+ " INTEGER, " + col_lat + " TEXT, " + col_lng + " TEXT,"
				+ col_icon + " TEXT" + ")");

		db.execSQL("CREATE TABLE " + table_lastlocation + "(" + col_lat
				+ " TEXT, " + col_lng + " TEXT, " + col_city + " TEXT" + ")");

		db.execSQL("CREATE TABLE " + table_users + "(" + col_id + " INTEGER, "
				+ col_login + " TEXT, " + col_password + " TEXT)");

		db.execSQL("CREATE TABLE " + table_tables + "(" + col_tablenumbers
				+ " INTEGER, " + col_seats + " INTEGER, " + col_cafeid
				+ " INTEGER)");

		db.execSQL("CREATE TABLE " + table_events + "(" + col_name + " TEXT, "
				+ col_description + " TEXT, " + col_start + " TEXT, " + col_end
				+ " TEXT, " + col_cafeid + " INTEGER)");

		db.execSQL("CREATE TABLE " + table_informations + "(" + col_lastupdate
				+ " TEXT)");

		db.execSQL("CREATE TABLE " + table_reservations + "(" + col_tableid
				+ " INTEGER, " + col_eventid + " INTEGER, " + col_cafeid
				+ " INTEGER, " + col_userid + " INTEGER, " + col_dateandtime
				+ " TEXT); ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + table_cafe);
		db.execSQL("DROP TABLE IF EXISTS " + table_details);
		db.execSQL("DROP TABLE IF EXISTS " + table_hours);
		db.execSQL("DROP TABLE IF EXISTS " + table_menu);
		db.execSQL("DROP TABLE IF EXISTS " + table_rating);
		db.execSQL("DROP TABLE IF EXISTS " + table_locations);
		db.execSQL("DROP TABLE IF EXISTS " + table_lastlocation);
		db.execSQL("DROP TABLE IF EXISTS " + table_users);
		db.execSQL("DROP TABLE IF EXISTS " + table_tables);
		db.execSQL("DROP TABLE IF EXISTS " + table_events);
		db.execSQL("DROP TABLE IF EXISTS " + table_reservations);

		onCreate(db);
	}

	public void clearTables() {
		SQLiteDatabase db = getWritableDatabase();

		db.execSQL("DELETE FROM " + table_cafe);
		db.execSQL("DELETE FROM " + table_details);
		db.execSQL("DELETE FROM " + table_hours);
		db.execSQL("DELETE FROM " + table_menu);
		db.execSQL("DELETE FROM " + table_rating);
		db.execSQL("DELETE FROM " + table_locations);
		db.execSQL("DELETE FROM " + table_lastlocation);
		db.execSQL("DELETE FROM " + table_users);
		db.execSQL("DELETE FROM " + table_tables);
		db.execSQL("DELETE FROM " + table_events);
		db.execSQL("DELETE FROM " + table_reservations);

	}

	public void addCafe(Cafe cafe) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(col_id, cafe.getId());
		values.put(col_name, cafe.getName());
		values.put(col_street, cafe.getStreet());
		values.put(col_building, cafe.getBuilding());
		values.put(col_local, cafe.getLocal());
		values.put(col_district, cafe.getDistrict());
		values.put(col_city, cafe.getCity());

		db.insert(table_cafe, null, values);

		values = new ContentValues();
		values.put(col_id, cafe.getId());
		values.put(col_phone, cafe.getPhone());
		values.put(col_website, cafe.getWebsite());
		values.put(col_description, cafe.getDescription());
		values.put(col_image, cafe.getImage());

		db.insert(table_details, null, values);

		values = new ContentValues();
		values.put(col_id, cafe.getId());
		values.put(col_pn, cafe.getMonday());
		values.put(col_wt, cafe.getTuesday());
		values.put(col_sr, cafe.getWednesday());
		values.put(col_cz, cafe.getThursday());
		values.put(col_pt, cafe.getFriday());
		values.put(col_sb, cafe.getSaturday());
		values.put(col_nd, cafe.getSunday());

		db.insert(table_hours, null, values);

		values = new ContentValues();
		values.put(col_cafeid, cafe.getId());
		values.put(col_points, cafe.getPoints());

		db.insert(table_rating, null, values);

		values = new ContentValues();
		values.put(col_id, cafe.getId());
		values.put(col_lat, cafe.getLat());
		values.put(col_lng, cafe.getLng());
		values.put(col_icon, cafe.getIcon());

		db.insert(table_locations, null, values);

		db.close();
	}

	public void addMenu(Cafe cafe) {
		SQLiteDatabase db = this.getWritableDatabase();

		List<MenuCafe> menuCafe = cafe.getMenu();

		for (MenuCafe menu : menuCafe) {
			ContentValues values = new ContentValues();

			values.put(col_cafeid, menu.getCafeId());
			values.put(col_product, menu.getProduct());
			values.put(col_quantity, menu.getQuantity());
			values.put(col_price, menu.getPrice());

			db.insert(table_menu, null, values);
		}

		db.close();
	}

	public void addUsers(List<User> users) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (User user : users) {
			ContentValues values = new ContentValues();

			values.put(col_id, user.getId());
			values.put(col_login, user.getLogin());
			values.put(col_password, user.getPassword());

			db.insert(table_users, null, values);
		}
		db.close();
	}

	public void addTables(List<Table> tables) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (Table table : tables) {
			ContentValues values = new ContentValues();

			values.put(col_tablenumbers, table.getTableNumber());
			values.put(col_seats, table.getSeats());
			values.put(col_cafeid, table.getCafeId());

			db.insert(table_tables, null, values);
		}
		db.close();
	}

	public void addEvents(List<Event> events) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (Event event : events) {
			ContentValues values = new ContentValues();

			values.put(col_name, event.getName());
			values.put(col_description, event.getDescription());
			values.put(col_start, DateConverter.dateToString(event.getStart()));
			values.put(col_end, DateConverter.dateToString(event.getEnd()));
			values.put(col_cafeid, String.valueOf(event.getCafeId()));

			db.insert(table_events, null, values);
		}

		db.close();
	}

	public void addReservations(List<Reservation> reservations) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (Reservation reservation : reservations) {
			ContentValues values = new ContentValues();

			values.put(col_tableid, reservation.getTableId());
			values.put(col_eventid, reservation.getEventId());
			values.put(col_cafeid, reservation.getCafeId());
			values.put(col_userid, reservation.getUserId());
			values.put(col_dateandtime, reservation.getDateAndTime());

			db.insert(table_reservations, null, values);
		}

		db.close();
	}

	public void addLastLocation(double lat, double lng, String city) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(col_lat, lat);
		values.put(col_lng, lng);
		values.put(col_city, city);

		db.insert(table_lastlocation, null, values);

		db.close();
	}

	public void addLastUpdate(Date lastUpdate) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(col_lastupdate, DateConverter.dateToString(lastUpdate));

		Log.i("DatabaseHandles addLastUpdate() ",
				DateConverter.dateToString(lastUpdate));

		db.insert(table_informations, null, values);

		db.close();
	}

	public List<String> getLastLocation() {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.query(table_lastlocation, new String[] { col_lat,
				col_lng, col_city }, null, null, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		List<String> lastLocation = new ArrayList<String>();

		lastLocation.add(cursor.getString(0));
		lastLocation.add(cursor.getString(1));
		lastLocation.add(cursor.getString(2));

		cursor.close();
		db.close();

		return lastLocation;
	}

	public Cafe getCafe(int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.query(table_cafe, new String[] { col_id, col_name,
				col_street, col_building, col_local, col_district, col_city },
				col_id + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);

		Cafe cafe = new Cafe();

		if (cursor != null) {
			cursor.moveToFirst();

			cafe.setId(cursor.getInt(0));
			cafe.setName(cursor.getString(1));
			cafe.setStreet(cursor.getString(2));
			cafe.setBuilding(cursor.getString(3));
			cafe.setLocal(cursor.getString(4));
			cafe.setDistrict(cursor.getString(5));
			cafe.setCity(cursor.getString(6));
		}
		cursor.close();

		cursor = db.query(table_details, new String[] { col_phone, col_website,
				col_description, col_image }, col_id + "= ?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();

			cafe.setPhone(cursor.getString(0));
			cafe.setWebsite(cursor.getString(1));
			cafe.setDescription(cursor.getString(2));
			cafe.setImage(cursor.getString(3));
		}
		cursor.close();

		cursor = db.query(table_hours, new String[] { col_pn, col_wt, col_sr,
				col_cz, col_pt, col_sb, col_nd }, col_id + "= ?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			cafe.setMonday(cursor.getString(0));
			cafe.setTuesday(cursor.getString(1));
			cafe.setWednesday(cursor.getString(2));
			cafe.setThursday(cursor.getString(3));
			cafe.setFriday(cursor.getString(4));
			cafe.setSaturday(cursor.getString(5));
			cafe.setSunday(cursor.getString(6));
		}

		cursor.close();

		cursor = db.query(table_menu, new String[] { col_cafeid }, col_cafeid
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);

		if (cursor.getCount() > 0) {
			cafe.setIsMenu(true);
		} else {
			cafe.setIsMenu(false);
		}

		cursor.close();

		cursor = db.query(table_tables, new String[] { col_cafeid }, col_cafeid
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);

		if (cursor.getCount() > 0) {
			cafe.setIsTables(true);
		} else {
			cafe.setIsTables(false);
		}

		cursor.close();

		cursor = db.query(table_rating, new String[] { col_points }, col_cafeid
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			cafe.setPoints(cursor.getInt(0));
		}

		cursor.close();
		db.close();

		return cafe;
	}

	public List<MenuCafe> getMenu(int CafeId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<MenuCafe> menuCafe = new ArrayList<MenuCafe>();

		Cursor cursor = db
				.query(table_menu, new String[] { col_product, col_quantity,
						col_price }, col_cafeid + "=?",
						new String[] { String.valueOf(CafeId) }, null, null,
						null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			do {
				MenuCafe menu = new MenuCafe();
				menu.setProduct(cursor.getString(0));
				menu.setQuantity(cursor.getString(1));
				menu.setPrice(cursor.getString(2));
				menuCafe.add(menu);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return menuCafe;
	}

	public List<Table> getTables(int cafeId) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Table> tables = new ArrayList<Table>();

		Cursor cursor = db.rawQuery("SELECT t.*, r." + col_userid + ", r."
				+ col_dateandtime + ", u." + col_login + "  FROM "
				+ table_tables + " t LEFT JOIN " + table_reservations
				+ " r ON t." + col_tablenumbers + " = r. " + col_tableid
				+ " AND t." + col_cafeid + " = r." + col_cafeid + " LEFT JOIN "
				+ table_users + " u ON r." + col_userid + " = u." + col_id
				+ " WHERE t." + col_cafeid + " = " + cafeId, null);

		if (cursor.moveToFirst()) {
			cursor.moveToFirst();

			do {
				Table table = new Table();
				table.setTableNumber(cursor.getInt(cursor
						.getColumnIndex(col_tablenumbers)));
				table.setSeats(cursor.getInt(cursor.getColumnIndex(col_seats)));

				String loginFromDB = cursor.getString(cursor
						.getColumnIndex(col_login));

				SharedPreferences preferences = context.getSharedPreferences(
						"Settings", Context.MODE_PRIVATE);

				if (loginFromDB != null) {
					String date = cursor.getString(cursor
							.getColumnIndex(col_dateandtime));
					int userId = cursor.getInt(cursor
							.getColumnIndex(col_userid));

					if (DateConverter.isPossibleReservation(date)) {
						if (userId == getUserId(preferences.getString("login",
								""))) {
							table.setYourReservation(true);
							table.setReservedForOtherUser(false);
						} else {
							table.setReservedForOtherUser(true);
							table.setYourReservation(false);
						}
					} else {
						table.setYourReservation(false);
						table.setReservedForOtherUser(false);
					}
				}

				tables.add(table);
			} while (cursor.moveToNext());
		}

		db.close();
		cursor.close();

		return tables;
	}

	public ArrayList<Event> getEvents() {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Event> events = new ArrayList<Event>();

		Cursor cursor = db
				.rawQuery("SELECT * FROM " + table_events + ";", null);

		if (cursor.moveToFirst()) {
			do {
				Event event = new Event();

				event.setName(cursor.getString(cursor.getColumnIndex(col_name)));
				event.setDescription(cursor.getString(cursor
						.getColumnIndex(col_description)));
				event.setStart(DateConverter.stringToDate(cursor
						.getString(cursor.getColumnIndex(col_start))));
				event.setEnd(DateConverter.stringToDate(cursor.getString(cursor
						.getColumnIndex(col_end))));
				event.setCafeId(cursor.getInt(cursor.getColumnIndex(col_cafeid)));

				event.setCafe(getCafe(event.getCafeId()));

				events.add(event);

			} while (cursor.moveToNext());
		}

		db.close();
		cursor.close();

		return events;
	}

	public List<Cafe> getAllCafes(HashMap<String, String> columns) {
		sharedPreferences = context.getSharedPreferences("Settings",
				Context.MODE_PRIVATE);
		column = sharedPreferences.getString("column", column);
		order = sharedPreferences.getString("order", order);
		limit = sharedPreferences.getInt("limit", limit);

		city = columns.get("City");
		name = columns.get("Name");
		street = columns.get("Street");
		district = columns.get("District");
		findCity = columns.get("FindCity");
		product = columns.get("Product");

		List<Cafe> cafeList = new ArrayList<Cafe>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor, query;

		if (name == null || street == null || district == null
				|| product == null || findCity == null || name.equals("")
				&& street.equals("") && district.equals("")
				&& product.equals("") && findCity.equals("")) {
			query = db.rawQuery(
					"SELECT c.*, r.Points, l.Lat, l.Lng FROM " + table_cafe
							+ " c JOIN " + table_rating
							+ " r ON c.Id = r.CafeId JOIN " + table_locations
							+ " l on c.Id = l.Id " + " WHERE City = '" + city
							+ "' ORDER BY " + column + " " + order + " LIMIT "
							+ String.valueOf(limit), null);

			if (query.getCount() != 0) {
				cursor = query;
			} else {
				cursor = db.rawQuery("SELECT c.*, r.Points, l.Lat, l.Lng FROM "
						+ table_cafe + " c JOIN " + table_rating
						+ " r ON c.Id = r.CafeId JOIN " + table_locations
						+ " l on c.Id = l.Id " + " ORDER BY " + column + " "
						+ order + " LIMIT " + String.valueOf(limit), null);
			}
		} else {
			String sql = "SELECT c.*, r.Points, l.Lat, l.Lng FROM "
					+ table_cafe + " c JOIN " + table_rating
					+ " r ON c.Id = r.CafeId JOIN " + table_locations
					+ " l on c.Id = l.Id " + " WHERE 1=1 ";

			if (!name.equals(""))
				sql += "AND c.Name LIKE '%" + name + "%' ";

			if (!street.equals(""))
				sql += "AND c.Street LIKE '%" + street + "%' ";

			if (!district.equals(""))
				sql += "AND c.District LIKE '%" + district + "%' ";

			if (!findCity.equals(""))
				sql += "AND c.City LIKE '%" + findCity + "%' ";

			if (!product.equals(""))
				sql += "AND c.Id=(SELECT DISTINCT m.CafeId FROM menu m WHERE m.Product LIKE '%"
						+ product + "%') ";

			sql += " ORDER BY " + column + " " + order + " LIMIT "
					+ String.valueOf(limit) + " COLLATE LOCALIZED; ";

			cursor = db.rawQuery(sql, null);
		}

		if (cursor.moveToFirst()) {
			do {
				Cafe cafe = new Cafe();
				cafe.setId(cursor.getInt(0));
				cafe.setName(cursor.getString(1));
				cafe.setStreet(cursor.getString(2));
				cafe.setBuilding(cursor.getString(3));
				cafe.setLocal(cursor.getString(4));
				cafe.setDistrict(cursor.getString(5));
				cafe.setCity(cursor.getString(6));
				cafe.setPoints(cursor.getInt(7));
				cafe.setLat(cursor.getString(8));
				cafe.setLng(cursor.getString(9));
				cafe.setIcon("icon1");
				cafe.setSnippet(cafe.getStreet() + " " + cafe.getBuilding()
						+ ", " + cafe.getCity());
				cafeList.add(cafe);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return cafeList;
	}

	public Date getLastUpdate() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM Informations ORDER BY LastUpdate DESC LIMIT 1;",
				null);
		Date lastUpdate = new Date();

		if (cursor.moveToFirst())
			lastUpdate = DateConverter.stringToDate(cursor.getString(0));

		Log.i("DatabaseHandler getLastUpdate() ", lastUpdate.toString());

		cursor.close();
		db.close();

		return lastUpdate;
	}

	public boolean authenticateUser(User user) {
		SQLiteDatabase db = this.getReadableDatabase();
		boolean isAuthenticate;
		Cursor cursor = db.rawQuery("SELECT * FROM users WHERE Login = '"
				+ user.getLogin() + "' AND Password = '" + user.getPassword()
				+ "'; ", null);

		if (cursor.getCount() > 0) {
			isAuthenticate = true;
		} else {
			isAuthenticate = false;
		}

		cursor.close();
		db.close();

		return isAuthenticate;
	}

	public int getUserId(String login) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT + " + col_id + " FROM users WHERE "
				+ col_login + " = '" + login + "'; ", null);

		if (cursor.moveToFirst())
			return cursor.getInt(0);
		else
			return 0;
	}

	public void setReservation(List<Integer> ids,
			ReservationType reservationType, int cafeId, int userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Date date = new Date();

		for (Integer id : ids) {
			if (reservationType == ReservationType.TABLE)
				values.put(col_tableid, id);
			else
				values.put(col_eventid, id);

			values.put(col_cafeid, cafeId);
			values.put(col_userid, userId);
			values.put(col_dateandtime, DateConverter.dateToString(date));
		}

		db.insert(table_reservations, null, values);

		db.close();
	}
}
