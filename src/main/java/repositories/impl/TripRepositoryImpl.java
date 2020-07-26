package repositories.impl;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.RowData;
import models.trips.Trip;
import repositories.TripRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TripRepositoryImpl implements TripRepository {
    private Connection connection;

    public TripRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public HashMap<String, Trip> getTripsMedallions(String pickupDate, List<String> medallions) {
        if (medallions == null || medallions.size() == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < medallions.size(); i++) {
            builder.append("?,");
        }

        String stmt = "SELECT medallion, count(*) AS nb FROM cab_trip_data WHERE medallion IN ("
                + builder.deleteCharAt(builder.length() - 1).toString() + ") AND DATE(pickup_datetime)=? GROUP BY 1";

        List<Object> params = new ArrayList<Object>(medallions);
        params.add(pickupDate);

        QueryResult queryResult = connection.sendPreparedStatement(stmt, params, true).join();

        HashMap<String, Trip> trips = new HashMap<String, Trip>();
        for (Iterator<RowData> iter = queryResult.getRows().iterator(); iter.hasNext(); ) {
            RowData n = iter.next();

            Trip trip = new Trip(n.getString(0), n.getLong("nb"));
            trips.put(trip.getMedallionId(), trip);
        }

        return trips;
    }
}
