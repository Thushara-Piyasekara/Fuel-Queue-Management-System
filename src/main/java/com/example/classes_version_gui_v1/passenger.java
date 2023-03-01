package com.example.classes_version_gui_v1;
import java.io.Serializable;

/**
 COPYRIGHT (C) 2022 Thushara Piyasekara.
 All Rights Reserved.
 Class used to define passenger objects
 Solves SD II Course Work - Class Version
 @author Thushara Piyasekara(w1899372)
 @version 2.5 2022-08-08
 */

public class passenger implements Serializable
{
    private String firstName;
    private String secondName;
    private String vehicleNo;
    private int noOfLiters;
    static private int passengerCount = 0;

    //    -------------- Constructor --------------
    public passenger(String firstname, String secondName, String vehicleNo, int noOfLiters) // constructor for passenger class
    {
        this.firstName = firstname;
        this.secondName = secondName;
        this.vehicleNo = vehicleNo;
        this.noOfLiters = noOfLiters;

        passengerCount++;
    }

    //    -------------- Getters --------------
    public String getFirstName() //returns first name
    {
        return firstName;
    }

    public String getSecondName() //returns first name
    {
        return secondName;
    }

    public String getVehicleNo()//returns vehicle number
    {
        return vehicleNo;
    }

    public int getNoOfLiters() // returns the no of liters requested
    {
        return noOfLiters;
    }

    public String getfullName()  //useful for printing passenger names
    {
        return firstName + " " + secondName;
    }
}
