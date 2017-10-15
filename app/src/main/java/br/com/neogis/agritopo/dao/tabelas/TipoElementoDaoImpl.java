package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class TipoElementoDaoImpl extends DaoController implements TipoElementoDao {
    public TipoElementoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<TipoElemento> getAll() {
        return null;
    }

    @Override
    public TipoElemento get(int id) {
        return null;
    }

    @Override
    public void insert(TipoElemento obj) {

    }

    @Override
    public void update(TipoElemento obj) {

    }

    @Override
    public void delete(TipoElemento obj) {

    }
}
