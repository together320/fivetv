package com.brazvip.fivetv.beans.vod;

import java.io.Serializable;


public class VodGroupL2 implements Serializable, Comparable<VodGroupL2> {
    public String _id;
    public String name;
    public boolean restricted;
    public boolean restrictedAccess;

    /* JADX DEBUG: Method merged with bridge method */
    @Override // java.lang.Comparable
    public int compareTo(VodGroupL2 vodGroupL2) {
        if (vodGroupL2 == null) {
            return 1;
        }
        return this._id.compareTo(vodGroupL2._id);
    }

    public String getId() {
        return this._id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRestricted() {
        if (this.restricted) {
            return true;
        }
        return this.restrictedAccess;
    }

    public void setId(String str) {
        this._id = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setRestricted(boolean z) {
        this.restricted = z;
    }
}
