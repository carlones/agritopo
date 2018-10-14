package br.com.neogis.agritopo.dao.tabelas;

import android.support.annotation.NonNull;

import org.osmdroid.util.GeoPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.Integracao.ISincronizavel;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;
import br.com.neogis.agritopo.model.Area;
import br.com.neogis.agritopo.model.Distancia;
import br.com.neogis.agritopo.model.MyGeoPoint;
import br.com.neogis.agritopo.utils.Utils;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Elemento implements ISincronizavel{

    private int elementoid;

    private Classe classe;

    private TipoElemento tipoElemento;

    private String titulo;

    private String descricao;

    private String geometria;

    private String created_at;

    private String modified_at;

    private List<ElementoImagem> images;

    public Elemento(TipoElemento tipoElemento, Classe classe, Object geometria) {
        this(tipoElemento, classe, "", "", geometria);
    }

    private Elemento(TipoElemento tipoElemento, Classe classe, String titulo, String descricao, Object geometria) {
        this(tipoElemento, classe, titulo, descricao, "");
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
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
        this.images = new ArrayList<>();
    }

    public static String getInformacaoExtra(Elemento elemento) {
        String extra = "";
        switch (elemento.getClasse().getClasseEnum()) {
            case PONTO:
                extra = Utils.getFormattedLocationInDegree(elemento.getGeometriaMyGeoPoint());
                break;
            case AREA:
                Area area = new Area(elemento);
                extra = area.getAreaDescricao();
                break;
            case DISTANCIA:
                Distancia d = new Distancia(elemento);
                extra = d.getDistanciaDescricao();
                break;
        }
        return extra;
    }

    public String getGeometria() {
        return geometria;
    }

    public void setGeometria(String geometria) {
        this.geometria = geometria;
    }

    public GeoPoint getPontoCentral() {
        switch (classe.getClasseEnum()) {
            case PONTO:
                return getGeometriaMyGeoPoint();
            case AREA:
                return getPontoCentralArea();
            case DISTANCIA:
                return getPontoCentralDistancia();
            default:
                return null;
        }
    }

    private GeoPoint getPontoCentralDistancia() {
        List<MyGeoPoint> list = getGeometriaListMyGeoPoint();
        return getPontoCentralLista(list);
    }

    private GeoPoint getPontoCentralArea() {
        List<MyGeoPoint> list = getGeometriaListMyGeoPoint();
        return getPontoCentralLista(list);
    }

    @NonNull
    private GeoPoint getPontoCentralLista(List<MyGeoPoint> list) {
        double centroLat = 0.0;
        double centroLon = 0.0;
        for (GeoPoint ponto : list) {
            centroLat += ponto.getLatitude();
            centroLon += ponto.getLongitude();
        }
        centroLat = centroLat / list.size();
        centroLon = centroLon / list.size();
        return new GeoPoint(centroLat, centroLon);
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

    private void setModified_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.modified_at = dateFormat.format(cal.getTime());
    }

    public void addImage(String path){
        for(ElementoImagem imagem : images)
            if (imagem.getImagem().getArquivo().equals(path))
                return;


        images.add(new ElementoImagem(
                0,
                elementoid,
                new Imagem(
                        0,
                        path
                )
        ));
    }

    public List<ElementoImagem> getImages(){
        return images;
    }

    public void setImages(List<ElementoImagem> images){ this.images = images; }

    public long getId(){
        return (long)elementoid;
    }

    public TipoAlteracao getTipoAlteracao(){
        return TipoAlteracao.Elemento;
    }
}
