
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Finds the shortest TSP Path
 */
public class TSP {
	ArrayList<Trip> tripList = new ArrayList<Trip>();
	ArrayList<String> tripListStrings = new ArrayList<String>();
	int numCities;
	String[][] distances;
	int maxPath = Integer.MAX_VALUE;
	ArrayList<String> nodes;
	int[][] intDistanceMatrix;
	String dynamicTrip;
	public TSP(int cities, String[][] distanceMatrix, ArrayList<String> nodesString){
		numCities = cities;
		distances = distanceMatrix;
		nodes = nodesString;
	}

    /**
     * Returns dynamic trip result
     */
	public String getDynamicTrip(){
		return dynamicTrip;
	}

    /**
     * Returns the trip list from trip array of trip objects
     */
	public ArrayList<String> getTripList(){
		for(Trip t: tripList){
			String stringTrip = t.certificateKey();
			tripListStrings.add(stringTrip);
		}
		return tripListStrings;
	}

    /**
     * Finds the shorest route using brute force.
     */
	public String printShortTrip(){
		for(Trip t: tripList){
			String stringTrip = t.certificateKey();
		}
		int min = Integer.MAX_VALUE;
		Trip minTrip = null;
		for(int i=0; i<tripList.size(); i++){
			if(tripList.get(i).length < min){
				min = tripList.get(i).length;
				minTrip = tripList.get(i);
			}
		}
		String trip = minTrip.certificateKey();
		return trip;

	}

    /**
     * Prints the results of the dynamic program.
     */
	public void printDynamicTrip(List<Integer> certificate, int result){
		Trip dyTrip = new Trip(certificate, result, nodes);
		String trip = dyTrip.certificateKey();
		dynamicTrip = trip;
	}

    /**
     * Finds trip distances in the matrix
     */
	public boolean distanceReader(List<Integer> certificate){
		int sum = 0;
		int row = 0;
		int returnValue = 0;
		int column = 1;

		for(int i=0; i<numCities-1; i++){
			int value =  intDistanceMatrix[certificate.get(column)][certificate.get(row)];
			if(value == 0 ){
				value =  intDistanceMatrix[certificate.get(row)][certificate.get(column)];
			}
			sum +=  value;
			row++;
			column++;
		}

		returnValue =  intDistanceMatrix[certificate.get(column-1)][certificate.get(0)];
		int total = sum + returnValue;

		if(certificate.get(0) == 0){

			tripList.add(new Trip(certificate, total, nodes));
		}

		if(sum<=maxPath){
			return false;
		}

		return true;
	}

    /**
     * Gets cities to send to permuation method
     */
	public boolean generateCities(){
		List<Integer> cityList = new LinkedList<Integer>();
		for(int i = 0; i < numCities; i++){
			cityList.add(i);
		}
		return permutations(new LinkedList<Integer>(), cityList);
	}

    /**
     * Finds all possible combinations of trips using permutations
     */
	private boolean permutations(List<Integer> used, List<Integer> unused){
		if(unused.isEmpty()){

			if(distanceReader(used)){
				return true;
			}

		} else {

			for(int i = 0; i < unused.size(); i++){
				int item = unused.remove(0);
				used.add(item);

				if(permutations(used, unused))
					return true;

				used.remove(used.size()-1);
				unused.add(item);
			}
		}
		return false;
	}

    /**
     * Converts the string array to integers
     */
	public void stringToIntArray(){
		
		int tableStringLength = distances.length;
		intDistanceMatrix = new int[tableStringLength][tableStringLength];

		for(int i=0; i<tableStringLength; i++) {
			for(int j=0; j<tableStringLength; j++) {
				intDistanceMatrix[i][j]= Integer.parseInt(distances[i][j]);
			}
		}
	}

    /**
     * Finds the shorest trip using dynamic programming
     */
	public int dynamicShortestPath() {
		//find shortest path length
		int var;
		int n = intDistanceMatrix.length;
		int[][] dp = new int[1 << n][n];
		for (int[] d : dp)
			Arrays.fill(d, Integer.MAX_VALUE / 2);
		dp[1][0] = 0;
		for (int mask = 1; mask < 1 << n; mask += 2) {
			for (int i = 1; i < n; i++) {
				if ((mask & 1 << i) != 0) {
					for (int j = 0; j < n; j++) {
						if ((mask & 1 << j) != 0) {
							int value = intDistanceMatrix[j][i];
							int value2 = intDistanceMatrix[i][j];
							
							
							if(value == 0)
								value = value2;
							if(value !=0)
								//the trips
								dp[mask][i] = Math.min(dp[mask][i], dp[mask ^ (1 << i)][j] + value);
						}
					}
				}
			}
		}
		int res = Integer.MAX_VALUE;
		for (int i = 1; i < n; i++) {
			res = Math.min(res, dp[(1 << n) - 1][i] + intDistanceMatrix[i][0]);

		}
		int current = (1 << n) - 1;
		int[] order = new int[n];
		int value=0;
		int jvalue=0;
		int last = 0;
		for (int i = n - 1; i >= 1; i--) {
			int bj = -1;
			for (int j = 1; j < n; j++) {
				if ((current & 1 << j) != 0){
					if(bj != -1){
						value = intDistanceMatrix[bj][last];
						int value2 = intDistanceMatrix[last][bj];
						if(value == 0)
							value = value2;

						jvalue = intDistanceMatrix[j][last];
						int jvalue2 = intDistanceMatrix[last][j];
						if(jvalue == 0)
							jvalue = jvalue2;
					}
					if ((current & 1 << j) != 0 && (bj == -1 || dp[current][bj] + value > dp[current][j] + jvalue)) {
						
						bj = j;
					}
				}
			}
			order[i] = bj;
			current ^= 1 << bj;
			last = bj;
		}
		List<Integer> intList = new ArrayList<Integer>(order.length);

		for (int i=0; i<order.length; i++) {
			intList.add(order[i]);
		}
		printDynamicTrip(intList,res);
		return res;
	}
}