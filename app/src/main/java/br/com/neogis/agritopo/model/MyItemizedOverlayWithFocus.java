package br.com.neogis.agritopo.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;

import java.util.List;

/**
 * Created by carlo on 03/01/2018.
 */

class MyItemizedOverlayWithFocus<Item extends MyOverlayItem> extends ItemizedOverlayWithFocus<Item> {
    public MyItemizedOverlayWithFocus(Context pContext, List<Item> aList, OnItemGestureListener<Item> aOnItemTapListener) {
        super(pContext, aList, aOnItemTapListener);
    }

    public MyItemizedOverlayWithFocus(List<Item> aList, OnItemGestureListener<Item> aOnItemTapListener, Context pContext) {
        super(aList, aOnItemTapListener, pContext);
    }

    public MyItemizedOverlayWithFocus(List<Item> aList, Drawable pMarker, Drawable pMarkerFocused, int pFocusedBackgroundColor, OnItemGestureListener<Item> aOnItemTapListener, Context pContext) {
        super(aList, pMarker, pMarkerFocused, pFocusedBackgroundColor, aOnItemTapListener, pContext);
    }
}
