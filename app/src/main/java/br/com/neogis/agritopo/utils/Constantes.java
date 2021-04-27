package br.com.neogis.agritopo.utils;

/**
 * Created by carlo on 03/01/2018.
 */

public final class Constantes {
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 2000;

    public static final int PEGAR_ELEMENTO_DISTANCIA_REQUEST = 3000;
    public static final int PEGAR_ELEMENTO_AREA_REQUEST = 4000;
    public static final int PEGAR_ELEMENTO_PONTO_REQUEST = 5000;
    public static final int PEGAR_MAPA_MODO_REQUEST = 6000;
    public static final int PEGAR_MENU_CADASTROS_REQUEST = 7000;
    public static final int PEGAR_NOME_ARQUIVO_EXPORTAR_REQUEST = 8000;
    public static final int PEGAR_NOME_ARQUIVO_IMPORTAR_REQUEST = 8500;
    public static final int ALTERAR_ELEMENTO_REQUEST = 9000;
    public static final int PEGAR_MENU_CAMADAS_REQUEST = 10000;
    public static final int PEGAR_SERIAL_KEY = 11000;
    public static final int PEGAR_EULA = 12500;
    public static final int RECARREGAR_LICENCA = 13000;
    public static final int PEGAR_CADASTRO_USUARIO = 14000;

    public static final int ONLINE = 1;
    public static final int OFFLINE = 0;

    public static final double RAIO_DA_TERRA_EM_METROS = 6371000;
    public static final int KM_EM_METROS = 1000;
    public static final int KM2_EM_METROS2 = 1000000;
    public static final int HECTARE_EM_METROS2 = 10000;

    public static final String ARG_LICENCA_TIPO = "licenca_tipo";
    public static final String ARG_MAPA_ID = "mapa_id";
    public static final String ARG_MAPA_MODO = "mapa_modo";
    public static final String ARG_MAPA_ZOOMINICIAL = "mapa_zoom_inicial";
    public static final String ARG_MAPA_LATITUDEATUAL = "mapa_latitude_atual";
    public static final String ARG_MAPA_LONGITUDEATUAL = "mapa_longitude_atual";
    public static final String ARG_ELEMENTOID = "elementoid";
    public static final String ARG_ELEMENTO_CENTRALIZAR = "focar_elemento";
    public static final String ARG_CLASSEID = "classeid";
    public static final String ARG_TIPOELEMENTOID = "tipoelementoid";
    public static final String ARG_GPS_POSICAO = "gps_posicao";
    public static final String ARG_GEOMETRIA = "geometria";
    public static final String ARG_POSICAO_LISTA = "posicao_lista";
    public static final String ARG_EXPORTAR_NOME_ARQUIVO = "exportar_nome_arquivo";
    public static final String ARG_EXPORTAR_TIPO_ARQUIVO = "exportar_tipo_arquivo";
    public static final String ARG_IMPORTAR_NOME_ARQUIVO = "importar_nome_arquivo";
    public static final String ARG_SERIALKEY_CHAVE = "chave";
    public static final String ARG_SERIALKEY_MANUAL = "solicitacao manual - sim ou nao";
    //public static final String ENDERECO_SERVIDOR_LICENCIAMENTO = "https://portal.neogis.com.br";
    //public static final String ENDERECO_SERVIDOR_INTEGRACAO = "http://10.0.0.159:1000";
    //public static final String ENDERECO_SERVIDOR_LICENCIAMENTO = "http://10.0.0.159:1000";
    //public static final String ENDERECO_SERVIDOR_INTEGRACAO = "https://portal.neogis.com.br";
    //public static final String ENDERECO_SERVIDOR_LICENCIAMENTO = "https://portal.neogis.com.br";

    public static final int LICENCA_GRATUITA_LIMITE_ARQUIVO_RASTER = 25000000;
    public static final int LICENCA_GRATUITA_LIMITE_ARQUIVO_VETORIAL = 20000;
    public static final int LICENCA_GRATUITA_LIMITE_ELEMENTOS = 10;
    public static final String ARQUIVO_LICENCA = "chave.ngs";

    public static final String K = "g\\S0k3HN`HNS\"Sw2+NaHXs]l@w~/a+rY";
}
