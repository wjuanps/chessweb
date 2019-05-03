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
public class Peca implements Serializable {

    public enum TipoPeca implements Serializable {
        /**
         * indica que nao ha peca naquela posicao do tabuleiro
         */
        VAZIO,
        /**
         *
         */
        PEAO,
        /**
         *
         */
        TORRE,
        /**
         *
         */
        CAVALO,
        /**
         *
         */
        BISPO,
        /**
         *
         */
        DAMA,
        /**
         *
         */
        REI;
    }

    private Posicao posicao;
    private String fundo;
    private String image;
    private String casa;

    public Peca() {
    }
    
    /**
     * 
     * @param posicao
     * @param fundo
     * @param image 
     */
    public Peca(Posicao posicao, String fundo, String image) {
        this.posicao = posicao;
        this.fundo = fundo;
        this.image = image;
    }
    
    /**
     *
     * @param posicao
     * @param tabuleiro
     * @return
     */
    public static boolean isBranca(Posicao posicao, Tabuleiro tabuleiro) {
        return ((posicao.isPosicaoValida() && tabuleiro.getTabuleiro().get(posicao.getX()).get(posicao.getY()) > 6));
    }

    /**
     *
     * @param posicao
     * @param tabuleiro
     * @return
     */
    public static TipoPeca getPeca(Posicao posicao, Tabuleiro tabuleiro) {
        int indice = tabuleiro.getTabuleiro().get(posicao.getX()).get(posicao.getY());
        return ((indice < 7 && indice > 0) ? TipoPeca.values()[indice] : (indice != 0) ? TipoPeca.values()[indice - 6] : TipoPeca.VAZIO);
    }

    /**
     *
     * @param val
     * @return
     */
    public static TipoPeca getPeca(int val) {
        return ((val < 7 && val > 0) ? TipoPeca.values()[val] : (val != 0) ? TipoPeca.values()[val - 6] : TipoPeca.VAZIO);
    }

    /**
     *
     * @param posicao
     * @param tabuleiro
     * @return
     */
    public static boolean isVazio(Posicao posicao, Tabuleiro tabuleiro) {
        return getPeca(posicao, tabuleiro).equals(TipoPeca.VAZIO);
    }

    /**
     *
     * @param posicao
     * @param tabuleiro
     * @return
     */
    public static int getValor(Posicao posicao, Tabuleiro tabuleiro) {
        switch (getPeca(posicao, tabuleiro)) {
            case PEAO:
                return 1;
            case BISPO:
                return 5;
            case CAVALO:
                return 5;
            case TORRE:
                return 10;
            case DAMA:
                return 40;
            case REI:
                return 100;
            default:
                return 0;
        }
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public String getFundo() {
        return fundo;
    }

    public void setFundo(String fundo) {
        this.fundo = fundo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }
}