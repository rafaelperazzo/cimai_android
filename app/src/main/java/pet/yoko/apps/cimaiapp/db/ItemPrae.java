package pet.yoko.apps.cimaiapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ItemPrae implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "ano")
    int ano;

    @ColumnInfo(name = "beneficiarios")
    int beneficiarios;

    @ColumnInfo(name = "alimentacao")
    int alimentacao;

    @ColumnInfo(name = "creche")
    int creche;

    @ColumnInfo(name = "emergencial")
    int emergencial;

    @ColumnInfo(name = "financeiro_eventos")
    int financeiro_eventos;

    @ColumnInfo(name = "inclusao")
    int inclusao;

    @ColumnInfo(name = "moradia")
    int moradia;

    @ColumnInfo(name = "oculos")
    int oculos;

    @ColumnInfo(name = "transporte")
    int transporte;

    @ColumnInfo(name = "bia")
    int bia;

    @ColumnInfo(name = "indiretos")
    int indiretos;

    public ItemPrae(int ano, int beneficiarios, int alimentacao, int creche, int emergencial, int financeiro_eventos, int inclusao, int moradia, int oculos, int transporte, int bia, int indiretos) {
        this.ano = ano;
        this.beneficiarios = beneficiarios;
        this.alimentacao = alimentacao;
        this.creche = creche;
        this.emergencial = emergencial;
        this.financeiro_eventos = financeiro_eventos;
        this.inclusao = inclusao;
        this.moradia = moradia;
        this.oculos = oculos;
        this.transporte = transporte;
        this.bia = bia;
        this.indiretos = indiretos;
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

    public int getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(int beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public int getAlimentacao() {
        return alimentacao;
    }

    public void setAlimentacao(int alimentacao) {
        this.alimentacao = alimentacao;
    }

    public int getCreche() {
        return creche;
    }

    public void setCreche(int creche) {
        this.creche = creche;
    }

    public int getEmergencial() {
        return emergencial;
    }

    public void setEmergencial(int emergencial) {
        this.emergencial = emergencial;
    }

    public int getFinanceiro_eventos() {
        return financeiro_eventos;
    }

    public void setFinanceiro_eventos(int financeiro_eventos) {
        this.financeiro_eventos = financeiro_eventos;
    }

    public int getInclusao() {
        return inclusao;
    }

    public void setInclusao(int inclusao) {
        this.inclusao = inclusao;
    }

    public int getMoradia() {
        return moradia;
    }

    public void setMoradia(int moradia) {
        this.moradia = moradia;
    }

    public int getOculos() {
        return oculos;
    }

    public void setOculos(int oculos) {
        this.oculos = oculos;
    }

    public int getTransporte() {
        return transporte;
    }

    public void setTransporte(int transporte) {
        this.transporte = transporte;
    }

    public int getBia() {
        return bia;
    }

    public void setBia(int bia) {
        this.bia = bia;
    }

    public int getIndiretos() {
        return indiretos;
    }

    public void setIndiretos(int indiretos) {
        this.indiretos = indiretos;
    }
}
