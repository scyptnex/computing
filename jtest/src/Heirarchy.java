public class Heirarchy {

    interface Shape{
        public void foo();
    }

    static class Tri implements Shape {
        int x = 2;
        @Override
        public void foo() {
            System.out.println("Tri " + x);
        }
    }

    static class Isoc extends Tri{
        public Isoc(){
            x = 3;
        }
    }

    static class RIsoc extends Isoc {
        @Override
        public void foo() {
            System.out.println("RIsoc " + x);
        }
    }

    public static void main(String[] args){
        Isoc s = new Isoc();
        if(args.length > 0){
            s = new RIsoc();
        }
        s.foo();
    }

}
