package com.example.batallanaval.controller;

import com.example.batallanaval.model.Cell;
import com.example.batallanaval.model.CellState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameController {

    @FXML
    private GridPane gridPane;

    private static final int SIZE = 10;
    private Cell[][] cells = new Cell[SIZE][SIZE];
    private List<List<Cell>> ships = new ArrayList<>();

    @FXML
    public void initialize() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setStyle("-fx-background-color: #c0f1f3;");
                button.setOnAction(this::onHandleButtonPlay);
                gridPane.add(button, col, row);
                cells[row][col] = new Cell(button);
            }
        }
        initializeShips();
    }

    @FXML
    void onHandleButtonPlay(ActionEvent event) {
        Button button = (Button) event.getSource();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getButton() == button) {
                    handleCellClick(cells[row][col]);
                    return;
                }
            }
        }
    }

    private void handleCellClick(Cell cell) {
        if (cell.getState() == CellState.AGUA) {
            cell.getButton().setStyle("-fx-background-color: #0022ff;"); // Indica agua
        } else if (cell.getState() == CellState.BARCO) {
            cell.getButton().setStyle("-fx-background-color: #d73b3b;"); // Indica parte del barco tocada
            cell.setState(CellState.BARCO_HUNDIDO);
            checkIfShipSunk();
        } else if (cell.getState() == CellState.BARCO_HUNDIDO) {
            cell.getButton().setStyle("-fx-background-color: darkred;"); // Indica barco hundido
        }
    }

    // LISTADO DE BARCOS
    private void initializeShips() {
        // Barco 1
        List<Cell> ship1 = Arrays.asList(cells[2][7], cells[2][8], cells[2][9]);
        ships.add(ship1);
        for (Cell cell : ship1) {
            cell.setState(CellState.BARCO);
        }

        // Barco 2
        List<Cell> ship2 = Arrays.asList(cells[4][1], cells[4][2], cells[4][3], cells[4][4]);
        ships.add(ship2);
        for (Cell cell : ship2) {
            cell.setState(CellState.BARCO);
        }

        // Barco 3
        List<Cell> ship3 = Arrays.asList(cells[6][5], cells[7][5], cells[8][5]);
        ships.add(ship3);
        for (Cell cell : ship3) {
            cell.setState(CellState.BARCO);
        }

        // Barco 4
        List<Cell> ship4 = Arrays.asList(cells[9][0], cells[9][1]);
        ships.add(ship4);
        for (Cell cell : ship4) {
            cell.setState(CellState.BARCO);
        }
    }


    private void checkIfShipSunk() {
        for (List<Cell> ship : ships) {
            boolean allSunk = true;
            for (Cell cell : ship) {
                if (cell.getState() != CellState.BARCO_HUNDIDO) {
                    allSunk = false;
                    break;
                }
            }
            if (allSunk) {
                for (Cell cell : ship) {
                    cell.getButton().setStyle("-fx-background-color: #8b0000;");
                }
            }
        }
    }
}
