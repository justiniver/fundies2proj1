package uk.ac.london;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.*;

public class Image {
    private ArrayList<ArrayList<Color>> pixels; // creates pixels which is a 2D ArrayList of RGB values
    private int imageCounter = 0; // useful counter when I create multiple image files
    private Stack<ArrayList<ArrayList<Color>>> track2DArrayList; // stack created to keep track of pixel grids

    // method to create a deep copy of a 2D ArrayList containing Color objects
    private ArrayList<ArrayList<Color>> deepCopyPixels(ArrayList<ArrayList<Color>> original) {
        // initialize 2D ArrayList of color to store deep copy
        ArrayList<ArrayList<Color>> copy = new ArrayList<>();
        for (int y = 0; y < original.size(); y++) {
            ArrayList<Color> row = original.get(y); // retrieves current row from original 2D ArrayList
            ArrayList<Color> newRow = new ArrayList<>(); // initialize new ArrayList to store a copy of current row
            for (int x = 0; x < row.size(); x++) { // iterate over each Color object in current row
                newRow.add(row.get(x)); // copy each Color object.
            }
            copy.add(newRow); // add copied row to 2D ArrayList I made
        }
        return copy;
    }

    // asks user input for filepath to image then converts image to 2D ArrayList
    public void imageToArray() throws Exception {
        Scanner filePath = new Scanner(System.in);
        pixels = new ArrayList<>();
        track2DArrayList = new Stack();
        // loads image
        File imageFile = new File(filePath.next());
        BufferedImage image = ImageIO.read(imageFile);

        // iterates over images pixels
        for (int y = 0; y < image.getHeight(); y++) {
            ArrayList<Color> row = new ArrayList<>();
            for (int x = 0; x < image.getWidth(); x++) {
                // gets color of the current pixel
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel);
                row.add(color);
            }
            // add row to the pixels array
            pixels.add(row);
        }
        // add copy of pixels grid to my pixel grid tracker
        track2DArrayList.push(deepCopyPixels(pixels));
    }

    // finds index of the bluest column
    public int getBluestColumn() {

        int rowCount = pixels.size(); // total rows in the image
        int columnCount = pixels.getFirst().size(); // total columns in image
        int[] blueArray = new int[columnCount]; // array to hold the sum of blue values for each column

        for (int y = 0; y < rowCount; y++) { // iterates over each row
            for (int x = 0; x < columnCount; x++) { // iterates over each column in the row
                blueArray[x] = blueArray[x] + pixels.get(y).get(x).getBlue(); // accumulates the blue value for the column
            }
        }

        // compares the blue values of each index and returns the index with the max value
        int maxIndex = 0;
        int maxVal = 0;
        for (int i = 0; i < blueArray.length; i++) {
            if (blueArray[i] > maxVal) {
                maxVal = blueArray[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    // gets index of a random column
    public int getRandomColumn(){
        Random rand = new Random();
        return rand.nextInt(pixels.getFirst().size());
    }

    // method that takes in a column index and highlights that column in standard highlighting color (yellow-ish)
    // and creates an image of highlighted grid
    public void highlightColumn(int colIndexToHighlight) throws Exception {
        int rowCount = pixels.size(); // total rows in the image
        int columnCount = pixels.getFirst().size(); // total columns in image
        // new buffer for alteredImage
        BufferedImage newImg = new BufferedImage( columnCount, rowCount, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < rowCount; y++) { // iterates over each row
            for (int x = 0; x < columnCount; x++) { // iterates over each column in the row
                if (x == colIndexToHighlight) { // if were at the column we want to highlight
                    Color highlightColor = new Color(238, 255, 0); // my highlight color
                    newImg.setRGB(x, y, highlightColor.getRGB()); // sets my highlight color to newImg
                } else {
                    // for other columns/pixels, use their original color
                    Color originalColor = pixels.get(y).get(x);
                    newImg.setRGB(x, y, originalColor.getRGB());
                }
            }
        }
        // image counter increases before creating tempImage
        imageCounter++;
        // saves new tempImg
        File newFile = new File("tempImage" + imageCounter + ".png");
        ImageIO.write(newImg, "png", newFile);
        System.out.println("tempImage" + imageCounter + " stored");
    }

    // method that takes in a column index and removes that column and creates a new image of removed grid
    public void removeColumn(int colIndexToRemove) throws Exception {
        // iterate through each row and removes the column at colIndexToRemove
        for (int i = 0; i < pixels.size(); i++) {
            pixels.get(i).remove(colIndexToRemove);
        }

        int rowCount = pixels.size(); // total rows in the image
        int columnCount = pixels.getFirst().size(); // total columns in image

        // create a new BufferedImage with the column removed
        BufferedImage newImg = new BufferedImage(columnCount, rowCount, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++) { // iterate up to the original column count
                Color originalColor = pixels.get(y).get(x); // access the pixel from the modified row
                newImg.setRGB(x, y, originalColor.getRGB());
            }
        }
        // image counter increases before creating tempImage
        imageCounter++;
        // add copy of pixels grid to my pixel grid tracker
        track2DArrayList.push(deepCopyPixels(pixels));
        // save the new tempImg
        File newFile = new File("tempImage" + imageCounter + ".png");
        ImageIO.write(newImg, "png", newFile);
        System.out.println("tempImage" + imageCounter + " stored");
    }

    // if user undoes, we want to pop (remove) the element added most recently
    public void userUndo() {
        // if only element in stack is original grid, don't let them undo
        if (!track2DArrayList.isEmpty() && track2DArrayList.size() > 1) {
            track2DArrayList.pop(); // remove and retrieve the first elem
        } else {
            System.out.println("You cannot undo anymore");
        }
    }

    // when user quits we want to export the final image, newImg.png
    public void makeFinalImage() throws Exception {
        // gets the last pixel grid we want to make into the final image
        ArrayList<ArrayList<Color>> arrayToPrint = track2DArrayList.pop();
        int rowCount = arrayToPrint.size(); // total rows in the image
        int columnCount = arrayToPrint.getFirst().size(); // total columns in image

        // create a new BufferedImage with the column removed
        BufferedImage newImg = new BufferedImage(columnCount, rowCount, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++) { // iterate up to the original column count
                Color originalColor = arrayToPrint.get(y).get(x); // access the pixel from the modified row
                newImg.setRGB(x, y, originalColor.getRGB());
            }
        }
        // save the new tempImg
        File newFile = new File("newImg.png");
        ImageIO.write(newImg, "png", newFile);
        System.out.println("newImg stored");
    }
}
