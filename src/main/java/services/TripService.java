package services;

import models.trips.Trip;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TripService {
    List<Trip> getTripsMedallions(String pickupDate, List<String> medallions, boolean useCache) throws ExecutionException, InterruptedException;

    void clearCacheTrips();
}