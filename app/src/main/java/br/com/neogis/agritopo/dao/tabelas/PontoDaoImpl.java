package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class PontoDaoImpl extends DaoController implements PontoDao {
    public PontoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Ponto> getAll() {
        return null;
    }

    @Override
    public Ponto get(int id) {
        return null;
    }

    @Override
    public void insert(Ponto obj) {

    }

    @Override
    public void update(Ponto obj) {

    }

    @Override
    public void delete(Ponto obj) {

    }
}
