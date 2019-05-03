/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Jogador;
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
public class Peao implements Piece {

    private final Tabuleiro tabuleiro;
    
    private Posicao posicaoEnPassant = null;
    private Posicao posicaoPecaCapturadaEnPassant = null;
    private final Rei rei;

    public Peao(Tabuleiro tabuleiro, Pieces pieces) {
        this.tabuleiro = tabuleiro;
        this.rei = new Rei(this.tabuleiro, pieces);
    }
    
    /**
     *
     * @param origem
     * @param destino
     * @param pecas
     * @return
     */
    public List<Posicao> listaEnPassant(Posicao origem, Posicao destino, List<Piece> pecas) {
        final List<Posicao> listaEnPassant = new ArrayList<>();
        posicaoEnPassant = null;
        posicaoPecaCapturadaEnPassant = null;

        if (Peca.getPeca(destino, tabuleiro).equals(this.getTipoPeca())) {
            if ((origem.getX() == 1 && destino.getX() == 3)
                    || (origem.getX() == 6 && destino.getX() == 4)) {
                for (int i = -1; i < 2; i += 2) {
                    Posicao p = new Posicao(destino.getX(), destino.getY() + (i * 1));
                    if (p.isPosicaoValida()) {
                        if (Peca.getPeca(p, tabuleiro).equals(this.getTipoPeca())) {
                            List<List<Integer>> tab = this.tabuleiro.getTabuleiro();
                            int aux = tab.get(p.getX()).get(p.getY());
                            tab.get(p.getX()).set(p.getY(), 0);

                            if (rei.listaReiEmCheck(tab, pecas).isEmpty()) {
                                final int x = (Peca.isBranca(destino, this.tabuleiro)) ? destino.getX() + 1 : destino.getX() - 1;
                                posicaoEnPassant = new Posicao(x, destino.getY());
                                posicaoPecaCapturadaEnPassant = destino;

                                listaEnPassant.add(p);
                            }
                            
                            tab.get(p.getX()).set(p.getY(), aux);
                        }
                    }
                }
            }
        }

        return listaEnPassant;
    }

    /**
     *
     * @param origem
     * @param destino
     * @return
     */
    public boolean isPromocaoPeao(Posicao origem, Posicao destino) {
        if (Peca.getPeca(origem, this.tabuleiro).equals(this.getTipoPeca())) {
            return ((Jogador.BRANCAS && destino.getX() == 0)
                    || (Jogador.PRETAS && destino.getX() == 7));
        }
        return false;
    }

    /**
     *
     * @param origem
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisMovimentos(Posicao origem) {
        final List<Posicao> posicoes = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            int aux = (Jogador.BRANCAS) ? origem.getX() - i : origem.getX() + i;
            Posicao posicao = new Posicao(aux, origem.getY());
            if (posicao.isPosicaoValida()) {
                posicoes.add(posicao);

                if ((origem.getX() != 6 && origem.getX() != 1)
                        || !posicao.isVazio(this.tabuleiro)) {
                    break;
                }
            }
        }

        return posicoes;
    }

    /**
     *
     * @param posicaoSelecionada
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisCapturas(Posicao posicaoSelecionada) {
        final List<Posicao> pontos = new ArrayList<>();
        int aux = (Jogador.BRANCAS) ? posicaoSelecionada.getX() - 1 : posicaoSelecionada.getX() + 1;

        Posicao posicao;
        for (int i = -1; i < 2; i += 2) {
            if (!(posicao = new Posicao(aux, posicaoSelecionada.getY() + (1 * i))).isVazio(this.tabuleiro)) {
                pontos.add(posicao);
            }
        }
        return pontos;
    }

    public Posicao getPosicaoEnPassant() {
        return posicaoEnPassant;
    }

    public Posicao getPosicaoPecaCapturadaEnPassant() {
        return posicaoPecaCapturadaEnPassant;
    }

    @Override
    public TipoPeca getTipoPeca() {
        return TipoPeca.PEAO;
    }

    @Override
    public int getValor() {
        return 1;
    }
}
