package br.com.neogis.agritopo.service.Sincronizacao;

import android.util.Base64;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;
import br.com.neogis.agritopo.utils.Constantes;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.http.HTTPGet;
import br.com.neogis.agritopo.utils.http.HTTPParameter;
import br.com.neogis.agritopo.utils.http.HTTPResponse;

/**
 * Created by marci on 14/08/2018.
 */

public class IntegradorRecepcao {
    public List<SincronizacaoView> Recepcionar(String emailUsuario, Date dataUltimaSincronizacao){
        try {
            HTTPParameter parameter = new HTTPParameter(ObterUrl());
            parameter.adicionarParametro("userEmail", emailUsuario);
            parameter.adicionarParametro("lastSync", DateUtils.formatDateddMMyyyyThhmmssZ(dataUltimaSincronizacao));

            HTTPResponse response = new HTTPGet(parameter).execute().get();
            if(!response.sucesso)
                return null;

            Type tipo = new TypeToken<ArrayList <SincronizacaoView>>(){}.getType();
            return new JsonParse().getParser().fromJson(DeserializarBase64(response.resposta), tipo);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private URL ObterUrl() throws MalformedURLException {
        try {
            URL url = new URL(Constantes.ENDERECO_SERVIDOR_INTEGRACAO + "/api/Integracao/ObterAlteracoes");
            return url;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private String DeserializarBase64(String base64) throws UnsupportedEncodingException {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
