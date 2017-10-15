package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ConfiguracaoDaoImpl extends DaoController implements ConfiguracaoDao {
    public ConfiguracaoDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Configuracao> getAll() {
        return null;
    }

    @Override
    public Configuracao get(int id) {
        return null;
    }

    @Override
    public void insert(Configuracao obj) {

    }

    @Override
    public void update(Configuracao obj) {

    }

    @Override
    public void delete(Configuracao obj) {

    }
}
