package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ImovelMapaDaoImpl extends DaoController implements ImovelMapaDao {
    public ImovelMapaDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<ImovelMapa> getAll() {
        return null;
    }

    @Override
    public ImovelMapa get(int id) {
        return null;
    }

    @Override
    public void insert(ImovelMapa obj) {

    }

    @Override
    public void update(ImovelMapa obj) {

    }

    @Override
    public void delete(ImovelMapa obj) {

    }
}
