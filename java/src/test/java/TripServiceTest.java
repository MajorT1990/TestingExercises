import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import tripservice.*;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.SuppressCode.suppressConstructor;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserSession.class,TripDAO.class })
public class TripServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserSession userSessionStub;

    @Before
    public void init(){
        suppressConstructor(UserSession.class);
        mockStatic(UserSession.class);
        this.userSessionStub = mock(UserSession.class);
        when(UserSession.getInstance()).thenReturn(userSessionStub);

    }

    @Test(expected = UserNotLoggedInException.class)
    public void userShouldBeLoggedIn() throws UserNotLoggedInException {

        when(this.userSessionStub.getLoggedUser()).thenReturn(null);

        TripService tripService = new TripService();
        tripService.getTripsByUser(new User());
    }

    @Test
    public void userWithNoFriendsCanSeeNoTrips() throws UserNotLoggedInException {
        User loggedInUser = new User();
        User friendLessUser = new User();

        when(this.userSessionStub.getLoggedUser()).thenReturn(loggedInUser);

        TripService tripService = new TripService();
        assertEquals(0,tripService.getTripsByUser(friendLessUser).size());
    }


    @Test()
    public void youCanOnlySeeTripsOfAUserIfHeIsYourFriend() throws UserNotLoggedInException {
        User loggedInUser = new User();
        User userWithOneFriend = new User();

        userWithOneFriend.addFriend(loggedInUser);
        when(this.userSessionStub.getLoggedUser()).thenReturn(loggedInUser);

        mockStatic(TripDAO.class);
        when(TripDAO.findTripsByUser(userWithOneFriend)).thenReturn(Arrays.asList(new Trip(),new Trip()));


        TripService tripService = new TripService();
        assertEquals(2,tripService.getTripsByUser(userWithOneFriend).size());
    }
}
