import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Conf.class);
        context.register(Flomp.class);
        //context.scan("");

        // Let us raise a start event.
        context.start();

        HelloWorld obj = context.getBean(HelloWorld.class);

        Flomp fl = context.getBean(Flomp.class);

        obj.getMessage();
        fl.getHelloWorld().getMessage();

        Flomp fl2 = context.getBean(Flomp.class);
        fl2.getHelloWorld().getMessage();

        // Let us raise a stop event.
        context.stop();
        context.registerShutdownHook();
    }
}