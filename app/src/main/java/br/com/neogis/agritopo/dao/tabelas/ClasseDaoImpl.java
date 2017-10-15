package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ClasseDaoImpl extends DaoController implements ClasseDao {
    public ClasseDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Classe> getAll() {
        return null;
    }

    @Override
    public Classe get(int id) {
        return null;
    }

    @Override
    public void insert(Classe obj) {

    }

    @Override
    public void update(Classe obj) {

    }

    @Override
    public void delete(Classe obj) {

    }
}
