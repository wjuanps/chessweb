/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Jogador;
import br.com.wjuan.chess_web.model.vo.Peca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sophia
 */
public class Notacao {

    private final List<String> listaJogada = new ArrayList<>();

    /**
     *
     * @param casa
     * @param posicao
     */
    public void setNotacao(String casa, Posicao posicao) {
//        String peca = Peca.getPeca(posicao).toString().substring(0, 1);
//        peca = (peca.equals("P")) ? "" : peca;
//        
//        String jogada = "";
//        
//        if (Jogador.BRANCAS) {
//            jogada += peca.concat(casa);
//            listaJogada.add(jogada);
//        } else {
//            String s = listaJogada.get(listaJogada.size() - 1);
//            s += " ".concat(peca).concat(casa);
//            listaJogada.set(listaJogada.size() - 1, s);
//        }
    }

    /**
     *
     * @return
     */
    public String getNotacao() {
        final StringBuilder s = new StringBuilder();
        int n = 1;
        
        for (String string : listaJogada) {
            s.append(n++).append(".").append(string).append("\n");
        }
        return s.toString();
    }
}
