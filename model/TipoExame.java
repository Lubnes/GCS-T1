package model;

public enum TipoExame {
    RAIO_X("Raio-X"),
    TOMOGRAFIA("Tomografia"),
    RESSONANCIA_MAGNETICA("Ressonância Magnética"),
    ULTRASSOM("Ultrassom"),
    ELETROCARDIOGRAMA("Eletrocardiograma"),
    HEMOGRAMA("Hemograma"),
    GLICEMIA("Glicemia"),
    COLESTEROL("Colesterol"),
    ENDOSCOPIA("Endoscopia"),
    COLONOSCOPIA("Colonoscopia"),
    DENSITOMETRIA_OSSEA("Densitometria Óssea"),
    ECOCARDIOGRAMA("Ecocardiograma");

    private final String descricao;

    TipoExame(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {return descricao; }

    @Override
    public String toString() {
        return descricao;
    }
}
