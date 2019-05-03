/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.network;

import br.com.wjuan.chess_web.model.bo.Chess;
import br.com.wjuan.chess_web.model.bo.TabuleiroBO;
import br.com.wjuan.chess_web.model.vo.Jogador;
import br.com.wjuan.chess_web.model.vo.Move;
import static br.com.wjuan.chess_web.network.Constantes.JOGADOR_BRANCAS;
import static br.com.wjuan.chess_web.network.Constantes.JOGADOR_PRETAS;
import static br.com.wjuan.chess_web.network.Constantes.PECAS;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sophia
 */
public class ChessServidor extends JFrame implements Serializable, Network, Constantes {

    private final JTextArea outputArea;
    private final Player[] players;

    private int jogadorAtual = 0;
    private int qtdJogadores = 0;

    private final StringBuilder mensagem;

    public ChessServidor() {
        super("Servidor WChess web - By Juan Soares");

        this.mensagem = new StringBuilder();

        players = new Player[2];

        this.outputArea = new JTextArea();
        final JScrollPane scroll = new JScrollPane(outputArea);
        this.getContentPane().add(scroll, BorderLayout.CENTER);
        this.outputArea.setEditable(false);
        this.outputArea.setFont(new Font("Serif", Font.PLAIN, 18));
        this.outputArea.setText("Chess Web - By Juan Soares\n");

        //new javax.swing.ImageIcon(getClass().getResource("/images/minus-circled-mini.png"))
        URL url = this.getClass().getResource("/imagens/network.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(image);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 500));
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        this.mostrarMensagem("Servidor Online - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n");
        this.mostrarMensagem("Servidor aguardando jogadores se conectarem...");
    }

    @Override
    public boolean listen() throws BindException {
        return listen(PORTA_PADRAO);
    }

    @Override
    public boolean listen(int porta) throws BindException {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(porta);
            while (true) {
                Socket socket = serverSocket.accept();
                this.players[this.qtdJogadores] = new Player(socket, this.qtdJogadores);
                
                new Thread(this.players[this.qtdJogadores]).start();
                this.qtdJogadores++;
            }
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     *
     * Imprimi na tela do sistema uma mensagem
     * 
     * @param mensagem
     */
    private void mostrarMensagem(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            this.outputArea.append(mensagem);
        });
    }
    
    public StringBuilder getMensagem() {
        return mensagem;
    }
    
    /**
     *
     */
    private class Player implements Runnable {

        private Chess chess = new Chess();

        private String peca;
        private String jogadorNome;
        private int jogadorOponenente;

        private boolean empate = false;
        private boolean mate = false;

        private ObjectOutputStream output;
        private ObjectInputStream input;

        /**
         *
         * @param socket
         * @param number
         */
        public Player(Socket socket, int number) {
            jogadorAtual = number;
            this.jogadorOponenente = (jogadorAtual + 1) % 2;
            this.peca = PECAS[jogadorAtual];

            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.output.flush();

                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {}
        }

