package br.com.neogis.agritopo.dao.tabelas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.neogis.agritopo.MyGeoPoint;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Elemento {

    private int elementoid;

    private TipoElemento tipoElemento;

    private Classe classe;

    private String titulo;

    private String descricao;

    private String geometria;

    private String created_at;

    private String modified_at;

    public Elemento(TipoElemento tipoElemento, Classe classe, String titulo, String descricao, Object geometria) {
        this(tipoElemento, classe, titulo, descricao, "");
        JSONSerializer serializer = new JSONSerializer();
        setGeometria(serializer.serialize(geometria));
    }

    public Elemento(int elementoid, TipoElemento tipoElemento, Classe classe, String titulo, String descricao, String geometria, String created_at, String modified_at) {
        this(tipoElemento, classe, titulo, descricao, geometria);
        this.elementoid = elementoid;
        this.created_at = created_at;
        this.modified_at = modified_at;
    }

    public Elemento(TipoElemento tipoElemento, Classe classe, String titulo, String descricao, String geometria) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.tipoElemento = tipoElemento;
        this.classe = classe;
        this.titulo = titulo;
        this.descricao = descricao;
        this.geometria = geometria;
        this.created_at = dateFormat.format(cal.getTime());
        this.modified_at = dateFormat.format(cal.getTime());
    }

    public String getGeometria() {
        return geometria;
    }

    public void setGeometria(String geometria) {
        this.geometria = geometria;
    }

    public MyGeoPoint getGeometriaMyGeoPoint() {
        JSONDeserializer<MyGeoPoint> deserializer = new JSONDeserializer<>();
        return deserializer.deserialize(geometria);
    }

    public List<MyGeoPoint> getGeometriaListMyGeoPoint() {
        JSONDeserializer<List<MyGeoPoint>> deserializer = new JSONDeserializer<>();
        return deserializer.deserialize(geometria);
    }

    public int getElementoid() {
        return elementoid;
    }

    public void setElementoid(int elementoid) {
        this.elementoid = elementoid;
        setModified_at();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        setModified_at();
    }

    public TipoElemento getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(TipoElemento tipoElemento) {
        this.tipoElemento = tipoElemento;
        setModified_at();
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
        setModified_at();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        setModified_at();
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public void setModified_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.modified_at = dateFormat.format(cal.getTime());
    }
}