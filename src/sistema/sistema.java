package sistema;

import model.*;
import java.util.*;
import java.time.LocalDate;

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
            sc.nextLine();

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

    private void listarMinhas() {
        if (!(usuarioAtual instanceof Paciente)) {
            System.out.println("Apenas pacientes podem ver suas autorizações.");
            return;
        }

        System.out.println("\n=== Minhas Autorizações ===");
        Paciente paciente = (Paciente) usuarioAtual;

        List<AutorizacaoExame> minhas = new ArrayList<>(paciente.getAutorizacoes());
        minhas.sort(Comparator.comparing(AutorizacaoExame::getDataCadastro));

        if (minhas.isEmpty()) System.out.println("Nenhuma autorização encontrada.");
        else minhas.forEach(System.out::println);
    }

    private void marcarRealizado() {
        if (!(usuarioAtual instanceof Paciente)) {
            System.out.println("Apenas pacientes podem marcar exames como realizados.");
            return;
        }

        Paciente paciente = (Paciente) usuarioAtual;
        List<AutorizacaoExame> pendentes = new ArrayList<>();

        for (AutorizacaoExame a : paciente.getAutorizacoes())
            if (!a.isRealizado()) pendentes.add(a);

        if (pendentes.isEmpty()) {
            System.out.println("Nenhuma autorização pendente.");
            return;
        }

        System.out.println("\n=== Marcar Exame como Realizado ===");
        for (int i = 0; i < pendentes.size(); i++)
            System.out.println(i + " - " + pendentes.get(i));

        System.out.print("Escolha a autorização: ");
        int idx = sc.nextInt(); sc.nextLine();
        if (idx < 0 || idx >= pendentes.size()) { System.out.println("Inválido."); return; }

        System.out.print("Data de realização (AAAA-MM-DD): ");
        String dataStr = sc.nextLine();

        try {
            LocalDate data = LocalDate.parse(dataStr);
            boolean ok = pendentes.get(idx).marcarComoRealizado(data);
            if (ok) System.out.println("Exame marcado como realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Data inválida. Use o formato AAAA-MM-DD.");
        }
    }

    private void listarAutorizacoes() {
        if (!(usuarioAtual instanceof Medico)) {
            System.out.println("Apenas médicos podem listar autorizações.");
            return;
        }

        System.out.println("\n=== Listar Autorizações ===");
        System.out.println("Filtrar por:");
        System.out.println("1 - Paciente");
        System.out.println("2 - Tipo de exame");
        System.out.print("Opção: ");
        int opcao = sc.nextInt();
        sc.nextLine();

        List<AutorizacaoExame> resultado = new ArrayList<>();

        if (opcao == 1) {
            List<Paciente> pacientes = new ArrayList<>();
            for (Usuario u : usuarios)
                if (u instanceof Paciente) {
                    pacientes.add((Paciente) u);
                    System.out.println((pacientes.size() - 1) + " - " + u.getNome());
                }
            System.out.print("Escolha o paciente: ");
            int idx = sc.nextInt(); sc.nextLine();
            if (idx < 0 || idx >= pacientes.size()) { System.out.println("Inválido."); return; }
            Paciente p = pacientes.get(idx);
            for (AutorizacaoExame a : autorizacoes)
                if (a.getPaciente().equals(p)) resultado.add(a);

        } else if (opcao == 2) {
            TipoExame[] exames = TipoExame.values();
            for (int i = 0; i < exames.length; i++)
                System.out.println(i + " - " + exames[i].getDescricao());
            System.out.print("Escolha o exame: ");
            int idx = sc.nextInt(); sc.nextLine();
            if (idx < 0 || idx >= exames.length) { System.out.println("Inválido."); return; }
            TipoExame tipo = exames[idx];
            for (AutorizacaoExame a : autorizacoes)
                if (a.getExame().equals(tipo)) resultado.add(a);

        } else {
            System.out.println("Opção inválida.");
            return;
        }

        resultado.sort(Comparator.comparing(AutorizacaoExame::getDataCadastro));
        if (resultado.isEmpty()) System.out.println("Nenhuma autorização encontrada.");
        else resultado.forEach(System.out::println);
        }

    private void carregarDados() {
        Administrador admin1 = new Administrador("Guilherme Admin", "GU");
        Administrador admin2 = new Administrador("Fernando Admin", "FE");

        Medico med1 = new Medico("Dr. Roberto Silva", "RS");
        Medico med2 = new Medico("Dra. Ana Souza", "AS");
        Medico med3 = new Medico("Dr Paulo Mendes", "PM");
        Medico med4 = new Medico("Dra. Julia Lima", "JL");

        Paciente pac1 = new Paciente("Joao Oliveira", "JO");
        Paciente pac2 = new Paciente("Maria Santos", "MS");
        Paciente pac3 = new Paciente("Lucas Ferreira", "LF");
        Paciente pac4 = new Paciente("Beatriz Costa", "BC");
        Paciente pac5 = new Paciente("Tiago Almeida", "TA");

        usuarios.addAll(Arrays.asList(admin1, admin2, med1, med2, med3, med4, pac1, pac2, pac3, pac4, pac5));

        AutorizacaoExame a1 = new AutorizacaoExame(med1, pac1, TipoExame.RAIO_X);
        AutorizacaoExame a2 = new AutorizacaoExame(med1, pac2, TipoExame.HEMOGRAMA);
        AutorizacaoExame a3 = new AutorizacaoExame(med2, pac1, TipoExame.TOMOGRAFIA);
        AutorizacaoExame a4 = new AutorizacaoExame(med2, pac3, TipoExame.ULTRASSOM);
        AutorizacaoExame a5 = new AutorizacaoExame(med3, pac4, TipoExame.ELETROCARDIOGRAMA);
        AutorizacaoExame a6 = new AutorizacaoExame(med3, pac5, TipoExame.GLICEMIA);
        AutorizacaoExame a7 = new AutorizacaoExame(med4, pac2, TipoExame.COLONOSCOPIA);
        AutorizacaoExame a8 = new AutorizacaoExame(med4, pac3, TipoExame.ECOCARDIOGRAMA);
        AutorizacaoExame a9 = new AutorizacaoExame(med1, pac4, TipoExame.RESSONANCIA_MAGNETICA);
        AutorizacaoExame a10 = new AutorizacaoExame(med2, pac5, TipoExame.DENSITOMETRIA_OSSEA);

        autorizacoes.addAll(Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10));

        pac1.adicionarAutorizacao(a1);
        pac1.adicionarAutorizacao(a3);
        pac2.adicionarAutorizacao(a2);
        pac2.adicionarAutorizacao(a7);
        pac3.adicionarAutorizacao(a4);
        pac3.adicionarAutorizacao(a8);
        pac4.adicionarAutorizacao(a5);
        pac4.adicionarAutorizacao(a9);
        pac5.adicionarAutorizacao(a6);
        pac5.adicionarAutorizacao(a10);

        usuarioAtual = admin1;
}
}