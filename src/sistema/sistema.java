package sistema;

import model.*;
import java.util.*;

public class Sistema {

    private List<Usuario> usuarios = new ArrayList<>();
    private List<AutorizacaoExame> autorizacoes = new ArrayList<>();
    private Usuario usuarioAtual;
    private Scanner sc;

    public void iniciar() {
        carregarDados();

        sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\nUsuário atual: " + usuarioAtual.getNome());
            System.out.println("1 - Trocar usuário");
            System.out.println("2 - Criar autorização");
            System.out.println("3 - Listar autorizações");
            System.out.println("4 - Marcar exame realizado");
            System.out.println("5 - Minhas autorizações");
            System.out.println("6 - Criar usuário");
            System.out.println("7 - Buscar usuário");
            System.out.println("8 - Estatísticas");
            System.out.println("0 - Sair");

            opcao = sc.nextInt();

            switch (opcao) {
                case 1: trocarUsuario(); break;
                case 2: criarAutorizacao(); break;
                case 3: listarAutorizacoes(); break;
                case 4: marcarRealizado(); break;
                case 5: listarMinhas(); break;
                case 6: criarUsuario(); break;
                case 7: buscarUsuario(); break;
                case 8: estatisticas(); break;
            }

        } while (opcao != 0);

    }

    private void trocarUsuario() {
        System.out.println("\n=== Lista de Usuários ===");
            
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println(i + " - " + usuarios.get(i).getNome());
        }

        System.out.print("Escolha o índice do usuário: ");
        int indice = sc.nextInt();

        if (indice >= 0 && indice < usuarios.size()) {
            usuarioAtual = usuarios.get(indice);
            System.out.println("Usuário alterado para: " + usuarioAtual.getNome());
        } else {
            System.out.println("Índice inválido!");
        }
    }

    private void criarAutorizacao() {
        if (( usuarioAtual instanceof Medico ) == false ) {
            System.out.println( "Apenas médicos podem criar autorizações.");
            return;
        }

        System.out.println("\n=== Criar Autorização ===");

        List<Paciente> pacientes = new ArrayList<>();
        System.out.println("Pacientes disponíveis:");

        for (Usuario u : usuarios) {
            if (u instanceof Paciente) {
                pacientes.add((Paciente) u);
                System.out.println((pacientes.size() - 1) + " - " + u.getNome());
            }
        }

        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }

        System.out.print("Escolha o paciente: ");
        int indice = sc.nextInt();
        sc.nextLine(); 

        if ( indice < 0 || indice >= pacientes.size()) {
            System.out.println("Paciente inválido.");
            return;
        }

        Paciente paciente = pacientes.get(indice);

        System.out.print("Nome do exame: ");
        String nomeExame = sc.nextLine();

        AutorizacaoExame aut = new AutorizacaoExame( (Medico) usuarioAtual, paciente, nomeExame);

        autorizacoes.add(aut);

        System.out.println("Autorização criada com sucesso!");
    }
}