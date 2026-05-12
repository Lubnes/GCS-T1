package model;

public abstract class Usuario {

    private static int proximoId = 1000;

    private final int id;
    protected String nome;
    protected String iniciais;

    public Usuario(String nome, String iniciais) {
        this.id = proximoId++;
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