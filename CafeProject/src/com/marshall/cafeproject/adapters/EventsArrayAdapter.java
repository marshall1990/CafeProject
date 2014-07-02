package com.marshall.cafeproject.adapters;

import java.util.ArrayList;
import com.marshall.cafeproject.Cafe;
import com.marshall.cafeproject.DateConverter;
import com.marshall.cafeproject.Event;
import com.marshall.cafeproject.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventsArrayAdapter extends ArrayAdapter<Event> {

	private LayoutInflater inflater;
	private TextView tvName, tvDescription, tvStart, tvEnd, tvCafeName,
			tvAddress;

	public EventsArrayAdapter(Context context, ArrayList<Event> events) {
		super(context, R.layout.events_item, events);

		inflater = LayoutInflater.from(getContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event event = this.getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.events_item, null);

			getControls(convertView);

			tvName.setText(event.getName());
			tvDescription.setText(event.getDescription());
			tvStart.setText(DateConverter.dateToString(event.getStart()));
			tvEnd.setText(DateConverter.dateToString(event.getEnd()));

			Cafe cafe = event.getCafe();

			tvCafeName.setText(cafe.getName());
			tvAddress.setText(cafe.getStreet() + " " + cafe.getBuilding()
					+ ", " + cafe.getCity());
		}

		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	private void getControls(View view) {
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvDescription = (TextView) view.findViewById(R.id.tvDescription);
		tvStart = (TextView) view.findViewById(R.id.tvStart);
		tvEnd = (TextView) view.findViewById(R.id.tvEnd);
		tvAddress = (TextView) view.findViewById(R.id.tvAddress);
		tvCafeName = (TextView) view.findViewById(R.id.tvCafeName);
	}

}
