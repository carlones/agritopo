package br.com.neogis.agritopo.utils.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by marci on 14/08/2018.
 */

public class HTTPGet extends AsyncTask<Void, Void, HTTPResponse> {
    private HTTPParameter parameters;

    public HTTPGet(HTTPParameter parametros){
        this.parameters = parametros;
    }

    @Override
    protected HTTPResponse doInBackground(Void... voids) {
        try{
            parameters.url = new URL(parameters.url.toString() + "?" + parameters.obterParametros());
            HttpURLConnection connection = (HttpURLConnection) parameters.url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Charset", "utf-8");

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());
            if(scanner.hasNext())
                return new HTTPResponse(scanner.next(), true);
            else
                return new HTTPResponse("", true);
        } catch (IOException e) {
            Log.e("HTTPGet", e.getMessage());
            return new HTTPResponse(e.getMessage(), false);
        }
    }
}
