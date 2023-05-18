package com.brazvip.fivetv.beans;

import java.util.List;

public class Group {
    public int id;
    public String name;
    public int type;
    public boolean restrictedAccess;

    public Group() {
        id = 0;
        name = "";
        type = 0;
        restrictedAccess = false;
    }
}
