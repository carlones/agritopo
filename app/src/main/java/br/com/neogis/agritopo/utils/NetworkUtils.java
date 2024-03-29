package br.com.neogis.agritopo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

            conexao = getHttpURLConnection(apiEnd);

            contentType = conexao.getContentType();
            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                conexao = getHttpURLConnection(apiEnd);
                contentType = conexao.getContentType();
                codigoResposta = conexao.getResponseCode();
                if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST)
                    is = conexao.getInputStream();
                else
                    is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }

//      Conforme Carlos, isto já está sendo tratado.
//        if(codigoResposta >= HttpURLConnection.HTTP_BAD_REQUEST) {
//            if( contentType.contains("application/json") ) {
//                RetornoErroServidorJson json = new JsonParse().getParser().fromJson(retorno, RetornoErroServidorJson.class);
//                retorno = json.erro;
//            }
//            throw new RetornoErroServidorException(retorno);
//        }
        return retorno;
    }

    @NonNull
    private static HttpURLConnection getHttpURLConnection(URL apiEnd) throws IOException {
        HttpURLConnection conexao;
        conexao = (HttpURLConnection) apiEnd.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setReadTimeout(15000);
        conexao.setConnectTimeout(15000);
        conexao.connect();
        return conexao;
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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
