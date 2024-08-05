package uk.ac.london;

import java.util.*;

public class UserInterface {


    // print options for user
    public static void printMenu() {
        System.out.println("Please make a selection");
        System.out.println("b - removes bluest column");
        System.out.println("r - removes random column");
        System.out.println("u - undo previous edit");
        System.out.println("q - quit");
    }

    // print response given their selection
    public static void printResponse(String selection) {
        switch(selection) {
            case "b":
                break;
            case "r":
                break;
            case "u":
                break;
            case "q":
                break;
            default:
                System.out.println("That is not a valid option");
                break;

        }
    }

    // takes in an input and checks whether they would like to continue
    // if not, returns true
    public static boolean doesNotContinue() {
        Scanner yesNoString = new Scanner(System.in);
        return yesNoString.next().toUpperCase().charAt(0) == 'N';
    }

    // src/main/resources/beach.png // useful filepath when testing

    public static void main(String[] args) throws Exception {

        System.out.println("Hello! Enter a file path: ");
        Image myImage = new Image(); // creates new instance of the image class
        myImage.imageToArray(); // immediately makes the image an array

        // keep track of if we want to keep the program running
        boolean shouldQuit = false;

        Scanner scan = new Scanner(System.in);
        String choice;

        // while shouldQuit is false, keep going
        while(!shouldQuit) {

            // display options to the user
            printMenu();

            choice = scan.next();
            printResponse(choice);

            if(choice.equals("b")) {
                myImage.highlightColumn(myImage.getBluestColumn());
                System.out.println("Remove bluest column. Continue? y/n");
                if (!doesNotContinue()) {
                    // removes bluest column if user proceeds
                    myImage.removeColumn(myImage.getBluestColumn());
                }
            }

            if(choice.equals("r")) {
                myImage.highlightColumn(myImage.getRandomColumn());
                System.out.println("Remove random column. Continue? y/n");
                if (!doesNotContinue()) {
                    // removes random column if user proceeds
                    myImage.removeColumn(myImage.getRandomColumn());
                }
            }

            if(choice.equals("u")) {
                System.out.println("Undo. Continue? y/n");
                if (!doesNotContinue()) {
                    myImage.userUndo();
                }
            }

            // if choice is quit, exit the while-loop
            if(choice.equals("q")) {
                System.out.println("Are you sure you would like to quit? y/n");
                if (!doesNotContinue()) {
                    myImage.makeFinalImage();
                }
                shouldQuit = true;
            }
        }
        scan.close();
    }
}
