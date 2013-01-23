package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.database.UserDatabaseHelper;
import com.csun.bookboi.types.User;

public class UserParser extends AbstractParser<User> {
	@Override
	public User parse(JSONObject json) throws JSONException {
		User obj = new User();
		if (json.has(UserDatabaseHelper.USER_ID)) {
			obj.setId(json.getInt(UserDatabaseHelper.USER_ID));
		}
		if (json.has(UserDatabaseHelper.USER_USERNAME)) {
			obj.setUsername(json.getString(UserDatabaseHelper.USER_USERNAME));
		}
		if (json.has(UserDatabaseHelper.USER_PASSWORD)) {
			obj.setPassword(json.getString(UserDatabaseHelper.USER_PASSWORD));
		}
		return obj;
	}
}
