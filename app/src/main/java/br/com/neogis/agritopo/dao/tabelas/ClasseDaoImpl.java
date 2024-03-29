package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDao;

/**
 * Created by carlo on 15/10/2017.
 */

public class ClasseDaoImpl extends DaoController implements ClasseDao {

    public ClasseDaoImpl(Context context) {
        super(context);
    }

    private List<Classe> getListaObjetos(Cursor cursor) {
        List<Classe> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            l.add(new Classe(
                    cursor.getInt(cursor.getColumnIndex("classeid")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            ));
        }
        return l;
    }

    @Override
    public List<Classe> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT classeid, nome\n" +
                        "FROM classe", new String[]{});
        try {
            return getListaObjetos(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public Classe get(int id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT classeid, nome " +
                        "FROM classe " +
                        "WHERE classeid = ?",
                new String[]{Integer.toString(id)}
        );
        try {
            List<Classe> l = getListaObjetos(cursor);
            fecharConexao();
            if (l.isEmpty())
                return null;
            else
                return l.get(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void insert(Classe obj) {
        abrirGravacao();
        obj.setClasseid(getId("classe"));
        ContentValues cv = new ContentValues();
        cv.put("classeid", obj.getClasseid());
        cv.put("nome", obj.getNome());
        if (db.insert("classe", null, cv) == -1) {
            new Exception("Erro ao inserir classe");
        }
        fecharConexao();
    }

    @Override
    public void update(Classe obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("nome", obj.getNome());

        if (db.update("classe", cv, "classeid = ?", new String[]{(Integer.toString(obj.getClasseid()))}) != 1) {
            new Exception("Erro ao atualizar classe");
        }
        fecharConexao();
    }

    @Override
    public void delete(Classe obj) {
        abrirGravacao();
        if (db.delete("classe", "classeid = ?", new String[]{(Integer.toString(obj.getClasseid()))}) != 1) {
            new Exception("Erro ao excluir classe");
        }
        fecharConexao();
    }
}
