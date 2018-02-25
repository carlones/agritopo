package br.com.neogis.agritopo.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;

import br.com.neogis.agritopo.R;

public final class Configuration {
    private static final Configuration INSTANCE = new Configuration();

    public int CorDoCursor;
    public boolean ExibirAreaDistanciaDuranteMapeamento;
    public boolean UsarMiraDuranteMapeamento;
    public String DiretorioExportacaoArquivos;

    public Configuration(){

    }

    public static Configuration getInstance(){
        return INSTANCE;
    }

    public void LoadConfiguration(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        LoadGeneralConfiguration(context, prefs);
        LoadMappingConfiguration(context, prefs);
    }

    private void LoadGeneralConfiguration(Context context, SharedPreferences prefs){
        DiretorioExportacaoArquivos = prefs.getString(context.getResources().getString(R.string.pref_key_diretorio_exportar_arquivos),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo") + "/";
    }

    private void LoadMappingConfiguration(Context context, SharedPreferences prefs){
        CorDoCursor = prefs.getInt(context.getResources().getString(R.string.pref_key_color_cursor), Color.YELLOW);
        ExibirAreaDistanciaDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_exibir_area_mapeamento), true);
        UsarMiraDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_utilizar_cursor_mapeamento), true);
    }
}
