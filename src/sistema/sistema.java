package sistema;

import model.*;
import java.util.*;

public class Sistema {

    private List<Usuario> usuarios = new ArrayList<>();
    private List<AutorizacaoExame> autorizacoes = new ArrayList<>();
    private Usuario usuarioAtual;

    public void iniciar() {
        carregarDados();

        Scanner sc = new Scanner(System.in);
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