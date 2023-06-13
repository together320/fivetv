package com.phoenix.libtv.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.SparseArray;

import com.phoenix.libtv.ILibTvService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class LibTvService extends Service {
    public static final String ACTION = "com.phoenix.libtv.service.LibTvService";
    private static final String TAG = "LibTvService";
    private ILibTvService.Stub impl;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.impl;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.impl = new LibTvServerImpl();
    }

    @Override // android.app.Service
    public void onDestroy() {
    }

    public int onStartCommand(final Intent intent, int n, final int n2) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("LibTvService", "LibTvService Channel", NotificationManager.IMPORTANCE_LOW);
//            this.getSystemService(NotificationManager.class).createNotificationChannel(channel);
//        }
//        try {
//            final PendingIntent activity = PendingIntent.getActivity((Context)this, 0, new Intent(), 0);
//            final ArrayList list = new ArrayList();
//            final ArrayList list2 = new ArrayList();
//            final ArrayList list3 = new ArrayList();
//            final Notification notification = new Notification();
//            notification.when = System.currentTimeMillis();
//            notification.audioStreamType = -1;
//            final ArrayList list4 = new ArrayList();
//            final StringBuilder sb = new StringBuilder();
//            sb.append(((Context)this).getString(2131820602));
//            sb.append(" running");
//            final String string = sb.toString();
//            CharSequence subSequence;
//            if (string == null) {
//                subSequence = string;
//            }
//            else {
//                subSequence = string;
//                if (string.length() > 5120) {
//                    subSequence = string.subSequence(0, 5120);
//                }
//            }
//            notification.icon = 2131623949;
//            final ArrayList list5 = new ArrayList();
//            final Bundle extras = new Bundle();
//            Notification.Builder notification$Builder;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notification$Builder = new Notification.Builder((Context)this, "LibTvService");
//            }
//            else {
//                notification$Builder = new Notification.Builder((Context)this);
//            }
//            notification$Builder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, (RemoteViews)null).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(subSequence).setContentText((CharSequence)null).setContentInfo((CharSequence)null).setContentIntent(activity).setDeleteIntent(notification.deleteIntent).setFullScreenIntent((PendingIntent)null, (notification.flags & 0x80) != 0x0).setLargeIcon((Bitmap)null).setNumber(0).setProgress(0, 0, false);
//            if (n < 21) {
//                notification$Builder.setSound(notification.sound, notification.audioStreamType);
//            }
//            notification$Builder.setSubText((CharSequence)null).setUsesChronometer(false).setPriority(0);
//            final Iterator iterator = list.iterator();
//            if (iterator.hasNext()) {
//                d.r(iterator.next());
//                if (n < 20) {
//                    final Object a = o.a;
//                    throw null;
//                }
//                throw null;
//            }
//            else {
//                notification$Builder.setShowWhen(true);
//                if (n < 21) {
//                    final List f = a5.f.f(a5.f.g(list2), list4);
//                    if (f != null) {
//                        final ArrayList list6 = (ArrayList)f;
//                        if (!list6.isEmpty()) {
//                            extras.putStringArray("android.people", (String[])list6.toArray(new String[list6.size()]));
//                        }
//                    }
//                }
//                if (n >= 20) {
//                    notification$Builder.setLocalOnly(false).setGroup((String)null).setGroupSummary(false).setSortKey((String)null);
//                }
//                Bundle extras2 = null;
//                Label_0880: {
//                    if (n >= 21) {
//                        notification$Builder.setCategory((String)null).setColor(0).setVisibility(0).setPublicVersion((Notification)null).setSound(notification.sound, notification.audioAttributes);
//                        List f2 = list4;
//                        if (n < 28) {
//                            f2 = f.f(f.g(list2), list4);
//                        }
//                        if (f2 != null && !f2.isEmpty()) {
//                            final Iterator iterator2 = f2.iterator();
//                            while (iterator2.hasNext()) {
//                                notification$Builder.addPerson((String)iterator2.next());
//                            }
//                        }
//                        if (list3.size() > 0) {
//                            final Bundle bundle = new Bundle();
//                            Bundle bundle2;
//                            if ((bundle2 = bundle.getBundle("android.car.EXTENSIONS")) == null) {
//                                bundle2 = new Bundle();
//                            }
//                            final Bundle bundle3 = new Bundle(bundle2);
//                            final Bundle bundle4 = new Bundle();
//                            if (list3.size() <= 0) {
//                                bundle2.putBundle("invisible_actions", bundle4);
//                                bundle3.putBundle("invisible_actions", bundle4);
//                                bundle.putBundle("android.car.EXTENSIONS", bundle2);
//                                extras.putBundle("android.car.EXTENSIONS", bundle3);
//                                extras2 = bundle;
//                                break Label_0880;
//                            }
//                            Integer.toString(0);
//                            d.r(list3.get(0));
//                            final Object a2 = o.a;
//                            new Bundle();
//                            throw null;
//                        }
//                    }
//                    extras2 = null;
//                }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    notification$Builder.setExtras(extras2).setRemoteInputHistory((CharSequence[])null);
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    notification$Builder.setBadgeIconType(0).setSettingsText((CharSequence)null).setShortcutId((String)null).setTimeoutAfter(0L).setGroupAlertBehavior(0);
//                    if (!TextUtils.isEmpty((CharSequence)"LibTvService")) {
//                        notification$Builder.setSound((Uri)null).setDefaults(0).setLights(0, 0, 0).setVibrate((long[])null);
//                    }
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    final Iterator iterator3 = list2.iterator();
//                    if (iterator3.hasNext()) {
//                        d.r(iterator3.next());
//                        throw null;
//                    }
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    n.k(notification$Builder, true);
//                    n.j(notification$Builder);
//                }
//                if (Build.VERSION.SDK_INT < 26) {
//                    if (Build.VERSION.SDK_INT < 24) {
//                        if (Build.VERSION.SDK_INT < 21) {
//                            if (Build.VERSION.SDK_INT < 20) {
//                                final SparseArray a3 = o.a(list5);
//                                if (a3 != null) {
//                                    extras.putSparseParcelableArray("android.support.actionExtras", a3);
//                                }
//                            }
//                        }
//                        notification$Builder.setExtras(extras);
//                    }
//                }
//                this.startForeground(1, notification$Builder.build());
//            }
//        }
//        catch (final Exception ex) {
//            ex.getMessage();
//        }
        return Service.START_STICKY;
    }
}
