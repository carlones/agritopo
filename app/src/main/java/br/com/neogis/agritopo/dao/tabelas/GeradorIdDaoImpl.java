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

public class GeradorIdDaoImpl extends DaoController implements GeradorIdDao {

    public GeradorIdDaoImpl(Context context) {
        super(context);
    }

    private List<GeradorId> getListaObjetos(Cursor cursor) {
        List<GeradorId> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            l.add(new GeradorId(cursor.getString(cursor.getColumnIndex("tabela")), cursor.getInt(cursor.getColumnIndex("id_atual"))));
        }
        return l;
    }

    @Override
    public List<GeradorId> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery("SELECT tabela, id_atual FROM geradorid", new String[]{});
        return getListaObjetos(cursor);
    }

    @Override
    public GeradorId get(int id) {
        return null;
    }

    private GeradorId get(String id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery("SELECT tabela, id_atual FROM geradorid WHERE tabela = ?", new String[]{id});
        List<GeradorId> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void insert(GeradorId obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("tabela", obj.getTabela());
        cv.put("id_atual", obj.getId_atual());
        if (db.insert("geradorid", null, cv) == -1) {
            new Exception("Erro ao inserir último id gerado");
        }
        fecharConexao();
    }

    @Override
    public void update(GeradorId obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("tabela", obj.getTabela());
        cv.put("id_atual", obj.getId_atual());
        if (db.update("geradorid", cv, "tabela = ?", new String[]{obj.getTabela()}) != 1) {
            new Exception("Erro ao atualizar último id gerado");
        }
        fecharConexao();
    }

    @Override
    public void delete(GeradorId obj) {

    }

    public int getProximoId(String tabela) {
        GeradorId g = get(tabela);
        if (g == null) {
            g = new GeradorId(tabela, 1);
            insert(g);
        } else {
            g.setId_atual(g.getId_atual() + 1);
            update(g);
        }
        return g.getId_atual();
    }
}
