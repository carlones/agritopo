package br.com.neogis.agritopo.dao.tabelas.Integracao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by marci on 06/08/2018.
 */


public class AlteracaoDaoImpl extends DaoController implements AlteracaoDao {
    public AlteracaoDaoImpl(Context context) {
        super(context);
    }

    private List<Alteracao> getListaObjetos(Cursor cursor) {
        List<Alteracao> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            Alteracao alteracao = new Alteracao();
            alteracao.setId(cursor.getLong(cursor.getColumnIndex("id")));
            alteracao.setTipoId(cursor.getLong(cursor.getColumnIndex("tipoid")));
            alteracao.setTipo(TipoAlteracao.values()[cursor.getInt(cursor.getColumnIndex("tipo"))]);
            alteracao.setData(new Date(cursor.getLong(cursor.getColumnIndex("data"))));
            alteracao.setGUID(cursor.getString(cursor.getColumnIndex("guid")));
            l.add(alteracao);
        }
        return l;
    }

    @Override
    public List<Alteracao> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, tipoid, data, tipo, guid \n" +
                        "FROM alteracao " +
                        "ORDER BY id", new String[]{});
        return getListaObjetos(cursor);
    }

    @Override
    public List<Alteracao> getByData(Date data){
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, tipoid, data, tipo, guid \n" +
                        "FROM alteracao " +
                        "WHERE data >= ?",
                new String[]{Long.toString(data.getTime())});
        return getListaObjetos(cursor);
    }

    @Override
    public Alteracao getByTipoEId(TipoAlteracao tipo, long tipoId){
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, tipoid, data, tipo, guid " +
                        "FROM alteracao " +
                        "WHERE tipo = ? and tipoid = ?",
                new String[]{Integer.toString(tipo.ordinal()), Long.toString(tipoId)}
        );
        List<Alteracao> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public Alteracao getByTipoEGUID(TipoAlteracao tipo, String guid){
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, tipoid, data, tipo, guid " +
                        "FROM alteracao " +
                        "WHERE tipo = ? and guid = ?",
                new String[]{Integer.toString(tipo.ordinal()), guid}
        );
        List<Alteracao> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public Alteracao get(long id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT id, tipoid, data, tipo, guid " +
                        "FROM alteracao " +
                        "WHERE id = ?",
                new String[]{Long.toString(id)}
        );
        List<Alteracao> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void save(Alteracao obj){
        if(obj.getId() == 0)
            insert(obj);
        else
            update(obj);
    }

    private void insert(Alteracao obj) {
        abrirGravacao();
        obj.setId(getId("alteracao"));
        ContentValues cv = new ContentValues();
        cv.put("id", obj.getId());
        cv.put("tipoid", obj.getTipoId());
        cv.put("data", obj.getData().getTime());
        cv.put("tipo", obj.getTipo().ordinal());
        cv.put("guid", obj.getGUID());
        if (db.insert("alteracao", null, cv) == -1) {
            new Exception("Erro ao inserir alteracao");
        }

        fecharConexao();
    }

    private void update(Alteracao obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("tipoid", obj.getTipoId());
        cv.put("data", obj.getData().getTime());
        cv.put("tipo", obj.getTipo().ordinal());
        cv.put("guid", obj.getGUID());

        if (db.update("alteracao", cv, "id = ?", new String[]{(Long.toString(obj.getId()))}) != 1) {
            new Exception("Erro ao atualizar alteracao");
        }
        fecharConexao();
    }

    @Override
    public void delete(Alteracao obj) {
        abrirGravacao();
        if (db.delete("alteracao", "id = ?", new String[]{(Long.toString(obj.getId()))}) != 1) {
            new Exception("Erro ao excluir alteracao");
        }
        fecharConexao();
    }

    public void insert(ISincronizavel sinc){
        Alteracao alteracao = new Alteracao();
        alteracao.setTipo(sinc.getTipoAlteracao());
        alteracao.setTipoId(sinc.getId());
        alteracao.setData(new Date());
        alteracao.setGUID(UUID.randomUUID().toString());
        insert(alteracao);
    }

    public void update(ISincronizavel sinc){
        Alteracao alteracao = getByTipoEId(sinc.getTipoAlteracao(), sinc.getId());

        if(alteracao == null) {
            insert(sinc);
            return;
        }

        alteracao.setData(new Date());
        update(alteracao);
    }
}
