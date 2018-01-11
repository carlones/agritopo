package br.com.neogis.agritopo.dao.tabelas;

/**
 * Created by carlo on 15/10/2017.
 */

public class Classe {

    private int classeid;

    private String nome;

    public Classe(int classeid, String nome) {
        this.classeid = classeid;
        this.nome = nome;
    }

    public Classe(String nome) {
        this.nome = nome;
    }

    public int getClasseid() {
        return classeid;
    }

    public void setClasseid(int classeid) {
        this.classeid = classeid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ClasseEnum getClasseEnum() {
        ClasseEnum classeEnum = ClasseEnum.PONTO;
        for (ClasseEnum e : ClasseEnum.values()) {
            if (e.getValor() == classeid) {
                classeEnum = e;
                break;
            }
        }
        return classeEnum;
    }
}
