package br.com.neogis.agritopo.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.List;

/**
 * Created by carlo on 03/01/2018.
 */

class MyItemizedIconOverlay<Item extends MyOverlayItem> extends ItemizedIconOverlay<Item> {
    public MyItemizedIconOverlay(List pList, Drawable pDefaultMarker, OnItemGestureListener pOnItemGestureListener, Context pContext) {
        super(pList, pDefaultMarker, pOnItemGestureListener, pContext);
    }

    public MyItemizedIconOverlay(List pList, OnItemGestureListener pOnItemGestureListener, Context pContext) {
        super(pList, pOnItemGestureListener, pContext);
    }

    public MyItemizedIconOverlay(Context pContext, List pList, OnItemGestureListener pOnItemGestureListener) {
        super(pContext, pList, pOnItemGestureListener);
    }
}
