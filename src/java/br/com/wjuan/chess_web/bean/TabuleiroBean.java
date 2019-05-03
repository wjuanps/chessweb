/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.bean;

import br.com.wjuan.chess_web.model.bo.Chess;
import br.com.wjuan.chess_web.model.bo.TabuleiroBO;
import br.com.wjuan.chess_web.model.vo.Jogador;
import br.com.wjuan.chess_web.model.vo.Move;
import br.com.wjuan.chess_web.model.vo.Peca;
import br.com.wjuan.chess_web.model.vo.Peca.TipoPeca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.network.ChessCliente;
import br.com.wjuan.chess_web.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Sophia
 */
@ManagedBean
@SessionScoped
public class TabuleiroBean implements Serializable {

    /**
     * Para Partidas Online *
     */
    private ChessCliente chessCliente = null;
    private final StringBuilder minhasPecas = new StringBuilder();
    private final List<String> mensagem = new ArrayList<>();
    private final List<String> chats = new ArrayList<>();
    private boolean jogadaRealizada = false;
    private boolean proximoJogador = false;
    private boolean online = false;
    private String nomeJogador;
    private String chat;
    /**
     * Para Partidas Online *
     */

    private List<List<Peca>> tabuleiroView;

    private Chess chess;
    private TabuleiroBO tabuleiro;

    private int click = 0;
    private boolean primeiraJogada = true;
    private boolean reiEmXeque = false;
    private boolean computador = true;
    private boolean promocaoPeao = false;

    private boolean mate = false;
    private boolean empate = false;

    private Posicao pecaSelecionada = null;
    private Posicao pecaPromocaoPeao = null;

    private final List<Posicao> listaPossiveisMovimentos = new ArrayList<>();
    private List<Posicao> listaPossiveisCapturas = new ArrayList<>();
    private final List<String> listaPecasCapturadas = new ArrayList<>();
    private List<Posicao> listaEnPassant;

    private final String[] imagensPecas = {
        "", "preta-peao", "preta-torre", "preta-cavalo", "preta-bispo", "preta-rainha", "preta-rei",
        "branca-peao", "branca-torre", "branca-cavalo", "branca-bispo", "branca-rainha", "branca-rei"
    };

    @PostConstruct
    public void init() {
        this.tabuleiro = new TabuleiroBO();
        this.chess = new Chess();
        this.tabuleiroView = new ArrayList<>();
        List<List<Integer>> tab = this.tabuleiro.getTabuleiro();

        for (int i = 0; i < 8; i++) {
            final List<Peca> pnts = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                final String fundo = ((i + j) % 2 == 0) ? "#cecece" : "#00001f";
                pnts.add(new Peca(new Posicao(i, j), fundo, imagensPecas[tab.get(i).get(j)].concat(".png")));
            }
            this.tabuleiroView.add(pnts);
        }

