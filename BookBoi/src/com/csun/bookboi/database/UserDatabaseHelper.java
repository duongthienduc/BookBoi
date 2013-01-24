package com.csun.bookboi.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.csun.bookboi.database.BookDatabaseHelper.SearchOption;
import com.csun.bookboi.utils.Pair;
import com.csun.bookboi.utils.PairFactory;

public class UserDatabaseHelper {
	public static final String USER_ID = "id";
	public static final String USER_USERNAME = "username";
	public static final String USER_PASSWORD = "password";
	
	public static final String ACTION_LOGIN = "http://bookboi.com/chan/login.php";
	public static final String ACTION_SIGNUP = "http://bookboi.com/chan/sign_up.php";
	
	public static Pair<String, List<NameValuePair>> buildSignUpQuery(String username, String password) {
		List<NameValuePair> p = new ArrayList<NameValuePair>();
		p.add(new BasicNameValuePair("username", username));
		p.add(new BasicNameValuePair("password", password));
		return PairFactory.makePair(ACTION_SIGNUP, p);
	}
}
