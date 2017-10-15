package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ImovelElementoDaoImpl extends DaoController implements ImovelElementoDao {
    public ImovelElementoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<ImovelElemento> getAll() {
        return null;
    }

    @Override
    public ImovelElemento get(int id) {
        return null;
    }

    @Override
    public void insert(ImovelElemento obj) {

    }

    @Override
    public void update(ImovelElemento obj) {

    }

    @Override
    public void delete(ImovelElemento obj) {

    }
}
