package br.com.neogis.agritopo.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.utils.NetworkUtils;

public final class Configuration {
    private static Configuration INSTANCE = null;
    private static Context applicationContext;
    //Mapeamento
    public int CorDoCursor;
    public boolean ExibirAreaDistanciaDuranteMapeamento;
    public boolean UsarMiraDuranteMapeamento;
    public float EspessuraMiraMapeamento;
    public float ProximidadeElementos;

    //Geral
    public TipoMedidaArea MedidaUtilizadaEmAreas;
    public String DiretorioExportacaoArquivos;
    public String DiretorioLeituraArquivos;
    public String DiretorioFotos;

    public Configuration(){

    }

    public static Configuration getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new Configuration();
            if(applicationContext != null)
                INSTANCE.LoadConfiguration(applicationContext);
        }

        return INSTANCE;
    }

    public void LoadConfiguration(Context context){
        applicationContext = context.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        LoadGeneralConfiguration(applicationContext, prefs);
        LoadMappingConfiguration(applicationContext, prefs);
    }

    private void LoadGeneralConfiguration(Context context, SharedPreferences prefs){
        MedidaUtilizadaEmAreas = TipoMedidaArea.values()[Integer.parseInt(prefs.getString(context.getResources().getString(R.string.pref_key_measure_area), "0"))];
        DiretorioExportacaoArquivos = prefs.getString(context.getResources().getString(R.string.pref_key_diretorio_exportar_arquivos),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo") + "/";
        DiretorioLeituraArquivos = prefs.getString(context.getResources().getString(R.string.pref_key_diretorio_leitura_arquivos),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo") + "/";
        DiretorioFotos = DiretorioLeituraArquivos + File.separator + "Media" + File.separator + "Fotos" + File.separator;
    }

    private void LoadMappingConfiguration(Context context, SharedPreferences prefs){
        CorDoCursor = prefs.getInt(context.getResources().getString(R.string.pref_key_color_cursor), Color.YELLOW);
        ExibirAreaDistanciaDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_exibir_area_mapeamento), true);
        UsarMiraDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_utilizar_cursor_mapeamento), true);
        EspessuraMiraMapeamento = 2 + (8 * prefs.getFloat(context.getResources().getString(R.string.pref_key_espessura_mira_mapeamento), 0.5f));
        ProximidadeElementos = 100 * prefs.getFloat(context.getResources().getString(R.string.pref_key_seletor_proximidade), 0.5f);
    }

    public enum TipoMedidaArea {
        Hectare,
        KilometrosQuadrados
    };
}
