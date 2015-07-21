import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype")
public class Flomp {

    private static long cur = 0;
    private long l;
    private HelloWorld helloWorld;

    @Autowired
    public Flomp(HelloWorld helloWorld){
        this.helloWorld = helloWorld;
        this.l = cur++;
    }

    public HelloWorld getHelloWorld() {
        System.out.print(l + ": ");
        return helloWorld;
    }

}
