package br.com.neogis.agritopo.kml;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import br.com.neogis.agritopo.R;

/**
 * Created by marci on 23/04/2018.
 */

public class MyKmlPolygon extends KmlPolygon{
    @Override
    public void applyDefaultStyling(Polygon polygonOverlay, Style defaultStyle, KmlPlacemark kmlPlacemark, KmlDocument kmlDocument, MapView map) {
        super.applyDefaultStyling(polygonOverlay, defaultStyle, kmlPlacemark, kmlDocument, map);

        if (kmlPlacemark.mName != null && !"".equals(kmlPlacemark.mName) || kmlPlacemark.mDescription != null && !"".equals(kmlPlacemark.mDescription) || polygonOverlay.getSubDescription() != null && !"".equals(polygonOverlay.getSubDescription())) {
            polygonOverlay.setInfoWindow(new MyBasicInfoWindow(R.layout.bonuspack_bubble, map));
        }
    }
}
