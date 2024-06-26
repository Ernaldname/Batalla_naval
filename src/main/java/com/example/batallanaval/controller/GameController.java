package com.example.batallanaval.controller;

import com.example.batallanaval.model.Cell;
import com.example.batallanaval.model.CellState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.*;

public class GameController {

    private static final int SIZE = 10; // Define el tamaño del tablero

    @FXML
    private GridPane gridPanePlayer1; // Tablero de la máquina
    @FXML
    private GridPane gridPanePlayer2; // Tablero del jugador

    private Cell[][] cells1 = new Cell[SIZE][SIZE]; // CELDAS DE LA MAQUINA
    private Cell[][] cells2 = new Cell[SIZE][SIZE]; // CELDAS DEL JUGADOR

    // LISTAS DE LISTAS PARA EL USO DEL TABLERO
    private List<List<Cell>> ships1; // Lista de listas de celdas para los barcos de la máquina
    private List<List<Cell>> ships2; // Lista de listas de celdas para los barcos del jugador

    private Random random = new Random();

    // ESTO UTILIZA ELEMENTOS DE TIPO STRING PARA CADA CELDA, GARANTIZA QUE SEA UNICO CADA ELEMENTO Y NOS AYUDA A EVITAR REPETICIONES
    private Set<String> machineShots = new HashSet<>();

    private boolean playerTurn = true;

    // CARACTERISTICAS DE AMBOS TABLEROS E INDICA LAS ACCIONES DEL BOTON SOBRE CADA CASILLA
    private void initializeBoard(GridPane gridPane, Cell[][] cells, boolean isPlayer) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(40, 40);
                if (isPlayer) {
                    button.setOnAction(this::onHandleButtonPlay);
                }
                gridPane.add(button, col, row);
                cells[row][col] = new Cell(button, CellState.AGUA);
            }
        }
    }

    // Método para inicializar el tablero y otras configuraciones iniciales
    @FXML
    public void initialize() {

        initializeBoard(gridPanePlayer1, cells1, false); // Inicializa el tablero de la máquina
        initializeBoard(gridPanePlayer2, cells2, true);  // Inicializa el tablero del jugador

        // Coloca los barcos de ambos jugadores
        ships1 = placeShips(cells1); // máquina
        ships2 = placeShips(cells2); // jugador

        // Comienza el juego
        playerTurn = random.nextBoolean();
        if (!playerTurn) {
            machineTurn();
        }
    }

    private List<List<Cell>> placeShips(Cell[][] cells) {
        List<List<Cell>> ships = new ArrayList<>();

        // Coloca el primer barco de tres celdas en la primera fila como ejemplo
        ships.add(placeShip(cells, 3));

        // Coloca el segundo barco de cuatro celdas en la segunda fila como ejemplo
        ships.add(placeShip(cells, 4));

        // Puedes continuar agregando más barcos aquí

        return ships;
    }

    private List<Cell> placeShip(Cell[][] cells, int length) {
        List<Cell> ship = new ArrayList<>();

        Random random = new Random();

        // Genera coordenadas aleatorias para la ubicación del barco
        int row = random.nextInt(SIZE);
        int col = random.nextInt(SIZE - length + 1); // Asegura que el barco se ajuste completamente en el tablero

        // Verifica si las celdas para colocar el barco están libres
        boolean canPlaceShip = true;
        for (int i = 0; i < length; i++) {
            if (cells[row][col + i].getState() != CellState.AGUA) {
                canPlaceShip = false;
                break;
            }
        }

        // Si las celdas están libres, coloca el barco
        if (canPlaceShip) {
            for (int i = 0; i < length; i++) {
                cells[row][col + i].setState(CellState.BARCO);
                ship.add(cells[row][col + i]);
            }
        } else {
            // Si no se puede colocar el barco en la posición generada, intenta nuevamente
            ship = placeShip(cells, length);
        }

        return ship;
    }


    @FXML
    void onHandleButtonPlay(ActionEvent event) {
        if (playerTurn) {
            Button button = (Button) event.getSource();
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (cells2[row][col].getButton() == button) {
                        if (!handleCellClick(cells2[row][col], ships2)) {
                            playerTurn = false;
                            machineTurn();
                        }
                        return;
                    }
                }
            }
        }
    }

    private void machineTurn() {
        if (!playerTurn) {
            boolean hit;
            do {
                hit = machineShoot();
            } while (hit && !playerTurn); // El bucle se detiene si la máquina acierta y el jugador no ha cambiado el turno
        }
    }

    private boolean machineShoot() {
        int row, col;
        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        } while (machineShots.contains(row + "," + col)); // Evitar disparos repetidos

        machineShots.add(row + "," + col);

        boolean hit = handleCellClick(cells1[row][col], ships1);
        if (!hit) {
            playerTurn = true; // Si la máquina no acierta, cambia el turno al jugador
        }

        return hit;
    }

    private boolean handleCellClick(Cell cell, List<List<Cell>> ships) {
        if (cell.getState() == CellState.AGUA) {
            cell.getButton().setDisable(true); // Deshabilita el botón para evitar futuros eventos
            cell.getButton().setStyle("-fx-background-color: #0022ff;"); // Indica agua
            return false;
        } else if (cell.getState() == CellState.BARCO) {
            cell.getButton().setDisable(true); // Deshabilita el botón para evitar futuros eventos
            cell.getButton().setStyle("-fx-background-color: #ad3636;"); // Indica parte del barco tocada
            cell.setState(CellState.BARCO_HUNDIDO);
            checkIfShipSunk(ships);
            return true;
        } else if (cell.getState() == CellState.BARCO_HUNDIDO) {
            cell.getButton().setDisable(true); // Deshabilita el botón para evitar futuros eventos
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
                if (areAllShipsSunk(ships)) {
                    endGame();
                }
            }
        }
    }

    private boolean areAllShipsSunk(List<List<Cell>> ships) {
        for (List<Cell> ship : ships) {
            for (Cell cell : ship) {
                if (cell.getState() != CellState.BARCO_HUNDIDO) {
                    return false;
                }
            }
        }
        return true;
    }

    private void endGame() {
        // Aquí puedes agregar el código para finalizar el juego, como mostrar un mensaje de victoria
        System.out.println("Juego terminado");
    }
}
