package com.csun.bookboi.types;

import java.util.ArrayList;
import java.util.Collection;

public class Group<T extends BookBoiType> extends ArrayList<T> implements BookBoiType {
    private static final long serialVersionUID = 1L;
    private String mType;
    
    public Group() {
        super();
    }
    
    public Group(Collection<T> collection) {
        super(collection);
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}