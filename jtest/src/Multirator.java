import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Multirator <E> implements Iterator<E> {

    private List<Iterator<E>> subLists;
    private Iterator<Iterator<E>> currentIterator;
    private Iterator<E> currentItem;

    public Multirator(){
        this(Collections.emptyList());
    }

    public Multirator(List<Iterable<E>> subs){
        subLists = subs.stream().map(Iterable::iterator).collect(Collectors.toList());
        restart();
    }

    @Override
    public boolean hasNext() {
        if (currentItem == null) return false;
        if (currentItem.hasNext()) return true;
        while(currentIterator.hasNext()){
            currentItem = currentIterator.next();
            if (currentItem.hasNext()) return true;
        }
        restart();
        return currentItem != null;
    }

    @Override
    public E next() {
        E ret = currentItem.next();
        if(currentIterator.hasNext()){
            currentItem = currentIterator.next();
            hasNext();
        } else {
            restart();
        }
        return ret;
    }

    private void restart(){
        subLists = subLists.stream().filter(Iterator::hasNext).collect(Collectors.toList());
        currentIterator = subLists.iterator();
        currentItem = currentIterator.hasNext() ? currentIterator.next() : null;
    }

    public static class Tests {
        @SafeVarargs
        private static <L> List<L> l(L ... ls){
            return Arrays.asList(ls);
        }
        private static <T> Iterable<T> i(Iterator<T> it){
            // hack on the grouds that an iterable is a functional interface
            // dont expect this to work if you ask for the iterator more than once :P
            Iterable<T> itr = () -> it;
            return StreamSupport.stream(itr.spliterator(), false).collect(Collectors.toList());
        }
        @Test
        public void testDefaultMultirator(){
            Multirator<String> m = new Multirator<>();
            assertThat(i(m), is(equalTo(l())));
        }
        @Test
        public void testEmptyMultirator(){
            Multirator<String> m = new Multirator<>(l());
            assertThat(i(m), is(equalTo(l())));
        }
        @Test
        public void testMultiratorOfEmpty(){
            Multirator<String> m = new Multirator<>(l(l()));
            assertThat(i(m), is(equalTo(l())));
        }
        @Test
        public void testMultiratorOfEmpties(){
            Multirator<String> m = new Multirator<>(l(l(), l()));
            assertThat(i(m), is(equalTo(l())));
        }
        @Test
        public void testMultiratorSquish(){
            Multirator<String> m = new Multirator<>(l(l(), l("a"), l()));
            assertThat(i(m), is(equalTo(l("a"))));
        }
        @Test
        public void testMultiratorOrder(){
            Multirator<String> m = new Multirator<>(l(l("a", "d"), l("b"), l("c", "e", "f"), l()));
            assertThat(i(m), is(equalTo(l("a", "b", "c", "d", "e", "f"))));
        }
    }
}
