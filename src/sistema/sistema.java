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

        TipoExame[] exames = TipoExame.values();
        for (int i = 0; i < exames.length; i++) {
            System.out.println(i + " - " + exames[i].getDescricao());
        }
        System.out.print("Escolha o exame: ");
        int indiceExame = sc.nextInt();
        sc.nextLine();

        if (indiceExame < 0 || indiceExame >= exames.length) {
            System.out.println("Exame inválido.");
            return;
        }

        TipoExame tipoExame = exames[indiceExame];

        AutorizacaoExame aut = new AutorizacaoExame( (Medico) usuarioAtual, paciente, tipoExame);

        autorizacoes.add(aut);

        System.out.println("Autorização criada com sucesso!");
    }

    private void criarUsuario() {
        if(!(usuarioAtual instanceof Administrador)) {
            System.out.println("Apenas administradores podem criar usuários.");
            return;
        }

        System.out.println("\n=== Criar Usuario ===");

        System.out.println("\nNome: ");
        String nome =  sc.nextLine();

        System.out.println("\nIniciais: ");
        String iniciais =  sc.nextLine();

        System.out.println("\nSelecione o tipo de usuario:");
        System.out.println("\n1 - Administrador");
        System.out.println("\n2 - Médico");
        System.out.println("\n3 - Paciente");
        int tipo =  sc.nextInt();
        sc.nextLine();

        switch (tipo) {
            case 1: usuarios.add(new Administrador(nome, iniciais)); break;
            case 2: usuarios.add(new Medico(nome, iniciais)); break;
            case 3: usuarios.add(new Paciente(nome, iniciais)); break;
            default: System.out.println("Tipo inválido."); break;
        } 
    }

    private void buscarUsuario() {
        if ((usuarioAtual instanceof Administrador)== false) {
            System.out.println("Apenas administradores podem buscar usuários.");
            return;
        }

        sc.nextLine(); // limpa o ENTER que sobrou do nextInt()

        System.out.print("Digite parte do nome do médico/paciente: ");
        String trecho = sc.nextLine().trim().toLowerCase();

        if (trecho.isEmpty()) {
            System.out.println("Busca vazia.");
            return;
        }

        List<Usuario> encontrados = new ArrayList<>();

        for (Usuario u : usuarios) {
            if ((u instanceof Medico || u instanceof Paciente)
                    && u.getNome().toLowerCase().contains(trecho)) {
                encontrados.add(u);
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum médico ou paciente encontrado com esse nome.");
            return;
        }

        System.out.println("\n=== Resultados da busca ===");
        for (int i = 0; i < encontrados.size(); i++) {
            Usuario u = encontrados.get(i);
            String tipo = (u instanceof Medico) ? "Médico" : "Paciente";
            System.out.println(i + " - " + u.getNome() + " (" + tipo + ")");
        }

        System.out.print("Escolha o índice do usuário: ");
        int indice = sc.nextInt();
        sc.nextLine();

        if (indice < 0 || indice >= encontrados.size()) {
            System.out.println("Índice inválido.");
            return;
        }

        Usuario escolhido = encontrados.get(indice);

        System.out.println("\n=== Usuário selecionado ===");
        System.out.println("Nome: " + escolhido.getNome());
        System.out.println("Iniciais: " + escolhido.getIniciais());
        System.out.println("Tipo: " + (escolhido instanceof Medico ? "Médico" : "Paciente"));

        listarAutorizacoesDoUsuario(escolhido);
    }

    private void listarAutorizacoesDoUsuario( Usuario usuario ) {
        List<AutorizacaoExame> lista = new ArrayList<>();

        for ( AutorizacaoExame a : autorizacoes ) {
            if (a.getMedicoSolicitante() == usuario || a.getPaciente() == usuario) {
                lista.add(a);
            }
        }

        if (lista.isEmpty()) {
            System.out.println("Esse usuário não possui autorizações.");
            return;
        }

        lista.sort(Comparator.comparing(AutorizacaoExame::getDataCadastro));

        System.out.println("\n=== Autorizações do usuário ===");
        for (AutorizacaoExame a : lista) {
            System.out.println("Código: " + a.getCodigo());
            System.out.println("Data: " + a.getDataCadastro());
            System.out.println("Médico: " + a.getMedicoSolicitante().getNome());
            System.out.println("Paciente: " + a.getPaciente().getNome());
            System.out.println("Exame: " + a.getExame().getDescricao());

            if (a.getDataRealizacao() != null) {
                System.out.println("Realizado em: " + a.getDataRealizacao());
            } else {
                System.out.println("Realizado em: não realizado");
            }

            System.out.println("------------------------");
        }
    }

    private void estatisticas() {
        if ((usuarioAtual instanceof Administrador ) == false ) {
            System.out.println("Apenas administradores podem ver estatísticas.");
            return;
        }
        int numMedicos = 0;
        int numPacientes = 0;

        for (Usuario u : usuarios) {
            if (u instanceof Medico) {
                numMedicos++;
            } else if (u instanceof Paciente) {
                numPacientes++;
            }
        }

        int totalAutorizacoes = autorizacoes.size();
        int realizadas = 0;

        for ( AutorizacaoExame a : autorizacoes ) {
            if (a.getDataRealizacao() != null) {
                realizadas++;
            }
        }

        double percentualRealizadas = (totalAutorizacoes == 0) ? 0.0 : (realizadas * 100.0/totalAutorizacoes);

        System.out.println("\n=== Estatísticas Gerais ===");
        System.out.println("Número de médicos: " + numMedicos);
        System.out.println("Número de pacientes: " + numPacientes);
        System.out.println("Número de autorizações emitidas: " + totalAutorizacoes);
        System.out.printf("Percentual de autorizações realizadas: %.2f%%%n", percentualRealizadas);
    }
}