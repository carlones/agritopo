package br.com.neogis.agritopo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

/**
 * Created by carlo on 14/10/2017.
 */

public final class Utils {

    /**
     * Mostra mensagem no formato torradeira
     *
     * @param context  indica o contexto
     * @param mensagem mensagem a exibir
     */
    public static void toast(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    /**
     * Retorna um Ponto com o tamanho da tela onde x é comprimento (width) e y é altura (height)
     *
     * @param context indica o contexto
     * @return Point onde x é width e y é height
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static void desenharMira(Canvas c) {
        int tamanhoCruz = 100;
        float diametroCirculo = 60.0f;
        float espessuraLinha = 6.0f;

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(espessuraLinha);
        paint.setStyle(Paint.Style.STROKE);
        int centroX = c.getWidth() / 2;
        int centroY = c.getHeight() / 2;

        c.drawLine(centroX - (tamanhoCruz / 2), centroY, centroX + (tamanhoCruz / 2), centroY, paint);
        c.drawLine(centroX, centroY - (tamanhoCruz / 2), centroX, centroY + (tamanhoCruz / 2), paint);
        c.drawCircle(centroX, centroY, diametroCirculo / 2, paint);
    }

    public static String getFormattedLocationInDegree(GeoPoint point) {
        double latitude = point.getLatitude() ;
        double longitude = point.getLongitude();
        StringBuilder builder = new StringBuilder();

        String letter = latitude < 0 ? " S" : " N";

        String latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS);
        String[] latitudeSplit = latitudeDegrees.split(":");
        builder.append(latitudeSplit[0]);
        builder.append("°");
        builder.append(String.format("%02d", Integer.parseInt(latitudeSplit[1])));
        builder.append("'");
        String[] seconds = latitudeSplit[2].split(",");
        builder.append(String.format("%02d", Integer.parseInt(seconds[0])));
//        builder.append(".");
//        builder.append(String.format("%04d", Integer.parseInt(seconds[1])));
        builder.append("\"");
        builder.append(letter);

        builder.append(" ");

        letter = longitude < 0 ? " W" : " E";

        String longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS);
        String[] longitudeSplit = longitudeDegrees.split(":");
        builder.append(longitudeSplit[0]);
        builder.append("°");
        builder.append(String.format("%02d", Integer.parseInt(longitudeSplit[1])));
        builder.append("'");
        seconds = longitudeSplit[2].split(",");
        builder.append(String.format("%02d", Integer.parseInt(seconds[0])));
//        builder.append(".");
//        builder.append(String.format("%04d", Integer.parseInt(seconds[1])));
        builder.append("\"");
        builder.append(letter);

        return builder.toString();
    }
}
