package services.impl;

import com.github.fppt.jedismock.RedisServer;
import models.trips.Trip;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import redis.clients.jedis.Jedis;
import repositories.TripRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TripServiceImplTest {

    TripServiceImpl tripService;
    private TripRepository tripRepositoryMock = mock(TripRepository.class);
    private RedisServer server;
    private  Jedis jedis;

    @Before
    public void before() throws IOException {
        server = RedisServer.newRedisServer();
        server.start();
        jedis = new Jedis(server.getHost(), server.getBindPort());
    }

    @Test
    public void getTripsWithoutCache() throws ExecutionException, InterruptedException {
        String medallion1 = "med1";
        String medallion2 = "med2";

        List<String> medallionsRepo = Arrays.asList(medallion1, medallion2);
        HashMap<String, Trip> trips = new HashMap<>();
        Trip trip1 = new Trip(medallion1, (long) 3);
        Trip trip2 = new Trip(medallion2, (long) 2);

        trips.put(medallion1, trip1);
        trips.put(medallion2, trip2);
        tripService = new TripServiceImpl(tripRepositoryMock, jedis);

        when(tripRepositoryMock.getTripsMedallions("date1", medallionsRepo)).thenReturn(trips);


        List<String> medallions = Arrays.asList(medallion1, medallion2);
        List<Trip> actual = tripService.getTripsMedallions("date1", medallions, false);

        List<Trip> expected = Arrays.asList(trip1, trip2);

        Assert.assertTrue(new ReflectionEquals(expected.get(0)).matches(actual.get(0)));
        Assert.assertTrue(new ReflectionEquals(expected.get(1)).matches(actual.get(1)));
    }


    @Test
    public void getTripsWithCache() throws ExecutionException, InterruptedException {
        String medallion1 = "med1";
        String medallion2 = "med2";

        List<String> medallionsRepo = Arrays.asList(medallion2);
        HashMap<String, Trip> trips = new HashMap<>();

        Trip trip2 = new Trip(medallion2, (long) 2);
        trips.put(medallion2, trip2);

        tripService = new TripServiceImpl(tripRepositoryMock, jedis);
        jedis.set(medallion1 +"_"+ "date1", "5");

        when(tripRepositoryMock.getTripsMedallions("date1", medallionsRepo)).thenReturn(trips);


        List<String> medallions = Arrays.asList(medallion1, medallion2);
        List<Trip> actual = tripService.getTripsMedallions("date1", medallions, true);

        Trip trip1 = new Trip(medallion1, (long) 5);
        trips.put(medallion1, trip1);
        List<Trip> expected = Arrays.asList(trip1, trip2);

        Assert.assertTrue(new ReflectionEquals(expected.get(0)).matches(actual.get(0)));
        Assert.assertTrue(new ReflectionEquals(expected.get(1)).matches(actual.get(1)));
    }
}