package p041t080;

import util.Read;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class Euler054PokerHands {


    public static void main(String[] args){
        System.out.println(handPair(new CardHand("JS 4C 3H 3S QC")));
        System.out.println(handPair(new CardHand("JS 4C 7H 3S QC")));
        System.out.println(handTwoPair(new CardHand("4S 4C 3H 3S QC")));
        System.out.println(handTwoPair(new CardHand("JS 4C 3H 3S QC")));
        System.out.println(handTriple(new CardHand("4S 4C JH JS JC")));
        System.out.println(handTriple(new CardHand("4S 4C 3H 3S QC")));
        System.out.println(handStraight(new CardHand("4S 5C 3H 7S 6C")));
        System.out.println(handStraight(new CardHand("4S 5C 3H 6S 6C")));
        System.out.println(handFlush(new CardHand("4C 5C 3C 9C 6C")));
        System.out.println(handFlush(new CardHand("4C 5C 3C 6S 6C")));
        System.out.println(handFullHouse(new CardHand("4C 4C 3C 3C 3C")));
        System.out.println(handFullHouse(new CardHand("4C 3C 4C 3S 4C")));
        System.out.println(handFullHouse(new CardHand("4C 4C 3C 3S 2C")));
        System.out.println(handQuad(new CardHand("TS TC TH TD JC")));
        System.out.println(handQuad(new CardHand("4C 4C 4C 3S 3C")));
        System.out.println(handStraightFlush(new CardHand("KC JC QC AC TC")));
        System.out.println(handStraightFlush(new CardHand("KC JC QS AC TC")));
        System.out.println(handRoyalFlush(new CardHand("KC JC QC AC TC")));
        System.out.println(handRoyalFlush(new CardHand("KC JC QC 9C TC")));

        WinnerEvaluator we = new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handTwoPair(h);
            }
        };
        System.out.println();
        System.out.println(we.getWinner(new CardHand("3C 2H 3H 2C 2S"), new CardHand("4C KS 4H 3D 3S")));
        System.out.println(we.getWinner(new CardHand("4C KS 4H 4D 4S"), new CardHand("TH KH JH QH AH")));
        System.out.println(we.getWinner(new CardHand("4C KS 3H AD 7S"), new CardHand("TH KH JH QH AH")));
        System.out.println(we.getWinner(new CardHand("3C 2H 3H 2C AS"), new CardHand("2C KS 2H 3D 3S")));
        System.out.println();
        System.out.println(simplePokerWinner(new CardHand("3C 2H 4H 7C 9S"), new CardHand("2C 3S 4H 9D 7S")));
        System.out.println(simplePokerWinner(new CardHand("3C 2H 4H 7C 9S"), new CardHand("3C 2H 4H 7C 9S")));

        int count = 0;
        for(String ph : Read.fRead("/home/nic/computing/Euler/54.in")){
            CardHand a = new CardHand(ph.substring(0, 14));
            CardHand b = new CardHand(ph.substring(15));
            int winner = simplePokerWinner(a, b);
            if(winner == 1){
                System.out.println(a + "   -   " + b);
                count++;
            } else if (winner == -1){
                System.out.println(b + "   -   " + a);
            } else {
                throw new RuntimeException("No winner " + a + ", " + b);
            }
        }
        System.out.println(count);
    }

    public static int simplePokerWinner(CardHand a, CardHand b){
        assert a.suits.length == 5 && b.suits.length == 5;
        ArrayList<WinnerEvaluator> wes = new ArrayList<>();
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handRoyalFlush(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handStraightFlush(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handQuad(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handFullHouse(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handFlush(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handStraight(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handTriple(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handTwoPair(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return handPair(h);
            }
        });
        wes.add(new WinnerEvaluator() {
            @Override
            public int handValue(CardHand h) {
                return 1;//both hands have equal nonnegative value, force the high-card winner
            }
        });
        for(WinnerEvaluator we : wes){
            int winr = we.getWinner(a, b);
            if (winr != 0) return winr;
        }
        return 0;
    }

    public static abstract class WinnerEvaluator{
        public abstract int handValue(CardHand h);
        public int getWinner(CardHand a, CardHand b){
            int av = handValue(a);
            int bv = handValue(b);
            if(av > bv) return 1;
            else if (bv > av) return -1;
            else if (av == -1 && bv == -1) return 0;
            else return highCardVictory(a, b);
        }
    }

    public static int highCardVictory(CardHand a, CardHand b){
        for(int i=a.suits.length-1; i>=0; i--){
            if(a.vals[i] > b.vals[i]){
                return 1;
            } else if(a.vals[i] < b.vals[i]){
                return -1;
            }
        }
        return 0;
    }

    public static int handRoyalFlush(CardHand h){
        //14 -> Ace high straight flush
        return handStraightFlush(h) == 14 ? 1 : -1;
    }

    public static int handStraightFlush(CardHand h){
        if(handFlush(h) != -1) return handStraight(h);
        return -1;
    }

    public static int handQuad(CardHand h){
        for(int i=h.count-1; i>2; i--){
            if(h.vals[i] == h.vals[i-1] && h.vals[i] == h.vals[i-2] && h.vals[i] == h.vals[i-3]) return h.vals[i];
        }
        return -1;
    }

    //not immune to hand-size
    public static int handFullHouse(CardHand h){
        if(h.vals[0] == h.vals[1] && h.vals[3] == h.vals[4]){
            if(h.vals[0] == h.vals[2]){
                return h.vals[0]*100 + h.vals[4];
            } else if (h.vals[4] == h.vals[2]){
                return h.vals[0] + h.vals[4]*100;
            }
        }
        return -1;
    }

    public static int handFlush(CardHand h){
        for(int i=1; i<h.count; i++){
            if(h.suits[0] != h.suits[i]) return -1;
        }
        return h.vals[h.count-1];
    }

    public static int handStraight(CardHand h){
        for(int i=0; i<h.count-1; i++){
            if(h.vals[i] != h.vals[i+1]-1) return -1;
        }
        return h.vals[h.count-1];
    }

    public static int handTriple(CardHand h){
        for(int i=h.count-1; i>1; i--){
            if(h.vals[i] == h.vals[i-1] && h.vals[i] == h.vals[i-2]) return h.vals[i];
        }
        return -1;
    }

    public static int handTwoPair(CardHand h){
        int ret = 0;
        int found = 0;
        for(int i=h.count-1; i>0; i--){
            if(h.vals[i] == h.vals[i-1]){
                ret *= 100;
                ret += h.vals[i];
                found++;
                i--;
            }
        }
        return found == 2 ? ret : -1;
    }

    public static int handPair(CardHand h){
        for(int i=h.count-1; i>0; i--){
            if(h.vals[i] == h.vals[i-1]) return h.vals[i];
        }
        return -1;
    }

    public static class CardHand{

        public final int count;
        public final int[] vals, suits;

        public CardHand(String h){
            String[] cards = h.split(" ");
            count = cards.length;
            vals = new int[count];
            suits = new int[count];
            int i = 0;
            for(String s : cards){
                if(s.charAt(0) <= '9'){
                    vals[i] = s.charAt(0) - '0';
                } else {
                    switch(s.charAt(0)){
                        case 'T' :{
                            vals[i] = 10;
                            break;
                        }
                        case 'J' :{
                            vals[i] = 11;
                            break;
                        }
                        case 'Q' :{
                            vals[i] = 12;
                            break;
                        }
                        case 'K' :{
                            vals[i] = 13;
                            break;
                        }
                        case 'A' :{
                            vals[i] = 14;
                            break;
                        }
                        default :{
                            throw new RuntimeException("Invalid value " + s.charAt(0) + " : " + h);
                        }
                    }
                }
                switch(s.charAt(1)){
                    case 'C' :{
                        suits[i] = 0;
                        break;
                    }
                    case 'D' :{
                        suits[i] = 1;
                        break;
                    }
                    case 'H' :{
                        suits[i] = 2;
                        break;
                    }
                    case 'S' :{
                        suits[i] = 3;
                        break;
                    }
                    default :{
                        throw new RuntimeException("Invalid suit " + s.charAt(1) + " : " + h);
                    }
                }
                i++;
            }
            orderHand();
        }

        //bubble sort because i'm lazy
        private void orderHand(){
            for(int end=count; end >= 1; end--){
                for(int check = 0; check < end-1; check++){
                    if(vals[check] > vals[check+1]){
                        int vt = vals[check];
                        int st = suits[check];
                        vals[check] = vals[check+1];
                        suits[check] = suits[check+1];
                        vals[check+1] = vt;
                        suits[check+1] = st;
                    }
                }
            }
        }

        @Override
        public String toString(){
            StringBuffer ret = new StringBuffer();
            for(int i=0; i<suits.length; i++){
                ret.append(" " + vals[i] + "-" + suits[i]);
            }
            return ret.substring(1);
        }
    }

}
