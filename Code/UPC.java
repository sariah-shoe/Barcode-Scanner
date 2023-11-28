package HW1;

// This is the starting version of the UPC-A scanner
//   that needs to be filled in for the homework

public class UPC {
	
	//--------------------------------------------
	// Scan in the bit pattern from the image
	// Takes the filename of the image
	// Returns an int array of the 95 scanned bits
	//--------------------------------------------
	public static int[] scanImage(String filename) {
		// Scan in my image
		DUImage barCodeImage = new DUImage(filename);
		
		// Create my array for code
		int[] picPat = null;
		picPat = new int[95];
		
		// Create an int to keep track of where I am in the array
		int arrayTracker = 0;
		
		// Create a loop that starts at 5 and goes until 195 (this is the length of the barcode) and the value changes by 2 (length of a bar)
		for(int i = 5; i < 195; i=i+2) {
			// Check to see if the bar is black or white (0 or no) then add to array
			int color = barCodeImage.getRed(i, 10);
			if (color == 0) {
				picPat[arrayTracker] = 1;
			}
			else {
				picPat[arrayTracker] = 0;
			}
			
			// Add one to array tracker
			arrayTracker ++;
		
		}
		
		return(picPat);
	}
	
	
	//--------------------------------------------
	// Performs a full scan decode that turns all 95 bits
	//   into 12 digits
	// Takes the full 95 bit scanned pattern
	// Returns an int array of 12 digits
	//   If any digit scanned incorrectly it is returned as a -1
	// If the start, middle, or end patterns are incorrect
	//   it provides an error and exits
	//--------------------------------------------
	public static int[] decodeScan(int[] scanPattern) {
		
		int[][] digitPat = {{0,0,0,1,1,0,1},
	            {0,0,1,1,0,0,1},	
	            {0,0,1,0,0,1,1},
	            {0,1,1,1,1,0,1},
	            {0,1,0,0,0,1,1},
	            {0,1,1,0,0,0,1},
	            {0,1,0,1,1,1,1},
	            {0,1,1,1,0,1,1},
	            {0,1,1,0,1,1,1},
	            {0,0,0,1,0,1,1}};
		
		// YOUR CODE HERE...
		//Create the array to hold the digits
		int[] allDigit = null;
		allDigit = new int[12];
		int allDigitCounter = 0;
		
		// Iterate through the left 6 digits. The iterator is the index of each new digit
		for(int l = 3; l < 45; l = l+7) {
			
			// Reset my variables for comparison
			int[] currentDigit = {0,0,0,0,0,0,0};
			int currentPos = 0;
			
			// Iterate through the 7 numbers that make up the digit, starting at the index of the current digit
			for(int m = l; m < l+7; m++) {
				//Set the 1 and 0 values for the current digit
				currentDigit[currentPos] = scanPattern[m];
				currentPos ++;
			}
			
			// Find matching digit and add it to all my digits
			// Reset my index number
			int index = 0;
			boolean digitFound = false;
			
			//For loop that goes through every number combination and compares it to my current digit 
			for(int[] w : digitPat) {
				// Goes through each int in my current digit combo
				for(int z = 0; z < 7; z++) {
					// If the int doesn't match my current digit then break this loop, this index isn't right
					if (w[z] != currentDigit[z]) {
						break;
					}
					// If I've gone through all the digits for this index and they all match my current digit, that is the digit!
					if (z == 6) {
						allDigit[allDigitCounter] = (index);
						digitFound = true;
						break;
					}
				}
				// If I've found my digit, don't loop again
				if (digitFound == true) {
					break;
				}
				// Increase my index to try the next one
				else {
					index ++;
				}
			}
			
			// If I've gone through all my digits and didn't get a match, its an invalid scan
			if (digitFound == false) {
				allDigit[allDigitCounter] = -1;
			}
			
			// Iterate through my next digit
			allDigitCounter ++;
			
		}
		
		// Iterate through the right 6 digits. The iterator is the index of each new digit
		for(int l = 50; l < 92; l = l+7) {
			
			// Reset my variables for comparison
			int[] currentDigit = {0,0,0,0,0,0,0};
			int currentPos = 0;
			
			// Iterate through the 7 numbers that make up the digit, starting at the index of the current digit
			for(int m = l; m < l+7; m++) {
				//Set the 1 and 0 values for the current digit
				currentDigit[currentPos] = scanPattern[m];
				currentPos ++;
			}
			
			// Find matching digit and add it to all my digits
			// Reset my index number
			int index = 0;
			boolean digitFound = false;
			
			//For loop that goes through every number combination and compares it to my current digit 
			for(int[] w : digitPat) {
				// Goes through each int in my current digit combo
				for(int z = 0; z < 7; z++) {
					// If the int doesn't match my current digit then break this loop, this index isn't right
					if (w[z] == currentDigit[z]) {
						break;
					}
					// If I've gone through all the digits for this index and they all match my current digit, that is the digit!
					if (z == 6) {
						allDigit[allDigitCounter] = (index);
						digitFound = true;
						break;
					}
				}
				// If I've found my digit, don't loop again
				if (digitFound == true) {
					break;
				}
				// Increase my index to try the next one
				else {
					index ++;
				}
			}
			
			// If I've gone through all my digits and didn't get a match, its an invalid scan
			if (digitFound = false) {
				allDigit[allDigitCounter] = -1;
			}
			
			// Iterate through my next digit
			allDigitCounter ++;
			
		}		
		
		return(allDigit);
	}
	
