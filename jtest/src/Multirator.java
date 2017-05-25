import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Multirator <E> implements Iterator<E> {

    private List<Iterator<E>> subLists;
    private Iterator<Iterator<E>> current;

    public Multirator(List<Iterable<E>> subs){
        subLists = subs.stream().map(Iterable::iterator).collect(Collectors.toList());
        restart();
    }

    @Override
    public boolean hasNext() {
        return subLists.stream().anyMatch(Iterator::hasNext);
    }

    @Override
    public E next() {
        Iterator<E> cur = null;
        while(cur == null || !cur.hasNext()){
            // infinite loop when the initial list is empty
            if(!current.hasNext()) restart();
            cur = current.next();
        }
        return cur.next();
    }

    private void restart(){
        current = subLists.iterator();
    }

    public static void main(String[] args){
        ArrayList<String> l1 = Stream.of("A", "B", "C").collect(Collectors.toCollection(ArrayList::new));
        LinkedList<String> l2 = Stream.of().map(Object::toString).collect(Collectors.toCollection(LinkedList::new));
        ArrayList<String> l3 = Stream.of("D", "E", "F", "G", "H", "I", "J", "K").collect(Collectors.toCollection(ArrayList::new));
        Multirator<String> mr = new Multirator<>(Arrays.asList(l1, l2, l3));
        while(mr.hasNext()){
            System.out.print(mr.next() + " ");
        }
        System.out.println();
        mr = new Multirator<>(Arrays.asList(l1, l1));
        while(mr.hasNext()){
            System.out.print(mr.next() + " ");
        }
        System.out.println();
        mr = new Multirator<>(Arrays.asList(l2, l2));
        while(mr.hasNext()){
            System.out.print(mr.next() + " ");
        }
        System.out.println();
        mr = new Multirator<>(new ArrayList<>());
        while(mr.hasNext()){
            System.out.print(mr.next() + " ");
        }
        System.out.println();
        mr = new Multirator<>(Arrays.asList(l3));
        while(mr.hasNext()){
            System.out.print(mr.next() + " ");
        }
        System.out.println();
    }
}
