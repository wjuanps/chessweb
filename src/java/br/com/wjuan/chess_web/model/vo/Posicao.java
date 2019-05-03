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
public class Posicao implements Serializable {

    private int x;
    private int y;

    /**
     * Construtor vazio
     */
    public Posicao() {
    }

    /**
     *
     * Construtor que inicializa x e y
     * 
     * @param x
     * @param y
     */
    public Posicao(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * Verifica se uma determinada posição é válida
     * 
     * @return
     */
    public boolean isPosicaoValida() {
        return (this.x >= 0 && this.y >= 0
                && this.x <= 7 && this.y <= 7);
    }

    /**
     *
     * Verifica se uma determinada posição possui ou não alguma peça
     * 
     * @param tabuleiro
     * @return
     */
    public boolean isVazio(Tabuleiro tabuleiro) {
        return (this.isPosicaoValida() && tabuleiro.getTabuleiro().get(this.x).get(this.y) == 0);
    }

    /**
     *
     * Verifica se duas posições são iguais
     * 
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Posicao) {
            return (this.x == ((Posicao) object).getX()
                    && this.y == ((Posicao) object).getY());
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public String toString() {
        return (this.x + " " + this.y);
    }

    /**
     * 
     * @param other
     * @param tabuleiro
     * @return 
     */
    public boolean isMesmaCor(Posicao other, Tabuleiro tabuleiro) {
        if (Peca.isBranca(this, tabuleiro)) {
            return Peca.isBranca(other, tabuleiro);
        } 
        return !Peca.isBranca(other, tabuleiro);
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
