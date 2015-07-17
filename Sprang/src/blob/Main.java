package blob;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        AuthorizedInfoStore ais = new SimpleAIS();
        if(args.length == 1){
            User u = new User(args[0]);
            if(ais.isAuthorized(u)){
                Scanner in = new Scanner(System.in);
                printHelp();
                while(in.hasNext()){
                    String com = in.nextLine();
                    String[] wrds = com.split(" ");
                    if(wrds[0].equals("help")){
                        printHelp();
                    } else if(wrds[0].equals("get") && wrds.length == 2){
                        try {
                            System.out.println(ais.getInfo(u, wrds[1]));
                        } catch (AuthorityException e) {
                            assert false;
                        }
                    } else if(wrds[0].equals("set") && wrds.length == 3){
                        try {
                            ais.addInfo(u, wrds[1], wrds[2]);
                        } catch (AuthorityException e) {
                            assert false;
                        }
                    } else {
                        System.err.println("Unrecognised command " + com);
                    }
                }
            } else {
                System.err.println("Unauthorised user " + u.getUname());
            }
        } else {
            System.err.println("Usage, " + Main.class.getSimpleName() + " <username>");
        }
    }

    public static void printHelp(){
        System.out.println("Commands:\n" +
                "    help:        print this message\n" +
                "    get <k>:     retrieve the value of key <k>\n" +
                "    set <k> <v>: add (or modify) the key <k> with value <v>");
    }

}
