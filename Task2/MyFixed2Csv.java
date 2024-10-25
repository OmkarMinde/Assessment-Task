package Task2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyFixed2Csv {
public static void main(String[] args) {
    // checks if at least three command-line arguments are provided.
    if (args.length < 3) {
        System.out.println("Usage: java inputfile <fixed-width-file> <n-columns> <length1> <length2> <length3> <length4>");
        return;
    }
    
    // Fixed-width file
    String fileName = args[0]; //first argument as fileName= input.txt
    String inputFile = "C:\\Users\\Admin\\Desktop\\Csv\\" + fileName; // Fixed-width file
    
    int nColumns = Integer.parseInt(args[1]); // Number of columns
    int[] lengths = new int[nColumns]; // Lengths of each column
    
    // Parse the lengths from the command line arguments
    for (int i = 2; i < args.length; i++) {  // loop starts from the third argument (index 2) because i=0 is file name, i=1 is total columns
        lengths[i - 2] = Integer.parseInt(args[i]); // Fill lengths array 
    }
    
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Admin\\Desktop\\Csv\\output.csv"))) {

           String line;
           while ((line = reader.readLine()) != null) {
               // Create empty StringBuilder object
               StringBuilder csvLine = new StringBuilder(); //StringBuilder is created to construct the CSV line.
               int currentIndex = 0;

               for (int i = 0; i < lengths.length; i++) {
                   // Check if currentIndex is within the bounds of the line
                   if (currentIndex + lengths[i] <= line.length()) {
                       String field = line.substring(currentIndex, currentIndex + lengths[i]).trim();
                        
                       // Remove leading zeros only for the second and third fields
                       // condition checks if the current column index is either 1 (second field) or 2 (third field).
                       if (i == 1 || i == 2) 
                       {
                        // regular expression to replace leading zeros with an empty string
                           field = field.replaceFirst("^0+(?!$)", ""); 
                       }

                       csvLine.append("\"").append(field).append("\",");
                       currentIndex += lengths[i];
                   } 
                   else 
                   {
                       // If the substring is out of bounds
                       System.out.println("Warning: Skipping column " + i + " due to out of bounds.");
                       break; 
                   }
               }

               // Remove the last comma and write the line
               if (csvLine.length() > 0) {
                   csvLine.setLength(csvLine.length() - 1); // Remove last comma
                   writer.write(csvLine.toString());
                   writer.newLine();
               }
           }

           System.out.println("CSV file has been created : output.csv");

       } catch (IOException e) {
           e.printStackTrace();
       }
 }    
}