package model;

public abstract class Usuario {

    protected int id;
    protected String nome;
    protected String iniciais;

    public Usuario(int id, String nome, String iniciais) {
        this.id = id;
        this.nome = nome;
        this.iniciais = iniciais;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getIniciais() {
        return iniciais;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIniciais(String iniciais) {
        this.iniciais = iniciais;
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + iniciais + ")";
    }
}