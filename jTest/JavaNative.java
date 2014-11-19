public class JavaNative
{
  public native int intMethod(int n);
  public native boolean booleanMethod(boolean bool);
  public native String stringMethod(String text);
  public native int intArrayMethod(int[] intArray);

  public static void main(String[] args){
    System.loadLibrary("Sample1");
    JavaNative sample = new JavaNative();
    int     square = sample.intMethod(5);
    boolean bool   = sample.booleanMethod(true);
    String  text   = sample.stringMethod("Java");
    int     sum    = sample.intArrayMethod(
                        new int[]{1,1,2,3,5,8,13} );

    System.out.println("intMethod: " + square);
    System.out.println("booleanMethod: " + bool);
    System.out.println("stringMethod: " + text);
    System.out.println("intArrayMethod: " + sum);
  }
}
