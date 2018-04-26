package br.com.neogis.agritopo.kml;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.model.MyMarker;

/**
 * Created by marci on 24/04/2018.
 */

public class MyKmlPoint extends KmlPoint {
    @Override
    public Overlay buildOverlay(MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark, KmlDocument kmlDocument) {
        Marker marker = new MyMarker(map);
        marker.setTitle(kmlPlacemark.mName);
        marker.setSnippet(kmlPlacemark.mDescription);
        marker.setInfoWindow(new MyBasicInfoWindow(R.layout.bonuspack_bubble, map));
        marker.setSubDescription(kmlPlacemark.getExtendedDataAsText());
        marker.setPosition(this.getPosition());
        marker.setRelatedObject(this);
        if(styler == null) {
            this.applyDefaultStyling(marker, defaultStyle, kmlPlacemark, kmlDocument, map);
        } else {
            styler.onPoint(marker, kmlPlacemark, this);
        }

        return marker;
    }
}
