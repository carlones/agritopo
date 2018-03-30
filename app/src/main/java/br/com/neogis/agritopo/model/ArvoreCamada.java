package br.com.neogis.agritopo.model;

import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;

public class ArvoreCamada {

    public static int RAIZ = 0;
    public static int PASTA = 1;
    public static int GEOMETRIA = 2;

    public int indice;
    public String nome;
    private int tipo;
    public boolean selecionado;
    public Overlay overlay;
    public ArrayList<ArvoreCamada> filhos;

    public ArvoreCamada(String nome, int tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.selecionado = false;
        filhos = new ArrayList<>();
    }

    public void adicionarFilho(ArvoreCamada filho) {
        filhos.add(filho);
    }

    public Boolean TemAlgumItemSelecionado(){
        if(selecionado)
            return true;

        for(ArvoreCamada filho : filhos)
            if(filho.TemAlgumItemSelecionado())
                return true;

        return  false;
    }

    public boolean ehPasta() {
        return tipo == PASTA;
    }


}
