/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Jogador;
import br.com.wjuan.chess_web.model.vo.Move;
import br.com.wjuan.chess_web.model.vo.Peca;
import br.com.wjuan.chess_web.model.vo.Peca.TipoPeca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.model.vo.Tabuleiro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sophia
 */
public class Rei implements Piece {

    private final Pieces pieces;
    private final Tabuleiro tabuleiro;
    
    private Posicao roquePequeno = null;
    private Posicao roqueGrande = null;
    private Posicao posicaoReiEmCheck;

    private boolean posicaoOriginalBrancas = true;
    private boolean posicaoOriginalPretas = true;

    public Rei(Tabuleiro tabuleiro, Pieces pieces) {
        this.tabuleiro = tabuleiro;
        this.pieces = pieces;
    }

    /**
     *
     * @param destinoRei
     * @param origemRei
     */
    public void rocar(Posicao destinoRei, Posicao origemRei) {
        int OrigemTorre = (destinoRei.equals(roquePequeno)) ? 7 : 0;
        int destinoTorre = (destinoRei.equals(roquePequeno)) ? 5 : 3;
        int torre = (Jogador.BRANCAS) ? 8 : 2;
        List<List<Integer>> tab = tabuleiro.getTabuleiro();

        final Move movimentarRei = new Move(origemRei, destinoRei);
        movimentarRei.executarMovimento(tabuleiro);
        
        tab.get(origemRei.getX()).set(destinoTorre, torre);
        tab.get(origemRei.getX()).set(OrigemTorre, 0);

        if (Jogador.BRANCAS) {
            posicaoOriginalBrancas = false;
        } else {
            posicaoOriginalPretas = false;
        }
    }

    /**
     *
     * @param pecas
     * @param origem
     * @param b
     * @return
     */
    public boolean isPossivelRoque(List<Piece> pecas, Posicao origem, boolean... b) {
        if ((Jogador.BRANCAS && !posicaoOriginalBrancas)
                || (Jogador.PRETAS && !posicaoOriginalPretas)) {
            return false;
        }

        if (roquePequeno(pecas, origem, b)) {
            roquePequeno = new Posicao(origem.getX(), origem.getY() + 2);
        }

        if (roqueGrande(pecas, origem, b)) {
            roqueGrande = new Posicao(origem.getX(), origem.getY() - 2);
        }

        return (roquePequeno != null || roqueGrande != null);
    }

    /**
     *
     * @param origem
     * @return
     */
    private boolean roquePequeno(List<Piece> pecas, Posicao origem, boolean... b) {

        if ((Jogador.BRANCAS && !b[1])
                || (Jogador.PRETAS && !b[3])) {
            return false;
        }

        final List<List<Integer>> tab = this.tabuleiro.getTabuleiro();

        for (int i = 1; i <= 2; i++) {
            Posicao destino = new Posicao(origem.getX(), origem.getY() + i);
            
            if (!destino.isVazio(this.tabuleiro)) {
                return false;
            }

            final Move move = new Move(origem, destino);
            move.executarMovimento(tabuleiro);
            if (!listaReiEmCheck(tab, pecas).isEmpty()) {
                move.desfazerMovimento(tabuleiro);
                return false;
            }
            
            move.desfazerMovimento(tabuleiro);
        }

        return true;
    }

    /**
     *
     * @param origem
     * @return
     */
    private boolean roqueGrande(List<Piece> pecas, Posicao origem, boolean... b) {

        if ((Jogador.BRANCAS && !b[0])
                || (Jogador.PRETAS && !b[2])) {
            return false;
        }

        final List<List<Integer>> tab = this.tabuleiro.getTabuleiro();

        for (int i = 1; i <= 3; i++) {
            Posicao destino = new Posicao(origem.getX(), origem.getY() - i);
            if (!destino.isVazio(this.tabuleiro)) {
                return false;
            }

            final Move move = new Move(origem, destino);
            move.executarMovimento(this.tabuleiro);
            if (!listaReiEmCheck(tab, pecas).isEmpty()) {
                move.desfazerMovimento(this.tabuleiro);
                return false;
            }
            
            move.desfazerMovimento(this.tabuleiro);
        }

        return true;
    }

    /**
     *
     * @param tabuleiro
     * @param pecas
     * @return
     */
    public List<Posicao> listaReiEmCheck(List<List<Integer>> tabuleiro, List<Piece> pecas) {
        int a = (Jogador.BRANCAS) ? 12 : 6;

        final List<Posicao> lista = new ArrayList<>();
        Posicao rei = null;
        externo:
        for (int i = 0; i < tabuleiro.size(); i++) {
            for (int j = 0; j < tabuleiro.get(i).size(); j++) {
                if (tabuleiro.get(i).get(j) == a) {
                    rei = new Posicao(i, j);
                    break externo;
                }
            }
        }

        posicaoReiEmCheck = rei;
        for (int i = 0; i < tabuleiro.size(); i++) {
            for (int j = 0; j < tabuleiro.get(i).size(); j++) {
                if (tabuleiro.get(i).get(j) != 0) {
                    for (Piece peca : pecas) {
                        Posicao origem = new Posicao(i, j);
                        if (peca.getTipoPeca().equals(Peca.getPeca(origem, this.tabuleiro))) {
                            final List<Posicao> aux = pieces.listaPossiveisCapturas(origem, this.tabuleiro, peca);
                            if (aux.contains(rei)) {
                                lista.add(origem);
                            }
                        }
                    }
                    if (!lista.isEmpty()) {
                        return lista;
                    }
                }
            }
        }

        posicaoReiEmCheck = null;
        return lista;
    }

