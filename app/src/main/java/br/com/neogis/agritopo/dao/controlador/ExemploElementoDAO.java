package br.com.neogis.agritopo.dao.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.osmdroid.util.GeoPoint;

import java.util.Calendar;

import br.com.neogis.agritopo.dao.tabelas.TipoElemento;

/**
 * Created by carlo on 14/10/2017.
 */

class ExemploElementoDAO {
    private SQLiteDatabase db;
    private BancoDeDadosSQLite banco;

    public ExemploElementoDAO(Context context) {
        banco = new BancoDeDadosSQLite(context);
    }


    public boolean inserirPontoDeInteresse(TipoElemento tipoElemento, String titulo, String descricao, GeoPoint ponto) {
        db = banco.getWritableDatabase();
        // TODO: 14/10/2017 inserir poi
        //ContentValues valores = new ContentValues();
        //valores.put(CriaBanco.TITULO, titulo);
        //valores.put(CriaBanco.AUTOR, autor);
        //valores.put(CriaBanco.EDITORA, editora);

        //long resultado = db.insert(CriaBanco.TABELA, null, valores);
        // TODO: 14/10/2017 inserir area
        db.close();

        return false;//resultado >= 0;
    }

    private int inserirElemento(TipoElemento tipoElemento, String titulo, String descricao) {
        db = banco.getWritableDatabase();

        java.util.Date currentTime = Calendar.getInstance().getTime();
        ContentValues valores = new ContentValues();
        // TODO: 14/10/2017 auto incremento para as tabelas
        //valores.put("tipoelementoid", tipoElemento.getValor());
        valores.put("titulo", titulo);
        valores.put("descricao", descricao);
        valores.put("created_at", currentTime.toString());
        valores.put("modified_at", currentTime.toString());
/*
        long resultado = db.insert("elementoid", null, valores);
        final Connection conn = obtemConexao();
        final PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
// seta os valores dos parâmetros
        ps.executeUpdate();
        final ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            final int lastId = rs.getInt(1);
        }*/
        return 0;
    }

}
