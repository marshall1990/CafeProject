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

public class AddCafeMenuFragment extends Fragment {

	private View myFragmentView;
	private Button btnAddProduct;
	private EditText etProduct, etQuantity, etPrice;
	private TextView tvProduct;
	private MenuCollections menuCollections;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.add_cafe_menu_layout,
				container, false);

		return myFragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuCollections = new MenuCollections();

		getControls();

		btnAddProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String product = etProduct.getText().toString();

				if (!product.equals("")) {
					menuCollections.setProduct(product, etQuantity.getText()
							.toString(), etPrice.getText().toString());
					clearProductsForm();
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
		btnAddProduct = (Button) myFragmentView.findViewById(R.id.bAddProduct);
		tvProduct = (TextView) myFragmentView.findViewById(R.id.tvProduct);
		etProduct = (EditText) myFragmentView.findViewById(R.id.etProduct);
		etQuantity = (EditText) myFragmentView.findViewById(R.id.etQuantity);
		etPrice = (EditText) myFragmentView.findViewById(R.id.etPrice);
	}

	private void clearProductsForm() {
		tvProduct.setText(getResources().getString(R.string.product) + " "
				+ menuCollections.getProducts().size() + 1);
		etProduct.setText("");
		etQuantity.setText("");
		etPrice.setText("");
	}
}
