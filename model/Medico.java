package model;

public class Medico extends Usuario {

    public Medico(String nome, String iniciais) {
        super(nome, iniciais);
    }

    @Override
    public String toString() {
        return "Médico: " + super.toString();
    }
}