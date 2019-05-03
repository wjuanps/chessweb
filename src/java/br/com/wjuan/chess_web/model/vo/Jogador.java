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
public class Jogador implements Serializable {
    
    public static boolean BRANCAS = true;
    public static boolean PRETAS  = false;
    
    private String corDaPeca;

    public Jogador(String corDaPeca) {
        this.corDaPeca = corDaPeca;
    }
    
    public String getCorDaPeca() {
        return corDaPeca;
    }

    public void setCorDaPeca(String corDaPeca) {
        this.corDaPeca = corDaPeca;
    }
    
}
