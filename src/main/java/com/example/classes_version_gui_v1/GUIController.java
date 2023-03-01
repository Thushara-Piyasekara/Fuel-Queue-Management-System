package com.example.classes_version_gui_v1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 COPYRIGHT (C) 2022 Thushara Piyasekara.
 All Rights Reserved.
 Class used as the Controller of the GUI
 Solves SD II Course Work - Class Version
 @author Thushara Piyasekara(w1899372)
 @version 2.5 2022-08-08
 */

public class GUIController implements Initializable
{
    @FXML private TextField filterField; // search bar
    @FXML private TableView<passenger> queueTableView1,queueTableView2,queueTableView3,queueTableView5,queueTableView4,queueTableViewWait; // 6 TableView objects for each table
    @FXML private TableColumn<passenger,String> fName1,fName2,fName3,fName5,fName4,fNameWait;
    @FXML private TableColumn<passenger,String> sName1,sName2,sName3,sName5,sName4,sNameWait;
    @FXML private TableColumn<passenger,String> vehicleNo1,vehicleNo2,vehicleNo3,vehicleNo5,vehicleNo4,vehicleNoWait;
    @FXML private TableColumn<passenger,Integer> noOfLiters1,noOfLiters2,noOfLiters3,noOfLiters5,noOfLiters4,noOfLitersWait;
// above 4 rows contain the columns of tables
    private final ObservableList<passenger> queueList1 = FXCollections.observableArrayList();
    private final ObservableList<passenger> queueList2 = FXCollections.observableArrayList();
    private final ObservableList<passenger> queueList3 = FXCollections.observableArrayList();
    private final ObservableList<passenger> queueList4 = FXCollections.observableArrayList();
    private final ObservableList<passenger> queueList5 = FXCollections.observableArrayList();
    private final ObservableList<passenger> queueListWait = FXCollections.observableArrayList();
//6 observable lists for viewing and searching passenger details

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setCells(fName1,sName1,vehicleNo1,noOfLiters1); // 5 fuel queues
        setCells(fName2,sName2,vehicleNo2,noOfLiters2);
        setCells(fName3,sName3,vehicleNo3,noOfLiters3);
        setCells(fName4,sName4,vehicleNo4,noOfLiters4);
        setCells(fName5,sName5,vehicleNo5,noOfLiters5);
        setCells(fNameWait,sNameWait,vehicleNoWait,noOfLitersWait);

        addAndSearch(queueList1, queueTableView1, 1); // 5 fuel queues
        addAndSearch(queueList2, queueTableView2, 2);
        addAndSearch(queueList3, queueTableView3, 3);
        addAndSearch(queueList4, queueTableView4, 4);
        addAndSearch(queueList5, queueTableView5, 5);


        for (int i = FuelStation.front; i < FuelStation.rear + 1; i++) // waiting list
        {
            queueListWait.add(FuelStation.waitingList[i]);
        }
        FilteredList<passenger> filteredData1 = new FilteredList<>(queueListWait, b -> true); // waiting list
        GUISearch(filteredData1,queueTableViewWait);
    }

    //Assigns the related object properties for the columns
    public void setCells(TableColumn<passenger,String> fName,TableColumn<passenger,String> sName,TableColumn<passenger,String> vehicleNo,TableColumn<passenger,Integer> noOfLiters)
    {
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        sName.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        vehicleNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
        noOfLiters.setCellValueFactory(new PropertyValueFactory<>("noOfLiters"));
    }


    //Adds passengers to the respective observable list and Calls the "GUISearch" method
    public void addAndSearch(ObservableList<passenger> obserQueueList, TableView<passenger> queueTableView, int queuNo)
    {
        for (passenger passenger : FuelStation.allPumps[queuNo-1].getQueue())
        {
            obserQueueList.add(passenger);
        }

        FilteredList<passenger> filteredData = new FilteredList<>(obserQueueList, b -> true);
        GUISearch(filteredData,queueTableView);
    }

    /**
     * This method was referenced from https://www.youtube.com/watch?v=FeTrcNBVWtg
     * This method makes the search function of the GUI work
     * The method was edited to suit the program
     */
    public void GUISearch(FilteredList<passenger> filteredData , TableView<passenger> queueTableView)
    {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(passenger -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (passenger.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (passenger.getSecondName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (passenger.getVehicleNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches vehicle No.
                } else if (String.valueOf(passenger.getNoOfLiters()).contains(lowerCaseFilter))
                    return true;
                else
                    return false;
            });
        });
        SortedList<passenger> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(queueTableView.comparatorProperty());
        queueTableView.setItems(sortedData);
    }
}