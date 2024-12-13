package com.senai;
//Sistema de Cadastro com Menu e Funções

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class CadastroDeUsuario {
    static String[] cabecalho = {"ID", "Nome", "Telefone", "Email"};
    static String[][] matrizCadastro = {{"", ""}};
    static Scanner scanner = new Scanner(System.in);
    static File arquivoBancoDeDados = new File(System.getProperty("user.home"), "bancoDeDados.txt");

    public static void main(String[] args) {


        matrizCadastro[0] = cabecalho;
        String menu = """
                _______________________________________
                |   Escolha uma opção:                |
                |       1- Exibir Usuarios            |
                |       2- Cadastrar Usuario          |
                |       3- Atualizar Usuario          |
                |       4- Deletar usuario            |
                |       5- Sair                       |
                |_____________________________________|
                """;

        int opcao;
        do {
            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    exibirUsuarios();
                    break;
                case 2:
                    cadastrarUsuarios();
                    break;
                case 3:
                    atualizarUsuarios();
                    break;
                case 4:
                    deletarUsuarios();
                    break;
                case 5:
                    System.out.println("Programa encerrado.");
                    scanner.close();
                    break;
                default:
                    System.out.println("Digite um número válido.");
            }
        } while (opcao != 5);
    }

    public static void exibirUsuarios() {
        StringBuilder tabela = new StringBuilder();
        for (String[] linha : matrizCadastro) {
            for (int coluna = 0; coluna < linha.length; coluna++) {
                int tamanhoColuna = coluna == 0 ? 5 : (coluna == 2 ? 10 : 25);
                tabela.append(String.format("%-" + tamanhoColuna + "s |", linha[coluna]));
            }
            tabela.append("\n");
        }
        System.out.println(tabela);
    }

    public static void cadastrarUsuarios() {
        System.out.print("Quantas pessoas você deseja cadastrar?");
        int qtdUsuarios = scanner.nextInt();
        scanner.nextLine();

        String[][] novaMatriz = new String[matrizCadastro.length + qtdUsuarios][cabecalho.length];
        for (int linha = 0; linha < matrizCadastro.length; linha++) {
            novaMatriz[linha] = Arrays.copyOf(matrizCadastro[linha], matrizCadastro[linha].length);
        }
        System.out.println("Preencha os dados a seguir: ");
        for (int linha = matrizCadastro.length; linha < novaMatriz.length; linha++) {

            System.out.println(cabecalho[0] + linha);
            novaMatriz[linha][0] = String.valueOf(linha);

            for (int coluna = 1; coluna < cabecalho.length; coluna++) {
                System.out.print(cabecalho[coluna] + ": ");
                novaMatriz[linha][coluna] = scanner.nextLine();
            }

        }
        matrizCadastro = novaMatriz;
        salvarDadosNoArquivo();
    }

    public static void atualizarUsuarios() {
        exibirUsuarios();
        System.out.println("\nDigite o id do usuário que deseja atualizar:");
        int idEscolhido = scanner.nextInt();
        scanner.nextLine();

        System.out.println(cabecalho[0] + " - " + idEscolhido);
        for (int coluna = 1; coluna < cabecalho.length; coluna++) {
            System.out.println(cabecalho[coluna] = ": ");
            matrizCadastro[idEscolhido][coluna] = scanner.nextLine();
        }
        exibirUsuarios();
        salvarDadosNoArquivo();
    }

    public static void deletarUsuarios() {
        System.out.println("Digite o ID do usuário que deseja deletar o registro.");
        int idEscolhido = scanner.nextInt();
        scanner.nextLine();
        String[][] novaMatriz = new String[matrizCadastro.length - 1][cabecalho.length];
        novaMatriz[0] = cabecalho;
        for (int linha = 1, idNovaMatriz = 1; linha < matrizCadastro.length; linha++) {

            if (linha == idEscolhido) {
                continue;

            }
            novaMatriz[idNovaMatriz] = Arrays.copyOf(matrizCadastro[linha], matrizCadastro[linha].length);
            novaMatriz[idNovaMatriz][0] = String.valueOf(idNovaMatriz);
            idNovaMatriz++;
        }
        matrizCadastro = novaMatriz;
        System.out.println("Usuário deletado com sucesso!");
        exibirUsuarios();
        salvarDadosNoArquivo();
    }

    public static void salvarDadosNoArquivo() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(arquivoBancoDeDados))) {
            for (String[] linha : matrizCadastro) {
                bufferedWriter.write(String.join(",", linha) + "\n");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void carregarDadosNoArquivo() {
        if (!arquivoBancoDeDados.exists()) {
            try {
                if (arquivoBancoDeDados.createNewFile()) {
                    System.out.println("Arquivo" + arquivoBancoDeDados.getName() + " criado com sucesso");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivoBancoDeDados))) {
                String linha;
                StringBuilder conteudoArquivo = new StringBuilder();
                while ((linha = bufferedReader.readLine()) != null) {
                    if (!linha.trim().isEmpty()) {
                        conteudoArquivo.append(linha).append("\n");
                    }
                }
                String[] linhaDadosUsuario = conteudoArquivo.toString().split("\n");
                matrizCadastro = new String[linhaDadosUsuario.length][cabecalho.length];

                for (int i = 0; i < linhaDadosUsuario.length; i++) {
                    matrizCadastro[i] = linhaDadosUsuario[i].split(",");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
