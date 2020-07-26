import io.javalin.Javalin;
import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import services.impl.ServiceImpl;
import utils.Validation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Routes {
    private static ServiceImpl service;

    public void setRoutes(Javalin app, ServiceImpl s) {
        service = s;

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(e);
        });

        app.get("/trips/clear-cache/", Routes::clearCacheHandler);
        app.get("/trips/:pickupDate/", Routes::tripsHandler);
    }

    private static void tripsHandler(Context ctx) throws ExecutionException, InterruptedException {
        App.logger.info(String.format("Request %s: %s%s", ctx.method(), ctx.path(), ctx.queryString()));

        String pickupDate = ctx.pathParam("pickupDate", String.class).check(s -> Validation.verifyInputDate(s)).get();
        Validator<Boolean> cache = ctx.queryParam("cache", boolean.class);
        List<String> medallions = Arrays.asList(ctx.queryParam("medallions").split(",", -1));

        ctx.json(service.tripService.getTripsMedallions(pickupDate, medallions, cache.get()));
    }

    private static void clearCacheHandler(Context ctx) {
        service.tripService.clearCacheTrips();
    }
}
