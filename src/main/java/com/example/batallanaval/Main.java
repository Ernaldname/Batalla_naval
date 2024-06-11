/**
 * @AUTOR: Edinson Arnulfo Ramirez Mendez - 2224504-2724
 * @Correo: edinson.ramirez@correunivalle.edu.co
 * @Version: V1
 *
 * Descripcion de clase: Obtiene la instancia unica de juego para su ejecucion
 */

package com.example.batallanaval;

import com.example.batallanaval.view.GameStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String [] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws IOException {
        GameStage.getInstance();
    }
}

