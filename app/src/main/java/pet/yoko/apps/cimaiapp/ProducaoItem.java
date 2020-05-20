package pet.yoko.apps.cimaiapp;

import java.util.ArrayList;
import java.util.List;

public class ProducaoItem {

    String descricao;
    int quantidade;
    float percentual;

    ProducaoItem(String descricao, int quantidade) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.percentual = 0;
    }

    ProducaoItem(String descricao, int quantidade,float percentual) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.percentual = percentual;
    }

    public void setPercentual(float percentual) {
        this.percentual = percentual;
    }
    public int getQuantidade() {
        return quantidade;
    }

}



