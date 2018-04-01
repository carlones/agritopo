package br.com.neogis.agritopo.runnable;

import org.osmdroid.views.MapView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import br.com.neogis.agritopo.singleton.CamadaHolder;

/**
 * Created by marci on 30/03/2018.
 */

public class CamadasLoader implements Runnable {
    private MapView mapView;
    public CamadasLoader(MapView map){
        mapView = map;
    }

    @Override
    public void run() {
        File pasta_camadas = new File(br.com.neogis.agritopo.singleton.Configuration.getInstance().DiretorioLeituraArquivos);
        FilenameFilter filtro = new FilenameFilter() {
            String[] extensoesValidas = {"kml"};

            @Override
            public boolean accept(File dir, String name) {
                String extensao = name.substring(name.lastIndexOf(".") + 1);
                extensao = extensao.toLowerCase();
                return Arrays.asList(extensoesValidas).contains(extensao);
            }
        };
        File[] listaArquivosCamadas = pasta_camadas.listFiles(filtro);
        //CamadaHolder.getInstance().limparCamadas();
        if (listaArquivosCamadas != null) {
            for (File arquivo : listaArquivosCamadas) {
                if (!CamadaHolder.getInstance().arquivoExiste(arquivo))
                    CamadaHolder.getInstance().adicionarArquivo(arquivo, mapView);
            }
        }
    }
}
