package com.marshall.cafeproject.fragments;

import com.marshall.cafeproject.MenuCollections;
import com.marshall.cafeproject.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCafeTablesFragment extends Fragment {

	private View myFragmentView;
	private Button btnAddTable;
	private EditText etSeats;
	private TextView tvTable;
	private MenuCollections menuCollections;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.add_cafe_tables_layout,
				container, false);

		return myFragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuCollections = new MenuCollections();

		getControls();

		btnAddTable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String seats = etSeats.getText().toString();

				if (!seats.equals("") && seats.length() < 10) {
					menuCollections.setTable(
							menuCollections.getTables().size() + 1,
							Integer.parseInt(seats));
					clearTablesForm();
				} else {
					Toast.makeText(
							getActivity(),
							getResources().getString(R.string.enter_valid_data),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void getControls() {
		btnAddTable = (Button) myFragmentView.findViewById(R.id.btnAddTable);
		tvTable = (TextView) myFragmentView.findViewById(R.id.tvTable);
		etSeats = (EditText) myFragmentView.findViewById(R.id.etSeats);
	}

	private void clearTablesForm() {
		tvTable.setText(getResources().getString(R.string.table) + " "
				+ menuCollections.getTables().size() + 1);
		etSeats.setText("");
	}
}
