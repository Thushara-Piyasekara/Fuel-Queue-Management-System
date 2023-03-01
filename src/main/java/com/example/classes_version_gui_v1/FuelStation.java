package com.example.classes_version_gui_v1;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 COPYRIGHT (C) 2022 Thushara Piyasekara.
 All Rights Reserved.
 Main Class for Fuel Queue Management System
 Solves SD II Course Work - Class Version
 @author Thushara Piyasekara(w1899372)
 @version 2.5 2022-08-08
 */
public class FuelStation implements Serializable
{
    static final int MAX_PASSENGERS_PER_QUEUE = 6;
    // above constant is used to determine the maximum number of customers per queue
    static int stock = 6600;
    //stock variable to keep track of fuel stock
    static final int STOCK_WARNING_AMOUNT = 500;
    //Warning amount
    static FuelQueue pump1 = new FuelQueue(1);
    static FuelQueue pump2 = new FuelQueue(2);
    static FuelQueue pump3 = new FuelQueue(3);
    static FuelQueue pump4 = new FuelQueue(4);
    static FuelQueue pump5 = new FuelQueue(5);
    static FuelQueue[] allPumps = {pump1, pump2, pump3, pump4, pump5}; //Array with all the FuelQueue objects
    static Scanner userIn = new Scanner(System.in);


    //    ---------------------------- Main Method ------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws InterruptedException
    {
        boolean menuLoop = true;
        while (menuLoop)//  Loops the menu
        {
            try
            {
                System.out.println(
                        """
                                100 or VFQ: View all Fuel Queues.
                                101 or VEQ: View all Empty Queues.
                                102 or ACQ: Add customer to a Queue.
                                103 or RCQ: Remove a customer from a Queue
                                104 or PCQ: Remove a served customer.
                                105 or VCS: View Customers Sorted in alphabetical order.
                                106 or SPD: Store Program Data into file
                                107 or LPD: Load Program Data from file.
                                108 or STK: View Remaining Fuel Stock.
                                109 or AFS: Add Fuel Stock.
                                110 or IFQ: View income of each Queue.
                                111 or GUI: View the GUI.
                                999 or EXT: Exit the Program.
                                """);

                System.out.print("Please select an option : ");
                String menu = userIn.next().toUpperCase();
                printDollars();
                switch (menu)
                {
                    case "100", "VFQ" -> VFQ();
                    case "101", "VEQ" -> VEQ();
                    case "102", "ACQ" -> ACQ();
                    case "103", "RCQ" -> RCQ();
                    case "104", "PCQ" -> PCQ();
                    case "105", "VCS" -> VCS();
                    case "106", "SPD" -> SPD();
                    case "107", "LPD" -> LPD();
                    case "108", "STK" -> STK();
                    case "109", "AFS" -> AFS();
                    case "110", "IFQ" -> IFQ();
                    case "111", "GUI" ->
                    {
                        System.out.println("GUI Loaded Successfully!");
                        myLaunch(GUIApplication.class);
                    }
                    case "999", "EXT" ->
                    {
                        System.out.println("Exiting the program....");
                        menuLoop = false;
                    }
                    default -> System.out.println("Please enter a valid input."); //used for input validation
                }
            } catch (InputMismatchException ex)
            {
                printDollars();
                System.out.println("Invalid input!");
                String Consumer = userIn.nextLine(); // This is used to consume the remaining user input from Scanner
            }
            printDollars();
            Thread.sleep(1000);
        }
    }

    //    ---------------------------- other Methods -----------------------------------------------------------------------------------------------------------------

    static void printDollars() //Prints a line with dollar sign to divide the outputs(used for merely aesthetic purposes)
    {
        System.out.println("\n" + "$".repeat(40) + "\n");
    }

    static void VFQ() //View All Fuel Queues
    {
        for (FuelQueue pump : allPumps)
        {
            pump.printQueue();
        }
    }

    static void VEQ() //View All Empty Fuel Queues
    {
        for (FuelQueue pump : allPumps)
        {
            pump.printEmptyQueue(MAX_PASSENGERS_PER_QUEUE);
        }
    }

