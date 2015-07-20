import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Flomp {

    private HelloWorld helloWorld;

    @Autowired
    public Flomp(HelloWorld helloWorld){
        this.helloWorld = helloWorld;
    }

    public HelloWorld getHelloWorld() {
        return helloWorld;
    }

}
