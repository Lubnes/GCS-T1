package model;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario {

    private List<AutorizacaoExame> autorizacoes;

    public Paciente(int id, String nome, String iniciais) {
        super(id, nome, iniciais);
        this.autorizacoes = new ArrayList<>();
    }
}