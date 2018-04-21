package br.com.neogis.agritopo.runnable;

import android.os.AsyncTask;

import org.osmdroid.views.MapView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import br.com.neogis.agritopo.singleton.CamadaHolder;
import br.com.neogis.agritopo.utils.AsyncResponse;
import br.com.neogis.agritopo.utils.Utils;

/**
 * Created by marci on 30/03/2018.
 */

public class CamadasLoader {
    private MapView mapView;
    public CamadasLoader(MapView map){
        mapView = map;
    }

    public void carregar(final AsyncResponse callback) {
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

        CamadaHolder.getInstance().carregando = true;
        if (listaArquivosCamadas != null) {
            CarregarEmBackground carregador = new CarregarEmBackground(listaArquivosCamadas, mapView, new AsyncResponse() {
                @Override
                public void processFinish() {
//                    Utils.info("carregamento kml terminou");
                    CamadaHolder.getInstance().carregando = false;
                    if( callback != null )
                        callback.processFinish();
                }
            });
            carregador.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            CamadaHolder.getInstance().carregando = false;
        }
    }

    private class CarregarEmBackground extends AsyncTask<Void, Void, Void>
    {
        AsyncResponse callback;
        File[] arquivos;
        MapView mapa;

        CarregarEmBackground(File[] arquivos, MapView mapa, AsyncResponse callback) {
            this.arquivos = arquivos;
            this.mapa = mapa;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (File arquivo : arquivos) {
//                Utils.info("Kml carregando: " + arquivo.toString());
                if (!CamadaHolder.getInstance().arquivoExiste(arquivo))
                    CamadaHolder.getInstance().adicionarArquivo(arquivo, mapView);
//                Utils.info("Kml pronto: " + arquivo.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            callback.processFinish();
        }
    }
}
