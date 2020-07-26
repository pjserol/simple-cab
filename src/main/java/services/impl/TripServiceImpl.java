package services.impl;

import models.trips.Trip;
import redis.clients.jedis.Jedis;
import repositories.TripRepository;
import services.TripService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripServiceImpl  implements TripService {
    private Jedis jedis;
    private TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository, Jedis jedis) {
    this.tripRepository = tripRepository;
    this.jedis = jedis;
    }


    @Override
    public List<Trip> getTripsMedallions(String pickupDate, List<String> medallions, boolean useCache) throws ExecutionException, InterruptedException {
        List<Trip> trips=new ArrayList<Trip>();
        List<String> medallionsToQuery = new ArrayList<String>();

        if (useCache) {
            for (String id : medallions) {
                String res = jedis.get(id + "_" + pickupDate);
                if (res !=null){
                    Trip trip = new Trip(id, Long.parseLong(res));
                    trips.add(trip);
                } else {
                    medallionsToQuery.add(id);
                }
            }
        } else {
            medallionsToQuery = medallions;
        }

        HashMap<String, Trip> tripsHash = this.tripRepository.getTripsMedallions(pickupDate, medallionsToQuery);
        for (String id : medallionsToQuery) {
            Trip t = tripsHash.get(id);
            Long nbTrips =(long) 0 ;
            if (t != null){
                nbTrips = t.getNbTrips();
            }

            trips.add(new Trip(id, nbTrips));
            jedis.set(id+"_"+pickupDate,nbTrips.toString());
        }
        
        return trips;
    }

    @Override
    public void clearCacheTrips() {
        jedis.flushAll();
    }
}
