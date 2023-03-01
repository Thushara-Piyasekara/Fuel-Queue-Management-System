package com.example.classes_version_gui_v1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 COPYRIGHT (C) 2022 Thushara Piyasekara.
 All Rights Reserved.
 Class used to save passenger objects
 Solves SD II Course Work - Class Version
 @author Thushara Piyasekara(w1899372)
 @version 2.5 2022-08-08
 */

public class FuelQueue implements Serializable
{
    private ArrayList<passenger> Queue = new ArrayList<>(); //used to save the passenger objects
    private int queueNum; //used when printing messages
    private int totalFuelAmount = 0; // used to calculate the income of each queue
    private static final int PRICE_OF_ONE_LITER = 430; //price of one liter of fuel

    //    -------------- Constructor ---------------
    public FuelQueue(int queueNum) // constructor for FuelQueue Class
    {
        this.queueNum = queueNum;
    }

    //    -------------- Getters -------------------
    public ArrayList<passenger> getQueue() //get the passenger objects as an ArrayList
    {
        return Queue;
    }

    public int getLength() // get the number of passengers in the queue
    {
        return Queue.size();
    }

    public int getTotalFuelAmount() // get the total fuel requirement of the queue
    {
        return totalFuelAmount;
    }

    public int getQueueNum() // get the queue number
    {
        return queueNum;
    }

    //    -------------- other Methods --------------
    public void addPassenger(passenger passengerObject) //adding a passenger
    {
        Queue.add(passengerObject);
        this.totalFuelAmount += passengerObject.getNoOfLiters();
    }

    public void printQueue() //prints the names of customers of a given queue
    {
        System.out.println("Pump " + queueNum + " : ");
        if (getLength() == 0)
        {
            System.out.println("      This Queue is empty");
        }
        else
        {
            for (int i = 0; i < getLength(); i++)
            {
                System.out.println("      " + (i + 1) + ". " + Queue.get(i).getfullName());
            }
        }
    }

    public void printEmptyQueue(int MAX_PASSENGERS_PER_QUEUE) //prints the empty spots in the queue
    {
        System.out.println("Pump " + queueNum + " : ");
        if (getLength() == 0)
        {
            System.out.println("      This Queue is empty");
        }
        else if (getLength()<MAX_PASSENGERS_PER_QUEUE)
        {
            for (int i = 0; i < getLength(); i++)
            {
                System.out.println("      " + (i + 1) + ". " + Queue.get(i).getfullName());
            }
            for (int i = getLength(); i < MAX_PASSENGERS_PER_QUEUE; i++)
            {
                System.out.println("      " + (i + 1) + ". Empty");
            }
        }else
        {
            System.out.println("This Queue is full");
        }
    }

    public void servePassenger() //remove the passenger in the front of the queue
    {
        Queue.remove(0);
        this.totalFuelAmount -= Queue.get(0).getNoOfLiters();
//        passenger.deductPassengerCount();

        System.out.println("Served Customer removed successfully from pump " + queueNum);
    }

    public void removePassenger(int passengerNo) //remove the passenger of a given index
    {
        this.totalFuelAmount -= Queue.get(passengerNo).getNoOfLiters();
        Queue.remove(passengerNo);
//        passenger.deductPassengerCount();

        System.out.println("Customer removed successfully from pump" + queueNum);
    }

    public void printIncome() // prints the income of a given queue
    {
        System.out.println("Pump " + queueNum + " Income: " + totalFuelAmount*PRICE_OF_ONE_LITER);
    }
}
