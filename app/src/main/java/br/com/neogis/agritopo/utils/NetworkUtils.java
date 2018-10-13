package br.com.neogis.agritopo.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import br.com.neogis.agritopo.parse.JsonParse;

/**
 * Created by marci on 14/04/2018.
 */

public class NetworkUtils {
    public static String getJSONFromAPI(String url) throws Exception {
        String retorno;
        int codigoResposta;
        String contentType;
        HttpURLConnection conexao;
        try {
            URL apiEnd = new URL(url);
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            contentType = conexao.getContentType();
            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        if(codigoResposta >= HttpURLConnection.HTTP_BAD_REQUEST) {
            if( contentType.contains("application/json") ) {
                RetornoErroServidorJson json = new JsonParse().getParser().fromJson(retorno, RetornoErroServidorJson.class);
                retorno = json.erro;
            }
            throw new RetornoErroServidorException(retorno);
        }
        return retorno;
    }

    public static String postJSON(String url, JSONObject json) throws Exception {
        String retorno;
        int codigoResposta;
        String contentType;
        HttpURLConnection conexao;
        try {
            URL apiEnd = new URL(url);
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("POST");
            conexao.setDoOutput(true);
            conexao.setUseCaches(false);
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.setRequestProperty("Content-Type","application/json");
            conexao.connect();

            OutputStreamWriter out = new OutputStreamWriter(conexao.getOutputStream());
            out.write(json.toString());
            out.close();

            contentType = conexao.getContentType();
            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        if(codigoResposta >= HttpURLConnection.HTTP_BAD_REQUEST) {
            if( contentType.contains("application/json") ) {
                RetornoErroServidorJson retornoJson = new JsonParse().getParser().fromJson(retorno, RetornoErroServidorJson.class);
                retorno = retornoJson.erro;
            }
            throw new RetornoErroServidorException(retorno);
        }
        return retorno;
    }

    private static String converterInputStreamToString(InputStream is){
        StringBuilder buffer = new StringBuilder();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
