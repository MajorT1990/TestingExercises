import beeroclock.BeerOClockChecker;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

public class BeerOClockCheckerTest {
    @Test
    public void testIsBeerOClockLowerPartitionLowerBound() throws Exception {
        assertFalse(getBeerTimeCheckerMock(15,59,59).isBeerOClock());
    }

    @Test
    public void testIsBeerOClockLowerPartitionUpperBound() throws Exception {
        assertTrue(getBeerTimeCheckerMock(16,0,1).isBeerOClock());
    }

    @Test
    public void testIsBeerOClockUpperPartitionLowerBound() throws Exception {
        assertTrue(getBeerTimeCheckerMock(23,59,59).isBeerOClock());
    }

    @Test
    public void testIsBeerOClockUpperPartitionUpperBound() throws Exception {
        assertFalse(getBeerTimeCheckerMock(0,0,1).isBeerOClock());
    }


    private BeerOClockChecker getBeerTimeCheckerMock(int hour, int minutes, int seconds) throws Exception {
        BeerOClockChecker beerTimeChecker = PowerMockito.spy(new BeerOClockChecker());
        Calendar notBeerTime = Calendar.getInstance();
        notBeerTime.set(Calendar.HOUR_OF_DAY,hour);
        notBeerTime.set(Calendar.MINUTE,minutes);
        notBeerTime.set(Calendar.SECOND,seconds);
        notBeerTime.set(Calendar.MILLISECOND,0);

        when(beerTimeChecker, method(BeerOClockChecker.class, "now"))
                .withNoArguments()
                .thenReturn(notBeerTime);

        return beerTimeChecker;
    }
}