package br.com.neogis.agritopo.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import br.com.neogis.agritopo.R;

/**
 * Created by marci on 24/02/2018.
 */

public final class Configuration {
    private static final Configuration INSTANCE = new Configuration();

    public int CorDoCursor;
    public boolean ExibirAreaDistanciaDuranteMapeamento;
    public boolean UsarMiraDuranteMapeamento;

    public Configuration(){

    }

    public static Configuration getInstance(){
        return INSTANCE;
    }

    public void LoadConfiguration(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        LoadMappingConfiguration(context, prefs);
    }

    private void LoadMappingConfiguration(Context context, SharedPreferences prefs){
        CorDoCursor = prefs.getInt(context.getResources().getString(R.string.pref_key_color_cursor), Color.YELLOW);
        ExibirAreaDistanciaDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_exibir_area_mapeamento), true);
        UsarMiraDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_utilizar_cursor_mapeamento), true);
    }
}
