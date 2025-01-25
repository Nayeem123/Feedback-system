package feedback_system;

public class Test {
    public static void main(String[] args){
        System.out.println("Hi");

        // arrays
        int[] array = {1,2,3,4,5};
        for(int value: array)
            System.out.println(value);
        int min=array[0];
        for(int i = 0;i<array.length;i++){
            for(int j=0;j<array.length;j++)
            {
                if(array[j] < array[i])
                {
                    int temp = array[j];
                    array[j] = array[i];
                    array[i] = temp;
                }
            }
        }

        for(int value: array)
            System.out.print(value + " ");

        try{
            int res = 6/0;
        }
        catch (Exception exception)
        {
            System.out.println(exception);
        }
        System.out.println("after catch");
    }
}
