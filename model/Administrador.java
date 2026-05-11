package model;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {

    public Administrador(String nome, String iniciais) {
        super(nome, iniciais);
    }

    @Override
    public String toString() {
        return "Administrador: " + super.toString();
    }
}
