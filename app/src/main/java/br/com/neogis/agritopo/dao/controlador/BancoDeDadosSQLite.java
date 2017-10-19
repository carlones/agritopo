package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by carlo on 14/10/2017.
 */

public class BancoDeDadosSQLite extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "neogis_agritopo.db";
    private static final int VERSAO = 2;

    public BancoDeDadosSQLite(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Agritopo", "BancoDeDadosSQLite: criando banco");

        db.execSQL("CREATE TABLE classe (\n" +
                " classeid INT NOT NULL PRIMARY KEY,\n" +
                " nome VARCHAR(150)\n" +
                ");\n");
        db.execSQL("CREATE TABLE configuracao (\n" +
                " configuracaoid VARCHAR(50) NOT NULL PRIMARY KEY,\n" +
                " nome VARCHAR(50),\n" +
                " tipo VARCHAR(50),\n" +
                " valor VARCHAR(250)\n" +
                ");\n");
        db.execSQL("CREATE TABLE geradorid (\n" +
                " tabela VARCHAR(80) NOT NULL PRIMARY KEY,\n" +
                " id_atual INT\n" +
                ");\n");
        db.execSQL("CREATE TABLE imagem (\n" +
                " imagemid INT NOT NULL PRIMARY KEY,\n" +
                " arquivo VARCHAR(1000)\n" +
                ");\n");
        db.execSQL("CREATE TABLE ponto (\n" +
                " pontoid INT NOT NULL PRIMARY KEY,\n" +
                " altitude NUMERIC(18,6),\n" +
                " latitude NUMERIC(18,6),\n" +
                " longitude NUMERIC(18,6)\n" +
                ");\n");
        db.execSQL("CREATE TABLE pontoimagem (\n" +
                " pontoid INT NOT NULL,\n" +
                " imagemid INT NOT NULL,\n" +
                "\n" +
                " PRIMARY KEY (pontoid,imagemid),\n" +
                "\n" +
                " FOREIGN KEY (pontoid) REFERENCES ponto (pontoid),\n" +
                " FOREIGN KEY (imagemid) REFERENCES imagem (imagemid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE tipoelemento (\n" +
                " tipoelementoid INT NOT NULL PRIMARY KEY,\n" +
                " nome VARCHAR(50)\n" +
                ");\n");
        db.execSQL("CREATE TABLE campodinamico (\n" +
                " campodinamicoid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " nome VARCHAR(200),\n" +
                " tipocomponente VARCHAR(100),\n" +
                "\n" +
                " PRIMARY KEY (campodinamicoid,tipoelementoid),\n" +
                "\n" +
                " FOREIGN KEY (tipoelementoid) REFERENCES tipoelemento (tipoelementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE campodinamicovalores (\n" +
                " campodinamicoid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " valorpermitido VARCHAR(200),\n" +
                "\n" +
                " PRIMARY KEY (campodinamicoid,tipoelementoid),\n" +
                "\n" +
                " FOREIGN KEY (campodinamicoid,tipoelementoid) REFERENCES campodinamico (campodinamicoid,tipoelementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE elemento (\n" +
                " elementoid INT NOT NULL PRIMARY KEY,\n" +
                " classeid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " titulo VARCHAR(200),\n" +
                " descricao VARCHAR(500),\n" +
                " created_at TIMESTAMP,\n" +
                " modified_at TIMESTAMP,\n" +
                "\n" +
                " FOREIGN KEY (classeid) REFERENCES classe (classeid),\n" +
                " FOREIGN KEY (tipoelementoid) REFERENCES tipoelemento (tipoelementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE elementocampodinamico (\n" +
                " campodinamicoid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " elementoid INT NOT NULL,\n" +
                " valor VARCHAR(200),\n" +
                "\n" +
                " PRIMARY KEY (campodinamicoid,tipoelementoid,elementoid),\n" +
                "\n" +
                " FOREIGN KEY (campodinamicoid,tipoelementoid) REFERENCES campodinamico (campodinamicoid,tipoelementoid),\n" +
                " FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE elementoponto (\n" +
                " pontoid INT NOT NULL,\n" +
                " elementoid INT NOT NULL,\n" +
                "\n" +
                " PRIMARY KEY (pontoid,elementoid),\n" +
                "\n" +
                " FOREIGN KEY (pontoid) REFERENCES ponto (pontoid),\n" +
                " FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)\n" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Agritopo", "BancoDeDadosSQLite: atualizando da versão " + Integer.toString(oldVersion) + " para a versão " + Integer.toString(newVersion));
    }
}
