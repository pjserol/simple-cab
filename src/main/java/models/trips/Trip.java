package models.trips;

import com.google.gson.annotations.SerializedName;

public class Trip {

    @SerializedName("medallionId")
    private String medallionId;

    @SerializedName("total")
    private Long nbTrips;

    public Trip(String medallionId, Long nbTrips) {
        this.medallionId = medallionId;
        this.nbTrips = nbTrips;
    }

    public String getMedallionId() {
        return medallionId;
    }

    public Long getNbTrips() {
        return nbTrips;
    }
}
