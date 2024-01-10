import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        int[] arr = new int[n];

        for(int i=0;i<n;i++){
            arr[i] = Integer.parseInt(st.nextToken());
        }

    
        for(int i=0;i<n;i++){
            boolean flag = false;
            for(int j=0;j<n - 1 - i;j++){
                if(arr[j] > arr[j+1]){
                    flag = true;
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
            if(!flag)break;
        }

        for(int i=0;i<n;i++) System.out.print(arr[i] + " ");


    }
}