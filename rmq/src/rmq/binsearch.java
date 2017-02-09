package rmq;

import java.io.IOException;

public class binsearch {

    public static void main(String[] args) throws IOException {
        for(int len=0; len<50; len++){
            int[] arr = new int[len];
            for(int val=0; val<len; val++){
                arr[val]=2*val;
            }
            for(int chk=-1; chk<len*2; chk++){
                int idx = search(arr, chk);
                if(idx < 0 && Math.abs(chk)%2 == 0) throw new IOException("FAILED " + len + " " + chk + " " + idx);
                if(idx > 0 && Math.abs(chk)%2 == 1) throw new IOException("FAILED " + len + " " + chk + " " + idx);
                System.out.print(chk + "|" + idx + " ");
            }
            System.out.println("passed " + len);
        }
    }

    public static int search(int[] arr, int key){
        int low = 0;
        int high = arr.length-1;
        while(low <= high){
            int mid = (low+high)/2;
            if(arr[mid] < key){
                low = mid+1;
            } else if(arr[mid] > key){
                high = mid-1;
            } else {
                return mid;
            }
        }
        return -(low+1);
    }

}
