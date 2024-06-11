/**
 * @AUTOR: Edinson Arnulfo Ramirez Mendez - 2224504-2724
 * @Correo: edinson.ramirez@correunivalle.edu.co
 * @Version: V1
 *
 * Descripcion de clase: Estado principal del juego y mecanismos
 */

package com.example.batallanaval.controller;

import com.example.batallanaval.model.Cell;
import com.example.batallanaval.model.CellState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.*;

/**
 * Clase que representa el controlador del juego Batalla Naval.
 */
public class GameController {

    // Contador de vida
    @FXML
    private Label VIDA;

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

    private int shipsRemaining; // Declara la variable shipsRemaining para los barcos que siguen a flote

    private Random random = new Random();

    // ESTO UTILIZA ELEMENTOS DE TIPO STRING PARA CADA CELDA, GARANTIZA QUE SEA UNICO CADA ELEMENTO Y NOS AYUDA A EVITAR REPETICIONES
    private Set<String> machineShots = new HashSet<>();

    private boolean playerTurn = true;

    /**
     * Inicializa el tablero del juego.
     */
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

    /**
     * Inicializa el juego.
     */
    @FXML
    public void initialize() {
        initializeBoard(gridPanePlayer1, cells1, true);
        initializeBoard(gridPanePlayer2, cells2, true);

        ships1 = placeShips(cells1);
        ships2 = placeShips(cells2);

        // shipsRemaining = ships1.size(); // Inicializa shipsRemaining
        // VIDA.setText(String.valueOf(shipsRemaining)); // Actualiza el Label "vida"

        playerTurn = random.nextBoolean();
        if (!playerTurn) {
            machineTurn();
        }
    }

    /**
     * Coloca los barcos en el tablero.
     */
    private List<List<Cell>> placeShips(Cell[][] cells) {
        List<List<Cell>> ships = new ArrayList<>();

        // 1 Portaaviones
        ships.add(placeShip(cells, 4));

        // 2 Submarinos
        ships.add(placeShip(cells, 3));
        ships.add(placeShip(cells, 3));

        // 3 Destructores
        ships.add(placeShip(cells, 2));
        ships.add(placeShip(cells, 2));
        ships.add(placeShip(cells, 2));

        // 4 Fragatas
        ships.add(placeShip(cells, 1));
        ships.add(placeShip(cells, 1));
        ships.add(placeShip(cells, 1));
        ships.add(placeShip(cells, 1));

        // Puedes continuar agregando más barcos aquí

        return ships;
    }

    /**
     * Coloca un barco en una posición aleatoria del tablero.
     */
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

    /**
     * Maneja el evento de click en una celda del tablero del jugador.
     */
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

    /**
     * Turno de la máquina para disparar.
     */
    private void machineTurn() {
        if (!playerTurn) {
            boolean hit;
            do {
                hit = machineShoot();
            } while (hit && !playerTurn); // El bucle se detiene si la máquina acierta y el jugador no ha cambiado el turno
        }
    }

    /**
     * La máquina realiza un disparo.
     */
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

    /**
     * Maneja el clic en una celda del tablero.
     */
    private boolean handleCellClick(Cell cell, List<List<Cell>> ships) {
        if (cell.getState() == CellState.AGUA) {
            cell.getButton().setDisable(true); // Deshabilita el botón para evitar futuros eventos
            cell.getButton().setStyle("-fx-background-color:  #00f6ff;"); // Indica agua
            return false;
        } else if (cell.getState() == CellState.BARCO) {
            cell.getButton().setDisable(true); // Deshabilita el botón para evitar futuros eventos
            cell.getButton().setStyle("-fx-background-color:  #ff0000;");
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

    /**
     * Verifica si un barco ha sido hundido y realiza las acciones correspondientes.
     *
     * @param ships Lista de listas de celdas que representan los barcos.
     */

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
                    cell.getButton().setStyle("-fx-background-color: #000000;");
                }
               // shipsRemaining--;
               // VIDA.setText(String.valueOf(shipsRemaining));
                if (areAllShipsSunk(ships)) {
                    endGame();
                }
            }
        }
    }

    /**
     * Verifica si todos los barcos han sido hundidos.
     *
     * @param ships Lista de listas de celdas que representan los barcos.
     * @return True si todos los barcos han sido hundidos, false de lo contrario.
     */
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

    // Inicia en falso hasta la falta de barcos arroje la alerta
    private boolean gameEnded = false;

    /**
     * Desactiva los controles relevantes del juego.
     */
    private void disableControls() {
        // Desactiva los botones del tablero para evitar más interacción del jugador
        for (Node node : gridPanePlayer2.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setDisable(true);
            }
        }
        for (Node node : gridPanePlayer1.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setDisable(true);
            }
        }
    }

    /**
     * Configuracion del alert box
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Finaliza el juego y arroja la alerta
     */
    private void endGame() {
        if (!gameEnded) { // Verifica si el juego ya ha finalizado para evitar que se repita el mensaje
            gameEnded = true; // Marca el juego como finalizado

            // Llama al método showAlert para mostrar el Alert Box
            showAlert(AlertType.INFORMATION, "Fin del juego", "¡El juego ha terminado!");

            // Desactiva los controles relevantes para evitar más interacción del usuario
            disableControls();
        }
    }
}
