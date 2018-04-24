package br.com.neogis.agritopo.singleton;

import android.support.annotation.NonNull;

import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.ArrayList;

import br.com.neogis.agritopo.kml.MyKmlDocument;
import br.com.neogis.agritopo.model.ArvoreCamada;
import br.com.neogis.agritopo.utils.Utils;

public class CamadaHolder {
    static private CamadaHolder _instance;

    public ArrayList<ArvoreCamada> camadas;
    public boolean carregando = true;

    private CamadaHolder() {
        camadas = new ArrayList<>();
    }

    static public CamadaHolder getInstance() {
        if (_instance == null)
            _instance = new CamadaHolder();
        return _instance;
    }

    public boolean arquivoExiste(File arquivo) {
        boolean resultado = false;
        String nome = getFileName(arquivo);
        for (ArvoreCamada camada : camadas) {
            if (camada.nome.equals(nome)) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    public void adicionarArquivo(File arquivo, MapView map) {
        MyKmlDocument kmlDocument = new MyKmlDocument();
        if (!kmlDocument.parseKMLFile(arquivo)) {
            Utils.info("Erro durante parse do arquivo KML");
        } else {
            ArvoreCamada raiz = new ArvoreCamada(getFileName(arquivo), ArvoreCamada.RAIZ);
            mapearConteudoKML(kmlDocument.mKmlRoot, raiz, map, kmlDocument);
            raiz.indice = camadas.size();
            camadas.add(raiz);
        }
    }

    @NonNull
    private String getFileName(File arquivo) {
        String nome = arquivo.getName();
        int pos_ponto_extensao = nome.lastIndexOf(".");
        if (pos_ponto_extensao > 0)
            nome = nome.substring(0, pos_ponto_extensao);
        return nome;
    }

    private void mapearConteudoKML(KmlFolder pasta, ArvoreCamada pai, MapView map, MyKmlDocument kmlDocument) {
        for (KmlFeature f : pasta.mItems) {
            if (f.getClass() == KmlFolder.class) {
                ArvoreCamada ac = new ArvoreCamada(f.mName, ArvoreCamada.PASTA);
                pai.adicionarFilho(ac);
                mapearConteudoKML((KmlFolder) f, ac, map, kmlDocument);
            } else if (f.getClass() == KmlPlacemark.class) {
                ArvoreCamada ac = new ArvoreCamada(f.mName, ArvoreCamada.GEOMETRIA);
                ac.overlay = f.buildOverlay(map, null, null, kmlDocument);
                pai.adicionarFilho(ac);
            }
        }

    }

    public void limparSelecionados() {
        for (ArvoreCamada ac : camadas) {
            marcarDesmarcarSelecaoArvore(ac, false);
        }
    }

    public void marcarDesmarcarSelecaoArvore(ArvoreCamada nodo, Boolean selecionado) {
        nodo.selecionado = selecionado;
        for (ArvoreCamada filho : nodo.filhos) {
            marcarDesmarcarSelecaoArvore(filho, selecionado);
        }
    }

    public void exibirCamadasSelecionadasNoMapa(MapView map) {
        for (ArvoreCamada ac : camadas) {
            alternarExibicaoCamada(ac, map);
        }
    }

    public void exibirCamadasSelecionadasNoMapa(ArvoreCamada camada, MapView map) {
        alternarExibicaoCamada(camada, map);
    }

    private void alternarExibicaoCamada(ArvoreCamada nodo, MapView map) {
        if (nodo.overlay != null) {
            if (nodo.selecionado) {
                if (!map.getOverlays().contains(nodo.overlay))
                    map.getOverlays().add(nodo.overlay);
            } else {
                if (map.getOverlays().contains(nodo.overlay))
                    map.getOverlays().remove(nodo.overlay);
            }
        }
        for (ArvoreCamada filho : nodo.filhos) {
            alternarExibicaoCamada(filho, map);
        }
    }

    public void limparCamadas() {
        camadas.clear();
    }
}
