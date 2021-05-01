package br.com.neogis.agritopo.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.utils.Constantes;

public final class Configuration {
    private static Configuration INSTANCE = null;
    private static Context applicationContext;

    //Mapeamento
    public int CorDoCursor;
    public boolean ExibirAreaDistanciaDuranteMapeamento;
    public boolean UsarMiraDuranteMapeamento;
    public float EspessuraMiraMapeamento;
    public float ProximidadeElementos;
    public float SeguirPorTempo;
    public float SeguirPorDistancia;
    public boolean DeixarTelaAtivaDuranteMapeamentoComGPS;

    //Geral
    public TipoMedidaArea MedidaUtilizadaEmAreas;
    public String DiretorioExportacaoArquivos;
    public String DiretorioLeituraArquivos;
    public String DiretorioFotos;
    public String Licenca;

    //Sincronizacao
    public String HostSincroniaObjetos;
    //public String HostSincroniaLicenciamento;

    private Configuration() {

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
        String extStore = System.getenv("EXTERNAL_STORAGE");
        if (extStore.equals(""))
            extStore = Environment.getExternalStorageDirectory().getAbsolutePath();
        DiretorioExportacaoArquivos = prefs.getString(context.getResources().getString(R.string.pref_key_diretorio_exportar_arquivos),extStore + "/agritopo") + "/";
        DiretorioLeituraArquivos = prefs.getString(context.getResources().getString(R.string.pref_key_diretorio_leitura_arquivos),extStore + "/agritopo") + "/";
        DiretorioFotos = DiretorioLeituraArquivos + File.separator + "Media" + File.separator + "Fotos" + File.separator;
        Licenca = prefs.getString(context.getResources().getString(R.string.pref_key_licenca),"");
    }

    private void LoadMappingConfiguration(Context context, SharedPreferences prefs){
        // TODO: não duplicar valores padrão aqui e no xml
        CorDoCursor = prefs.getInt(context.getResources().getString(R.string.pref_key_color_cursor), Color.YELLOW);
        ExibirAreaDistanciaDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_exibir_area_mapeamento), true);
        UsarMiraDuranteMapeamento = prefs.getBoolean(context.getResources().getString(R.string.pref_key_utilizar_cursor_mapeamento), true);
        EspessuraMiraMapeamento = 2 + (8 * prefs.getFloat(context.getResources().getString(R.string.pref_key_espessura_mira_mapeamento), 0.5f));
        ProximidadeElementos = 100 * prefs.getFloat(context.getResources().getString(R.string.pref_key_seletor_proximidade), 0.5f);
        DeixarTelaAtivaDuranteMapeamentoComGPS = prefs.getBoolean(context.getResources().getString(R.string.pref_key_deixar_tela_ativa_mapeamento_gps), true);
        SeguirPorTempo = 10 * prefs.getFloat(context.getResources().getString(R.string.pref_key_seguir_por_tempo), 0.0f);
        SeguirPorDistancia = 10 * prefs.getFloat(context.getResources().getString(R.string.pref_key_seguir_por_distancia), 0.0f);
    }

    public enum TipoMedidaArea {
        Hectare,
        KilometrosQuadrados
    }
}
