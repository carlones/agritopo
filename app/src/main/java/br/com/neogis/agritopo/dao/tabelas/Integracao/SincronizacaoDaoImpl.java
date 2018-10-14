package br.com.neogis.agritopo.dao.tabelas.Integracao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by marci on 06/08/2018.
 */

public class SincronizacaoDaoImpl extends DaoController implements SincronizacaoDao {
    public SincronizacaoDaoImpl(Context context) {
        super(context);
    }

    private List<Sincronizacao> getListaObjetos(Cursor cursor) {
        List<Sincronizacao> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            Sincronizacao sinc = new Sincronizacao();
            sinc.setId(cursor.getInt(cursor.getColumnIndex("id")));
            sinc.setData(new Date(cursor.getLong(cursor.getColumnIndex("data"))));
            l.add(sinc);
        }
        return l;
    }

    @Override
    public List<Sincronizacao> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, data\n" +
                        "FROM sincronizacao", new String[]{});
        return getListaObjetos(cursor);
    }

    @Override
    public Sincronizacao get(int id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, data " +
                        "FROM sincronizacao " +
                        "WHERE id = ?",
                new String[]{Integer.toString(id)}
        );
        List<Sincronizacao> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void save(Sincronizacao obj){
        if(obj.getId() == 0)
            insert(obj);
        else
            update(obj);
    }

    @Override
    public void insert(Sincronizacao obj) {
        abrirGravacao();
        obj.setId(getId("sincronizacao"));
        ContentValues cv = new ContentValues();
        cv.put("id", obj.getId());
        cv.put("data", obj.getData().getTime());
        if (db.insert("sincronizacao", null, cv) == -1) {
            new Exception("Erro ao inserir sincronizacao");
        }

        fecharConexao();
    }

    @Override
    public void update(Sincronizacao obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("data", obj.getData().getTime());

        if (db.update("sincronizacao", cv, "id = ?", new String[]{(Integer.toString(obj.getId()))}) != 1) {
            new Exception("Erro ao atualizar sincronizacao");
        }
        fecharConexao();
    }

    @Override
    public void delete(Sincronizacao obj) {
        abrirGravacao();
        if (db.delete("sincronizacao", "id = ?", new String[]{(Integer.toString(obj.getId()))}) != 1) {
            new Exception("Erro ao excluir tipoelemento");
        }
        fecharConexao();
    }
}