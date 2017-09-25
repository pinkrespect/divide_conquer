import java.io.*;
import java.util.Scanner;

class Point {
    int x, y;
}
public class CP {
    static void insert(int array1[][], int array2[][], int first, int second){
        array1[0][first] = array2[0][second];
        array1[1][first] = array2[1][second];
    }
    static void merge(int array[][], int coordi, int start, int middle, int end){
        int sub1 = middle - start + 1;
        int sub2 = end - middle;
        int left[][] = new int[2][sub1];
        int right[][] = new int[2][sub2];

        for(int i = 0; i < sub1; ++i){
            insert(left, array, i, start+i);
        }

        for(int i = 0; i < sub2; ++i){
            insert(right, array, i, middle+1+i);
        }

        int i = 0, j = 0, k = start;

        while(i < sub1 && j < sub2) {
            if (left[coordi][i] <= right[coordi][j]) {
                insert(array, left, k, i);
                i++;
            } else {
                insert(array, right, k, j);
                j++;
            }
            k++;
        }

        while(i < sub1) {
            insert(array, left, k, i);
            i++;
            k++;
        }

        while(j < sub2) {
            insert(array, right, k, j);
            j++;
            k++;
        }
    }

    static void sort(int array[][], int coordi, int start, int end){
        if (start < end) {
            int middle = (start+end)/2;
            sort(array, coordi, start, middle);
            sort(array, coordi,middle+1, end);
            merge(array, coordi, start, middle, end);
        }
    }

    static int calc_distance(int[] pt1, int[] pt2){
        int distance, x_size, y_size;
        if (pt1[0] < pt2[0]) {
            x_size = pt2[0] - pt1[0];
        }else {
            x_size = pt1[0] - pt2[0];
        }

        if (pt1[1] < pt2[1]) {
            y_size = pt2[1] - pt1[1];
        } else {
            y_size = pt1[1] - pt2[1];
        }
        distance = (x_size * x_size) + (y_size * y_size);
        return distance;
    }

    static int[][] closestPair(int x_coordi[][], int y_coordi[][]){
        int min;
        if(x_coordi[0].length <= 3){
            min = Integer.MAX_VALUE;
            int [][] min_pt = new int [2][2];
            int distance;
            for(int i = 0; i < x_coordi[0].length; i++){
                for(int j = 0; j < x_coordi[0].length; j++){
                    if(j != i) {
                        distance = calc_distance(x_coordi[i], x_coordi[j]);
                        if (min >= distance) {
                            min = distance;
                            min_pt[0] = x_coordi[i];
                            min_pt[1] = x_coordi[j];
                        }
                    }
                }
            }
            return min_pt;
        }
        int length = x_coordi[0].length;
        int x_left[][] = new int[2][length/2];
        int y_left[][] = new int[2][length/2];
        int x_right[][] = new int[2][length - length/2];
        int y_right[][] = new int[2][length - length/2];

        for(int i = 0; i < x_left[0].length; i++) {
            insert(x_left, x_coordi, i, i);
        }
        for(int i = 0; i < x_right[0].length; i++) {
            insert(x_right, x_coordi, i, i+x_left.length-1);
        }

        for(int i = 0; i < x_left[0].length; i++) {
            insert(y_left, y_coordi, i, i);
        }
        for(int i = 0; i < y_right[0].length; i++) {
            insert(y_right, y_coordi, i, i + y_left.length - 1);
        }
        int center_pt[] = new int [2];
        center_pt[0] = x_left[0][0];
        center_pt[1] = x_left[1][0];

        int left_dist[][] = closestPair(x_left, y_left);
        int right_dist[][] = closestPair(x_right, y_right);

        int lr_min;
        if(calc_distance(left_dist[0], left_dist[1])>= calc_distance(right_dist[0], right_dist[1])) lr_min = calc_distance(right_dist[0], right_dist[1]);
        else lr_min = calc_distance(left_dist[0], left_dist[1]);

        int s_index[] = new int [length];
        int s_length = 0;
        for(int i = 0;i < length;i++){
            if(center_pt[0] - lr_min > x_coordi[0][i] || center_pt[0] + lr_min <= x_coordi[0][i]) {
                s_index[s_length] = i;
                i++;
            }
        }

        int s[][] = new int[2][s_length];
        for(int i = 0; i < s_length; i++) insert(s, x_coordi, i, s_index[i]);


        sort(s, 1, 0, s_length - 1);

        for(int i = 0; i < s_length; i++){

        }

        return null;

    }

    public static void main(String[] args) throws IOException{
        //read input from input.txt.
        Scanner scanner = new Scanner(new File("./input.txt"));

        int pts_number = scanner.nextInt();
        int count = 0;
        int[][] pts = new int[2][pts_number];
        for (int i = 0;scanner.hasNextInt() == true;i++) {
            pts[0][count] = scanner.nextInt();
            pts[1][count] = scanner.nextInt();
            count++;
        }
        // sort the given set of pts in the ascending order of x-coords.
        sort(pts,0, 0, pts_number-1);
        int[][] x_coordi = new int[2][pts_number];
        for(int i = 0; i < pts_number; i++){
            insert(x_coordi, pts, i, i);
        }
        sort(pts, 1,0, pts_number-1);
        int[][] y_coordi = new int[2][pts_number];
        for(int i = 0; i < pts_number; i++){
            insert(y_coordi, pts, i, i);
        }

        int[][] answer = closestPair(x_coordi, y_coordi);
        try {
            File file = new File("./output.txt") ;
            FileWriter writer = new FileWriter(file, true) ;
            writer.write(Integer.toString(calc_distance(answer[0], answer[0])));
            writer.flush();
            writer.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
