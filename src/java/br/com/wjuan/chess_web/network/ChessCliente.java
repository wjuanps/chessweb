/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.network;

import br.com.wjuan.chess_web.bean.TabuleiroBean;
import br.com.wjuan.chess_web.model.bo.TabuleiroBO;
import br.com.wjuan.chess_web.model.vo.Move;
import br.com.wjuan.chess_web.util.FacesUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.faces.application.FacesMessage;

/**
 *
 * @author Sophia
 */
public final class ChessCliente implements Serializable, Network, Constantes {

    private final TabuleiroBean tabuleiroBean;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;

    private String nomeJogador;
    private String minhaPeca;

    /**
     * Construtor
     * 
     * @param tabuleiroBean 
     */
    public ChessCliente(TabuleiroBean tabuleiroBean) {
        this.tabuleiroBean = tabuleiroBean;
    }

    /**
     * 
     * Método responsável por fazer a conexão do 
     * cliente com o servidor.
     * 
     * @param nomeJogador
     * @return 
     */
    @Override
    public boolean inicializarCliente(String nomeJogador) {
        this.nomeJogador = nomeJogador;
        try {
            this.connection = new Socket(InetAddress.getByName(HOST), PORTA_PADRAO);
            if (this.connection != null) {
                new Thread(new ThreadServidor(this.connection)).start();
            }
        } catch (UnknownHostException ex) {
            System.out.println("Não foi possivel encontrar o Host especificado!!");
            return false;
        } catch (IOException ex) {
            //FacesUtil.showMessage(FacesMessage.SEVERITY_ERROR, "Não foi posssivel estabelecer conexão!!", "Não foi posssivel estabelecer conexão!!");
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param tabuleiro
     * @param move
     */
    public void enviarJogada(TabuleiroBO tabuleiro, Move move) {
        System.out.println(move.toString());
        try {
            this.output.writeObject(tabuleiro);
            this.output.flush();

            this.output.writeObject(move);
            this.output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param mensagem
     */
    public void enviarMensagem(String mensagem) {
        try {
            this.output.writeObject("Chat:" + mensagem);
            this.output.flush();
        } catch (IOException ex) {
            this.tabuleiroBean.setMensagem("Não foi possivel enviar sua mensagem.");
        }
    }

    /**
     * Classe responsável por enviar mensagens ao servidor e receber as
     * mensagens enviadas pelo servidor.
     *
     * @see ChessServidor
     * @see #inicializarCliente(java.lang.String)
     */
    private class ThreadServidor implements Runnable {

        private final Socket connection;

        public ThreadServidor(Socket socket) {
            this.connection = socket;
            try {
                output = new ObjectOutputStream(this.connection.getOutputStream());
                output.flush();

                input = new ObjectInputStream(this.connection.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ChessCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            try {
                output.writeObject(nomeJogador);
                output.flush();

                String mensagem = "";
                Object object;
                do {
                    object = input.readObject();
                    if (object instanceof String) {
                        mensagem = (String) object;
                        if (mensagem.startsWith("Peca:")) {
                            String peca = mensagem.split(":")[1];
                            tabuleiroBean.setMensagem("Você jogará com as " + peca);
                            tabuleiroBean.setMinhasPecas(peca);
                        } else if (mensagem.contains("Jogada inválida")) {
                            tabuleiroBean.setProximoJogador(false);
                            tabuleiroBean.setJogadaRealizada(true);
                            tabuleiroBean.atualizarTabuleiro();
                            tabuleiroBean.marcarChequeMate();
                        } else if (mensagem.startsWith("Chat:")) {

                            tabuleiroBean.setChats(mensagem.split(":")[1]);

                        } else {
                            tabuleiroBean.setMensagem(mensagem);
                        }
                    } else if (object instanceof TabuleiroBO) {
                        tabuleiroBean.setTabuleiro((TabuleiroBO) object);
                        tabuleiroBean.setJogadaRealizada(true);
                        tabuleiroBean.setProximoJogador(true);
                        tabuleiroBean.setPrimeiraJogada(false);
                        tabuleiroBean.atualizarTabuleiro();
                    }
                } while (!mensagem.equals("Finalizado"));

            } catch (SocketException n) {
                tabuleiroBean.setMensagem("Ops...Ocorreu um erro no servidor. Servidor desconectado.");
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ChessCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean listen() {
        //essa classe não implementa esse método
        return false;
    }

    @Override
    public boolean listen(int porta) {
        //essa classe não implementa esse método
        return false;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public String getMinhaPeca() {
        return minhaPeca;
    }
}
