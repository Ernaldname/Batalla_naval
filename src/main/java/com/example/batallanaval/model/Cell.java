package com.example.batallanaval.model;

import javafx.scene.control.Button;

public class Cell {
    private Button button;
    private CellState state;

    public Cell(Button button, CellState state) {
        this.button = button;
        this.state = state;
    }

    public Button getButton() {
        return button;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }
}
