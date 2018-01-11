package br.com.neogis.agritopo.holder;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.ArrayList;

import br.com.neogis.agritopo.dao.Utils;
import br.com.neogis.agritopo.model.ArvoreCamada;

public class CamadaHolder {
    static private CamadaHolder _instance;

    public ArrayList<ArvoreCamada> camadas;

    static public CamadaHolder getInstance() {
        if( _instance == null )
            _instance = new CamadaHolder();
        return _instance;
    }

    private CamadaHolder() {
        camadas = new ArrayList<>();
    }

    public void adicionarArquivo(File arquivo, MapView map) {
        KmlDocument kmlDocument = new KmlDocument();
        if (!kmlDocument.parseKMLFile(arquivo)) {
            Utils.info("Erro durante parse do arquivo KML");
        }
        else {
            ArvoreCamada raiz = new ArvoreCamada(arquivo.getName(), ArvoreCamada.RAIZ);
            mapearConteudoKML(kmlDocument.mKmlRoot, raiz, map, kmlDocument);
            camadas.add(raiz);
        }
    }

    private void mapearConteudoKML(KmlFolder pasta, ArvoreCamada pai, MapView map, KmlDocument kmlDocument) {
        for (KmlFeature f : pasta.mItems) {
            if (f.getClass() == KmlFolder.class) {
                ArvoreCamada ac = new ArvoreCamada(f.mName, ArvoreCamada.PASTA);
                pai.adicionarFilho(ac);
                mapearConteudoKML((KmlFolder) f, ac, map, kmlDocument);
            }
            else if (f.getClass() == KmlPlacemark.class) {
                ArvoreCamada ac = new ArvoreCamada(f.mName, ArvoreCamada.GEOMETRIA);
                ac.overlay = f.buildOverlay(map, null, null, kmlDocument);
                pai.adicionarFilho(ac);
            }
        }

    }

    public void limparSelecionados() {
        for(ArvoreCamada ac: camadas) {
            desmarcarSelecaoArvore(ac);
        }
    }

    private void desmarcarSelecaoArvore(ArvoreCamada nodo) {
        nodo.selecionado = false;
        for(ArvoreCamada filho: nodo.filhos) {
            desmarcarSelecaoArvore(filho);
        }
    }

    public void exibirCamadasSelecionadasNoMapa(MapView map) {
        for(ArvoreCamada ac: camadas) {
            alternarExibicaoCamada(ac, map);
        }
    }

    private void alternarExibicaoCamada(ArvoreCamada nodo, MapView map) {
        if( nodo.overlay != null ) {
            if( nodo.selecionado ) {
                if( !map.getOverlays().contains(nodo.overlay) )
                    map.getOverlays().add(nodo.overlay);
           }
            else {
                if( map.getOverlays().contains(nodo.overlay) )
                    map.getOverlays().remove(nodo.overlay);
            }
        }
        for(ArvoreCamada filho: nodo.filhos) {
            alternarExibicaoCamada(filho, map);
        }
    }

    public void limparCamadas() {
        camadas.clear();
    }
}
