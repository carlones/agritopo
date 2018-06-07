package br.com.neogis.agritopo.kml;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import br.com.neogis.agritopo.R;

/**
 * Created by marci on 23/04/2018.
 */

public class MyBasicInfoWindow extends InfoWindow implements BetterLinkMovementMethod.OnLinkLongClickListener{
    public MyBasicInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);

        mView.setLongClickable(true);
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                close();
                return true;
            }
        });
    }

    @Override public void onOpen(Object item) {
        OverlayWithIW overlay = (OverlayWithIW)item;
        String title = overlay.getTitle();
        if (title == null)
            title = "";
        if (mView==null) {
            Log.w(IMapView.LOGTAG, "Error trapped, BasicInfoWindow.open, mView is null!");
            return;
        }
        TextView temp=((TextView)mView.findViewById(R.id.bubble_title));

        if (temp!=null) temp.setText(title);

        String snippet = overlay.getSnippet();
        if (snippet == null)
            snippet = "";
        Spanned snippetHtml = Html.fromHtml(snippet);
        TextView descriptionTextView = (TextView)mView.findViewById(R.id.bubble_description);
        descriptionTextView.setText(snippetHtml);

        BetterLinkMovementMethod movement = new BetterLinkMovementMethod();
        movement.setOnLinkLongClickListener(this);
        descriptionTextView.setMovementMethod(movement);

        //handle sub-description, hidding or showing the text view:
        TextView subDescText = (TextView)mView.findViewById(R.id.bubble_subdescription);
        String subDesc = overlay.getSubDescription();
        if (subDesc != null && !("".equals(subDesc))){
            subDescText.setText(Html.fromHtml(subDesc));
            subDescText.setVisibility(View.VISIBLE);
        } else {
            subDescText.setVisibility(View.GONE);
        }

    }

    @Override public void onClose() {
        //by default, do nothing
    }

    @Override
    public boolean onLongClick(TextView textView, String url) {
        close();
        return true;
    }

    public void setMap(MapView mapView){
        this.mMapView = mapView;
    }
}
