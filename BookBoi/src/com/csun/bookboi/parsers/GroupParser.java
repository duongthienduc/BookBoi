package com.csun.bookboi.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csun.bookboi.types.Book;
import com.csun.bookboi.types.BookBoiType;
import com.csun.bookboi.types.Group;

import java.util.Iterator;
import java.util.logging.Level;

/**
 * Reference:
 * http://www.json.org/javadoc/org/json/JSONObject.html
 * http://www.json.org/javadoc/org/json/JSONArray.html
 */
public class GroupParser extends AbstractParser<Group> {

    private Parser<? extends BookBoiType> mSubParser;

    public GroupParser(Parser<? extends BookBoiType> subParser) {
        mSubParser = subParser;
    }
    
    /**
     * When we encounter a JSONObject in a GroupParser, we expect one attribute
     * named 'type', and then another JSONArray attribute.
     */
    public Group<BookBoiType> parse(JSONObject json) throws JSONException {
        Group<BookBoiType> group = new Group<BookBoiType>();
        Iterator<String> it = (Iterator<String>) json.keys();
        while (it.hasNext()) {
            String key = it.next();
            if (key.equals("type")) {
                group.setType(json.getString(key));
            } else {
                Object obj = json.get(key);
                if (obj instanceof JSONArray) {  
                    parse(group, (JSONArray)obj);
                } else {
                    throw new JSONException("Could not parse data.");
                }
            }
        }
        return group;
    }
    
    /**
     * Here we are getting a straight JSONArray and do not expect the 'type' attribute.
     */
    @Override
    public Group parse(JSONArray array) throws JSONException {
        Group<BookBoiType> group = new Group<BookBoiType>();
        parse(group, array);
        return group;
    }
    
    private void parse(Group group, JSONArray array) throws JSONException {
        for (int i = 0, m = array.length(); i < m; i++) {
            Object element = array.get(i);
            BookBoiType item = null;
            if (element instanceof JSONArray) {
                item = mSubParser.parse((JSONArray)element);
            } else {
                item = mSubParser.parse((JSONObject)element);
            }
            group.add(item);
        }
    }
}
