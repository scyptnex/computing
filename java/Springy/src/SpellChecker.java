public class SpellChecker {

    private int checked = 0;

    public SpellChecker(){
        System.out.println("Inside SpellChecker constructor." );
    }

    public void checkSpelling(){
        System.out.println("Inside checkSpelling." );
        checked++;
    }

    public void rofl(){
        System.out.println("in " + checked);
    }

    public void coptr(){
        System.out.println("out " + checked);
    }

}