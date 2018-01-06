package br.com.neogis.agritopo.dao;

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
    public static final int PEGAR_NOME_ARQUIVO_KML_REQUEST = 8000;
    public static final int ALTERAR_ELEMENTO_REQUEST = 9000;

    public static final int ONLINE = 1;
    public static final int OFFLINE = 0;

    public static final double RAIO_DA_TERRA_EM_METROS = 6371000;
    public static final int KM_EM_METROS = 1000;
    public static final int KM2_EM_METROS2 = 1000000;

    public static final String ARG_MAPA_MODO = "mapa_modo";
    public static final String ARG_ELEMENTOID = "elementoid";
    public static final String ARG_CLASSEID = "classeid";
    public static final String ARG_TIPOELEMENTOID = "tipoelementoid";
    public static final String ARG_GEOMETRIA = "geometria";
    public static final String ARG_POSICAO_LISTA = "posicao_lista";
    public static final String ARG_EXPORTAR_NOME_ARQUIVO = "exportar_nome_arquivo";
    public static final String ARG_EXPORTAR_TIPO_ARQUIVO = "exportar_tipo_arquivo";

    public static final int CLASSE_AREA_ID = 2;
    public static final int CLASSE_DISTANCIA_ID = 3;
    public static final int CLASSE_PONTO_ID = 1;
}
