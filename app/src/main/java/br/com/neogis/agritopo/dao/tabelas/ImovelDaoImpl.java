package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ImovelDaoImpl extends DaoController implements ImovelDao {
    public ImovelDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Imovel> getAll() {
        return null;
    }

    @Override
    public Imovel get(int id) {
        return null;
    }

    @Override
    public void insert(Imovel obj) {

    }

    @Override
    public void update(Imovel obj) {

    }

    @Override
    public void delete(Imovel obj) {

    }
}
