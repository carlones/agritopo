package br.com.neogis.agritopo.controller;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.neogis.agritopo.singleton.Configuration;

public class MapFileController {
    public List<MapFile> Maps;

    private int selected = 0;

    public MapFileController(){
        Maps = new ArrayList<MapFile>();
    }

    public void LoadMaps(){
        Maps.clear();
        AddOnlineMap();
        File pasta_mapas = new File(Configuration.getInstance().DiretorioLeituraArquivos);
        FilenameFilter filtro = new FilenameFilter() {
            String[] extensoesValidas = {"mbtiles"};

            @Override
            public boolean accept(File dir, String name) {
                String extensao = name.substring(name.lastIndexOf(".") + 1);
                extensao = extensao.toLowerCase();
                return Arrays.asList(extensoesValidas).contains(extensao);
            }
        };

        File[] listaArquivosMapas = pasta_mapas.listFiles(filtro);
        if (listaArquivosMapas != null)
            for (File file : listaArquivosMapas)
                AddMap(file);
    }

    public Boolean ContainsMaps(){
        return Maps.size() > 1;
    }

    public Boolean IsOnline(){
        return selected == 0;
    }

    public MapFile GetMapFile(int index){
        return Maps.get(index);
    }

    public MapFile GetMapFileSelected(){
        return Maps.get(selected);
    }

    public void SetSelectedMap(MapFile mapFile){
        selected = Maps.indexOf(mapFile);
        ChangeSelected();
    }

    public void SetSelectedMap(int index){
        if(index >= Maps.size())
            return;

        selected = index;
        ChangeSelected();
    }

    public void SetOnline(){
        selected = 0;
        ChangeSelected();
    }

    private void ChangeSelected(){
        for (MapFile map : Maps)
            map.setSelected(false);
        GetMapFileSelected().setSelected(true);
    }

    private void AddMap(File file){
        MapFile mapFile = new MapFile();
        mapFile.setIndex(Maps.size());
        mapFile.setFile(file);
        mapFile.setName(file.getName());
        Maps.add(mapFile);

    }

    private void AddOnlineMap(){

        MapFile mapFile = new MapFile();
        mapFile.setIndex(Maps.size());
        mapFile.setName("Online (OpenStreetMaps)");
        Maps.add(mapFile);
    }
}
