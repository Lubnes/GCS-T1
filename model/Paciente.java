package model;
public class Paciente extends Usuario {

    public Paciente(int id, String nome, String iniciais) {
        super( id, nome, iniciais );
    }

    @Override
    public String toString() {
        return "Paciente: " + super.toString();
    }


}