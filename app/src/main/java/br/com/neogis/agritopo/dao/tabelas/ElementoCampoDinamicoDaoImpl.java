package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoCampoDinamicoDaoImpl extends DaoController implements ElementoCampoDinamicoDao {
    public ElementoCampoDinamicoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<ElementoCampoDinamico> getAll() {
        return null;
    }

    @Override
    public ElementoCampoDinamico get(int id) {
        return null;
    }

    @Override
    public void insert(ElementoCampoDinamico obj) {

    }

    @Override
    public void update(ElementoCampoDinamico obj) {

    }

    @Override
    public void delete(ElementoCampoDinamico obj) {

    }
}
