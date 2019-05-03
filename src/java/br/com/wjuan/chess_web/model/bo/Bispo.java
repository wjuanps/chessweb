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
public class Bispo implements Piece {

    private final Tabuleiro tabuleiro;

    public Bispo(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    /**
     * 
     * @param origem
     * @return 
     */
    @Override
    public List<Posicao> listaPossiveisMovimentos(Posicao origem) {
        final List<Posicao> posicoes = new ArrayList<>();
     
        Moves.getDiagonal(origem.getX(), origem.getY(), tabuleiro).forEach((destinos) -> {
            destinos.forEach((destino) -> {
                posicoes.add(destino);
            });
        });
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
        
        posicoes.addAll(Moves.getMinMax(origem.getX(), origem.getY(), 8, Tabuleiro.Direcao.DIAGONAL, tabuleiro));
        
        return posicoes;
    }

    @Override
    public TipoPeca getTipoPeca() {
        return TipoPeca.BISPO;
    }

    @Override
    public int getValor() {
        return 5;
    }
}
