package Task3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BhavCopy {
	  private List<String[]> data; // store the CSV data as an array of strings
	    private Map<String, String[]> symbolMap; // to map SYMBOL to its data

		// Constructor that initializes the data
	    public BhavCopy(String filename) throws IOException 
	    {
	        data = new ArrayList<>();
	        symbolMap = new HashMap<>();
	        loadBhavCopy(filename); // Load data from the specified file
	    }

		// Method to load data from a CSV file
	    private void loadBhavCopy(String filename) throws IOException 
	    {
	        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	            String line;
	            String[] headers = br.readLine().split(","); // Read header
	            while ((line = br.readLine()) != null) // Read each subsequent line
	            {
	                String[] values = line.split(",");
	                data.add(values); // Add array of values to the data list
	                symbolMap.put(values[0], values); // Store by SYMBOL
	            }
	        }
	    }

		//1.Method to retrieve information of particular symbol
	    public String getSymbolInfomation(String symbol) {
	        return Arrays.toString(symbolMap.get(symbol)); // Return the symbol's data as a string
	    }
        
		//2.Method to count number of symbols for a given series
	    public long countSymbolsBySeries(String series) {
	      long count=data.stream()
	              .filter(row -> row[1].trim().equalsIgnoreCase(series.trim()))
	              .count();
	      return count;
	    }

		 //3.Method to find List all symbols where ((CLOSE - OPEN) / OPEN) > N% 
		 // gains above a specified percentage
	    public List<String> gainAboveN(double n) {
	        return data.stream()
	                .filter(row -> {
	                    double close = Double.parseDouble(row[8]);
	                    double open = Double.parseDouble(row[4]);
	                    return ((close - open) / open) > (n / 100); //Check gain
	                })
	                .map(row -> row[0]) // Map to symbol
	                .collect(Collectors.toList()); // Collect matching symbols into a list	
	    }

		//4.List all symbols where ((HIGH - LOW) / LOW) > N%
		//Find top or bottom N symbols based on price movement
	    public List<String> topBottomN(double n, boolean isTop){
	        return data.stream()
	                .filter(row -> {
	                    double high = Double.parseDouble(row[5]);
	                    double low = Double.parseDouble(row[6]);
	                    return ((high - low) / low) > (n / 100);
	                })
	                .map(row -> row[0])
	                .sorted(isTop ? Comparator.reverseOrder() : Comparator.naturalOrder())
	                .limit(5) // Adjust as needed
	                .collect(Collectors.toList());
	    }

		//5.Standard deviation for all symbols of a given series
	    public double stdDevBySeries(String series){
	        List<Double> closePrices = data.stream()
	                .filter(row -> row[1].trim().equals(series.trim()))
	                .map(row -> Double.parseDouble(row[8]))
	                .collect(Collectors.toList());

	        double mean = closePrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
	        double variance = closePrices.stream().mapToDouble(price -> Math.pow(price - mean, 2)).average().orElse(0.0);
	        return Math.sqrt(variance);
	    }
	 
	    // 6. Top N symbols having maximum gain
	    public List<String> topNGainers(int n) 
	    {
	        return data.stream()
	                   .map(row -> new AbstractMap.SimpleEntry<>(row[0], 
	                        (Double.parseDouble(row[8]) - Double.parseDouble(row[4])) / Double.parseDouble(row[4]))) // Calculate gain
	                   .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
	                   .limit(n)
	                   .map(Map.Entry::getKey)
	                   .collect(Collectors.toList());
	    }

	    // 7. Bottom N symbols having lowest gain
	    public List<String> bottomNLossers(int n) 
	    {
	        return data.stream()
	                   .map(row -> new AbstractMap.SimpleEntry<>(row[0], 
	                        (Double.parseDouble(row[8]) - Double.parseDouble(row[4])) / Double.parseDouble(row[4]))) // Calculate gain
	                   .sorted(Map.Entry.comparingByValue())
	                   .limit(n)
	                   .map(Map.Entry::getKey)
	                   .collect(Collectors.toList());
	    }

	    // 8. Top N most traded (by volume) symbols
	    public List<String> topNTraded(int n) 
	    {
	        return data.stream()
	                   .sorted((row1, row2) -> Integer.compare(Integer.parseInt(row2[10].trim()), Integer.parseInt(row1[10].trim()))) // TTL_TRD_QNTY
	                   .limit(n)
	                   .map(row -> row[0].trim())
	                   .collect(Collectors.toList());
	    }

	    // 9. Bottom N least traded (by volume) symbols
	    public List<String> bottomNTraded(int n) {
	        return data.stream()
	                   .sorted(Comparator.comparingInt(row -> Integer.parseInt(row[10].trim()))) // TTL_TRD_QNTY
	                   .limit(n)
	                   .map(row -> row[0].trim())
	                   .collect(Collectors.toList());
	    }

	    // 10. Highest and lowest traded (by TOTRDVAL) for a given series
	    public List<String> highLowBySeries(String series) 
	    {
	        List<Map.Entry<String, Double>> tradedValues = data.stream()
	                .filter(row -> row[1].trim().equalsIgnoreCase(series.trim()))
	                .map(row -> new AbstractMap.SimpleEntry<>(
	                        row[0], // SYMBOL
	                        Double.parseDouble(row[10]) * Double.parseDouble(row[6]) // TTL_TRD_QNTY * LAST_PRICE
	                ))
	                .collect(Collectors.toList());

	        if (tradedValues.isEmpty()) 
	        {
	            return Arrays.asList("No data available");
	        }

	        String highest = tradedValues.stream()
	                .max(Map.Entry.comparingByValue())
	                .map(Map.Entry::getKey)
	                .orElse("No highest");

	        String lowest = tradedValues.stream()
	                .min(Map.Entry.comparingByValue())
	                .map(Map.Entry::getKey)
	                .orElse("No lowest");

	        return Arrays.asList(highest, lowest);
	    }
	    
	    public static void main(String[] args) throws IOException 
	    {
	    	BhavCopy processor = new BhavCopy("C:\\Users\\Admin\\Downloads\\bhavdata.csv");
	        Scanner scanner = new Scanner(System.in);
	        String input;

	        System.out.println("Enter command (or 'quit' to exit):");
	        while (!(input = scanner.nextLine()).equals("quit")) {
	            String[] parts = input.split(" ");
	            switch (parts[0]) 
	            {
	                case "SYMBOL":
	                    System.out.println(processor.getSymbolInfomation(parts[1]));
	                    break;
	     
	                case "COUNT":
	                	long ncount=processor.countSymbolsBySeries(parts[1]);
	                    System.out.println("Total no.of count symbols by series :"+ ncount);
	                    break;
	                
	                case "GAIN":
	                    System.out.println(processor.gainAboveN(Double.parseDouble(parts[1])));
	                    break;
	                
	                case "TOPBOT":
	                    System.out.println(processor.topBottomN(Double.parseDouble(parts[1]), true));
	                    break;
	                
	                case "STDDEV":
	                    System.out.println(processor.stdDevBySeries(parts[1]));
	                    break;
	               
	                case "TOPGAINER":
	                    int topN = Integer.parseInt(parts[1]);
	                    System.out.println("Top " + topN + " gainers: " + processor.topNGainers(topN));
	                    break;

	                case "TOPLAGGARDS":
	                    int bottomN = Integer.parseInt(parts[1]);
	                    System.out.println("Bottom " + bottomN + " laggards: " + processor.bottomNLossers(bottomN));
	                    break;

	                case "TOPTRADED":
	                    int mostTradedN = Integer.parseInt(parts[1]);
	                    System.out.println("Top " + mostTradedN + " most traded: " + processor.topNTraded(mostTradedN));
	                    break;

	                case "BOTTRADED":
	                    int leastTradedN = Integer.parseInt(parts[1]);
	                    System.out.println("Bottom " + leastTradedN + " least traded: " + processor.bottomNTraded(leastTradedN));
	                    break;

	                case "HIGHLOW":
	                    System.out.println("Highest and lowest traded in " + parts[1] + " series: " + processor.highLowBySeries(parts[1]));
	                    break;    
	               
	                default:
	                    System.out.println("Unknown command");
	            }
	        }
	        scanner.close();
	    
	    }	    
}
