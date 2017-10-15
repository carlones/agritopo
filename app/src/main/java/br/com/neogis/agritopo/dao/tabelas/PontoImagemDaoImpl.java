package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class PontoImagemDaoImpl extends DaoController implements PontoImagemDao {
    public PontoImagemDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<PontoImagem> getAll() {
        return null;
    }

    @Override
    public PontoImagem get(int id) {
        return null;
    }

    @Override
    public void insert(PontoImagem obj) {

    }

    @Override
    public void update(PontoImagem obj) {

    }

    @Override
    public void delete(PontoImagem obj) {

    }
}
