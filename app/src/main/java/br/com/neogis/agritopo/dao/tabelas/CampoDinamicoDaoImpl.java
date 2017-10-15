package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class CampoDinamicoDaoImpl extends DaoController implements CampoDinamicoDao {
    public CampoDinamicoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<CampoDinamico> getAll() {
        return null;
    }

    @Override
    public CampoDinamico get(int id) {
        return null;
    }

    @Override
    public void insert(CampoDinamico obj) {

    }

    @Override
    public void update(CampoDinamico obj) {

    }

    @Override
    public void delete(CampoDinamico obj) {

    }
}
