/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wjuan.chess_web.model.bo;

import br.com.wjuan.chess_web.model.vo.Posicao;
import br.com.wjuan.chess_web.model.vo.Tabuleiro;
import br.com.wjuan.chess_web.model.vo.Tabuleiro.Direcao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Sophia
 */
public abstract class Moves implements Serializable {
    // | 0 0 | 0 1 | 0 2 | 0 3 | 0 4 | 0 5 | 0 6 | 0 7 |
    // | 1 0 | 1 1 | 1 2 | 1 3 | 1 4 | 1 5 | 1 6 | 1 7 |
    // | 2 0 | 2 1 | 2 2 | 2 3 | 2 4 | 2 5 | 2 6 | 2 7 |
    // | 3 0 | 3 1 | 3 2 | 3 3 | 3 4 | 3 5 | 3 6 | 3 7 |
    // | 4 0 | 4 1 | 4 2 | 4 3 | 4 4 | 4 5 | 4 6 | 4 7 |
    // | 5 0 | 5 1 | 5 2 | 5 3 | 5 4 | 5 5 | 5 6 | 5 7 |
    // | 6 0 | 6 1 | 6 2 | 6 3 | 6 4 | 6 5 | 6 6 | 6 7 |
    // | 7 0 | 7 1 | 7 2 | 7 3 | 7 4 | 7 5 | 7 6 | 7 7 |

    /**
     *
     * @param a
     * @param b
     * @param n
     * @param tabuleiro
     * @return
     */
    public static Set<Posicao> getColuna(int a, int b, int n, Tabuleiro tabuleiro) {
        final Set<Posicao> colunas = new HashSet<>();

        if (n == 1) {
            Posicao destino = new Posicao(a + 1, b);
            if (destino.isVazio(tabuleiro)) {
                colunas.add(destino);
            }

            destino = new Posicao(a - 1, b);
            if (destino.isVazio(tabuleiro)) {
                colunas.add(destino);
            }

            return colunas;
        }

        final Set<Posicao> aux = new HashSet<>();
        for (int i = 0; i < n; i++) {
            final Posicao destino = new Posicao(i, b);
            if (!destino.isVazio(tabuleiro)) {
                if (i < a) {
                    aux.removeAll(aux);
                }
                if (i > a) {
                    break;
                }
            } else {
                aux.add(destino);
            }
        }

        colunas.add(new Posicao(a, b));
        colunas.addAll(aux);
        return colunas;
    }

    /**
     *
     * @param a
     * @param b
     * @param n
     * @param tabuleiro
     * @return
     */
    public static Set<Posicao> getFileira(int a, int b, int n, Tabuleiro tabuleiro) {
        final Set<Posicao> fileira = new HashSet<>();
        Posicao destino;

        if (n == 1) {
            destino = new Posicao(a, b + 1);
            if (destino.isVazio(tabuleiro)) {
                fileira.add(destino);
            }
            
            destino = new Posicao(a, b - 1);
            if (destino.isVazio(tabuleiro)) {
                fileira.add(destino);
            }
            return fileira;
        }

        final Set<Posicao> aux = new HashSet<>();
        for (int j = 0; j < n; j++) {
            destino = new Posicao(a, j);
            if (!destino.isVazio(tabuleiro)) {
                if (j < b) {
                    aux.removeAll(aux);
                }
                if (j > b) {
                    break;
                }
            } else if (j != b) {
                aux.add(new Posicao(a, j));
            }
        }

        fileira.add(new Posicao(a, b));
        fileira.addAll(aux);

        return fileira;
    }

