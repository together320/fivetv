package com.brazvip.fivetv.keyboard.custom.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/* compiled from: MyApplication */
/* renamed from: e.b.a.e.c.a */
/* loaded from: classes.dex */
public class MyKeyItemEntity implements MultiItemEntity {

    /* renamed from: a */
    public static final int f13960a = 0;

    /* renamed from: b */
    public static final int f13961b = 1;

    /* renamed from: c */
    public static final int f13962c = 2;

    /* renamed from: d */
    public static final int f13963d = 3;

    /* renamed from: e */
    public static final int f13964e = 4;

    /* renamed from: f */
    public static final int f13965f = 5;

    /* renamed from: g */
    public static final int f13966g = 6;

    /* renamed from: h */
    public static final int f13967h = 7;

    /* renamed from: i */
    public static final int f13968i = 8;

    /* renamed from: j */
    public static final int f13969j = 9;

    /* renamed from: k */
    public static final int f13970k = 36;

    /* renamed from: l */
    public static final int f13971l = 54;

    /* renamed from: m */
    public static final int f13972m = 1;

    /* renamed from: n 13973 */
    public String text;

    /* renamed from: o 13974 */
    public int pos;

    /* renamed from: p 13975 */
    public int type;

    public MyKeyItemEntity(int type, int pos, String text) {
        this.type = type;
        this.pos = pos;
        this.text = text;
    }
    public MyKeyItemEntity(int type, int i2) {
        this.type = type;
        this.pos = i2;
    }

    /* renamed from: a */
    public void m2335a(int i) {
        this.pos = i;
    }

    /* renamed from: b */
    public String m2333b() {
        return this.text;
    }

    /* renamed from: c */
    public int m2332c() {
        return this.pos;
    }

    /* renamed from: a */
    public void m2334a(String text) {
        this.text = text;
    }

    @Override // com.p116c.p117a.p118a.p119a.p122c.MultiItemEntity
    /* renamed from: a */
    public int getItemType() {
        return type;
    }

}