    /**
     *
     * @param pecas
     * @return
     */
    public List<Posicao> pecasPossibilitaramMate(List<Piece> pecas) {
        List<List<Integer>> tabuleiro = this.tabuleiro.getTabuleiro();
        final List<Posicao> lista = new ArrayList<>();
        final Posicao rei = this.posicaoReiEmCheck;

        for (int i = 0; i < tabuleiro.size(); i++) {
            for (int j = 0; j < tabuleiro.get(i).size(); j++) {
                Posicao origem = new Posicao(i, j);
                if (!Peca.isVazio(origem, this.tabuleiro)) {
                    if (origem.isMesmaCor(rei, this.tabuleiro)) {
                        for (Piece piece : pecas) {
                            if (piece.getTipoPeca().equals(Peca.getPeca(origem, this.tabuleiro))) {
                                final List<Posicao> todosOsMovimentos = new ArrayList<>(pieces.listaPossiveisCapturas(origem, this.tabuleiro, piece));
                                todosOsMovimentos.addAll(pieces.listaPossiveisMovimentos(origem, this.tabuleiro, piece));

                                if (!todosOsMovimentos.isEmpty()) {
                                    for (Posicao destino : todosOsMovimentos) {
                                        final Move move = new Move(origem, destino);
                                        move.executarMovimento(this.tabuleiro);

                                        final List<Posicao> listaAux = listaReiEmCheck(tabuleiro, pecas);
                                        if (!listaAux.isEmpty()) {
                                            lista.addAll(listaAux);
                                        }

                                        move.desfazerMovimento(this.tabuleiro);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return lista;
    }

    /**
     *
     * @param origem
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisMovimentos(Posicao origem) {
        final List<Posicao> destinos = new ArrayList<>();
        final List<Posicao> aux = new ArrayList<>();

        destinos.addAll(Moves.getColuna(origem.getX(), origem.getY(), 1, tabuleiro));
        destinos.addAll(Moves.getFileira(origem.getX(), origem.getY(), 1, tabuleiro));

        Moves.getDiagonal(origem.getX(), origem.getY(), tabuleiro).forEach((des) -> {
            des.forEach((destino) -> {
                if (destino.isVazio(this.tabuleiro)) {
                    aux.add(destino);
                }
            });
        });

        Posicao destino;
        for (Posicao pt : aux) {
            for (int i = -1; i < 2; i += 2) {
                if (pt.equals(destino = new Posicao(origem.getX() + (1 * i), origem.getY() + (1 * i)))
                        || pt.equals(destino = new Posicao(origem.getX() + (-(1 * i)), origem.getY() + (1 * i)))) {
                    if (destino.isVazio(this.tabuleiro)) {
                        destinos.add(destino);
                    }
                }
            }
        }

        return destinos;
    }

    /**
     *
     * @param origem
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisCapturas(Posicao origem) {
        final List<Posicao> posicoes = new ArrayList<>();

        Posicao destino;
        for (int i = -1; i < 2; i += 2) {
            if (!(destino = new Posicao(origem.getX() + (i * 1), origem.getY() + (i * 1))).isVazio(this.tabuleiro)) {
                posicoes.add(destino);
            }

            if (!(destino = new Posicao(origem.getX() + (-(1 * i)), origem.getY() + (1 * i))).isVazio(this.tabuleiro)) {
                posicoes.add(destino);
            }

            if (!(destino = new Posicao(origem.getX() + (-(1 * i)), origem.getY())).isVazio(this.tabuleiro)) {
                posicoes.add(destino);
            }

            if (!(destino = new Posicao(origem.getX(), origem.getY() + (-(1 * i)))).isVazio(this.tabuleiro)) {
                posicoes.add(destino);
            }
        }

        return posicoes;
    }

    @Override
    public TipoPeca getTipoPeca() {
        return TipoPeca.REI;
    }

    @Override
    public int getValor() {
        return 100;
    }
    
    public Posicao getRoquePequeno() {
        return roquePequeno;
    }

    public void setRoquePequeno(Posicao roquePequeno) {
        this.roquePequeno = roquePequeno;
    }

    public Posicao getRoqueGrande() {
        return roqueGrande;
    }

    public void setRoqueGrande(Posicao roqueGrande) {
        this.roqueGrande = roqueGrande;
    }

    public Posicao getPosicaoReiEmCheck() {
        return posicaoReiEmCheck;
    }

    public boolean isPosicaoOriginalBrancas() {
        return posicaoOriginalBrancas;
    }

    public void setPosicaoOriginalBrancas(boolean posicaoOriginalBrancas) {
        this.posicaoOriginalBrancas = posicaoOriginalBrancas;
    }

    public boolean isPosicaoOriginalPretas() {
        return posicaoOriginalPretas;
    }

    public void setPosicaoOriginalPretas(boolean posicaoOriginalPretas) {
        this.posicaoOriginalPretas = posicaoOriginalPretas;
    }
}
