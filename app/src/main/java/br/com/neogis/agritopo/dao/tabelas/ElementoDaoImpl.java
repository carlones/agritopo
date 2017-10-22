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

public class ElementoDaoImpl extends DaoController implements ElementoDao {
    public ElementoDaoImpl(Context context) {
        super(context);
    }

    private List<Elemento> getListaObjetos(Cursor cursor) {
        List<Elemento> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            TipoElementoDao tipoElementoDao = new TipoElementoDaoImpl(context);
            ClasseDao classeDao = new ClasseDaoImpl(context);
            l.add(new Elemento(
                    cursor.getInt(cursor.getColumnIndex("elementoid")),
                    tipoElementoDao.get(cursor.getInt(cursor.getColumnIndex("tipoelementoid"))),
                    classeDao.get(cursor.getInt(cursor.getColumnIndex("classeid"))),
                    cursor.getString(cursor.getColumnIndex("titulo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getString(cursor.getColumnIndex("geometria")),
                    cursor.getString(cursor.getColumnIndex("created_at")),
                    cursor.getString(cursor.getColumnIndex("modified_at"))
            ));
        }
        return l;
    }

    @Override
    public List<Elemento> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery("" +
                "SELECT elementoid\n" +
                "  ,classeid\n" +
                "  ,tipoelementoid\n" +
                "  ,titulo\n" +
                "  ,descricao\n" +
                "  ,geometria\n" +
                "  ,created_at\n" +
                "  ,modified_at\n" +
                "FROM elemento", new String[]{});
        return getListaObjetos(cursor);
    }

    @Override
    public Elemento get(int id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT elementoid\n" +
                        "  ,classeid\n" +
                        "  ,tipoelementoid\n" +
                        "  ,titulo\n" +
                        "  ,descricao\n" +
                        "  ,geometria\n" +
                        "  ,created_at\n" +
                        "  ,modified_at\n" +
                        "FROM elemento WHERE elementoid = ?"
                , new String[]{Integer.toString(id)});
        List<Elemento> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void insert(Elemento obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("elementoid", getId("elemento"));
        cv.put("classeid", obj.getClasse().getClasseid());
        cv.put("tipoelementoid", obj.getTipoElemento().getTipoelementoid());
        cv.put("titulo", obj.getTitulo());
        cv.put("descricao", obj.getDescricao());
        cv.put("geometria", obj.getGeometria());
        cv.put("created_at", obj.getCreated_at());
        cv.put("modified_at", obj.getModified_at());
        if (db.insert("elemento", null, cv) == -1) {
            new Exception("Erro ao inserir elemento");
        }
        fecharConexao();
    }

    @Override
    public void update(Elemento obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("classeid", obj.getClasse().getClasseid());
        cv.put("tipoelementoid", obj.getTipoElemento().getTipoelementoid());
        cv.put("titulo", obj.getTitulo());
        cv.put("descricao", obj.getDescricao());
        cv.put("geometria", obj.getGeometria());
        cv.put("modified_at", obj.getModified_at());

        if (db.update("elemento", cv, "elementoid = ?", new String[]{(Integer.toString(obj.getElementoid()))}) != 1) {
            new Exception("Erro ao atualizar elemento");
        }
        fecharConexao();
    }

    @Override
    public void delete(Elemento obj) {
        abrirGravacao();
        if (db.delete("elemento", "elementoid = ?", new String[]{(Integer.toString(obj.getElementoid()))}) != 1) {
            new Exception("Erro ao excluir elemento");
        }
        fecharConexao();
    }
}
