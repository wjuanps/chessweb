/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.vo;

import br.com.wjuan.chess_web.model.vo.Peca.TipoPeca;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sophia
 */
public class Move implements Serializable {

    private Posicao origem;
    private Posicao destino;
    private List<Posicao> enPassant;

    private int temp = 0;

    private boolean promocaoPeao = false;

    public Move() {
    }

    /**
     *
     * @param origem
     * @param destino
     * @param enPasant
     */
    public Move(Posicao origem, Posicao destino, List<Posicao> enPasant) {
        this.origem = origem;
        this.destino = destino;
        this.enPassant = enPasant;
    }

    /**
     *
     * @param origem
     * @param destino
     */
    public Move(Posicao origem, Posicao destino) {
        this.origem = origem;
        this.destino = destino;
    }

    /**
     *
     * @param tabuleiro
     */
    public void executarMovimento(Tabuleiro tabuleiro) {
        temp = tabuleiro.getTabuleiro().get(destino.getX()).get(destino.getY());
        tabuleiro.getTabuleiro().get(destino.getX()).set(destino.getY(), tabuleiro.getTabuleiro().get(origem.getX()).get(origem.getY()));
        tabuleiro.getTabuleiro().get(origem.getX()).set(origem.getY(), 0);

        if (Peca.getPeca(destino, tabuleiro).equals(TipoPeca.PEAO) && (destino.getX() == 0 || destino.getX() == 7)) {
            tabuleiro.getTabuleiro().get(destino.getX()).set(destino.getY(), (Peca.isBranca(destino, tabuleiro)) ? 11 : 5);
            promocaoPeao = true;
        }
    }

    /**
     *
     * @param tabuleiro
     */
    public void desfazerMovimento(Tabuleiro tabuleiro) {
        tabuleiro.getTabuleiro().get(origem.getX()).set(origem.getY(), tabuleiro.getTabuleiro().get(destino.getX()).get(destino.getY()));
        tabuleiro.getTabuleiro().get(destino.getX()).set(destino.getY(), temp);

        if (promocaoPeao) {
            tabuleiro.getTabuleiro().get(origem.getX()).set(origem.getY(), (Peca.isBranca(origem, tabuleiro)) ? 7 : 1);
            promocaoPeao = false;
        }
    }

    /**
     *
     * @return
     */
    public boolean isMovimentoValido() {
        return (this.origem.isPosicaoValida() && this.destino.isPosicaoValida());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Move) {
            return (this.origem.equals(((Move) object).getOrigem())
                    && this.destino.equals(((Move) object).getDestino()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.origem);
        hash = 59 * hash + Objects.hashCode(this.destino);
        hash = 59 * hash + this.temp;
        return hash;
    }

    @Override
    public String toString() {
        return ("Origem: " + origem + " - Destino: " + destino);
    }

    public Posicao getOrigem() {
        return origem;
    }

    public void setOrigem(Posicao origem) {
        this.origem = origem;
    }

    public Posicao getDestino() {
        return destino;
    }

    public void setDestino(Posicao destino) {
        this.destino = destino;
    }

    public List<Posicao> getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(List<Posicao> enPassant) {
        this.enPassant = enPassant;
    }

}
