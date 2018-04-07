package com.oom.masterzuo.event;

/**
 * Created by xiaojunhuang on 16/3/30.
 */
public class UIEvent {
    public final static int TYPE_STARTACTIVITY = 1;
    public final static int TYPE_SHOWFRAGMENT = 2;


    private int type;
    private Object object;
    private Object tag;

    public UIEvent(int type, Object object, Object tag) {
        this.type = type;
        this.object = object;
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }

    public Object getTag() {
        return tag;
    }
}
