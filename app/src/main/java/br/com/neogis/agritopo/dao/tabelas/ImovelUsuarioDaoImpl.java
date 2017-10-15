package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ImovelUsuarioDaoImpl extends DaoController implements ImovelUsuarioDao {
    public ImovelUsuarioDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<ImovelUsuario> getAll() {
        return null;
    }

    @Override
    public ImovelUsuario get(int id) {
        return null;
    }

    @Override
    public void insert(ImovelUsuario obj) {

    }

    @Override
    public void update(ImovelUsuario obj) {

    }

    @Override
    public void delete(ImovelUsuario obj) {

    }
}
