/**
 * IDSA Short Project 10
 * Team members:
 * Adarsh Raghupati   axh190002
 * Keerti Keerti      kxk190012
 */
package axh190002;

import java.io.FileNotFoundException;

public class RMQ {
    int[] arr;  //stores input array
    int[] arr_blk_minima; //stores block level minimum elements
    int[][] rmqTable;    // RMQ data structure for block minima
    int no_blk_ele;      // number of blocks

    /**
     * Costructor to initialize input array and constructs a RMQ data structure for block minima
     * @param arr
     */
    public RMQ(int[] arr){
        this.arr = arr;
        preProcess();
    }

    /**
     * This function initializes and computes block level minima array, RMQ table respectively
     */
    public void preProcess(){
        int len_arr = arr.length;
        no_blk_ele = (int) Math.ceil(Math.log(len_arr) /
                Math.log(2));//(int)Math.ceil((double)len_arr/len_blk_minima);
        int len_blk_minima = (int) Math.ceil(len_arr / no_blk_ele);
        arr_blk_minima = new int[len_blk_minima];
        int min = Integer.MAX_VALUE;
        int l = -1;
        /*
         traverse the gvn array and find
         min in each block. Store minimum in arr_blk_minima
         */
        for (int k = 0, blk = 0; k < len_arr; k++, blk++) {
            if (blk == no_blk_ele) {
                arr_blk_minima[++l] = min;
                blk = 0;
                min = Integer.MAX_VALUE;
            }
            if (arr[k] < min)
                min = arr[k];
        }
        if (l != len_blk_minima - 1)
            arr_blk_minima[++l] = min;

        /*
          Constructed a RMQ Matrix -- rmq_bkl_minima
         */
         rmqTable = new
                int[len_blk_minima][getCol(arr_blk_minima.length)];

        for (int row = 0; row < arr_blk_minima.length; row++)
            rmqTable[row][0] = arr_blk_minima[row];

        getRMQMatrix(rmqTable);

    }

    /**
     * Returns the minimum value in the given index range (inclusive of i and j).
     * @param i
     * @param j
     * @return
     */
    public int getMinimum(int i,int j){
        if(i==j)
            return arr[i];
        if (i % no_blk_ele == 0) {
            if ((j + 1) % no_blk_ele == 0)
                return findmin(rmqTable, i / no_blk_ele, j / no_blk_ele);
            else {
                int rest = (j+1) % no_blk_ele;
                int localmin = findmin(rmqTable, i / no_blk_ele, (j / no_blk_ele) - 1);
                int start = Math.max(((j / no_blk_ele)*no_blk_ele),i);
                for (; start <= j; start++) //((j / no_blk_ele)*no_blk_ele) + rest
                    localmin = Math.min(localmin, arr[start]);
                return localmin;
            }
        } else {
            if ((j + 1) % no_blk_ele == 0) {
                int rest = no_blk_ele - (i % no_blk_ele);
                int localmin = findmin(rmqTable, (i + rest) /
                        no_blk_ele, j/no_blk_ele);
                for (int start = i; start < (i + rest) && start <=j; start++)
                    localmin = Math.min(localmin, arr[start]);
                return localmin;
            } else {
                int irest = no_blk_ele - (i % no_blk_ele);
                int jrest = (j+1) % no_blk_ele;
                int localmin = findmin(rmqTable, (i + irest) /
                        no_blk_ele, (j / no_blk_ele) - 1);
                for (int start = i; start < (i + irest) && start <= j; start++)
                    localmin = Math.min(localmin, arr[start]);
                int start = Math.max(((j / no_blk_ele)*no_blk_ele),i);
                for (; start <= j; start++)//((j / no_blk_ele)*no_blk_ele) + jrest
                    localmin = Math.min(localmin, arr[start]);
                return localmin;
            }
        }

    }

    /**
     * Returns the block level minimum element in the given index range
     * @param arr
     * @param i
     * @param j
     * @return
     */
    public static int findmin(int[][] arr, int i, int j) {
        int min = Integer.MAX_VALUE;
        if(j<i)
            return min;
        int diff = j - i + 1, k = 1, count = 0;
        while (k <= diff) {
            k = k << 1;
            count++;
        }
        count--;
        min = Math.min(arr[i][count], arr[j - (int) Math.pow(2, count)
                + 1][count]);
        return min;
    }

    /**
     * Constructs RMQ data structure for block level minima
     * @param arr
     */
    public static void getRMQMatrix(int[][] arr) {
        if (arr == null || arr.length == 0)
            return;

        for (int col = 1; col < arr[0].length; col++) {
            for (int row = 0; row < arr.length; row++) {
                if (row + (int) Math.pow(2, col) > arr.length) {
                    break;
                }
                int var1 = arr[row][col - 1], var2 = arr[row + (int)
                        Math.pow(2, col - 1)][col - 1];
                arr[row][col] = Math.min(var1, var2);
            }
        }
    }

    /**
     * Calculates the value k such that length of block level array >= 2^k
     * @param len_arr_blk_minima
     * @return
     */
    public static int getCol(int len_arr_blk_minima) {
        int k = 1, count = 0;
        while (len_arr_blk_minima >= k) {
            k = k << 1;
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] arr = new int[]{4, 5, 8, 7, 9, 4, 5, 8, 4, 8,7};

        RMQ rmq = new RMQ(arr);
        System.out.println("Output:");
        for(int i=0;i<arr.length;i++){
            for(int j=i;j<arr.length;j++){
                System.out.println( "("+i+","+j+")="+rmq.getMinimum(i,j));
            }
        }


    }
}

