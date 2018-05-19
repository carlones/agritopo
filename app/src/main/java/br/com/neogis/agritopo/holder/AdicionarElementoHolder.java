package br.com.neogis.agritopo.holder;

import android.graphics.Canvas;
import android.location.Location;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

public class AdicionarElementoHolder extends Overlay
{
    // Behaviors definidos por cada classe filha; governam que botões mostrar na interface
    public boolean aceitaSeguirGps = false;
    public boolean aceitaDesfazer = false;

    private boolean seguindoGps = false;

    public void seguirGPS() {
        seguindoGps = true;
    }
    public void pararSeguirGPS() {
        seguindoGps = false;
    }
    public boolean seguindoGPS() {
        return seguindoGps;
    }
    public void registrarPontoGPS(Location location) {}

    @Override
    public void draw(Canvas c, MapView osmv, boolean shadow) {
    }

    public void finalizar() {}
    public void cancelar() {}
    public void desfazer() {}
}
