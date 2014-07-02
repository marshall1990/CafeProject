package com.marshall.cafeproject.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marshall.cafeproject.Login;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.User;
import com.marshall.cafeproject.database.DatabaseHandler;
import com.marshall.cafeproject.interfaces.LoginListener;

public class LoginFragment extends Fragment {

	private View loginFragmentView;
	private EditText etLogin, etPassword;
	private Button btnLogin;
	private LoginListener loginListener;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	public LoginFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loginFragmentView = inflater.inflate(R.layout.login_layout, container,
				false);
		return loginFragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getControls();
		
		loginListener = Login.getLoginListener();
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(getActivity());
				sharedPreferences = getActivity().getSharedPreferences("Settings", 0);
				editor = sharedPreferences.edit();
				
				if (db.authenticateUser(getData())) {
					editor.putString("login", etLogin.getText().toString());
					loginListener.onChangeState(true);
				} else {
					editor.putString("login", "");
					Toast.makeText(getActivity(), getResources().getString(R.string.incorrect_login_or_password), Toast.LENGTH_LONG).show();
				}
				editor.commit();
			}
		});
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	private void getControls() {
		etLogin = (EditText) loginFragmentView.findViewById(R.id.etLogin);
		etPassword = (EditText) loginFragmentView.findViewById(R.id.etPassword);
		btnLogin = (Button) loginFragmentView.findViewById(R.id.btnLogin);
	}
	
	private User getData() {
		User user = new User();
		user.setLogin(etLogin.getText().toString());
		user.setPassword(etPassword.getText().toString());
		
		return user;
	}
	
}
