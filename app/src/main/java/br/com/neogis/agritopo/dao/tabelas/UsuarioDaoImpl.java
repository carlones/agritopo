package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class UsuarioDaoImpl extends DaoController implements UsuarioDao {
    public UsuarioDaoImpl(Context context) {
        super(context);
    }

    private List<Usuario> getListaObjetos(Cursor cursor) {
        List<Usuario> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            Usuario usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndex("usuarioid")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("senha")),
                    cursor.getInt(cursor.getColumnIndex("estado"))
            );
            l.add(usuario);
        }
        return l;
    }


    @Override
    public List<Usuario> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery("" +
                "SELECT * FROM usuario", new String[]{});
        List<Usuario> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
    }

    @Override
    public Usuario get(int id) {
        abrirLeitura();
        String sql = "" +
                "SELECT * FROM usuario WHERE usuarioid = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(id)});
        List<Usuario> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void insert(Usuario obj) {
        abrirGravacao();
        obj.setUsuarioid(getId("usuario"));
        ContentValues cv = new ContentValues();
        cv.put("usuarioid", obj.getUsuarioid());
        cv.put("email", obj.getEmail());
        cv.put("senha", obj.getSenha());
        cv.put("estado", obj.getEstado());
        if (db.insert("usuario", null, cv) == -1) {
            new Exception("Erro ao inserir usuario");
        }
        fecharConexao();
    }

    @Override
    public void update(Usuario obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("usuarioid", obj.getUsuarioid());
        cv.put("email", obj.getEmail());
        cv.put("senha", obj.getSenha());
        cv.put("estado", obj.getEstado());
        if (db.update("usuario", cv, "usuarioid = ?", new String[]{(Integer.toString(obj.getUsuarioid()))}) != 1) {
            new Exception("Erro ao atualizar usuario");
        }
        fecharConexao();
    }

    @Override
    public void delete(Usuario obj) {
        abrirGravacao();
        if (db.delete("usuario", "usuarioid = ?", new String[]{(Integer.toString(obj.getUsuarioid()))}) != 1) {
            new Exception("Erro ao excluir usuario");
        }else

            fecharConexao();
    }
}