        atualizarTabuleiro();
    }

    /**
     *
     */
    public void enviarMensagem() {
        chessCliente.enviarMensagem(chat.replaceAll(":", ""));
        chat = "";
    }

    /**
     *
     */
    public synchronized void jogarOnline() {
        if (chessCliente == null) {
            chessCliente = new ChessCliente(this);
            if (chessCliente.inicializarCliente(nomeJogador)) {
                online = true;
                while (getMinhasPecas().isEmpty()) {
                    try {
                        wait(1000);
                    } catch (InterruptedException ex) {
                    }
                }

                reiniciarPartida();
                //Inverte o tabuleiro para as pretas
                if (getMinhasPecas().contains("Pretas")) {
                    this.tabuleiroView.removeAll(tabuleiroView);
                    for (int i = 7; i >= 0; i--) {
                        final List<Peca> pecas = new ArrayList<>();
                        for (int j = 7; j >= 0; j--) {
                            final String fundo = ((i + j) % 2 == 0) ? "#cecece" : "#00001f";
                            pecas.add(new Peca(new Posicao(i, j), fundo, imagensPecas[tabuleiro.getTabuleiro().get(i).get(j)].concat(".png")));
                        }
                        this.tabuleiroView.add(pecas);
                    }
                }

                computador = false;
            } else {
                chessCliente = null;
            }
        }
    }

    /**
     *
     */
    public void reiniciarPartida() {
        this.tabuleiro = new TabuleiroBO();

        Jogador.BRANCAS = true;
        Jogador.PRETAS = false;

        primeiraJogada = true;
        reiEmXeque = false;
        empate = false;
        click = 0;
        mate = false;

        this.atualizarTabuleiro();
    }

    /**
     *
     * @param p
     */
    public synchronized void ciclo(Posicao p) {

        if (mate || empate) {
            return;
        }

        if (online && ((getMinhasPecas().contains("Brancas") && Jogador.PRETAS)
                || (getMinhasPecas().contains("Pretas") && Jogador.BRANCAS))) {
            return;
        }

        click++;

        if (primeiraJogada && !Peca.isBranca(p, this.tabuleiro) && click == 1) {
            FacesUtil.showMessage(FacesMessage.SEVERITY_ERROR, "Chess Web - By Juan Soares", "O jogo começa com as Brancas!!");
            click = 0;
        } else {
            if ((Jogador.BRANCAS && !Peca.isBranca(p, this.tabuleiro) || Jogador.PRETAS && Peca.isBranca(p, this.tabuleiro))
                    && !p.isVazio(this.tabuleiro) && !listaPossiveisCapturas.contains(p)) {

                this.listaPossiveisMovimentos.removeAll(listaPossiveisMovimentos);
                this.listaPossiveisCapturas = new ArrayList<>();
                this.atualizarTabuleiro();
                FacesUtil.showMessage(FacesMessage.SEVERITY_ERROR, "Chess Web - By Juan Soares", "Movimento inválido!!");

                click = 0;
            } else {
                if (click == 1) {
                    if (p.isVazio(this.tabuleiro)) {
                        click = 0;
                        return;
                    }

                    marcarMovimentosValidos(p);

                    // marca no tabuleiroView um possivel roque
                    if (Peca.getPeca(p, this.tabuleiro).equals(TipoPeca.REI)
                            && tabuleiro.getRei().isPossivelRoque(tabuleiro.getPecas(), p, tabuleiro.getTorre().getPosicaoOriginal())) {
                        if (!reiEmXeque) {
                            this.marcarCasaRoque(tabuleiro.getRei().getRoquePequeno());
                            this.marcarCasaRoque(tabuleiro.getRei().getRoqueGrande());
                        }
                    }
                } else if (click == 2) {

                    final Move move = new Move(pecaSelecionada, p, listaEnPassant);
                    if (online) {

                        this.abrirTelaProPeao(p);
                        chessCliente.enviarJogada(tabuleiro, move);

                        while (!jogadaRealizada) {
                            try {
                                wait(1000);
                            } catch (InterruptedException ex) {
                            }
                        }

                        if (proximoJogador) {
                            Jogador.BRANCAS = !Jogador.BRANCAS;
                            Jogador.PRETAS = !Jogador.PRETAS;

                            listaEnPassant = tabuleiro.getPeao().listaEnPassant(pecaSelecionada, p, tabuleiro.getPecas());
                        }

                        setJogadaRealizada(false);
                        setProximoJogador(false);
                    } else {
                        
                        this.abrirTelaProPeao(p);
                        
                        if (chess.executarJogada(tabuleiro, move)) {
                            if (primeiraJogada) {
                                primeiraJogada = false;
                            }

                            Jogador.BRANCAS = !Jogador.BRANCAS;
                            Jogador.PRETAS = !Jogador.PRETAS;

                            //atualiza a lista se houver a possibilidade de uma captura en passant
                            listaEnPassant = tabuleiro.getPeao().listaEnPassant(pecaSelecionada, p, tabuleiro.getPecas());
                        }
                    }

                    this.atualizarTabuleiro();

                    this.listaPossiveisMovimentos.removeAll(listaPossiveisMovimentos);
                    this.listaPossiveisCapturas = new ArrayList<>();

                    tabuleiro.getRei().setRoqueGrande(null);
                    tabuleiro.getRei().setRoquePequeno(null);
                    this.pecaSelecionada = null;

                    this.click = 0;
                }
            }
        }
    }

    /**
     *
     */
    public void atualizarTabuleiro() {

        final List<List<Integer>> tab = this.tabuleiro.getTabuleiro();
        for (List<Peca> list : this.tabuleiroView) {
            for (Peca p : list) {
                String fundo = ((p.getPosicao().getX() + p.getPosicao().getY()) % 2 == 0) ? "#cecece" : "#00001f";
                p.setFundo(fundo);
                p.setImage(imagensPecas[tab.get(p.getPosicao().getX()).get(p.getPosicao().getY())].concat(".png"));
            }
        }

        marcarChequeMate();
    }

    public void marcarChequeMate() {
        this.reiEmXeque = tabuleiro.reiEmXeque(this.tabuleiro.getTabuleiro());
        this.marcarReiEmCheck(reiEmXeque, tabuleiro.getPosicaoReiEmCheck());

        if (reiEmXeque) {
            if (tabuleiro.xequeMate()) {
                if (!mate) {
                    FacesUtil.showMessage(FacesMessage.SEVERITY_INFO, "Chess Web - By Juan Soares", "Xeque Mate!!");
                }
                mate = true;
                for (Posicao pos : tabuleiro.getRei().pecasPossibilitaramMate(tabuleiro.getPecas())) {
                    view:
                    for (List<Peca> list : tabuleiroView) {
                        for (Peca peca : list) {
                            if (peca.getPosicao().equals(pos)) {
                                peca.setFundo("#f97377");
                                break view;
                            }
                        }
                    }
                }
            }
        } else {
            if (tabuleiro.empatePorAfogamentoRei()) {
                empate = true;
                if (!empate) {
                    FacesUtil.showMessage(FacesMessage.SEVERITY_INFO, "Chess Web - By Juan Soares", "Empate por afogamento do Rei!!");
                }
            }
        }
    }

    /**
     *
     * @param posicaoDestino
     */
    private void movimentar(Posicao posicaoDestino) {

        this.tabuleiro.movimentar(pecaSelecionada, posicaoDestino);
        this.primeiraJogada = false;
        Jogador.BRANCAS = !Jogador.BRANCAS;
        Jogador.PRETAS = !Jogador.PRETAS;

        listaEnPassant = tabuleiro.getPeao().listaEnPassant(pecaSelecionada, posicaoDestino, tabuleiro.getPecas());
    }

    /**
     *
     * @param origem
     */
    private void marcarMovimentosValidos(Posicao origem) {
        this.pecaSelecionada = origem;

        this.listaPossiveisMovimentos.addAll(tabuleiro.listaPossiveisMovimentos(origem, this.tabuleiro.getTabuleiro()));
        this.listaPossiveisCapturas.addAll(tabuleiro.listaPossiveisCapturas(origem, this.tabuleiro.getTabuleiro()));

        for (Posicao pos : this.listaPossiveisMovimentos) {
            view:
            for (List<Peca> list : tabuleiroView) {
                for (Peca peca : list) {
                    if (peca.getPosicao().equals(pos)) {
                        peca.setFundo("#5fc4e0");
                        break view;
                    }
                }
            }
        }

        for (Posicao pos : this.listaPossiveisCapturas) {
            if (pos.isPosicaoValida()) {
                view:
                for (List<Peca> list : tabuleiroView) {
                    for (Peca peca : list) {
                        if (peca.getPosicao().equals(pos)) {
                            peca.setFundo("#f8001e");
                            break view;
                        }
                    }
                }
            }
        }

        if (this.listaEnPassant != null && !this.listaEnPassant.isEmpty()) {
            if (this.listaEnPassant.contains(origem)) {
                final Posicao pos = tabuleiro.getPeao().getPosicaoEnPassant();
                view:
                for (List<Peca> list : tabuleiroView) {
                    for (Peca peca : list) {
                        if (peca.getPosicao().equals(pos)) {
                            peca.setFundo("#bba8ca");
                            break view;
                        }
                    }
                }
            }
        }

        if (!origem.isVazio(this.tabuleiro)) {
            view:
            for (List<Peca> list : tabuleiroView) {
                for (Peca peca : list) {
                    if (peca.getPosicao().equals(origem)) {
                        peca.setFundo("#f89a17");
                        break view;
                    }
                }
            }
        }
    }

    /**
     *
     * @param isEmCheck
     * @param rei
     */
    public void marcarReiEmCheck(boolean isEmCheck, Posicao rei) {
        if (isEmCheck && rei != null) {
            view:
            for (List<Peca> list : tabuleiroView) {
                for (Peca peca : list) {
                    if (peca.getPosicao().equals(rei)) {
                        peca.setFundo("#f8001e");
                        break view;
                    }
                }
            }
        }
    }

    /**
     *
     * @param origem
     */
    private void marcarCasaRoque(Posicao origem) {
        if (origem != null) {
            view:
            for (List<Peca> list : tabuleiroView) {
                for (Peca peca : list) {
                    if (peca.getPosicao().equals(origem)) {
                        peca.setFundo("#bba8ca");
                        break view;
                    }
                }
            }
        }
    }

    /**
     *
     */
    public void openDialg() {
        final Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);

        RequestContext.getCurrentInstance().openDialog("promocao-peao", opcoes, null);
    }

    /**
     *
     * @param i
     */
    public void promoverPeao(Integer i) {
        RequestContext.getCurrentInstance().closeDialog(i);
    }

    /**
     *
     * @param event
     */
    public void promocaoPeao(SelectEvent event) {
        this.tabuleiro.getTabuleiro().get(pecaPromocaoPeao.getX()).set(pecaPromocaoPeao.getY(), (Integer) event.getObject());
        this.pecaPromocaoPeao = null;
        this.promocaoPeao = false;
        this.reiEmXeque = tabuleiro.reiEmXeque(this.tabuleiro.getTabuleiro());
        this.atualizarTabuleiro();
    }

    /**
     * 
     * @param destino 
     */
    public void abrirTelaProPeao(Posicao destino) {
        if (Peca.getPeca(pecaSelecionada, this.tabuleiro).equals(TipoPeca.PEAO)) {
            if (tabuleiro.getPeao().isPromocaoPeao(pecaSelecionada, destino)) {
                this.openDialg();
                this.pecaPromocaoPeao = destino;
                this.promocaoPeao = true;
            }
        }
    }

    /**
     *
     * @param jogador
     * @return
     */
    public boolean jogadorAtual(String jogador) {
        switch (jogador) {
            case "Pretas":
                return Jogador.BRANCAS;
            case "Brancas":
                return Jogador.PRETAS;
            default:
                return true;
        }
    }

    public List<List<Peca>> getTabuleiroView() {
        return tabuleiroView;
    }

    public void setTabuleiroView(List<List<Peca>> tabuleiroView) {
        this.tabuleiroView = tabuleiroView;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public boolean isComputador() {
        return computador;
    }

    public void setComputador(boolean computador) {
        this.computador = computador;
    }

    public List<String> getListaPecasCapturadas() {
        return listaPecasCapturadas;
    }

    public List<String> getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem.add(mensagem);
    }

    public void setChats(String mensagem) {
        this.chats.add(mensagem);
    }

    public List<String> getChats() {
        return chats;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getMinhasPecas() {
        return minhasPecas.toString();
    }

    public void setMinhasPecas(String minhasPecas) {
        this.minhasPecas.append(minhasPecas);
    }

    public boolean isJogadaRealizada() {
        return jogadaRealizada;
    }

    public void setJogadaRealizada(boolean jogadaRealizada) {
        this.jogadaRealizada = jogadaRealizada;
    }

    public TabuleiroBO getTabuleiro() {
        return tabuleiro;
    }

    public void setTabuleiro(TabuleiroBO tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public void setPrimeiraJogada(boolean primeiraJogada) {
        this.primeiraJogada = primeiraJogada;
    }

    public void setProximoJogador(boolean proximoJogador) {
        this.proximoJogador = proximoJogador;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public void jogar() {
        if (computador) {
            if (promocaoPeao) {
                return;
            }

            if (!mate && !empate) {
                if (tabuleiro.jogar()) {
                    this.reiEmXeque = tabuleiro.reiEmXeque(this.tabuleiro.getTabuleiro());

                    Jogador.BRANCAS = true;
                    Jogador.PRETAS = false;

                    if (!this.reiEmXeque) {
                        this.reiEmXeque = tabuleiro.reiEmXeque(this.tabuleiro.getTabuleiro());
                    }
                }
            } else {
                Jogador.PRETAS = false;
            }
        }

        if (click == 0) {
            atualizarTabuleiro();
        }
    }
}
