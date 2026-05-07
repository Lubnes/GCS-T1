package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AutorizacaoExame {
    
    // Variável para controlar o incremento do código entre as instâncias
    private static int contadorCodigo = 1000; 

    private int codigo;
    private LocalDate dataCadastro;
    private Usuario medicoSolicitante; 
    private Usuario paciente;          
    private TipoExame exame;
    private LocalDate dataRealizacao;

    // Construtor
    public AutorizacaoExame(Usuario medicoSolicitante, Usuario paciente, TipoExame exame) {
        this.codigo = contadorCodigo++;
        this.dataCadastro = LocalDate.now(); 
        this.medicoSolicitante = medicoSolicitante;
        this.paciente = paciente;
        this.exame = exame;
        this.dataRealizacao = null;
    }

    // Getters
    public int getCodigo() {
        return codigo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Usuario getMedicoSolicitante() {
        return medicoSolicitante;
    }

    public Usuario getPaciente() {
        return paciente;
    }

    public TipoExame getExame() {
        return exame;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    // Retorna true se o exame já foi realizado
    public boolean isRealizado() {
        return dataRealizacao != null;
    }

    //Retorna true se marcou com sucesso, false se falhou na validação.
    public boolean marcarComoRealizado(LocalDate dataDesejada) {
        if (dataDesejada == null) {
            return false;
        }

        if (dataDesejada.isBefore(dataCadastro)) {
            System.out.println("Erro: A data de realização não pode ser anterior à data de cadastro.");
            return false;
        }

        long diasDeDiferenca = ChronoUnit.DAYS.between(dataCadastro, dataDesejada);
        if (diasDeDiferenca > 30) {
            System.out.println("Erro: O prazo máximo para realização do exame é de 30 dias após a solicitação.");
            return false;
        }

        // Se passou pelas validações, registra a data
        this.dataRealizacao = dataDesejada;
        return true;
    }

    @Override
    public String toString() {
        String status = isRealizado() ? "Realizado em: " + dataRealizacao : "Pendente";
        return String.format("[%d] Data: %s | Exame: %-20s | Médico: %-15s | Paciente: %-15s | Status: %s",
                codigo, 
                dataCadastro, 
                exame.getDescricao(), 
                medicoSolicitante.getNome(), 
                paciente.getNome(), 
                status);
    }
}
