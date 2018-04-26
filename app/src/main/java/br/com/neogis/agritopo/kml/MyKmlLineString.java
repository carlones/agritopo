package br.com.neogis.agritopo.kml;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.model.MyPolyline;

/**
 * Created by marci on 25/04/2018.
 */

public class MyKmlLineString extends KmlLineString {
    @Override
    public void applyDefaultStyling(Polyline lineStringOverlay, Style defaultStyle, KmlPlacemark kmlPlacemark, KmlDocument kmlDocument, MapView map) {
        super.applyDefaultStyling(lineStringOverlay, defaultStyle, kmlPlacemark, kmlDocument, map);

        if(kmlPlacemark.mName != null && !"".equals(kmlPlacemark.mName) || kmlPlacemark.mDescription != null && !"".equals(kmlPlacemark.mDescription) || lineStringOverlay.getSubDescription() != null && !"".equals(lineStringOverlay.getSubDescription())) {
            lineStringOverlay.setInfoWindow(new MyBasicInfoWindow(R.layout.bonuspack_bubble, map));
        }
    }

    @Override
    public Overlay buildOverlay(MapView map, Style defaultStyle, KmlFeature.Styler styler, KmlPlacemark kmlPlacemark, KmlDocument kmlDocument) {
        Polyline lineStringOverlay = new MyPolyline();
        lineStringOverlay.setGeodesic(true);
        lineStringOverlay.setPoints(this.mCoordinates);
        lineStringOverlay.setTitle(kmlPlacemark.mName);
        lineStringOverlay.setSnippet(kmlPlacemark.mDescription);
        lineStringOverlay.setSubDescription(kmlPlacemark.getExtendedDataAsText());
        if(styler != null) {
            styler.onLineString(lineStringOverlay, kmlPlacemark, this);
        } else {
            this.applyDefaultStyling(lineStringOverlay, defaultStyle, kmlPlacemark, kmlDocument, map);
        }

        return lineStringOverlay;
    }
}
