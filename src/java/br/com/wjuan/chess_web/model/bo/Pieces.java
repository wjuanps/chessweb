/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.model.vo.Tabuleiro;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sophia
 */
public final class Pieces {

    private final List<Piece> pecas;
    
    public Pieces(Piece... pecas) {
        this.pecas = new ArrayList(Arrays.asList(pecas));
    }

    public Pieces() {
        this.pecas = null;
    }

    /**
     *
     * @param origem
     * @param tabuleiro
     * @param piece
     * @return
     */
    public List<Posicao> listaPossiveisCapturas(Posicao origem, Tabuleiro tabuleiro, Piece piece) {
        final List<Posicao> listaPossiveisCapturas = piece.listaPossiveisCapturas(origem);
        final List<Posicao> destinos = new ArrayList<>();

        for (Posicao destino : listaPossiveisCapturas) {
            if (destino.isPosicaoValida()) {
                if (!origem.isMesmaCor(destino, tabuleiro)) {
                    destinos.add(destino);
                }
            }
        }

        return destinos;
    }

    /**
     *
     * @param posicao
     * @param tabuleiro
     * @param piece
     * @return
     */
    public List<Posicao> listaPossiveisMovimentos(Posicao posicao, Tabuleiro tabuleiro, Piece piece) {
        final List<Posicao> listaPossiveisMovimentos = piece.listaPossiveisMovimentos(posicao);
        final List<Posicao> destinos = new ArrayList<>();

        listaPossiveisMovimentos.stream().filter((des) -> (des.isVazio(tabuleiro))).forEachOrdered((destino) -> {
            destinos.add(destino);
        });
        return destinos;
    }
    
    public List<Piece> getPecas() {
        return this.pecas;
    }
}
