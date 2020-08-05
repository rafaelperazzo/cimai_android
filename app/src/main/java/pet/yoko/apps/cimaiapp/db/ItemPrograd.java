package pet.yoko.apps.cimaiapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ItemPrograd implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "ano")
    int ano;

    @ColumnInfo(name = "nCursos")
    int nCursos;

    @ColumnInfo(name = "sisu")
    int sisu;

    @ColumnInfo(name = "alunos_matriculados")
    int alunos_matriculados;

    @ColumnInfo(name = "alunos_ingressantes")
    int alunos_ingressantes;

    @ColumnInfo(name = "alunos_concluintes")
    int alunos_concluintes;

    public ItemPrograd(int ano, int nCursos, int sisu, int alunos_matriculados, int alunos_ingressantes, int alunos_concluintes) {
        this.ano = ano;
        this.nCursos = nCursos;
        this.sisu = sisu;
        this.alunos_matriculados = alunos_matriculados;
        this.alunos_ingressantes = alunos_ingressantes;
        this.alunos_concluintes = alunos_concluintes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getnCursos() {
        return nCursos;
    }

    public void setnCursos(int nCursos) {
        this.nCursos = nCursos;
    }

    public int getSisu() {
        return sisu;
    }

    public void setSisu(int sisu) {
        this.sisu = sisu;
    }

    public int getAlunos_matriculados() {
        return alunos_matriculados;
    }

    public void setAlunos_matriculados(int alunos_matriculados) {
        this.alunos_matriculados = alunos_matriculados;
    }

    public int getAlunos_ingressantes() {
        return alunos_ingressantes;
    }

    public void setAlunos_ingressantes(int alunos_ingressantes) {
        this.alunos_ingressantes = alunos_ingressantes;
    }

    public int getAlunos_concluintes() {
        return alunos_concluintes;
    }

    public void setAlunos_concluintes(int alunos_concluintes) {
        this.alunos_concluintes = alunos_concluintes;
    }
}
