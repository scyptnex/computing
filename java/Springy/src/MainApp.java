import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(Conf.class);

        // Let us raise a start event.
        context.start();

        HelloWorld obj = context.getBean(HelloWorld.class);

        obj.getMessage();

        // Let us raise a stop event.
        context.stop();
    }
}