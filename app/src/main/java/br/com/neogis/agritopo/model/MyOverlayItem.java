package br.com.neogis.agritopo.model;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by carlo on 03/01/2018.
 */

public class MyOverlayItem extends OverlayItem {
    private int elementoId;

    public MyOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint) {
        super(aTitle, aSnippet, aGeoPoint);
    }

    public MyOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint, int elementoId) {
        super(aTitle, aSnippet, aGeoPoint);
        this.elementoId = elementoId;
    }

    public MyOverlayItem(String aUid, String aTitle, String aDescription, IGeoPoint aGeoPoint, int elementoId) {
        super(aUid, aTitle, aDescription, aGeoPoint);
        this.elementoId = elementoId;
    }

    public int getElementoId() {
        return elementoId;
    }

    public void setElementoId(int elementoId) {
        this.elementoId = elementoId;
    }
}
