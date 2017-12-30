package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.neogis.agritopo.dao.tabelas.GeradorIdDaoImpl;

/**
 * Created by carlo on 15/10/2017.
 */

public class DaoController {
    protected SQLiteDatabase db;
    protected Context context;
    private BancoDeDadosSQLite banco;

    public DaoController(Context context) {
        this.context = context;
        banco = new BancoDeDadosSQLite(context);
    }

    protected void abrirGravacao() {
        db = banco.getWritableDatabase();
    }

    protected void abrirLeitura() {
        db = banco.getReadableDatabase();
    }

    protected void fecharConexao() {
        db.close();
    }

    protected int getId(String tabela) {
        return new GeradorIdDaoImpl(this.context).getProximoId(tabela);
    }
}