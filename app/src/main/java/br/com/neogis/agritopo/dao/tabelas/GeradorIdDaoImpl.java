package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class GeradorIdDaoImpl extends DaoController implements GeradorIdDao {
    public GeradorIdDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<GeradorId> getAll() {
        return null;
    }

    @Override
    public GeradorId get(int id) {
        return null;
    }

    @Override
    public void insert(GeradorId obj) {

    }

    @Override
    public void update(GeradorId obj) {

    }

    @Override
    public void delete(GeradorId obj) {

    }
}
