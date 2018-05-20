package br.com.neogis.agritopo.holder;

import android.app.Activity;
import android.graphics.Canvas;
import android.location.Location;
import android.view.WindowManager;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import br.com.neogis.agritopo.singleton.Configuration;

public class AdicionarElementoHolder extends Overlay
{
    protected MapView mapa;
    protected Activity activity;

    // Behaviors definidos por cada classe filha; governam que botões mostrar na interface
    public boolean aceitaSeguirGps = false;
    public boolean aceitaDesfazer = false;

    private boolean seguindoGps = false;

    public void seguirGPS() {
        deixarTelaAtiva();
        seguindoGps = true;
    }

    public void pararSeguirGPS() {
        naoDeixarTelaAtiva();
        seguindoGps = false;
    }
    public boolean seguindoGPS() {
        return seguindoGps;
    }
    public void registrarPontoGPS(Location location) {}

    private void deixarTelaAtiva() {
        if( Configuration.getInstance().DeixarTelaAtivaDuranteMapeamentoComGPS )
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void naoDeixarTelaAtiva() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
    }

    public void finalizar() { naoDeixarTelaAtiva(); }
    public void cancelar() { naoDeixarTelaAtiva(); }
    public void desfazer() {}
}
