/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Move;
import br.com.wjuan.chess_web.model.vo.Peca;
import br.com.wjuan.chess_web.model.vo.Posicao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Sophia
 */
public class Algorithm implements Serializable {

    private static final int PROFUNDIDADE = 2;

    private TabuleiroBO tb;

    private Move movimento;

    private int estCusto = 0;
    private int estCustoAtaque = 0;
    
    private boolean white;

    public Algorithm(TabuleiroBO tb) {
        this.tb = tb;
    }

    public Move nextMove() {
        estCusto = getCusto(false);
        estCustoAtaque = getCustoAtaque(false);

        negaScout(false, Integer.MIN_VALUE, Integer.MAX_VALUE, PROFUNDIDADE);
        //randomGen(false);
//        
//        this.white = false;
//        min(PROFUNDIDADE);
        return movimento;
    }

    /**
     * 
     * @param profundidade
     * @return 
     */
    private int max(int profundidade) {
        if (profundidade == 0) {
            return estimativa(white);
        }
        
        int best = Integer.MIN_VALUE;
        Stack<Move> moves = this.getTodosReaisMovimentos(true);
        
        if (moves != null) {
            moves = randomize(moves);
            while (moves.size() > 0) {
                Move move = moves.pop();
                move.executarMovimento(tb);
                
                int val = -min(profundidade - 1);
                if (val > best) {
                    best = val;
                    if (this.white) {
                        movimento = move;
                    }
                }
                move.desfazerMovimento(tb);
            }
        }
        
        return best;
    }
    
    /**
     * 
     * @param profundidade
     * @return 
     */
    private int min(int profundidade) {
        if (profundidade == 0) {
            return estimativa(white);
        }
        
        int best = Integer.MIN_VALUE;
        Stack<Move> moves = this.getTodosReaisMovimentos(false);
        
        if (moves != null) {
            moves = randomize(moves);
            while (moves.size() > 0) {
                Move move = moves.pop();
                move.executarMovimento(tb);
                
                int val = -max(profundidade - 1);
                if (val > best) {
                    best = val;
                    if (!this.white) {
                        movimento = move;
                    }
                }
                move.desfazerMovimento(tb);
            }
        }
        
        return best;
    }
    
    /**
     * 
     * @param white 
     */
    public void randomGen(boolean white){
        List<Move> moves = new ArrayList<>(getTodosReaisMovimentos(white));
        movimento = moves.get(new Random().nextInt(moves.size()));
    }
    
    /**
     *
     * @param white
     * @param alfa
     * @param beta
     * @param profundidade
     * @return
     */
    public int principalVariation(boolean white, int alfa, int beta, int profundidade) {
        if (profundidade == 0) {
            return estimativa(white);
        }

        int best = Integer.MIN_VALUE;
        Stack<Move> moves = this.getTodosReaisMovimentos(white);

        if (moves != null) {
            moves = randomize(moves);
            if (moves.size() > 0) {
                Move move = moves.remove(0);
                move.executarMovimento(tb);

                best = -principalVariation(!white, -beta, -alfa, profundidade - 1);
                if (profundidade == PROFUNDIDADE) {
                    movimento = move;
                }
                move.desfazerMovimento(tb);
            }
            while (moves.size() > 0 && best < beta) {
                Move move = moves.pop();
                move.executarMovimento(tb);

                int est = -principalVariation(!white, -alfa - 1, -alfa, profundidade - 1);
                if (est > alfa && est < beta) {
                    best = -principalVariation(!white, -beta, -est, profundidade - 1);
                    if (profundidade == PROFUNDIDADE) {
                        movimento = move;
                    }
                } else if (est > best) {
                    if (profundidade == PROFUNDIDADE) {
                        movimento = move;
                    }
                }
                move.desfazerMovimento(tb);
            }
        }

        return best;
    }

    /**
     *
     * @param white
     * @param alfa
     * @param beta
     * @param profundidade
     * @return
     */
    public int negaScout(boolean white, int alfa, int beta, int profundidade) {
        if (profundidade == 0) {
            return estimativa(white);
        }

        int best = Integer.MIN_VALUE;
        int n = beta;

        Stack<Move> moves = this.getTodosReaisMovimentos(white);
        if (moves != null) {
            moves = randomize(moves);
            if (moves.size() > 0 && profundidade == PROFUNDIDADE) {
                movimento = moves.elementAt(0);
            }
            while (moves.size() > 0 && best < beta) {
                Move move = moves.remove(0);
                move.executarMovimento(tb);

                int est = -negaScout(!white, -n, -Math.max(alfa, best), profundidade - 1);
                if (est > best) {
                    if (n == beta || profundidade <= 2) {
                        best = est;
                        if (profundidade == PROFUNDIDADE) {
                            movimento = move;
                        }
                    } else {
                        best = -negaScout(!white, -beta, -est, profundidade - 1);
                        if (profundidade == PROFUNDIDADE) {
                            movimento = move;
                        }
                    }
                }

                move.desfazerMovimento(tb);
                n = Math.max(alfa, best) + 1;
            }
        }
        return best;
    }

