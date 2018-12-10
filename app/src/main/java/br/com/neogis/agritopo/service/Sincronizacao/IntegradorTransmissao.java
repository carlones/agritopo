package br.com.neogis.agritopo.service.Sincronizacao;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;
import br.com.neogis.agritopo.singleton.Configuration;
import br.com.neogis.agritopo.utils.Constantes;
import br.com.neogis.agritopo.utils.http.HTTPParameter;
import br.com.neogis.agritopo.utils.http.HTTPPost;
import br.com.neogis.agritopo.utils.http.HTTPResponse;

/**
 * Created by marci on 07/08/2018.
 */

public class IntegradorTransmissao {
    public boolean Transmitir(SincronizacaoView view){
        try {
            HTTPParameter parameter = new HTTPParameter(ObterUrl());
            parameter.adicionarParametro("alteracao", ObterConteudo(view));

            HTTPResponse response = new HTTPPost(parameter).execute().get();
            return response.sucesso;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private URL ObterUrl() throws MalformedURLException {
        try {
            URL url = new URL(Configuration.getInstance().HostSincroniaObjetos + "/api/Integracao/SincronizarAlteracao");
            return url;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private String ObterConteudo(SincronizacaoView view){
        String json =  new JsonParse().getParser().toJson(view);
        byte[] data = new byte[0];
        try {
            data = json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }
}
