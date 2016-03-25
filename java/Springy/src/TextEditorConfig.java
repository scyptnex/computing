import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

@Configuration
public class TextEditorConfig {

    @Bean
    public TextEditor textEditor(){
        return new TextEditor( spellChecker() );
    }

    @Bean(initMethod = "rofl", destroyMethod = "coptr")
    @Scope("prototype")
    public SpellChecker spellChecker(){
        return new SpellChecker( );
    }
}