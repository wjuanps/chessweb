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
public class Cavalo implements Piece {

    private final Tabuleiro tabuleiro;

    public Cavalo(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    /**
     * 
     * @param origem
     * @return 
     * 
     * 1 2 1 2
     */
    @Override
    public List<Posicao> listaPossiveisMovimentos(Posicao origem) {
        final List<Posicao> destinos = new ArrayList<>();
        int x = origem.getX();
        int y = origem.getY();

        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                destinos.add(new Posicao(x + (i * 2), y + (j * 1)));
                destinos.add(new Posicao(x + (j * 1), y + (i * 2)));
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
        final List<Posicao> destinos = new ArrayList<>();
        
        for (Posicao destino : listaPossiveisMovimentos(origem)) {
            if (!destino.isVazio(tabuleiro)) {
                destinos.add(destino);
            }
        }

        return destinos;
    }

    @Override
    public TipoPeca getTipoPeca() {
        return TipoPeca.CAVALO;
    }

    @Override
    public int getValor() {
        return 5;
    }
}
