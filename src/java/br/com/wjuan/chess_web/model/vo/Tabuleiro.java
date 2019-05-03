/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sophia
 */
public class Tabuleiro implements Tabuleiros {
    
    /**
     * Informa a direção que será percorrida no tabuleiro
     */
    public enum Direcao {
        VERTICAL, HORIZONTAL, DIAGONAL
    };
    
    public List<List<Integer>> tabuleiro = null;

    /**
     *
     * @return
     */
    public List<List<Integer>> getTabuleiro() {
        if (this.tabuleiro == null) {
            this.tabuleiro = new ArrayList<>();
            for (int i = 0; i < TABULEIRO_INICIAL.length; i++) {
                final List<Integer> aux = new ArrayList<>();
                for (int j = 0; j < TABULEIRO_INICIAL[i].length; j++) {
                    aux.add(TABULEIRO_INICIAL[i][j]);
                }
                this.tabuleiro.add(aux);
            }
        }
        return this.tabuleiro;
    }

    /**
     * 
     * @param tabuleiro 
     */
    public void setTabuleiro(List<List<Integer>> tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    /**
     *
     * @param movimento
     */
    public void moverPecaNoTabuleiro(Move movimento) {
        if (movimento.getOrigem() == null || movimento.getDestino() == null) {
            return;
        }
        
        if (movimento.getOrigem().equals(movimento.getDestino())) {
            return;
        }

        if (movimento.isMovimentoValido()) {
            movimento.executarMovimento(this);
        }
    }
}
