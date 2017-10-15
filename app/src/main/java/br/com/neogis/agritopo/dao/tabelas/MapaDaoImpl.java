package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class MapaDaoImpl extends DaoController implements MapaDao {
    public MapaDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Mapa> getAll() {
        return null;
    }

    @Override
    public Mapa get(int id) {
        return null;
    }

    @Override
    public void insert(Mapa obj) {

    }

    @Override
    public void update(Mapa obj) {

    }

    @Override
    public void delete(Mapa obj) {

    }
}
