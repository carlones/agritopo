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

    public static final String ARG_MAPA_MODO = "mapa_modo";
    public static final String ARG_ELEMENTOID = "elementoid";
    public static final String ARG_CLASSEID = "classeid";
    public static final String ARG_TIPOELEMENTOID = "tipoelementoid";
    public static final String ARG_GEOMETRIA = "geometria";
    public static final String ARG_POSICAO_LISTA = "posicao_lista";
    public static final String ARG_NOME_ARQUIVO_KML = "nome_arquivo_kml";
}
