/**
 * IDSA Short Project 10
 * Team members:
 * Adarsh Raghupati   axh190002
 * Keerti Keerti      kxk190012
 */

package axh190002;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class SP10Driver {
    /**
     * Generates a text file with random numbers
     * @param size
     * @param fileName
     * @throws IOException
     */
    public static void generateArray(int size,String fileName) throws IOException {
        Random rd = new Random();
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter fw = new BufferedWriter(fileWriter);
        for (int i=0;i<size;i++){
            fw.write(String.valueOf(rd.nextInt(size)));
            fw.newLine();
        }
        fw.close();
        fileWriter.close();
    }

    /**
     * Reads the input file into a integer array
     * @param path
     * @param size
     * @return
     * @throws FileNotFoundException
     */
    public static int[] readArr(String path,int size) throws FileNotFoundException {
        int[] arr = new int[size];
        Scanner scanner = new Scanner(new File(path));
        for (int i=0;i<size;i++){
            arr[i] = scanner.nextInt();
        }
        return arr;
    }

    /**
     * Generates a output for each i,j combination and writes it to given file
     * @param filePath
     * @param arr
     * @throws IOException
     */
    public static void generateOutput(String filePath,int[] arr) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        BufferedWriter fw = new BufferedWriter(fileWriter);
        for (int col=0;col<arr.length;col++){
            for(int row=0;row<=col;row++){
                int i=row;
                int j=col;
                int min=arr[i];
                i++;
                for(;i<=j;i++){
                    min=Math.min(min,arr[i]);
                }
                fw.write(String.valueOf(min));
                fw.newLine();
            }
        }
        fw.close();
        fileWriter.close();
    }

    /**
     * Reads the output file into a matrix. This function is helpful in evaluating the result of RMQ getMinimum(i,j) function
     * @param file
     * @param size
     * @return
     * @throws FileNotFoundException
     */
    public static int[][] readTable(String file,int size) throws FileNotFoundException {
        int[][] table = new int[size][size];
        Scanner scanner = new Scanner(new File(file));
        for (int col=0;col<size;col++){
            for(int row=0;row<=col;row++){
                table[row][col] = scanner.nextInt();
            }
        }
        return table;
    }


    public static void main(String[] args) throws IOException {
        int size = 256000000;
        generateArray(size,"main-test-256M.txt");
        int[] arr = readArr("main-test-256M.txt",size);
        Timer timer = new Timer();
        timer.start();
        RMQ rmq = new RMQ(arr);
        System.out.println("Input size:"+(size/1000000)+"M");
        System.out.println("Preprocess time "+timer.end());
        Random random = new Random();
        timer = new Timer();
        timer.start();
        for(int i=0;i<1000000;i++){
           int start = random.nextInt(size);
           int end = random.nextInt(size);
           if(start>end)
               rmq.getMinimum(end,start);
           else
               rmq.getMinimum(start,end);
        }
        timer.end();
        System.out.println("Average query time "+ (timer.endTime-timer.startTime)/1000000f);




        //generateOutput("main-test-128M-out.txt",arr);
        /*int[][] out = readTable("test4-out.txt",size);
        RMQ rmq = new RMQ(arr);
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr.length;j++){
                int val;
                if(i>j){
                    val = rmq.getMinimum(j,i);
                } else{
                    val = rmq.getMinimum(i,j);
                }


               int actual;
               if(i>j){
                   actual = out[j][i];
               }else{
                   actual = out[i][j];
               }
               if(val!=actual){
                   System.out.println("Wrong ans for i="+i+" j="+j);
                   System.out.println("Expected:"+actual);
                   System.out.println("Observed:"+val);
                   System.exit(-1);
               }
            }
        }*/
    }
}
