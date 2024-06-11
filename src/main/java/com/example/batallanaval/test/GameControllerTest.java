/**
 *

import com.example.batallanaval.model.Cell;
import com.example.batallanaval.model.CellState;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameControllerTest {

    private GameController gameController;
    private Cell[][] cells;
    private GridPane gridPane;

    @Before
    public void setUp() {
        gameController = new GameController();
        cells = new Cell[GameController.SIZE][GameController.SIZE];
        gridPane = new GridPane();
    }

    @Test
    public void testInitializeBoard() {
        gameController.initializeBoard(gridPane, cells, true);
        // Verificar que se hayan inicializado correctamente todas las celdas
        for (int row = 0; row < GameController.SIZE; row++) {
            for (int col = 0; col < GameController.SIZE; col++) {
                assertNotNull(cells[row][col]);
                assertNotNull(cells[row][col].getButton());
                assertEquals(CellState.AGUA, cells[row][col].getState());
            }
        }
    }

    @Test
    public void testPlaceShips() {
        List<List<Cell>> ships = gameController.placeShips(cells);
        // Verificar que se hayan colocado correctamente los barcos
        assertEquals(2, ships.size()); // Verificar que se hayan agregado dos barcos
        for (List<Cell> ship : ships) {
            assertTrue(ship.size() >= 3 && ship.size() <= 4); // Verificar que los barcos tengan entre 3 y 4 celdas
            for (Cell cell : ship) {
                assertEquals(CellState.BARCO, cell.getState()); // Verificar que todas las celdas del barco estén marcadas como BARCO
            }
        }
    }

    @Test
    public void testHandleCellClick() {
        // Crear una lista de celdas representando un barco
        List<Cell> ship = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Button button = new Button();
            cells[0][i] = new Cell(button, CellState.BARCO);
            ship.add(cells[0][i]);
        }
        List<List<Cell>> ships = new ArrayList<>();
        ships.add(ship);

        // Simular un clic en una celda del tablero del jugador
        gameController.handleCellClick(cells[0][0], ships);

        // Verificar que la celda cambie su estado a BARCO_HUNDIDO y que el botón esté deshabilitado
        assertEquals(CellState.BARCO_HUNDIDO, cells[0][0].getState());
        assertTrue(cells[0][0].getButton().isDisabled());
    }

    // Puedes agregar más pruebas según sea necesario
}

 */
