package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoImagemDaoImpl extends DaoController implements ElementoImagemDao {
    public ElementoImagemDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<ElementoImagem> getAll() {
        return null;
    }

    @Override
    public ElementoImagem get(int id) {
        return null;
    }

    @Override
    public void insert(ElementoImagem obj) {

    }

    @Override
    public void update(ElementoImagem obj) {

    }

    @Override
    public void delete(ElementoImagem obj) {

    }
}
