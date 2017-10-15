package br.com.neogis.agritopo;

import android.content.Context;
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
}
