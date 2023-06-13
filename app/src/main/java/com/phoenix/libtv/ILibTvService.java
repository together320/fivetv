package com.phoenix.libtv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ILibTvService extends IInterface {

    /* loaded from: classes.dex */
    public static class Default implements ILibTvService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public boolean doLogin() {
            return false;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getCacheChannels(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getCacheDashboard() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getCacheEpg(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getCacheVod(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getCacheVodGroups() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getCacheVodSubgroups(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getLoginData() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getLoginPrefix() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getServerDate() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String getUserPass(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public boolean profileAuth(String str, String str2) {
            return false;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String profileCreate(String str, String str2, boolean z, String str3) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String profileDelete(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String profileUpdate(String str) {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public String profilesGet() {
            return null;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public boolean setAppLicense(String str) {
            return false;
        }

        @Override // com.phoenix.libtv.ILibTvService
        public void setAuthData(String str, String str2) {
        }

        @Override // com.phoenix.libtv.ILibTvService
        public void setConfig(String str) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ILibTvService {
        private static final String DESCRIPTOR = "com.phoenix.libtv.ILibTvService";
        public static final int TRANSACTION_doLogin = 1;
        public static final int TRANSACTION_getCacheChannels = 2;
        public static final int TRANSACTION_getCacheDashboard = 3;
        public static final int TRANSACTION_getCacheEpg = 4;
        public static final int TRANSACTION_getCacheVod = 5;
        public static final int TRANSACTION_getCacheVodGroups = 6;
        public static final int TRANSACTION_getCacheVodSubgroups = 7;
        public static final int TRANSACTION_getLoginData = 8;
        public static final int TRANSACTION_getLoginPrefix = 9;
        public static final int TRANSACTION_getServerDate = 10;
        public static final int TRANSACTION_getUserPass = 11;
        public static final int TRANSACTION_profileAuth = 12;
        public static final int TRANSACTION_profileCreate = 13;
        public static final int TRANSACTION_profileDelete = 14;
        public static final int TRANSACTION_profileUpdate = 15;
        public static final int TRANSACTION_profilesGet = 16;
        public static final int TRANSACTION_setAppLicense = 17;
        public static final int TRANSACTION_setAuthData = 18;
        public static final int TRANSACTION_setConfig = 19;

        /* loaded from: classes.dex */
        public static class Proxy implements ILibTvService {
            public static ILibTvService sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.phoenix.libtv.ILibTvService
            public boolean doLogin() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readInt() != 0;
                    }
                    return Stub.getDefaultImpl().doLogin();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getCacheChannels(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getCacheChannels(str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getCacheDashboard() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getCacheDashboard();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getCacheEpg(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getCacheEpg(str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getCacheVod(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(5, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getCacheVod(str);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getCacheVodGroups() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getCacheVodGroups();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getCacheVodSubgroups(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(7, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getCacheVodSubgroups(str);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getLoginData() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getLoginData();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getLoginPrefix() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getLoginPrefix();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getServerDate() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getServerDate();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String getUserPass(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(11, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().getUserPass(str);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public boolean profileAuth(String str, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (this.mRemote.transact(12, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readInt() != 0;
                    }
                    return Stub.getDefaultImpl().profileAuth(str, str2);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String profileCreate(String str, String str2, boolean z, String str3) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeString(str3);
                    if (this.mRemote.transact(13, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().profileCreate(str, str2, z, str3);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String profileDelete(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(14, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().profileDelete(str);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String profileUpdate(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(15, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().profileUpdate(str);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public String profilesGet() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readString();
                    }
                    return Stub.getDefaultImpl().profilesGet();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public boolean setAppLicense(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(17, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        return obtain2.readInt() != 0;
                    }
                    return Stub.getDefaultImpl().setAppLicense(str);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public void setAuthData(String str, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (this.mRemote.transact(18, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().setAuthData(str, str2);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.phoenix.libtv.ILibTvService
            public void setConfig(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(19, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                    } else {
                        Stub.getDefaultImpl().setConfig(str);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILibTvService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ILibTvService)) ? new Proxy(iBinder) : (ILibTvService) queryLocalInterface;
        }

        public static ILibTvService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ILibTvService iLibTvService) {
            if (Proxy.sDefaultImpl == null) {
                if (iLibTvService != null) {
                    Proxy.sDefaultImpl = iLibTvService;
                    return true;
                }
                return false;
            }
            throw new IllegalStateException("setDefaultImpl() called twice");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean doLogin = doLogin();
                    parcel2.writeNoException();
                    parcel2.writeInt(doLogin ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    String cacheChannels = getCacheChannels(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(cacheChannels);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    String cacheDashboard = getCacheDashboard();
                    parcel2.writeNoException();
                    parcel2.writeString(cacheDashboard);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    String cacheEpg = getCacheEpg(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(cacheEpg);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    String cacheVod = getCacheVod(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(cacheVod);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    String cacheVodGroups = getCacheVodGroups();
                    parcel2.writeNoException();
                    parcel2.writeString(cacheVodGroups);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    String cacheVodSubgroups = getCacheVodSubgroups(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(cacheVodSubgroups);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    String loginData = getLoginData();
                    parcel2.writeNoException();
                    parcel2.writeString(loginData);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    String loginPrefix = getLoginPrefix();
                    parcel2.writeNoException();
                    parcel2.writeString(loginPrefix);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    String serverDate = getServerDate();
                    parcel2.writeNoException();
                    parcel2.writeString(serverDate);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    String userPass = getUserPass(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(userPass);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean profileAuth = profileAuth(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(profileAuth ? 1 : 0);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    String profileCreate = profileCreate(parcel.readString(), parcel.readString(), parcel.readInt() != 0, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(profileCreate);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    String profileDelete = profileDelete(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(profileDelete);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    String profileUpdate = profileUpdate(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(profileUpdate);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    String profilesGet = profilesGet();
                    parcel2.writeNoException();
                    parcel2.writeString(profilesGet);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean appLicense = setAppLicense(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(appLicense ? 1 : 0);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    setAuthData(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    setConfig(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean doLogin();

    String getCacheChannels(String str) throws RemoteException;

    String getCacheDashboard();

    String getCacheEpg(String str) throws RemoteException;

    String getCacheVod(String str);

    String getCacheVodGroups();

    String getCacheVodSubgroups(String str);

    String getLoginData();

    String getLoginPrefix();

    String getServerDate();

    String getUserPass(String str);

    boolean profileAuth(String str, String str2);

    String profileCreate(String str, String str2, boolean z, String str3);

    String profileDelete(String str);

    String profileUpdate(String str);

    String profilesGet();

    boolean setAppLicense(String str);

    void setAuthData(String str, String str2);

    void setConfig(String str);
}
