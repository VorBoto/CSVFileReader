import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

    
public class CSVFileReader1{

    /**
     * General varibles need to describe the csv file and the
     * containing information.
     */
    private boolean hasHeader = false;
    private String[] header = null;
    private String[][] body = null;
    private char delimiter = ',';

    /**
     * General constructor for the csvfilereader requers the filename,
     * if it has a header line at the beginning, and what the delimting
     * character is.
     */
    public CSVFileReader1(String filename, boolean hasHeader, char dl){

	/*
	 * Get the general file information describing the size of
	 * the file.
	 */
	this.delimiter = dl;
	int rows = getNumRows(filename);
	int columns = getNumColumns(filename);

	// Ready variables for thier use later on
	String line = "";
	Scanner inputFile = null;
	
	try{
	    inputFile = new Scanner(new FileInputStream(filename));
	
	    /*
	     * Check if the file has a header and if it does grab that 
	     * information and place it in the header array.
	     * Then instanciate the 2D array that is the body of the csv file.
	     */
	    if (hasHeader){
		this.body = new String[rows-1][columns];
		line = inputFile.nextLine();
		this.header = getEntries(line, columns);
	    } else {
		this.body = new String[rows][columns];
	    }

	    for(int i=0; i<body.length; i++){
		line = inputFile.nextLine();
		this.body[i] = getEntries(line, columns);
	    }
	    
	} catch (FileNotFoundException e){
	    System.out.println("File is not found.");
	    System.exit(0);
	} finally {
	    if (inputFile != null){
		inputFile.close();
	    }
	}
    }

    /**
     * Constructor that takes the file name and wether or not it has a
     * header, but assumes that the delimter is the common ','.
     */
    public CSVFileReader1(String filename, boolean hasHeader){
	this(filename,hasHeader,',');
    }


    /**
     * Constructor that takes only the file name assumes there is no
     * header and the delmiter is ','.
     */
    public CSVFileReader1(String filename){
	this(filename,false,',');
    }

    /**
     * Open the file and gather how many coulmns the file has
     * by counting the number of entries in the first line.
     * @return number of columns in the file
     */
    private int getNumColumns(String filename){
	
	Scanner input = null;
	int columns = 0, index = 0;
	String line = "";
	
	try{
	    input = new Scanner(new FileInputStream(filename));

	    line = input.nextLine();
	    
	    while(!line.isEmpty()){
		char ch = line.charAt(0);
		String entry = "";
		if (ch == '"'){
		    
		    index = line.indexOf("\"",1);
		    entry = line.substring(1,index);
		    line = line.substring(index+1);
		    
		    if (line.length() > 1){
			ch = line.charAt(0);
			
			//if (ch == ','){
			if (ch == this.delimiter){
			    line = line.substring(1);
			}
		    }
		} else {
		    
		    //if (line.contains(",")){
		    if(line.contains(String.valueOf(this.delimiter))){
			//index = line.indexOf(",");
			index = line.indexOf(this.delimiter);
			entry = line.substring(0,index);
			line = line.substring(index+1);
		    } else {
			entry = line.substring(0,line.length());
			line = line.substring(line.length());
		    }	    
		}
		
		columns++;
	    }
	    
	} catch (FileNotFoundException e){
	    System.out.println("File is not found.");
	    System.exit(0);
	} finally {
	    if (input != null){
		input.close();
	    }
	}
	    
	return columns;
    }

    /**
     * Get the number of rows simpley by counting the number
     * of line in the file.
     * @return number of rows or lines in the file
     */
    private int getNumRows(String filename){

	Scanner input = null;
	int rows = 0;
	
	try{
	    input = new Scanner(new FileInputStream(filename));

	    while(input.hasNextLine()){
		input.nextLine();
		rows++;
	    }
	} catch (FileNotFoundException e){
	    System.out.println("File is not found.");
	    System.exit(0);
	} finally {
	    if (input != null){
		input.close();
	    }
	}
	    
	return rows;
    }

    /**
     * General extractor of information from each line.
     * Given a line this will extract and place in the body the
     * information contained in it.
     * @return entries array 
     * @param line This should be a single line of data from the file
     * @param columns This should be the number of entries in the line
     */
    private String[] getEntries(String line, int columns){

	String[] entries = new String[columns];
	int i = 0, index = 0;
	
	while(!line.isEmpty()){
	    char ch = line.charAt(0);
	    String entry = "";
	    if (ch == '"'){
		
		index = line.indexOf("\"",1);
		entry = line.substring(1,index);
		entries[i] = entry;
		line = line.substring(index+1);
		
		if (line.length() > 1){
		    ch = line.charAt(0);

		    //if (ch == ','){
		    if (ch == this.delimiter){
			line = line.substring(1);
		    }
		}
	    } else {
		
		//if(line.contains(",")){
		if(line.contains(String.valueOf(this.delimiter))){
		    //index = line.indexOf(",");
		    index = line.indexOf(this.delimiter);
		    entry = line.substring(0,index);
		    entries[i] = entry;
		    line = line.substring(index+1);
		    
		} else {
		    entry = line.substring(0,line.length());
		    entries[i] = entry;
		    line = line.substring(line.length());		    
		}	    
	    }
	   
	    i++;
	}
		
	return entries;
    }

    /**
     * Produces a text representation of the csv file in the form of 
     * standard csv formated manner
     * @return table of the data including the header if there is one
     */
    public String toString(){

	String table = "";

	if(hasHeader){
	    for(int i=0; i<header.length; i++){
		if(i<header.length-1)
		    table += (header[i]+",");
		else
		    table += header;
	    }
	    table += "\n";
	} else {
	    for(int i=0; i<body.length; i++){
		for(int j=0; j<body[i].length; j++){
		    if(j<body[i].length-1)
			table += (body[i][j]+",");
		    else
			table += body[i][j];
		}
		table += "\n";
	    }
	}

	return table;
    }
    
    public String[] getHeader(){
	return header;
    }

    public String[][] getBody(){
	return body;
    }

    /**
     * Method for returning the body of the csv as a 2D array
     * of integers
     */
    public int[][] getBodyInt(){

	int rows = this.body.length;
	int columns = this.body[0].length;
	
	int[][] bodyInt = new int[rows][columns];

	for(int i=0; i<this.body.length; i++){
	    for(int j=0; j<this.body[i].length; j++){
		bodyInt[i][j] = Integer.parseInt(body[i][j]);
	    }
	}

	return bodyInt;
    }

    /**
     * Method for returning the body of the csv as a 2D array
     * of doubles
     */
    public double[][] getBodyDouble(){

	int rows = this.body.length;
	int columns = this.body[0].length;

	double[][] bodyDouble = new double[rows][columns];

	for(int i=0; i<this.body.length; i++){
	    for(int j=0; j<this.body[i].length; j++){
		bodyDouble[i][j] = Double.parseDouble(body[i][j]);
	    }
	}

	return bodyDouble;
    }

		
    
}
