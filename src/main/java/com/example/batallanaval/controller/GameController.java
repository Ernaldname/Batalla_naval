package com.example.batallanaval.controller;

import com.example.batallanaval.model.Cell;
import com.example.batallanaval.model.CellState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameController {

    private static final int SIZE = 11; // Define el tamaño del tablero

    @FXML
    private GridPane gridPanePlayer1;
    @FXML
    private GridPane gridPanePlayer2;

    private Cell[][] cells1 = new Cell[SIZE][SIZE];
    private Cell[][] cells2 = new Cell[SIZE][SIZE];

    private List<List<Cell>> ships1; // Lista de listas de celdas para los barcos del jugador 1
    private List<List<Cell>> ships2; // Lista de listas de celdas para los barcos del jugador 2

    private Random random = new Random();
    private Set<String> machineShots = new HashSet<>(); // Para evitar tiros repetidos

    // Método para inicializar el tablero y otras configuraciones iniciales
    @FXML
    public void initialize() {
        initializeBoard(gridPanePlayer1, cells1);
        initializeBoard(gridPanePlayer2, cells2);

        // Coloca los barcos de ambos jugadores
        ships1 = placeShips(cells1);
        ships2 = placeShips(cells2);
    }

    private void initializeBoard(GridPane gridPane, Cell[][] cells) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setOnAction(this::onHandleButtonPlay);
                gridPane.add(button, col, row);
                cells[row][col] = new Cell(button, CellState.AGUA);
            }
        }
    }

    private List<List<Cell>> placeShips(Cell[][] cells) {
        // Lógica para colocar barcos en el tablero
        // Debes implementar esto según tu lógica específica para colocar barcos
        // Ejemplo de un barco de 3 celdas en la primera fila:
        cells[0][0].setState(CellState.BARCO);
        cells[0][1].setState(CellState.BARCO);
        cells[0][2].setState(CellState.BARCO);
        // Devuelve la lista de listas de celdas para los barcos
        return List.of(List.of(cells[0][0], cells[0][1], cells[0][2]));
    }

    @FXML
    void onHandleButtonPlay(ActionEvent event) {
        Button button = (Button) event.getSource();
        boolean hit = false;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells2[row][col].getButton() == button) {
                    hit = handleCellClick(cells2[row][col], ships2);
                    break;
                }
            }
        }
        if (!hit) {
            machineShoot();
        }
    }

    private void machineShoot() {
        int row, col;
        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        } while (machineShots.contains(row + "," + col)); // Evitar disparos repetidos
        machineShots.add(row + "," + col);
        handleCellClick(cells1[row][col], ships1);
    }

    private boolean handleCellClick(Cell cell, List<List<Cell>> ships) {
        if (cell.getState() == CellState.AGUA) {
            cell.getButton().setStyle("-fx-background-color: #0022ff;"); // Indica agua
            return false;
        } else if (cell.getState() == CellState.BARCO) {
            cell.getButton().setStyle("-fx-background-color: #ad3636;"); // Indica parte del barco tocada
            cell.setState(CellState.BARCO_HUNDIDO);
            checkIfShipSunk(ships);
            return true;
        } else if (cell.getState() == CellState.BARCO_HUNDIDO) {
            // Celda ya tocada, no hacer nada
            return false;
        }
        return false;
    }

    private void checkIfShipSunk(List<List<Cell>> ships) {
        for (List<Cell> ship : ships) {
            boolean sunk = true;
            for (Cell cell : ship) {
                if (cell.getState() != CellState.BARCO_HUNDIDO) {
                    sunk = false;
                    break;
                }
            }
            if (sunk) {
                for (Cell cell : ship) {
                    cell.getButton().setStyle("-fx-background-color: #000000;"); // Indica barco hundido
                }
            }
        }
    }
}
