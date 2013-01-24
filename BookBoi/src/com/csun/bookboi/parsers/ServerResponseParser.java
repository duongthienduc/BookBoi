package com.csun.bookboi.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.types.ServerResponse;

public class ServerResponseParser extends AbstractParser<ServerResponse> {
	public static final String SERVER_RESPONSE_COLUMN = "result";
	@Override
	public ServerResponse parse(JSONObject json) throws JSONException {
		ServerResponse obj = new ServerResponse();
		if (json.has(SERVER_RESPONSE_COLUMN)) {
			obj.setResult(json.getBoolean(SERVER_RESPONSE_COLUMN));
		}
		return obj;
	}
}
