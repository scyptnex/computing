import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Conf {

    @Bean(destroyMethod = "byebye")
    public HelloWorld hw(){
        HelloWorld ret = new HelloWorld();
        ret.setMessage("hi");
        return ret;
    }

    @Bean
    public CStartEventHandler cStartEventHandler(){
        return new CStartEventHandler();
    }

    @Bean
    public CEventHandler cStopEventHandler(){
        return new CEventHandler();
    }

}