    /**
     *
     * @param a
     * @param b
     * @param tabuleiro
     * @return
     */
    public static List<List<Posicao>> getDiagonal(int a, int b, Tabuleiro tabuleiro) {
        final List<Posicao> diagonalPrincipal = new ArrayList<>();
        final List<Posicao> diagonalSecundaria = new ArrayList<>();

        int auxI = (a - b);
        int auxJ = 0;

        if (auxI <= 0) {
            auxJ = Math.abs(auxI);
            auxI = 0;
        }

        int res = Integer.valueOf(String.valueOf(a + b));

        for (int i = 0; i < tabuleiro.getTabuleiro().size(); i++) {
            for (int j = 0; j < tabuleiro.getTabuleiro().get(i).size(); j++) {
                if (auxI == i && auxJ == j) {
                    diagonalPrincipal.add(new Posicao(auxI++, auxJ++));
                }

                int r = Integer.valueOf(String.valueOf(i + j));
                if (res == r) {
                    diagonalSecundaria.add(new Posicao(i, j));
                }
            }
        }
        final List<List<Posicao>> listaFinal = new ArrayList<>();
        List<Posicao> listaAux = new ArrayList<>();

        for (Posicao p : diagonalSecundaria) {
            if (!p.isVazio(tabuleiro)) {
                if (p.getX() < a) {
                    listaAux.removeAll(listaAux);
                }
                if (p.getY() < b) {
                    break;
                }
            } else {
                listaAux.add(p);
            }
        }

        listaAux.add(new Posicao(a, b));
        listaFinal.add(listaAux);
        listaAux = new ArrayList<>();
        for (Posicao p : diagonalPrincipal) {
            if (!p.isVazio(tabuleiro)) {
                if (p.getX() < a) {
                    listaAux.removeAll(listaAux);
                }
                if (p.getY() > b) {
                    break;
                }
            } else {
                listaAux.add(p);
            }
        }
        listaAux.add(new Posicao(a, b));
        listaFinal.add(listaAux);

        return listaFinal;
    }

    /**
     *
     * @param a
     * @param b
     * @param n
     * @param direcao
     * @param tabuleiro
     * @return
     */
    public static Set<Posicao> getMinMax(int a, int b, int n, Direcao direcao, Tabuleiro tabuleiro) {
        final Set<Posicao> posicoes = new HashSet<>();
        int max = 0;
        int min = 8;
        switch (direcao) {
            case VERTICAL:
                for (Posicao p : getColuna(a, b, n, tabuleiro)) {
                    if (p.getX() >= max) {
                        max = p.getX();
                    }
                    if (p.getX() <= min) {
                        min = p.getX();
                    }
                }
                posicoes.addAll(getPosicoes(max, min, b, direcao));
                break;
            case HORIZONTAL:
                for (Posicao p : getFileira(a, b, n, tabuleiro)) {
                    if (p.getY() > max) {
                        max = p.getY();
                    }
                    if (p.getY() < min) {
                        min = p.getY();
                    }
                }
                posicoes.addAll(getPosicoes(max, min, a, direcao));
                break;
            case DIAGONAL:
                int max2 = 0;
                int min2 = 8;
                int cont = 1;
                for (List<Posicao> list : getDiagonal(a, b, tabuleiro)) {
                    for (Posicao p : list) {
                        if ((p.getX() >= max)) {
                            max = p.getX();
                            min = p.getY();
                        }
                        if (p.getX() <= min2) {
                            max2 = p.getY();
                            min2 = p.getX();
                        }
                    }

                    Posicao ponto;
                    if (max >= 0 && min < 8) {
                        ponto = new Posicao(max + 1, min - 1);
                        if (ponto.isPosicaoValida() && cont == 1) {
                            posicoes.add(ponto);
                        }
                        ponto = new Posicao(max + 1, min + 1);
                        if (ponto.isPosicaoValida() && cont == 2) {
                            posicoes.add(ponto);
                        }
                    }

                    if (max2 >= 0 && min2 < 8) {
                        ponto = new Posicao(min2 - 1, max2 + 1);
                        if (ponto.isPosicaoValida() && cont == 1) {
                            posicoes.add(ponto);
                        }
                        ponto = new Posicao(min2 - 1, max2 - 1);
                        if (ponto.isPosicaoValida() && cont == 2) {
                            posicoes.add(ponto);
                        }
                    }
                    max = max2 = 0;
                    min = min2 = 8;
                    cont++;
                }
                break;
        }
        return posicoes;
    }

    /**
     *
     * @param max
     * @param min
     * @param k
     * @param direcao
     * @return
     */
    private static List<Posicao> getPosicoes(int max, int min, int k, Direcao direcao) {
        final List<Posicao> posicoes = new ArrayList<>();
        if (direcao.equals(Direcao.HORIZONTAL)) {
            if (max >= 0) {
                posicoes.add(new Posicao(k, max + 1));
            }
            if (min < 8) {
                posicoes.add(new Posicao(k, min - 1));
            }
        } else if (direcao.equals(Direcao.VERTICAL)) {
            if (max >= 0) {
                posicoes.add(new Posicao(max + 1, k));
            }
            if (min < 8) {
                posicoes.add(new Posicao(min - 1, k));
            }
        }

        return posicoes;
    }
}