	//--------------------------------------------
	// Do the checksum of the digits here
	// All digits are assumed to be in range 0..9
	// Returns true if check digit is correct and false otherwise
	//--------------------------------------------
	public static boolean verifyCode(int[] digits) {
		
		//In the UPC-A system, the check digit is calculated as follows:
		//	1.Add the digits in the even-numbered positions (zeroth, second, fourth, sixth, etc.) together and multiply by three.
		//	2.Add the digits in the even-numbered positions (first, third, fifth, etc.) to the result.
		//	3.Find the result modulo 10 (i.e. the remainder when divided by 10.. 10 goes into 58 5 times with 8 leftover).
		//	4.If the result is not zero, subtract the result from ten.

		// Note that what the UPC standard calls 'odd' are our evens since we are zero based and they are one based
		
		// YOUR CODE HERE...
		// Create an int to hold the checksum
		int checkSum = 0;
		
		//Add together the even numbered digits
		for(int i = 0; i < 12; i = i +2) {
			checkSum = checkSum + digits[i];
		}
		
		//Add together the odd numbered digits
		for(int j = 1; j < 12; j = j +2) {
			checkSum = checkSum + digits[j];
		}
		
		//Find the checksum % 10
		checkSum = checkSum % 10;
		
		//Return false if that is 0
		if (checkSum == 0) {
			return(false);
		}
		
		// If not minus 10
		checkSum = 10 - checkSum;
		
		//If the checksum equals the digit return true, otherwise false
		if (checkSum == digits[11]) {
			return(true);
		}
		return(false);
	}
	
	//--------------------------------------------
	// The main method scans the image, decodes it,
	//   and then validates it
	//--------------------------------------------	
	public static void main(String[] args) {
	        // file name to process.
	        // Note: change this to other files for testing
	        String barcodeFileName = "barcodeUpsideDown.png";

	        // optionally get file name from command-line args
	        if(args.length == 1){
		    barcodeFileName = args[0];
		}
		
		// scanPattern is an array of 95 ints (0..1)
		int[] scanPattern = scanImage(barcodeFileName);

		// Display the bit pattern scanned from the image
		System.out.println("Original scan");
		for (int i=0; i<scanPattern.length; i++) {
			System.out.print(scanPattern[i]);
		}
		System.out.println(""); // the \n
				
		
		// digits is an array of 12 ints (0..9)
		int[] digits = decodeScan(scanPattern);
		
		// YOUR CODE HERE TO HANDLE UPSIDE-DOWN SCANS
		// If I have a messed up scan try to flip all the digits and see the result
		if (digits[0] == -1) {
			for(int i = 0; i <(scanPattern.length); i++) {
				if (scanPattern[i] == 1) {
					scanPattern[i] = 0;
				}
				else {
					scanPattern[i] = 1;
				}
			}
		}
		
		digits = decodeScan(scanPattern);
		
		
		
		// Display the digits and check for scan errors
		boolean scanError = false;
		System.out.println("Digits");
		for (int i=0; i<12; i++) {
			System.out.print(digits[i] + " ");
			if (digits[i] == -1) {
				scanError = true;
			}
		}
		System.out.println("");
				
		if (scanError) {
			System.out.println("Scan error");
			
		} else { // Scanned in correctly - look at checksum
		
			if (verifyCode(digits)) {
				System.out.println("Passed Checksum");
			} else {
				System.out.println("Failed Checksum");
			}
		}
	}
}