    /**
     *
     * @param white
     * @param alfa
     * @param beta
     * @param profundidade
     * @return
     */
    private int alfaBeta(boolean white, int alfa, int beta, int profundidade) {
        if (profundidade == 0) {
            return estimativa(white);
        }

        int best = Integer.MIN_VALUE;
        Stack<Move> moves = this.getTodosReaisMovimentos(white);
        if (moves != null) {
            moves = randomize(moves);
            while (!moves.isEmpty() && best < beta) {
                Move move = moves.remove(0);
                move.executarMovimento(tb);

                if (best > alfa) {
                    alfa = best;
                }

                int est = -alfaBeta(!white, -beta, -alfa, profundidade - 1);
                if (est > best) {
                    best = est;
                }
                if (profundidade == PROFUNDIDADE) {
                    movimento = move;
                }
                move.desfazerMovimento(tb);
            }
        }
        return best;
    }

    /**
     *
     * @param moves
     * @return
     */
    private Stack<Move> randomize(Stack<Move> moves) {
        int size = moves.size();
        int b = 0;
        for (int i = 0; i < size; i++) {
            Move move = moves.elementAt(i);
            if (tb.getTabuleiro().get(move.getDestino().getX()).get(move.getDestino().getY()) != 0) {
                moves.remove(i);
                moves.insertElementAt(move, 0);
                b++;
            }
        }

        for (int i = b; i < size - 1; i++) {
            Move move = moves.elementAt(i);
            int j = new Random().nextInt(size - i - 1) + i + 1;
            moves.insertElementAt(moves.get(j), i);
            moves.insertElementAt(move, j);
        }
        return moves;
    }

    public int estimativa(boolean w) {
        return ((getCusto(w)) - estCusto) * 10 + (getCustoAtaque(w) - estCustoAtaque);
    }

    public int getCusto(boolean w) {
        int custo = 0;
        for (int i = 0; i < tb.getTabuleiro().size(); i++) {
            for (int j = 0; j < tb.getTabuleiro().get(i).size(); j++) {
                if (tb.getTabuleiro().get(i).get(j) != 0) {
                    final Posicao newPeca = new Posicao(i, j);
                    for (Piece peca : tb.getPecas()) {
                        if (Peca.getPeca(newPeca, tb).equals(peca.getTipoPeca())) {
                            custo += peca.getValor() * ((w) ? 1 : -1);
                            break;
                        }
                    }
                }
            }
        }
        return custo;
    }

    public int getCustoAtaque(boolean w) {
        int custo = 0;
        for (int i = 0; i < tb.getTabuleiro().size(); i++) {
            for (int j = 0; j < tb.getTabuleiro().get(i).size(); j++) {
                if (tb.getTabuleiro().get(i).get(j) != 0) {
                    final Posicao newPeca = new Posicao(i, j);
                    for (Posicao peca : tb.listaPossiveisCapturas(newPeca, tb.getTabuleiro())) {
                        custo += Peca.getValor(peca, tb) * ((w) ? 1 : -1);
                    }
                }
            }
        }
        return -custo;
    }

    public Stack<Move> getTodosReaisMovimentos(boolean white) {
        final Stack<Move> aux = new Stack<>();
        final Stack<Move> movimentos = new Stack<>();
        for (int i = 0; i < tb.getTabuleiro().size(); i++) {
            for (int j = 0; j < tb.getTabuleiro().get(i).size(); j++) {
                final Posicao origem = new Posicao(i, j);
                if (white) {
                    if (Peca.isBranca(origem, tb)) {
                        for (Posicao destino : tb.listaPossiveisMovimentos(origem, tb.getTabuleiro())) {
                            movimentos.add(new Move(origem, destino));
                        }
                        for (Posicao destino : tb.listaPossiveisCapturas(origem, tb.getTabuleiro())) {
                            movimentos.add(new Move(origem, destino));
                        }
                    }
                } else {
                    if (!Peca.isBranca(origem, tb)) {
                        for (Posicao destino : tb.listaPossiveisMovimentos(origem, tb.getTabuleiro())) {
                            movimentos.add(new Move(origem, destino));
                        }
                        for (Posicao destino : tb.listaPossiveisCapturas(origem, tb.getTabuleiro())) {
                            movimentos.add(new Move(origem, destino));
                        }
                    }
                }
            }
        }
        
        return movimentos;
    }

    public TabuleiroBO getTb() {
        return tb;
    }

    public void setTb(TabuleiroBO tb) {
        this.tb = tb;
    }

    public Move getMovimento() {
        return movimento;
    }

    public void setMovimento(Move movimento) {
        this.movimento = movimento;
    }

}
