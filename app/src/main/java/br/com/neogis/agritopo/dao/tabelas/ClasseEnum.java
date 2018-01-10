package br.com.neogis.agritopo.dao.tabelas;

import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.model.MyPolygon;
import br.com.neogis.agritopo.model.MyPolyline;

/**
 * Created by carlo on 10/01/2018.
 */

public enum ClasseEnum {
    PONTO(1), AREA(2), DISTANCIA(3);

    private final int valor;

    ClasseEnum(int valorOpcao) {
        valor = valorOpcao;
    }

    public int getValor() {
        return valor;
    }

    public String getDescricao() {
        switch (valor) {
            case 1:
                return "Ponto";
            case 2:
                return "Área";
            case 3:
                return "Distância";
            default:
                return "Ponto";
        }
    }

    public Class getClasse() {
        switch (valor) {
            case 1:
                return MyGeoPoint.class;
            case 2:
                return MyPolygon.class;
            case 3:
                return MyPolyline.class;
            default:
                return MyGeoPoint.class;
        }
    }
}
