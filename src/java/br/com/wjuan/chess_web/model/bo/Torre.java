/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Peca.TipoPeca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.model.vo.Tabuleiro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sophia
 */
public class Torre implements Piece {

    private final Tabuleiro tabuleiro;
    
    private boolean[] posicaoOriginal = {true, true, true, true};

    public Torre(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    /**
     *
     * @return
     */
    public boolean[] getPosicaoOriginal() {
        return posicaoOriginal;
    }

    public void setPosicaoOriginal(boolean[] posicaoOriginal) {
        this.posicaoOriginal = posicaoOriginal;
    }
    
    /**
     *
     * @param origem
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisMovimentos(Posicao origem) {
        final List<Posicao> posicoes = new ArrayList<>();

        posicoes.addAll(Moves.getColuna(origem.getX(), origem.getY(), 8, tabuleiro));
        posicoes.addAll(Moves.getFileira(origem.getX(), origem.getY(), 8, tabuleiro));

        return posicoes;
    }

    /**
     *
     * @param origem
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisCapturas(Posicao origem) {
        final List<Posicao> posicoes = new ArrayList<>();

        posicoes.addAll(Moves.getMinMax(origem.getX(), origem.getY(), 8, Tabuleiro.Direcao.VERTICAL, tabuleiro));
        posicoes.addAll(Moves.getMinMax(origem.getX(), origem.getY(), 8, Tabuleiro.Direcao.HORIZONTAL, tabuleiro));

        return posicoes;
    }

    @Override
    public TipoPeca getTipoPeca() {
        return TipoPeca.TORRE;
    }

    @Override
    public int getValor() {
        return 10;
    }

}
