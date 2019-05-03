/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Peca.TipoPeca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.model.vo.Tabuleiro;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sophia
 */
public interface Piece extends Serializable {

    /**
     *
     * @param origem
     * @return
     */
    List<Posicao> listaPossiveisCapturas(Posicao origem);

    /**
     *
     * @param origem
     * @return
     */
    List<Posicao> listaPossiveisMovimentos(Posicao origem);

    /**
     *
     * @return
     */
    TipoPeca getTipoPeca();
    
    /**
     *
     * @return 
     */
    int getValor();
}
