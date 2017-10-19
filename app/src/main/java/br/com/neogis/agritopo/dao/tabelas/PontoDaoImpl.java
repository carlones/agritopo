package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;
import br.com.neogis.agritopo.Ponto;

/**
 * Created by carlo on 15/10/2017.
 */

public class PontoDaoImpl extends DaoController implements PontoDao {

    Context context;

    public PontoDaoImpl(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<Ponto> getAll() {
        return null;
    }

    @Override
    public Ponto get(int id) {
        return null;
    }

    @Override
    public void insert(Ponto obj) {
        this.persistir(obj);
    }

    @Override
    public void update(Ponto obj) {
        this.persistir(obj);
    }

    @Override
    public void delete(Ponto obj) {

    }

    public void persistir(Ponto obj) {
        this.abrirGravacao();

        boolean ehNovoRegistro = false;
        int id = obj.getId();
        if( id == 0 ) {
            ehNovoRegistro = true;
            GeradorId geradorId = new GeradorId(this.context);
            id = geradorId.proximoId("ponto");
        }
        ContentValues cv = new ContentValues();
        //cv.put("titulo", obj.getTitulo());
        //cv.put("descricao", obj.getDescricao());
        cv.put("latitude", obj.getLatitude());
        cv.put("longitude", obj.getLongitude());
        cv.put("altitude", obj.getAltitude());

        if( ehNovoRegistro ) {
            cv.put("pontoid", id);
            db.insert("ponto", null, cv);
            obj.setId(id);
        }
        else {
            db.update("ponto", cv, "pontoid = ?", new String[]{Integer.toString(id)});
        }

        this.fecharConexao();
    }

}
