package eu.mister3551.msr.screen.javascript;

public class Native {

    public native String getCookieValue(String name) /*-{
        return $wnd.getCookieValue(name);
    }-*/;

    public native void setCookieValue(String name, String value, int days) /*-{
        $wnd.setCookieValue(name, value, days);
    }-*/;
}
