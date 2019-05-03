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
public class Rainha implements Piece {

    private final Tabuleiro tabuleiro;

    public Rainha(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    /**
     *
     * @param posicao
     * @return
     */
    @Override
    public List<Posicao> listaPossiveisMovimentos(Posicao posicao) {
        final List<Posicao> posicoes = new ArrayList<>();

        posicoes.addAll(Moves.getColuna(posicao.getX(), posicao.getY(), 8, tabuleiro));
        posicoes.addAll(Moves.getFileira(posicao.getX(), posicao.getY(), 8, tabuleiro));
        
        Moves.getDiagonal(posicao.getX(), posicao.getY(), tabuleiro).forEach((pts) -> {
            pts.forEach((pt) -> {
                posicoes.add(pt);
            });
        });

        return posicoes;
    }
    
    /**
     * 
     * @param ponto
     * @return 
     */
    @Override
    public List<Posicao> listaPossiveisCapturas(Posicao ponto) {
        final List<Posicao> pontos = new ArrayList<>();
        
        pontos.addAll(Moves.getMinMax(ponto.getX(), ponto.getY(), 8, Tabuleiro.Direcao.DIAGONAL, tabuleiro));
        pontos.addAll(Moves.getMinMax(ponto.getX(), ponto.getY(), 8, Tabuleiro.Direcao.VERTICAL, tabuleiro));
        pontos.addAll(Moves.getMinMax(ponto.getX(), ponto.getY(), 8, Tabuleiro.Direcao.HORIZONTAL, tabuleiro));
        
        return pontos;
    }

    @Override
    public TipoPeca getTipoPeca() {
        return TipoPeca.DAMA;
    }

    @Override
    public int getValor() {
        return 40;
    }
}