    static void ACQ() // Add Customer to Queue
    {
        boolean queueLimitCheck = getPassengerCount() < (MAX_PASSENGERS_PER_QUEUE * (allPumps.length)); // checks whether all queues are full

        System.out.print("Enter First name : ");
        String fName = userIn.next();
        System.out.print("Enter Second name : ");
        String sName = userIn.next();
        System.out.print("Enter Vehicle No : ");
        String vehicleNo = userIn.next();

        boolean fuelAmountLoop = false;
        do
        {
            System.out.print("Enter Number of liters : ");
            int noOfLiters = userIn.nextInt();

            if (queueLimitCheck)
            {
                if (noOfLiters > stock)
                {
                    System.out.println("Not enough fuel in stock. Please request a lower amount");
                    STK();
                    fuelAmountLoop = true;
                } else
                {
                    passenger customer = new passenger(fName, sName, vehicleNo, noOfLiters);
                    FuelQueue minPump = findMinLength();
                    minPump.addPassenger(customer);
                    stock -= customer.getNoOfLiters(); // Update Stock
                    System.out.println("Customer added to the Queue successfully!");
                    if (stock < STOCK_WARNING_AMOUNT)
                    {
                        System.out.println("Warning! Stock is below 500 Liters");
                    }
                    fuelAmountLoop = false;
                }
            } else // if all the Fuel Queues are Full, new customer will be added to the Waiting List
            {
                if ((stock - waitFuelReq) > noOfLiters) // checks whether the no of liters requested is smaller than the difference between remaining stock and the fuel requirement of the waiting list
                {
                    passenger customer = new passenger(fName, sName, vehicleNo, noOfLiters);
                    addToWaitingList(customer);
                    fuelAmountLoop = false;
                }else
                {
                    System.out.println("No of liters requested is too high! Please enter an amount lower than " + (stock - waitFuelReq) + "liters");
                    fuelAmountLoop = false;
                }
            }
        } while (fuelAmountLoop);
    }

    static FuelQueue findMinLength() // Finds and returns the pump with the least number of customers
    {
        int minLength = allPumps[0].getLength();
        FuelQueue minPump = allPumps[0];

        for (FuelQueue pump : allPumps)
        {
            if (pump.getLength() < minLength)
            {
                minLength = pump.getLength();
                minPump = pump;
            }
        }
        return minPump;
    }

    static int getPassengerCount() // Returns the total number of passengers
    {
        int totalCount = 0;
        for (FuelQueue pump : allPumps)
        {
            totalCount += pump.getLength();
        }
        return totalCount;
    }

    static void PCQ() // Serve Customer
    {
        boolean promptPumpAgain = false;
        do
        {
            System.out.print("Enter pump No : ");
            int pumpNo = userIn.nextInt() - 1;
            if (pumpNo >= 0 && pumpNo < allPumps.length)
            {
                allPumps[pumpNo].servePassenger();
                moveFromWaitingList(allPumps[pumpNo]);
                promptPumpAgain = false;
            } else
            {
                System.out.println("Invalid pump No");
                promptPumpAgain = true;
            }
        } while (promptPumpAgain);
    }

    static void RCQ()  //Remove a customer
    // could make this more efficient with try and catch. just replace the if else blocks with try and catch with "out of bounds" exceptions.
    {
        boolean promptPumpAgain = false;
        do
        {
            VFQ(); // View Fuel Queues
            printDollars();
            System.out.print("Enter pump No : ");
            int pumpNo = userIn.nextInt() - 1;

            if (pumpNo >= 0 && pumpNo < allPumps.length)
            {
                if (allPumps[pumpNo].getLength() > 0)
                {
                    allPumps[pumpNo].printQueue();
                    printDollars();
                    System.out.print("Enter the customer number : ");
                    int custNo = userIn.nextInt();
                    if (custNo > 0 && custNo <= allPumps[pumpNo].getQueue().size())
                    {
                        stock += allPumps[pumpNo].getQueue().get(custNo - 1).getNoOfLiters();
                        allPumps[pumpNo].removePassenger(custNo - 1);
                        moveFromWaitingList(allPumps[pumpNo]);
                        promptPumpAgain = false;
                    } else
                    {
                        System.out.println("Invalid customer number");
                        promptPumpAgain = true;
                    }
                } else
                {
                    System.out.println("The pump is empty");
                    promptPumpAgain = true;
                }

            } else
            {
                System.out.println("Invalid pump number");
                promptPumpAgain = true;
            }
            printDollars();
        } while (promptPumpAgain);
    }

    static void STK() //View Remaining Fuel Stock
    {
        System.out.println("Remaining Fuel stock : " + stock + " Liters");
    }

    static void AFS() //Add Fuel Stock
    {
        System.out.print("Enter the amount of stock to add : ");
        int stockAdd = userIn.nextInt();

        printDollars();
        if (stockAdd > 0)
        {
            stock = stock + stockAdd;
            System.out.println("Stock updated successfully!");
        } else
        {
            System.out.println("Invalid Input");
        }
    }

    static void VCS() // View Customers Sorted in alphabetical order
    {
        System.out.println("----------- Sorted List -----------");
        for (int i = 0; i < allPumps.length; i++)
        {
            System.out.println("Pump " + (i + 1) + " : ");
            sortAndPrint(allPumps[i]);
        }
    }

