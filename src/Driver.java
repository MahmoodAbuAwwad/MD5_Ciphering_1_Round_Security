import java.util.Random;
/*
*
*
*
@author Mahmoud Hussain 1160778	 
*
*
*/
public class Driver {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //M(t) for the First Round, Chosen Data from Example in Slides
        int X[] = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int A[] = new int[32];
        int B[] = new int[32];
        int C[] = new int[32];
        int D[] = new int[32];
        String T[] = new String[32]; // from Slides Example
        initialize_T_Values(T); //fill the T array with Values;

        int IV[] = new int[128]; //initial vector 128bits .
        for (int i = 0; i < IV.length; i++)
            IV[i] = new Random().nextInt(2); //Initial Vector Randomly Chosen Here

        //Divide Initial Vector to A,B,C,D 32bits each
        for (int i = 0; i < A.length; i++)
            A[i] = IV[i];//fill first 32 bits in A

        for (int i = 0; i < B.length; i++)
            B[i] = IV[i + 32];//fill Second 32 bits in B

        for (int i = 0; i < C.length; i++)
            C[i] = IV[i + 64];//fill Third 32 bits in C

        for (int i = 0; i < D.length; i++)
            D[i] = IV[i + 96];//fill Fourth 32 bits in D


        int[] temp = new int[32]; //temp array just to hold data
        int[] temp1 = new int[32]; //For Changing Between A,B,C,D in Final Step
        for (int i = 0; i < 16; i++) {
            //main loop for the 16 Steps
            temp = Calculate_FunctionG(B, C, D); //first

            //second--Add with  output G(B,C,D)
            A = PerformBinaryAdding(A, temp);


            //third--add with M(k)
            A = PerformBinaryAdding(A, Convert_X_32bitBinaryArray(X[i]));

            temp = Convert_X_32bitBinaryArray(hex_to_decimal(T[i]));
            //Fourth--add with T(i)
            A = PerformBinaryAdding(A, temp);


            A = CircularLeftShift(A);   //Fifth Circular LeftShift of A

            //Sixth-add A with B
            A = PerformBinaryAdding(A, B);

            /************* Changing D->A->B->C******************/

            for (int j = 0; j < 32; j++) {    //D->A
                temp[j] = A[j];
                A[j] = D[j];
            }
            for (int j = 0; j < 32; j++) {     //A->B
                temp1[j] = B[j];
                B[j] = temp[j];
            }

            for (int j = 0; j < 32; j++) {     //B->C
                temp[j] = C[j];
                C[j] = temp1[j];
            }
            for (int j = 0; j < 32; j++) {     //C->D
                D[j] = temp[j];
            }
        }

        for (int j = 0; j < 32; j++) {    //Print the next Round IV
            System.out.print(A[j]);
            System.out.print(B[j]);
            System.out.print(C[j]);
            System.out.print(D[j]);
        }


    }

    public static int[] Calculate_FunctionG(int B[], int C[], int D[]) {
        //function Calculate the function G of the First Round
        //G(A,B,C) = (B and C) or (not B and D)
        int[] temp1 = new int[32];//temp1 array holding the Output
        int[] temp2 = new int[32];//temp2 array holding the Output

        for (int i = 0; i < 32; i++)
            temp1[i] = B[i] & C[i]; //calculate (B and C)
        for (int i = 0; i < 32; i++)
            temp2[i] = ~B[i];//calculate not B
        for (int i = 0; i < 32; i++)
            temp2[i] = temp2[i] & D[i];//calculate (not B and D)
        for (int i = 0; i < 32; i++)
            temp1[i] = temp1[i] | temp2[i];//Calculate G(B,C,D)

        return temp1;

    }

    public static int[] Convert_X_32bitBinaryArray(int Decimal) {
        //Function Convert X(k) of this Step to 32 Bit Binary Array

        String bin = Integer.toBinaryString(Decimal);//convert in Binary String
        char[] ch = bin.toCharArray();//convert to Array of Charchter
        //System.out.println(ch.length);
        int[] temp = new int[32];  //define array to Carry the number Converted
        for (int i = 0; i < 32 - ch.length; i++) {
            //Expand the array to 32 bits
            temp[i] = 0;
        }
        int j = 0;
        for (int i = 32 - ch.length; i < 32; i++) {
            //fill the number to the array
            temp[i] = Integer.parseInt(String.valueOf(ch[j])); //convert the array to Integer of Zeros and Ones
            j++;
        }
        return temp;
    }

    public static int hex_to_decimal(String hex) {
        //Convert T(i) in order to Convert it to Binary to Add it to A 32bits .. before CLS  -- Copied from internet
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    public static String[] initialize_T_Values(String[] arr) {
        arr[0] = "D76AA478";
        arr[1] = "E8C7B756";
        arr[2] = "242070DB";
        arr[3] = "C1BDCEEE";
        arr[4] = "F57C0FAF";
        arr[5] = "4787C62A";
        arr[6] = "A8304613";
        arr[7] = "FD469501";
        arr[8] = "698098D8";
        arr[9] = "0B44F7AF";
        arr[10] = "FFFF5BB1";
        arr[11] = "895CD7BE";
        arr[12] = "6B901122";
        arr[13] = "FD987193";
        arr[14] = "A679438E";
        arr[15] = "49B40821";

        return arr;
    }

    public static int[] CircularLeftShift(int arr[]) {
        int temp[] = new int[32];
        temp = arr;
        int x = temp[0];
        for (int i = 0; i < arr.length - 1; i++) {
            temp[i] = temp[i + 1];
        }
        temp[arr.length - 1] = x;
        return temp;
    }

    public static int[] PerformBinaryAdding(int[] first, int[] second) {

        int[] tempt = new int[32];
        char[] temp = new char[first.length];
        char[] temp1 = new char[second.length];
        for (int i = 0; i < first.length; i++) {
            temp[i] = (char) (first[i] + '0');
        }                       //converting int array to char array
        for (int i = 0; i < first.length; i++) {
            temp1[i] = (char) (second[i] + '0');
        }
        String BinaryFirst = new String(temp);        //convert char array to String
        String BinarySecond = new String(temp1);


        int sum = Integer.parseInt(BinaryFirst, 2) + Integer.parseInt(BinarySecond, 2); //adding two numbers
        System.out.print(sum);

        tempt = Convert_X_32bitBinaryArray(sum); //Convert Sum to Binary Array again


        return tempt;
    }

}

