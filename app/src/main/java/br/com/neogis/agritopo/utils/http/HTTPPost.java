package br.com.neogis.agritopo.utils.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Created by marci on 07/08/2018.
 */

public class HTTPPost extends AsyncTask<Void, Void, HTTPResponse> {
    private HTTPParameter parameters;

    public HTTPPost(HTTPParameter parametros){
        this.parameters = parametros;
    }

    @Override
    protected HTTPResponse doInBackground(Void... voids) {
        try{
            HttpURLConnection connection = (HttpURLConnection) parameters.url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setDoOutput(true);

            if(parameters.temParametros()){
                PrintStream printStream = new PrintStream(connection.getOutputStream());
                printStream.println(parameters.obterParametros());
            }

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());
            if(scanner.hasNext())
                return new HTTPResponse(scanner.next(), true);
            else
                return new HTTPResponse("", true);
        } catch (IOException e) {
            Log.e("HTTPPost", e.getMessage(), e);
            return new HTTPResponse(e.getMessage(), false);
        }
    }
}
