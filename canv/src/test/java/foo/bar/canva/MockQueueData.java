package foo.bar.canva;

public class MockQueueData extends QueueData{

    public MockQueueData(long id, String name, int len) {
        super(id, name, randomData(len));
    }

    private static byte[] randomData(int len){
        byte[] ret = new byte[len];
        for(int i=0; i<len; i++){
            ret[i] = (byte)(Math.floor(Math.random()*256)-128);//signed
        }
        return ret;
    }
}
