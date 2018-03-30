package br.com.neogis.agritopo.controller;

import java.io.File;
/**
 * Created by marci on 29/03/2018.
 */

public class MapFile {
    private int index;
    private File file;
    private Boolean selected;
    private String name;

    public MapFile(){
        setSelected(false);
        setIndex(-1);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        // remover extensão
        int pos_ponto_extensao = name.lastIndexOf(".");
        if (pos_ponto_extensao > 0)
            this.name = name.substring(0, pos_ponto_extensao);
    }
}
