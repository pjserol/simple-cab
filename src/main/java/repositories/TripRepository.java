package repositories;

import models.trips.Trip;

import java.util.HashMap;
import java.util.List;

public interface TripRepository {
    HashMap<String, Trip> getTripsMedallions(String pickupDate, List<String> medallions);
}
