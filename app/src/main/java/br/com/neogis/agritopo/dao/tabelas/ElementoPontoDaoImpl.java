package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoPontoDaoImpl extends DaoController implements ElementoPontoDao {
    public ElementoPontoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<ElementoPonto> getAll() {
        return null;
    }

    @Override
    public ElementoPonto get(int id) {
        return null;
    }

    @Override
    public void insert(ElementoPonto obj) {

    }

    @Override
    public void update(ElementoPonto obj) {

    }

    @Override
    public void delete(ElementoPonto obj) {

    }
}
