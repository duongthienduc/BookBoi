package com.csun.bookboi.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.types.BookBoiType;
import com.csun.bookboi.types.Group;

public interface Parser<T extends BookBoiType> {
    public abstract T parse(JSONObject json) throws JSONException;
    public Group parse(JSONArray array) throws JSONException;
}