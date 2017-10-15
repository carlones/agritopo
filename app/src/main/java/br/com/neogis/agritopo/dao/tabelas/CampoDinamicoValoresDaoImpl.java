package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class CampoDinamicoValoresDaoImpl extends DaoController implements CampoDinamicoValoresDao {
    public CampoDinamicoValoresDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<CampoDinamicoValores> getAll() {
        return null;
    }

    @Override
    public CampoDinamicoValores get(int id) {
        return null;
    }

    @Override
    public void insert(CampoDinamicoValores obj) {

    }

    @Override
    public void update(CampoDinamicoValores obj) {

    }

    @Override
    public void delete(CampoDinamicoValores obj) {

    }
}
