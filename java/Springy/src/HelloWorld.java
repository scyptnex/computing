import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.atomic.AtomicLong;

public class HelloWorld {

    private static AtomicLong al = new AtomicLong(0);
    private long l = al.getAndIncrement();
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void getMessage() {
        System.out.println("Your Message(" + l + ") : " + message);
    }

    public void byebye(){
        System.out.println("byeeeee " + l);
    }
}