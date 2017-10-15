package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoDaoImpl extends DaoController implements ElementoDao {
    public ElementoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Elemento> getAll() {
        return null;
    }

    @Override
    public Elemento get(int id) {
        return null;
    }

    @Override
    public void insert(Elemento obj) {

    }

    @Override
    public void update(Elemento obj) {

    }

    @Override
    public void delete(Elemento obj) {

    }
}
