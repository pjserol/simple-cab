package services.mockimpl;

import models.trips.Trip;
import services.TripService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripMockServiceImpl implements TripService {

    @Override
    public List<Trip> getTripsMedallions(String pickupDate, List<String> medallions, boolean useCache) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public void clearCacheTrips() {
    }
}
