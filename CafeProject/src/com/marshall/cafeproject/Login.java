package com.marshall.cafeproject;

import com.marshall.cafeproject.interfaces.LoginListener;

public final class Login {
	
	private static LoginListener loginListener;

	public static LoginListener getLoginListener() {
		return loginListener;
	}

	public static void setLoginListener(LoginListener loginListener) {
		Login.loginListener = loginListener;
	}

}