    static void sortAndPrint(FuelQueue pumpIn) //Sorts and prints the elements of a given array
    {
        String[] sortedPump = new String[pumpIn.getLength()];

        for (int i = 0; i < pumpIn.getLength(); i++)
        {
            String fullName = pumpIn.getQueue().get(i).getfullName();
            sortedPump[i] = fullName;
        }

        for (int i = 0; i < sortedPump.length - 1; i++) //https://www.tutorialspoint.com/How-to-sort-a-String-array-in-Java#:~:text=To%20sort%20a%20String%20array%20in%20Java%2C%20you%20need%20to,greater%20than%200%2C%20swap%20them
        {
            for (int j = i + 1; j < sortedPump.length; j++)
            {
                if (sortedPump[i].compareTo(sortedPump[j]) > 0)
                {
                    String temp = sortedPump[i];
                    sortedPump[i] = sortedPump[j];
                    sortedPump[j] = temp;
                }
            }
        }
        for (int i = 0; i < sortedPump.length; i++)
        {
            System.out.println("      " + (i + 1) + ". " + sortedPump[i]);
        }
    }

    static void IFQ() //prints the income of each queue
    {
        for (FuelQueue pump : allPumps)
        {
            pump.printIncome();
        }
    }

//    ------------------------------------------ Waiting List ---------------------------------------------------------------------------------------------------------------

    static int sizeOfWaitingList = 100;
    static passenger[] waitingList = new passenger[sizeOfWaitingList];
    static int front = 0;
    static int rear = 0;
    static boolean isEmpty = true; // used to check whether the queue is empty or not
    static int waitFuelReq = 0; // used to track the fuel requirement of waiting list
    // Stock is deducted only when a passenger is added to a queue. When a passenger is added to the waiting list stock is not deducted.
    // When a passenger a moved from the waiting list to one of the queues stock is deducted according to the passenger's fuel requirement

    static void addToWaitingList(passenger passengerExtra) // add a passenger to the waiting list
    {
        if (isEmpty) //When the waiting list is used for the first time
        {
            waitingList[rear] = passengerExtra;
            isEmpty = false;
            System.out.println("All Fuel Queues are Full! Customer " + passengerExtra.getfullName() + " Added to the Waiting List");
        } else if ((rear + 1) % waitingList.length == front) //If the queue is full, rear and front should be adjacent
        {
            System.out.println("Queue is Full!");
        } else
        {
            rear = (rear + 1) % waitingList.length;
            waitingList[rear] = passengerExtra;
            System.out.println("All Fuel Queues are Full! Customer " +passengerExtra.getfullName() + " Added to the Waiting List");
        }
    }

    static void moveFromWaitingList(FuelQueue queueToAdd) // move a customer from the waiting list to a Fuel Queue
    {
        if (isEmpty)
        {
            System.out.println("Waiting List is empty");
        }
        else
        {   int fuelRequirement = waitingList[front].getNoOfLiters();
            if (fuelRequirement < stock)
            {
                queueToAdd.addPassenger(waitingList[front]);
                stock -= waitingList[front].getNoOfLiters();
                String Name = waitingList[front].getfullName();

                if (front == rear) //this is executed when there is only one element left in the queue
                {
                    isEmpty = true;
                } else
                {
                    front = (front + 1) % waitingList.length;
                }
                System.out.println("Customer " + Name + " Moved from the queue the Waiting List");
            } else  // if there is not enough fuel
            {
                System.out.println("Customer from Waiting list cannot be added to the Fuel Queue because not enough Fuel Stock");
            }
        }
    }

    //    ------------------------------------------ Read/Write From File ----------------------------------------------------------------------------------------------

    public static void SPD() // Save data to file
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(new File("Customer_Data.txt"));
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(allPumps);
            objectOut.writeObject(stock);
            objectOut.writeObject(waitingList);

            System.out.println("Data saved to file successfully!");
            objectOut.close();
            fileOut.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        } catch (IOException e)
        {
            System.out.println("IO exception");
        }
    }

    public static void LPD() // Load data from file
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(new File("Customer_Data.txt"));
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            allPumps = (FuelQueue[]) objectIn.readObject();
            stock = (int) objectIn.readObject();
            waitingList = (passenger[]) objectIn.readObject();

            System.out.println("Data loaded from file successfully!");
            objectIn.close();
            fileIn.close();

        } catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        } catch (IOException e)
        {
            System.out.println("IO Error");
        } catch (ClassNotFoundException e)
        {
            System.out.println("Class not found in File");
        }
    }

    //    ------------------------------------------ GUI ---------------------------------------------------------------------------------------------------------------

    // Used the below reference to launch the GUI more than one time.
    //https://stackoverflow.com/questions/24320014/
    private static volatile boolean javaFxLaunched = false;
    public static void myLaunch(Class<? extends Application> applicationClass)
    {
        if (!javaFxLaunched)
        { // First time
            Platform.setImplicitExit(false);
            new Thread(() -> Application.launch(applicationClass)).start();
            javaFxLaunched = true;
        } else
        { // Next times
            Platform.runLater(() ->
            {
                try
                {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
    }
}
