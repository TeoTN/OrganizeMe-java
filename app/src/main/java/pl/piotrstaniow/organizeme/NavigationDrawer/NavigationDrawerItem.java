package pl.piotrstaniow.organizeme.NavigationDrawer;

import android.graphics.drawable.Drawable;

/**
 * Created by piotr on 11.09.15.
 */
public class NavigationDrawerItem {
    public Integer icon;
    public CharSequence text;
    public long value;
    private boolean hasValueState;

    public NavigationDrawerItem(Integer icon, CharSequence text) {
        this.text = text;
        this.icon = icon;
        this.hasValueState = false;
    }

    public NavigationDrawerItem(Integer icon, CharSequence text, Integer value) {
        this.text = text;
        this.icon = icon;
        this.value = value;
        this.hasValueState = true;
    }

    public boolean hasValue() {
        return hasValueState;
    }
}
