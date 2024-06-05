package com.example.batallanaval.model;

import javafx.scene.control.Button;

public class Cell {
    private CellState state;
    private Button button;

    public Cell(Button button) {
        this.state = CellState.AGUA;  // Estado inicial es agua
        this.button = button;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public Button getButton() {
        return button;
    }
}
