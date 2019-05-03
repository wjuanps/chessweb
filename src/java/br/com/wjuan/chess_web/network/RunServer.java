/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.network;

import java.net.BindException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Sophia
 */
public final class RunServer {

    /**
     * Define o visual do sistema.
     */
    static {
        try {
            for (UIManager.LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
    }

    public static void main(String[] args) {
        try {
            ChessServidor chessServidor = new ChessServidor();
            if (!chessServidor.listen()) {
                chessServidor.dispose();
                chessServidor.setVisible(false);
                JOptionPane.showMessageDialog(null, "O servidor já está em uso.", "WChess server - By Juan Soares", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (BindException b) {}
    }
}
