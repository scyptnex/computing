import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Conf {

    @Bean(destroyMethod = "byebye")
    //@Scope("prototype")
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

    @Bean
    public BeanPostProcessor beanPostProcessor(){
        return new PostProc();
    }

}
