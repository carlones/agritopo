package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class UsuarioDaoImpl extends DaoController implements UsuarioDao {
    public UsuarioDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Usuario> getAll() {
        return null;
    }

    @Override
    public Usuario get(int id) {
        return null;
    }

    @Override
    public void insert(Usuario obj) {

    }

    @Override
    public void update(Usuario obj) {

    }

    @Override
    public void delete(Usuario obj) {

    }
}
