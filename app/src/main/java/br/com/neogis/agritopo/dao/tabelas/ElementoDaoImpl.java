package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaClasseDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDao;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoDaoImpl extends DaoController implements ElementoDao {
    private AlteracaoDao alteracaoDao;

    public ElementoDaoImpl(Context context, AlteracaoDao alteracaoDao) {
        super(context);
        this.alteracaoDao = alteracaoDao;
    }

    private List<Elemento> getListaObjetos(Cursor cursor) {
        List<Elemento> l = new ArrayList<>();
        TipoElementoDao tipoElementoDao = FabricaTipoElementoDao.Criar(context);
        ClasseDao classeDao = FabricaClasseDao.Criar(context);
        while (cursor.moveToNext()) {
            Elemento elemento = new Elemento(
                    cursor.getInt(cursor.getColumnIndex("elementoid")),
                    tipoElementoDao.get(cursor.getInt(cursor.getColumnIndex("tipoelementoid"))),
                    classeDao.get(cursor.getInt(cursor.getColumnIndex("classeid"))),
                    cursor.getString(cursor.getColumnIndex("titulo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getString(cursor.getColumnIndex("geometria")),
                    cursor.getString(cursor.getColumnIndex("created_at")),
                    cursor.getString(cursor.getColumnIndex("modified_at"))
            );
            elemento.setImages(
                    new ElementoImagemDaoImpl(context).getByElemento(elemento.getElementoid())
            );
            l.add(elemento);
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
        List<Elemento> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
    }

    @Override
    public List<Elemento> getAll(String orderBy) {
        abrirLeitura();
        String sql = "" +
                "SELECT elementoid\n" +
                "  ,classeid\n" +
                "  ,tipoelementoid\n" +
                "  ,titulo\n" +
                "  ,descricao\n" +
                "  ,geometria\n" +
                "  ,created_at\n" +
                "  ,modified_at\n" +
                "FROM elemento";
        if( orderBy != null && !orderBy.isEmpty() ) {
            sql += " ORDER BY " + orderBy;
        }
        Cursor cursor = db.rawQuery(sql, new String[]{});
        List<Elemento> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
    }

    @Override
    public List<Elemento> getByClasse(int classeid) {
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
                        "FROM elemento WHERE classeid = ?"
                , new String[]{Integer.toString(classeid)});
        List<Elemento> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
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
    public void save(Elemento obj) {
        if(obj.getElementoid() == 0) {
            insert(obj);
            alteracaoDao.insert(obj);
        }
        else{
            update(obj);
            alteracaoDao.update(obj);
        }
    }

    @Override
    public void save(Elemento obj, Alteracao alteracao) {
        if(obj.getElementoid() == 0)
            insert(obj);
        else
            update(obj);

        alteracao.setTipoId(obj.getId());
        alteracaoDao.save(alteracao);
    }

    private void insert(Elemento obj) {
        abrirGravacao();
        obj.setElementoid(getId("elemento"));
        ContentValues cv = new ContentValues();
        cv.put("elementoid", obj.getElementoid());
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
        else
            SalvarImagens(obj);
        fecharConexao();
    }

    private void update(Elemento obj) {
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
        else
            SalvarImagens(obj);
        fecharConexao();
    }

    @Override
    public void delete(Elemento obj) {
        abrirGravacao();
        if (db.delete("elemento", "elementoid = ?", new String[]{(Integer.toString(obj.getElementoid()))}) != 1) {
            new Exception("Erro ao excluir elemento");
        }else {
            fecharConexao();
            alteracaoDao.update(obj);
        }
    }

    private void SalvarImagens(Elemento obj){
        ExcluirImagens(obj);
        ImagemDao imagemDao = new ImagemDaoImpl(context);
        ElementoImagemDao elementoImagemDao = new ElementoImagemDaoImpl(context);
        for(ElementoImagem elementoImagem : obj.getImages()){
            if(elementoImagem.getImagem().getImagemid() == 0)
            {
                int id = imagemDao.insert(elementoImagem.getImagem());
                elementoImagem.getImagem().setImagemid(id);
            }
            else
                imagemDao.update(elementoImagem.getImagem());

            if(elementoImagem.getId() == 0) {
                elementoImagem.setIdElemento(obj.getElementoid());
                elementoImagemDao.insert(elementoImagem);
            }
            else
                elementoImagemDao.update(elementoImagem);

        }
    }

    private void ExcluirImagens(Elemento obj){
        ImagemDao imagemDao = new ImagemDaoImpl(context);
        ElementoImagemDao elementoImagemDao = new ElementoImagemDaoImpl(context);
        for(ElementoImagem elementoImagem : elementoImagemDao.getByElemento(obj.getElementoid())){
            imagemDao.delete(elementoImagem.getImagem());
            elementoImagemDao.delete(elementoImagem);
        }
    }
}
