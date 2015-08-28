import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class reads text files into trip matrices and finds the program's run time. It outputs a text file
 * of the results traveling salesman problem using dynamic programming and one using brute force, along with
 * a list all possible trips.
 */
public class Start {
    public static void main(String[] args) throws IOException {
        String workingdir = System.getProperty("user.dir");
        JFileChooser chooser = new JFileChooser(new File(workingdir));
        int result = chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        String fname = file.getName();
        ArrayList<String> nodesString = new ArrayList<String>();
        Scanner scanner = new Scanner(new File(fname));
        scanner.useDelimiter(System.getProperty("line.separator"));
        ArrayList<String> list = new ArrayList<String>();
        while (scanner.hasNext()) {
            list.add(scanner.next());
        }
        scanner.close();
        FileInputStream fstream = new FileInputStream(fname);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        int row = 0;
        String adjacency_matrix[][] = new String[list.size() - 1][];
        while ((line = br.readLine()) != null) {
            String[] nums = line.split("\\s+");
            if ((isNumber(nums[1]) == false) && !nums[1].equals("-")) {
                for (int i = 1; i < nums.length; i++) {
                    nodesString.add(nums[i]);
                }
                line = br.readLine();
                nums = line.split("\\s+");
            }
            String n = "0";
            adjacency_matrix[row] = new String[list.size() - 1];
            for (int col = 0; col < list.size() - 1; col++) {
                if (col + 1 < nums.length) {
                    adjacency_matrix[row][col] = nums[col + 1];

                } else {
                    adjacency_matrix[row][col] = n;
                }

            }
            row++;
        }

        in.close();
        br.close();
        ArrayList<String> tripList = new ArrayList<String>();
        TSP tsp = new TSP(list.size() - 1, adjacency_matrix, nodesString);
        tsp.stringToIntArray();
        System.nanoTime();
        long t0 = System.nanoTime();
        tsp.generateCities();
        String trip = tsp.printShortTrip();
        long t1 = System.nanoTime();
        JOptionPane.showMessageDialog(null, trip);
        long t2 = System.nanoTime();
        int res = tsp.dynamicShortestPath();
        long t3 = System.nanoTime();
        tripList = tsp.getTripList();
        String outputBF = "bf" + (list.size() - 1) + "cities.txt";
        String outputDP = "dp" + (list.size() - 1) + "cities.txt";
        PrintStream outBF = new PrintStream(new FileOutputStream(outputBF));
        for (String t : tripList) {
            outBF.println(t);
        }
        outBF.println("Time: " + (t1 - t0) + "ms");
        PrintStream outDP = new PrintStream(new FileOutputStream(outputDP));
        String dTrip = tsp.getDynamicTrip();
        outDP.println(dTrip);
        outDP.println("Time: " + (t3 - t2) + "ms");
    }

    /**
     * Checks if string is a number
     */
    public static boolean isNumber(String string) {
        try {
            Long.parseLong(string);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

}





