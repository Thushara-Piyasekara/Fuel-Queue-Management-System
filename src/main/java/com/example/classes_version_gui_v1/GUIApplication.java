package com.example.classes_version_gui_v1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 COPYRIGHT (C) 2022 Thushara Piyasekara.
 All Rights Reserved.
 Class used as an intermediate to launch the GUI
 Solves SD II Course Work - Class Version
 @author Thushara Piyasekara(w1899372)
 @version 2.5 2022-08-08
 */
public class GUIApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException // sets the stage and launches the GUI
    {
        Parent root = FXMLLoader.load(getClass().getResource("GUI-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Fuel Queue Management System - User Data");
        stage.show();
    }
}
