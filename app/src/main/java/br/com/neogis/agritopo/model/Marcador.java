package br.com.neogis.agritopo.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Marcador extends Marker {

    private int mTextLabelBackgroundColor = Color.WHITE;

    public Marcador(MapView mapView) {
        super(mapView);
    }

    public Marcador(MapView mapView, final Context resourceProxy) {
        super(mapView, resourceProxy);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        // Mostrar somente a partir de um certo nível de zoom, para não lotar a tela
        if (mapView.getZoomLevel() > 15) {
            super.draw(canvas, mapView, shadow);
        }
    }

    public void setIcon(final Drawable icon) {
        if (ENABLE_TEXT_LABELS_WHEN_NO_IMAGE && icon == null && this.mTitle != null && this.mTitle.length() > 0) {
            // Transparência
            int opacity = (int) (0.85 * 255);
            mTextLabelBackgroundColor = Color.argb(opacity, 255, 255, 255);

            Paint background = new Paint();
            background.setColor(mTextLabelBackgroundColor);

            Paint p = new Paint();
            p.setTextSize(mTextLabelFontSize);
            p.setColor(mTextLabelForegroundColor);

            p.setAntiAlias(true);
            p.setTypeface(Typeface.DEFAULT_BOLD);
            p.setTextAlign(Paint.Align.LEFT);

//            int width=(int)(p.measureText(getTitle()) + 0.5f);
//            float baseline=(int)(-p.ascent() + 0.5f);
//            int height=(int) (baseline +p.descent() + 0.5f);
            int width = 0;
            float baseline = (int) (-p.ascent() + 0.5f);
            String[] lines = getTitle().split("\n");
            int height = (int) (baseline + p.descent() + 0.5f) * lines.length;
            for (String line : lines) {
                int line_width = (int) (p.measureText(line) + 0.5f);
                if (line_width > width)
                    width = line_width;
            }

            Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(image);
            c.drawPaint(background);
//                c.drawText(getTitle(),0,baseline,p);
            TextPaint mTextPaint = new TextPaint();
            mTextPaint.setTextSize(mTextLabelFontSize);
            mTextPaint.setColor(mTextLabelForegroundColor);
            StaticLayout mTextLayout = new StaticLayout(getTitle(), mTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            mTextLayout.draw(c);

            mIcon = new BitmapDrawable(resource, image);
        } else if (!ENABLE_TEXT_LABELS_WHEN_NO_IMAGE && icon != null) {
            this.mIcon = icon;
        } else if (this.mIcon != null) {
            mIcon = icon;
        } else
            //there's still an edge case here, title label no defined, icon is null and textlabel is enabled
            mIcon = mDefaultIcon;

    }
}
