package br.com.neogis.agritopo.utils.http;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by marci on 07/08/2018.
 */

public class HTTPParameter {
    public URL url;
    private Map<String,String> parametros;
    private boolean primeiro;

    public HTTPParameter(URL url){
        this.url = url;
        parametros = new HashMap<>();
    }

    public HTTPParameter adicionarParametro(String nome, String conteudo){
        if(!parametros.containsKey(nome))
            parametros.put(nome, conteudo);

        return this;
    }

    public String obterParametros(){
        StringBuilder parametro = new StringBuilder();
        primeiro = true;
        Iterator<Map.Entry<String, String>> iterator = parametros.entrySet().iterator();
        while (iterator.hasNext()) {
            if(primeiro)
                primeiro = false;
            else
                parametro.append("&");

            Map.Entry<String,String> pairs = iterator.next();
            parametro
                    .append(pairs.getKey())
                    .append("=")
                    .append(pairs.getValue());
        }
        return parametro.toString();
    }

    public boolean temParametros(){
        return parametros.size() > 0;
    }
}