        @Override
        public synchronized void run() {
            try {
                if (qtdJogadores > 2) {
                    
                    this.enviarMensagem("Servidor lotado...\nTente novamente mais tarde!!\n");
                    
                } else {
                    
                    this.enviarMensagem("Peca:" + this.peca);
                    
                    final String nome = getNome();
                    mostrarMensagem("\nJogador " + nome + " conectado - " + this.peca);

                    if (jogadorAtual == JOGADOR_BRANCAS) {
                        
                        this.enviarMensagem("Aguardando por outro jogador...");
                        
                    } else {
                        this.enviarMensagem("Você jogará contra " + players[JOGADOR_BRANCAS].getJogadorNome() + "\nAguardando sua vez...");
                        players[JOGADOR_BRANCAS].enviarMensagem(players[JOGADOR_PRETAS].getJogadorNome() + " se conectou.\nSua vez de jogar.");
                    }

                    Move move = null;
                    TabuleiroBO tabuleiro = null;
                    Object object;
                    do {
                        object = this.input.readObject();
                        if (object instanceof TabuleiroBO) {
                            if (tabuleiro == null) {
                                tabuleiro = (TabuleiroBO) object;
                            }
                        } else if (object instanceof Move) {
                            if (move == null) {
                                move = (Move) object;
                            }
                        } else if (object instanceof String) {
                            String mensagem = (String) object;
                            if (mensagem.startsWith("Chat:")) {
                                if (players[jogadorOponenente] == null) {
                                    
                                    this.enviarMensagem("Aguarde um outro jogador se conectar...");
                                    
                                } else {
                                    StringBuilder msg = new StringBuilder("Chat:");
                                    msg.append(players[jogadorAtual].getJogadorNome());
                                    msg.append(" diz - ");
                                    msg.append(mensagem.split(":")[1]);

                                    for (Player player : players) {
                                        player.enviarMensagem(msg.toString());
                                    }
                                }
                            }
                        }

                        if (tabuleiro != null && move != null && !this.mate && !this.empate) {
                            if (players[JOGADOR_PRETAS] == null) {
                                this.enviarMensagem("Aguarde um outro jogador se conectar...");
                                this.enviarMensagem("Jogada inválida");

                                continue;
                            }

                            if (this.chess.executarJogada(tabuleiro, move)) {
                                for (Player player : players) {
                                    player.enviarMensagem(tabuleiro);
                                }

                                if (gameOver(tabuleiro)) {
                                    continue;
                                }

                                this.enviarMensagem("Aguarde sua vez...");
                                players[jogadorOponenente].enviarMensagem("Sua vez de jogar!!");

                            } else {
                                this.enviarMensagem("Jogada inválida");
                            }

                            tabuleiro = null;
                            move = null;
                        }

                    } while (true);
                }
            } catch (IOException | ClassNotFoundException ex) {}
        }

        /**
         *
         * @param tabuleiro
         * @return
         */
        private boolean gameOver(TabuleiroBO tabuleiro) throws IOException {
            Jogador.BRANCAS = !Jogador.BRANCAS;
            Jogador.PRETAS = !Jogador.PRETAS;
            final boolean xeque = tabuleiro.reiEmXeque(tabuleiro.getTabuleiro());
            if (xeque) {
                if (tabuleiro.xequeMate()) {
                    players[jogadorAtual].enviarMensagem("Você venceu por xeque mate!!");
                    players[jogadorOponenente].enviarMensagem("Você perdeu por xeque mate!!");
                    this.mate = true;
                }
            } else {
                if (tabuleiro.empatePorAfogamentoRei()) {
                    for (Player player : players) {
                        player.enviarMensagem("O jogo terminou empatado!!");
                    }
                    this.empate = true;
                }
            }
            Jogador.BRANCAS = !Jogador.BRANCAS;
            Jogador.PRETAS = !Jogador.PRETAS;

            return (this.mate || this.empate);
        }

        /**
         *
         * @return @throws IOException
         * @throws ClassNotFoundException
         */
        private String getNome() throws IOException, ClassNotFoundException {
            String nome = (String) this.input.readObject();
            setJogadorNome(nome);
            return getJogadorNome();
        }

        /**
         *
         * @param object
         * @throws IOException
         */
        public void enviarMensagem(Object object) throws IOException {
            this.output.writeObject(object);
            this.output.flush();
        }

        public boolean isEmpate() {
            return empate;
        }

        public void setEmpate(boolean empate) {
            this.empate = empate;
        }

        public String getJogadorNome() {
            return jogadorNome;
        }

        public void setJogadorNome(String jogadorNome) {
            this.jogadorNome = jogadorNome;
        }

    }

    @Override
    public boolean inicializarCliente(String nome) {
        //essa classe não implementa esse método
        return false;
    }
}
