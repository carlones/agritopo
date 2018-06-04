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
    private Location lastLocation = null;

    public void seguirGPS() {
        deixarTelaAtiva();
        seguindoGps = true;
    }

    public void pararSeguirGPS() {
        naoDeixarTelaAtiva();
        seguindoGps = false;
        lastLocation = null;
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

    public void locationChanged(Location location) {
        if( !seguindoGps ) return;

        boolean ultrapassouDistanciaLimite = lastLocation != null && location.distanceTo(lastLocation) >= 10.0; // metros
        long timeDiff = lastLocation == null ? 0 : location.getTime() - lastLocation.getTime();
        boolean ultrapassouTempoLimite = lastLocation != null && timeDiff >= (5 * 1000); // segundos

        if( lastLocation == null || ultrapassouDistanciaLimite || ultrapassouTempoLimite ) {
            lastLocation = location;
            registrarPontoGPS(location);
        }
    }
}
