package Task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FindSqrByNewtonB {

    // Method to calculate square root using Newton's method
    public static double getSqrootByNewton(double inputNumber) {
        double z = 1; // Initial guess
        int maxIteration = 25; // Max Iterations
        double tolerance = 0.001; // Tolerance for convergence

        for (int i = 0; i < maxIteration; i++) {
            double previousZ = z;
            z -= (z * z - inputNumber) / (2 * z); // Newton's method formula.

            if (Math.abs(z - previousZ) <= tolerance) { // Check for tolerance.
                break;
            }
        }
        return z; 
    }

    public static void main(String[] args) {
        // path of Input file
        String filePath = "C:\\Users\\Admin\\Desktop\\file.txt";

        // BufferedReader-Reads text from a character-input stream
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    // Parse the line to a double
                    double inputNumber = Double.parseDouble(line);

                    // Handle negative and zero case
                    if (inputNumber < 0) {
                        System.out.printf("%.2f Number provided is negative.%n", inputNumber);
                    } else if (inputNumber == 0) {
                        System.out.printf("%.2f Square root of zero is 0.%n", inputNumber);
                    } else {
                        // Calculate the square root for positive input
                        double result = getSqrootByNewton(inputNumber);
                        System.out.printf("The Square root of %.2f is %.4f%n", inputNumber, result);
                    }
                } catch (NumberFormatException e) {
                    // Handle non-numeric input
                    System.out.printf("%s Invalid input. Please enter a numeric value.%n", line);
                }
            }
        } catch (IOException e) {
            // Handle file not found
            System.out.println("File not found: " + e.getMessage());
        }
    }

}