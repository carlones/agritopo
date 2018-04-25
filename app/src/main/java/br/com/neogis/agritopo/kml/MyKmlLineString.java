package br.com.neogis.agritopo.kml;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import br.com.neogis.agritopo.R;

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
}
