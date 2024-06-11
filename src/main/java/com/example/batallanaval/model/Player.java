/**
 * @AUTOR: Edinson Arnulfo Ramirez Mendez - 2224504-2724
 * @Correo: edinson.ramirez@correunivalle.edu.co
 * @Version: V1
 *
 * Descripcion de clase: Contiene la informacion del jugador, nombre (No se usa en este caso, casillas y barcos)
 */

package com.example.batallanaval.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Cell[][] cells;
    private List<List<Cell>> ships;

    public Player(String name, int size) {
        this.name = name;
        this.cells = new Cell[size][size];
        this.ships = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public List<List<Cell>> getShips() {
        return ships;
    }
}
