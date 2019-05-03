/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.network;

import java.net.BindException;

/**
 *
 * @author Sophia
 */
public interface Network {
    
    /**
     *
     */
    int PORTA_PADRAO = 0xbb8;
    
    /**
     *
     */
    String HOST = "127.0.0.1";
    
    /**
     *
     * @return 
     * @throws java.net.BindException
     */
    boolean listen() throws BindException;
    
    /**
     *
     * @param porta
     * @return 
     * @throws java.net.BindException
     */
    boolean listen(int porta)  throws BindException;
    
    /**
     *
     * @param nome
     * @return 
     */
    boolean inicializarCliente(String nome);
}
