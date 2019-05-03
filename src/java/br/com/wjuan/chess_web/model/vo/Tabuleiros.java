/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.vo;

import java.io.Serializable;

/**
 *
 * @author Sophia
 */
public interface Tabuleiros extends Serializable {

    /**
     * Significado dos números
     *
     * 0 - Casa vazia
     *
     * ** Pretas ** 
     * 1 - Peão 
     * 2 - Torre 
     * 3 - Cavalo 
     * 4 - Bispo 
     * 5 - Rainha 
     * 6 - Rei
     *
     * ** Brancas ** 
     * 7  - Peão 
     * 8  - Torre 
     * 9  - Cavalo 
     * 10 - Bispo 
     * 11 - Rainha 
     * 12 - Rei
     */
    int[][] TABULEIRO_INICIAL = {
        {2, 3,  4,  5,  6,  4, 3, 2},
        {1, 1,  1,  1,  1,  1, 1, 1},
        {0, 0,  0,  0,  0,  0, 0, 0},
        {0, 0,  0,  0,  0,  0, 0, 0},
        {0, 0,  0,  0,  0,  0, 0, 0},
        {0, 0,  0,  0,  0,  0, 0, 0},
        {7, 7,  7,  7,  7,  7, 7, 7},
        {8, 9, 10, 11, 12, 10, 9, 8}
    };

    /**
     * Imprimi o tabuleiro no console
     */
    public static void imprimirTabuleiro() {
        for (int[] is : Tabuleiros.TABULEIRO_INICIAL) {
            for (int i : is) {
                System.out.print(" " + i);
            }
            System.out.println();
        }
    }

}
