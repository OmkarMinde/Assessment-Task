package Task1;

public class FindSqrByNewton {
public static void main(String[] args) {

	 // argument is passed for check
    if (args.length == 0) {
        System.out.println("Number not provided...");
        return;
    }

    // Parse the input argument
    double inputNumber;
     
    try {
        inputNumber = Double.parseDouble(args[0]);
    } catch (NumberFormatException e) {
        System.out.println(args[0] + " is not a valid number.");
        return;
    }

    // Handle zero input
    if (inputNumber == 0) {
        System.out.println("The square root of 0 is 0");
        return;
    }

    // to Handle negative input
    if (inputNumber < 0) {
        System.out.println(inputNumber + " Number Provide Negative");
        return;
    }

    // Initialize variables for Newton's method
    double z = 1.0;  // Initial guess
    int maxIterations = 25;
    double tolerance = 0.001;

    //Newton's Method
    for (int i = 0; i < maxIterations; i++) {
        double prevZ = z;
        z -= (z * z - inputNumber) / (2 * z);  //Newton-Raphson formula= z-(z*z-x)/(2*z)

        // Check for difference bet z and prevZ.
        if (Math.abs(z - prevZ) <= tolerance) {
            break;
        }
    }

    // result
    System.out.printf("The Square root of %.2f is %.4f%n",inputNumber,z);
}
    
}