/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Jogador;
import br.com.wjuan.chess_web.model.vo.Move;
import br.com.wjuan.chess_web.model.vo.Peca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.model.vo.Tabuleiro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sophia
 */
public class TabuleiroBO extends Tabuleiro {
    
    private Posicao posicaoReiEmCheck;
    private final Algorithm algorithm;
    private final Pieces pieces;
    private final List<Piece> pecas;

    private final Rei rei;
    private final Peao peao;
    private final Rainha rainha;
    private final Torre torre;
    private final Bispo bispo;
    private final Cavalo cavalo;

    public TabuleiroBO() {
        super();
        
        this.rei = new Rei(this, new Pieces());
        this.peao = new Peao(this, new Pieces());
        this.cavalo = new Cavalo(this);
        this.rainha = new Rainha(this);
        this.torre = new Torre(this);
        this.bispo = new Bispo(this);
        
        this.algorithm = new Algorithm(this);

        this.pieces = new Pieces(this.rei, this.rainha, this.peao, this.torre, this.bispo, this.cavalo);
        this.pecas = new ArrayList<>(this.pieces.getPecas());
    }

    /**
     *
     * @param origem
     * @param tab
     * @return
     */
    public List<Posicao> listaPossiveisMovimentos(Posicao origem, List<List<Integer>> tab) {

        final List<Posicao> auxList = new ArrayList<>();
        for (Piece peca : pecas) {
            if (peca.getTipoPeca().equals(Peca.getPeca(origem, this))) {
                auxList.addAll(pieces.listaPossiveisMovimentos(origem, this, peca));
                break;
            }
        }

        return (this.listaMovimentosValidos(auxList, origem, tab));
    }

    /**
     *
     * @param posicao
     * @param tab
     * @return
     */
    public List<Posicao> listaPossiveisCapturas(Posicao posicao, List<List<Integer>> tab) {

        final List<Posicao> auxList = new ArrayList<>();
        for (Piece peca : pecas) {
            if (peca.getTipoPeca().equals(Peca.getPeca(posicao, this))) {
                auxList.addAll(pieces.listaPossiveisCapturas(posicao, this, peca));
                break;
            }
        }

        return (this.listaMovimentosValidos(auxList, posicao, tab));
    }

    /**
     *
     * @param origem
     * @param destino
     */
    public void movimentar(Posicao origem, Posicao destino) {

        switch (Peca.getPeca(origem, this)) {
            case REI:
                if (Jogador.BRANCAS) {
                    rei.setPosicaoOriginalBrancas(false);
                } else {
                    rei.setPosicaoOriginalPretas(false);
                }
                break;
            case TORRE:
                Posicao posicoes[] = {
                    new Posicao(7, 0), new Posicao(7, 7),
                    new Posicao(0, 0), new Posicao(0, 7)
                };
                for (int i = 0; i < posicoes.length; i++) {
                    if (origem.equals(posicoes[i])) {
                        torre.getPosicaoOriginal()[i] = false;
                        break;
                    }
                }
                break;
            case PEAO:
                break;
        }

        super.moverPecaNoTabuleiro(new Move(origem, destino));
    }

    /**
     *
     * @param auxList
     * @param origem
     * @return
     */
    private List<Posicao> listaMovimentosValidos(List<Posicao> auxList, Posicao origem, List<List<Integer>> tab) {
        final List<Posicao> listaPossiveisMovimentos = new ArrayList<>();

        for (Posicao destino : auxList) {
            final Move move = new Move(origem, destino);
            move.executarMovimento(this);

            final boolean reiEmCheque = reiEmXeque(tab);
            if (!reiEmCheque) {
                listaPossiveisMovimentos.add(destino);
            }
            move.desfazerMovimento(this);
        }

        return listaPossiveisMovimentos;
    }

    /**
     *
     * @return
     */
    public boolean xequeMate() {
        List<List<Integer>> tab = super.getTabuleiro();
        return ((Jogador.BRANCAS) ? this.fimDeJogo(tab, 6, 13) : this.fimDeJogo(tab, 0, 7));
    }

    /**
     *
     * @return
     */
    public boolean empatePorAfogamentoRei() {
        List<List<Integer>> tabuleiro = super.getTabuleiro();
        return ((Jogador.BRANCAS) ? this.fimDeJogo(tabuleiro, 6, 13) : this.fimDeJogo(tabuleiro, 0, 7));
    }

    /**
     *
     * @param tabuleiro
     * @param i
     * @param i0
     * @return
     */
    private boolean fimDeJogo(List<List<Integer>> tabuleiro, int a, int b) {
        Posicao posicao;
        for (int i = 0; i < tabuleiro.size(); i++) {
            for (int j = 0; j < tabuleiro.get(i).size(); j++) {
                if (tabuleiro.get(i).get(j) != 0 && tabuleiro.get(i).get(j) > a && tabuleiro.get(i).get(j) < b) {
                    posicao = new Posicao(i, j);
                    if (!listaPossiveisMovimentos(posicao, tabuleiro).isEmpty()) {
                        return false;
                    }

                    if (!listaPossiveisCapturas(posicao, tabuleiro).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Posicao getPosicaoReiEmCheck() {
        return posicaoReiEmCheck;
    }

    /**
     *
     * @param tabuleiro
     * @return
     */
    public boolean reiEmXeque(List<List<Integer>> tabuleiro) {
        boolean reiEmXeque = !rei.listaReiEmCheck(tabuleiro, pecas).isEmpty();
        posicaoReiEmCheck = rei.getPosicaoReiEmCheck();
        return reiEmXeque;
    }

    public List<Piece> getPecas() {
        return pecas;
    }

    public Torre getTorre() {
        return torre;
    }

    public Rei getRei() {
        return rei;
    }

    public Peao getPeao() {
        return peao;
    }
    
    public boolean jogar() {

        if (Jogador.BRANCAS) {
            return false;
        }

        Move move = algorithm.nextMove();
        List<List<Integer>> tab = super.getTabuleiro();

        move.executarMovimento(this);

        Move auxMove = null;
        if (reiEmXeque(tab)) {
            move.desfazerMovimento(this);

            ext:
            for (int i = 0; i < tab.size(); i++) {
                for (int j = 0; j < tab.get(i).size(); j++) {
                    if (tab.get(i).get(j) != 0) {
                        final Posicao newPeca = new Posicao(i, j);
                        if (!Peca.isBranca(newPeca, this)) {
                            List<Posicao> list = listaPossiveisCapturas(newPeca, tab);
                            if (!list.isEmpty()) {
                                auxMove = new Move(newPeca, list.get(0));
                                break ext;
                            }   
                        }
                    }
                }
            }
            if (auxMove == null) {
                ext:
                for (int i = 0; i < tab.size(); i++) {
                    for (int j = 0; j < tab.get(i).size(); j++) {
                        if (tab.get(i).get(j) != 0) {
                            final Posicao newPeca = new Posicao(i, j);
                            if (!Peca.isBranca(newPeca, this)) {
                                List<Posicao> list = listaPossiveisMovimentos(newPeca, tab);
                                if (!list.isEmpty()) {
                                    auxMove = new Move(newPeca, list.get(0));
                                    break ext;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (auxMove != null) {
            move = auxMove;
        } else {
            move.desfazerMovimento(this);
        }
        
        this.movimentar(move.getOrigem(), move.getDestino());

        return true;
    }
}
