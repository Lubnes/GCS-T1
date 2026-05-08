package model;

import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario {

    private List<AutorizacaoExame> autorizacoes;

    public Paciente(int id, String nome, String iniciais) {
        super(id, nome, iniciais);
        this.autorizacoes = new ArrayList<>();
    }

    public List<AutorizacaoExame> getAutorizacoes() {
        return autorizacoes;
    }

    public void adicionarAutorizacao(AutorizacaoExame autorizacao) {
        autorizacoes.add(autorizacao);
    }

    @Override
    public String toString() {
        return "Paciente: " + super.toString();
    }

    public void listarAutorizacoes() {
        for (AutorizacaoExame autorizacao : autorizacoes) {
            System.out.println(autorizacao);
        }
    }
}