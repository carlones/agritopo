package br.com.neogis.agritopo.utils;

import br.com.neogis.agritopo.singleton.Configuration;

import br.com.neogis.agritopo.dao.Constantes;

/**
 * Created by marci on 26/02/2018.
 */

public class UtilMedidas {
    public static String ObterDescricaoMedidaArea(double area){
        double medidaBase = 0;
        String unidadeMedida = "m²";
        switch (Configuration.getInstance().MedidaUtilizadaEmAreas){
            case Hectare: {
                medidaBase = Constantes.HECTARE_EM_METROS2;
                unidadeMedida = "Ha";
                break;
            }
            case KilometrosQuadrados: {
                medidaBase = Constantes.KM2_EM_METROS2;
                unidadeMedida = "Km²";
                break;
            }
        }
        return (area >= medidaBase ? unidadeMedida : "m²");
    }

    public static double CalcularMedidaArea(double area){
        double medidaBase = 0;
        switch (Configuration.getInstance().MedidaUtilizadaEmAreas){
            case Hectare: {
                medidaBase = Constantes.HECTARE_EM_METROS2;
                break;
            }
            case KilometrosQuadrados: {
                medidaBase = Constantes.KM2_EM_METROS2;
                break;
            }
        }
        return (area >= medidaBase ? area / medidaBase : area);
    }

    public static String ObterDescricaoMedidaPerimetro(double perimetro){
        return (perimetro >= 10000 ? "Km" : "m");
    }

    public static double CalcularMedidaPerimetro(double perimetro){
        return perimetro >= 10000 ? perimetro / Constantes.KM_EM_METROS : perimetro;
    }
}
