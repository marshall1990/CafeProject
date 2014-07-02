package com.marshall.cafeproject;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.util.Log;

public class SendDataToServer {

	private static final String TAG_ID = "Id";
	private static final String TAG_COUNT = "Count";
	private static final String TAG_TYPE = "Type";
	private static final String TAG_CAFEID = "CafeId";
	private static final String TAG_USERID = "UserId";
	private static final String TAG_POINTS = "Points";
	private static final String TAG_ANDROIDID = "AndroidId";
	private static final String url_book_table = "http://marshall.ct8.pl/cafeproject/add_reservation.php";
	private JSONParser jsonParser;
	private List<NameValuePair> params;
	private static final String url_create_rating = "http://marshall.ct8.pl/cafeproject/create_rating.php";
	private static final String url_update_rating = "http://marshall.ct8.pl/cafeproject/update_rating.php";
	private static final String url_get_can_vote = "http://marshall.ct8.pl/cafeproject/get_can_vote.php";

	public SendDataToServer() {
		jsonParser = new JSONParser();
		params = new ArrayList<NameValuePair>();
	}

	public boolean setReservation(List<Integer> ids, ReservationType type,
			int cafeId, int userId) {
		int count = ids.size();

		params.add(new BasicNameValuePair(TAG_COUNT, String.valueOf(count)));


		int i = 1;
		if (count > 1) {
			for (Integer id : ids) {
				params.add(new BasicNameValuePair(TAG_ID + i, String
						.valueOf(id)));
				i++;
			}
		} else {
			params.add(new BasicNameValuePair(TAG_ID + i, ids.get(0).toString()));
		}

		params.add(new BasicNameValuePair(TAG_TYPE, type.toString()));
		params.add(new BasicNameValuePair(TAG_CAFEID, String.valueOf(cafeId)));
		params.add(new BasicNameValuePair(TAG_USERID, String.valueOf(userId)));

		try {
			JSONObject json = jsonParser.makeHttpRequest(url_book_table,
					"POST", params);
			Log.i("SendDataToServer setReservation() - JSON: ", json.toString());
		} catch (Exception e) {
			Log.e("SendDataToServer ", e.toString());
			return false;
		}

		return true;
	}

	public boolean vote(int id, int points, String android_id) {
		String url_rating;

		if (points == 0) {
			points = 1;
			url_rating = url_create_rating;
		} else {
			points++;

			url_rating = url_update_rating;
		}

		params.add(new BasicNameValuePair(TAG_ID, String.valueOf(id)));
		params.add(new BasicNameValuePair(TAG_POINTS, String.valueOf(points)));
		params.add(new BasicNameValuePair(TAG_ANDROIDID, android_id));

		try {
			JSONObject json = jsonParser.makeHttpRequest(url_rating, "POST",
					params);
			Log.d("SendDataToServer vote() - JSON: ", json.toString());
		} catch (Exception e) {
			Log.e("SendDataToServer ", e.toString());
			return false;
		}

		return true;
	}

	public boolean canVote(int id, String android_id) {
		params.add(new BasicNameValuePair(TAG_ID, String.valueOf(id)));
		params.add(new BasicNameValuePair(TAG_ANDROIDID, android_id));

		try {
			JSONObject json = jsonParser.makeHttpRequest(url_get_can_vote,
					"POST", params);
			int success = json.getInt("success");
			if (success == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e("SendDataToServer canVote() ", e.toString());
			return false;
		}
	}
}
