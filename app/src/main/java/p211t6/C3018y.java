package p211t6;

public final class C3018y extends AbstractC2955c1 {

    /* renamed from: a */
    public final String f10301a;

    /* renamed from: b */
    public final String f10302b;

    public C3018y(String str, String str2) {
        this.f10301a = str;
        this.f10302b = str2;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractC2955c1)) {
            return false;
        }
        AbstractC2955c1 abstractC2955c1 = (AbstractC2955c1) obj;
        if (this.f10301a.equals(((C3018y) abstractC2955c1).f10301a) && this.f10302b.equals(((C3018y) abstractC2955c1).f10302b)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return ((this.f10301a.hashCode() ^ 1000003) * 1000003) ^ this.f10302b.hashCode();
    }

//    public final String toString() {
//        StringBuilder m6212l = AbstractC0136d.m6212l("CustomAttribute{key=");
//        m6212l.append(this.f10301a);
//        m6212l.append(", value=");
//        return AbstractC0136d.m6213k(m6212l, this.f10302b, "}");
//    }
}
