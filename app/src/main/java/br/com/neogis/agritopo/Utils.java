package br.com.neogis.agritopo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

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
        paint.setColor(Color.MAGENTA);
        paint.setStrokeWidth(espessuraLinha);
        paint.setStyle(Paint.Style.STROKE);
        int centroX = c.getWidth() / 2;
        int centroY = c.getHeight() / 2;

        c.drawLine(centroX - (tamanhoCruz / 2), centroY, centroX + (tamanhoCruz / 2), centroY, paint);
        c.drawLine(centroX, centroY - (tamanhoCruz / 2), centroX, centroY + (tamanhoCruz / 2), paint);
        c.drawCircle(centroX, centroY, diametroCirculo / 2, paint);
    }

}
