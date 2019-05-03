/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Move;
import br.com.wjuan.chess_web.model.vo.Peca;
import br.com.wjuan.chess_web.model.vo.Peca.TipoPeca;
import br.com.wjuan.chess_web.model.vo.Posicao;

/**
 *
 * @author Sophia
 */
public final class Chess {

    /**
     *
     * @param tabuleiro
     * @param move
     * @return
     */
    public boolean executarJogada(TabuleiroBO tabuleiro, Move move) {
        final Posicao origem  = move.getOrigem();
        final Posicao destino = move.getDestino();

        if ((origem != null && Peca.getPeca(origem, tabuleiro).equals(TipoPeca.REI))
                && (destino.equals(tabuleiro.getRei().getRoquePequeno())
                || destino.equals(tabuleiro.getRei().getRoqueGrande()))) {

            //Executa o roque
            tabuleiro.getRei().rocar(destino, origem);
            return true;
        } else if (move.getEnPassant() != null && move.getEnPassant().contains(origem)
                && destino.equals(tabuleiro.getPeao().getPosicaoEnPassant())) {

            final Posicao enPassant = tabuleiro.getPeao().getPosicaoPecaCapturadaEnPassant();
            tabuleiro.getTabuleiro().get(enPassant.getX()).set(enPassant.getY(), 0);
            //executa o movimento
            tabuleiro.movimentar(origem, destino);
            return true;
        } else if (tabuleiro.listaPossiveisCapturas(origem, tabuleiro.getTabuleiro()).contains(destino)) {
            //executa a captura
            tabuleiro.movimentar(origem, destino);
            return true;
        } else if (tabuleiro.listaPossiveisMovimentos(origem, tabuleiro.getTabuleiro()).contains(destino)) {
            //executa o movimento
            tabuleiro.movimentar(origem, destino);
            return true;
        }

        return false;
    }
}
