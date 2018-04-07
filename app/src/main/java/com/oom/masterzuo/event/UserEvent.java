package com.oom.masterzuo.event;

/**
 * Created by xiaojunhuang on 16/3/30.
 */
public class UserEvent {
    public final static int UNLOGIN = 1;
    private int type;

    public UserEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
