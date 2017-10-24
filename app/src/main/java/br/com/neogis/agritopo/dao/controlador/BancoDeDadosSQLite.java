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
    private static final int VERSAO = 3;

    public BancoDeDadosSQLite(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Agritopo", "BancoDeDadosSQLite: criando banco");

        db.execSQL("\n" +
                "\n" +
                "CREATE TABLE imovel (\n" +
                " imovelid INT NOT NULL PRIMARY KEY,\n" +
                " descricao VARCHAR(200)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE mapa (\n" +
                " mapaid INT NOT NULL PRIMARY KEY,\n" +
                " descricao VARCHAR(200),\n" +
                " local VARCHAR(200),\n" +
                " sincronizado CHAR(10),\n" +
                " tipo VARCHAR(50)\n" +
                ");\n");
        db.execSQL("CREATE TABLE usuario (\n" +
                " usuarioid INT NOT NULL PRIMARY KEY,\n" +
                " email VARCHAR(300),\n" +
                " senha VARCHAR(30),\n" +
                " estado INT\n" +
                ");");

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
                " elementoid INT NOT NULL,\n" +
                " arquivo BLOB,\n" +
                "\n" +
                " FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)\n" +
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
                " geometria TEXT,\n" +
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
        db.execSQL("\n" +
                "CREATE TABLE imovelelemento (\n" +
                " imovelid INT NOT NULL,\n" +
                " elementoid INT NOT NULL,\n" +
                "\n" +
                " PRIMARY KEY (imovelid,elementoid),\n" +
                "\n" +
                " FOREIGN KEY (imovelid) REFERENCES imovel (imovelid),\n" +
                " FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE imovelmapa (\n" +
                " mapaid INT NOT NULL,\n" +
                " imovelid INT NOT NULL,\n" +
                "\n" +
                " PRIMARY KEY (mapaid,imovelid),\n" +
                "\n" +
                " FOREIGN KEY (mapaid) REFERENCES mapa (mapaid),\n" +
                " FOREIGN KEY (imovelid) REFERENCES imovel (imovelid)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE imovelusuario (\n" +
                " usuarioid INT NOT NULL,\n" +
                " imovelid INT NOT NULL,\n" +
                " permissao INT,\n" +
                "\n" +
                " PRIMARY KEY (usuarioid,imovelid),\n" +
                "\n" +
                " FOREIGN KEY (usuarioid) REFERENCES usuario (usuarioid),\n" +
                " FOREIGN KEY (imovelid) REFERENCES imovel (imovelid)\n" +
                ");\n" +
                "\n" +
                "\n");

        db.execSQL("INSERT INTO classe (classeid, nome) VALUES(1, 'Ponto'), (2, 'Area')");
        db.execSQL("INSERT INTO tipoelemento (tipoelementoid, nome) VALUES (1, 'Coleta de solo'), (2, 'Vertente'), (3, 'Terreno'), (4, 'Açude')");
        db.execSQL("INSERT INTO geradorid (tabela, id_atual) VALUES ('classe', 2)");
        db.execSQL("INSERT INTO geradorid (tabela, id_atual) VALUES ('tipoelemento', 4)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Agritopo", "BancoDeDadosSQLite: atualizando da versão " + Integer.toString(oldVersion) + " para a versão " + Integer.toString(newVersion));
        if (oldVersion == 2 && newVersion == 3) {
            db.execSQL("DROP TABLE campodinamico");
            db.execSQL("DROP TABLE campodinamicovalores");
            db.execSQL("DROP TABLE classe");
            db.execSQL("DROP TABLE configuracao");
            db.execSQL("DROP TABLE elemento");
            db.execSQL("DROP TABLE elementocampodinamico");
            db.execSQL("DROP TABLE geradorid");
            db.execSQL("DROP TABLE imagem");
            //db.execSQL("DROP TABLE imovel");
            //db.execSQL("DROP TABLE imovelelemento");
            //db.execSQL("DROP TABLE imovelmapa");
            //db.execSQL("DROP TABLE imovelusuario");
            //db.execSQL("DROP TABLE mapa");
            db.execSQL("DROP TABLE tipoelemento");
            //db.execSQL("DROP TABLE usuario");
            onCreate(db);
            Log.i("Agritopo", "BancoDeDadosSQLite: versão atualizada de 2 para 3");
        }
    }
}
