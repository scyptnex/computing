package mathy;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMisc {

    @Test
    public void testFibInitial(){
        assertTrue(Misc.fibo(0) == 1);
        assertTrue(Misc.fibo(1) == 1);
    }

    @Test
    public void testFibLarger(){
        assertTrue(Misc.fibo(2) == 2);
        assertTrue(Misc.fibo(3) == 3);
        assertTrue(Misc.fibo(4) == 5);
        assertTrue(Misc.fibo(5) == 8);
    }

    @Test
    public void testFibSum(){
        assertTrue(Misc.fibo(8) == Misc.fibo(7) + Misc.fibo(6));
    }
}
