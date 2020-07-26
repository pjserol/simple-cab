# Simple Cab

Cab Data Researcher is a company that provides insights on the open data about NY cab
trips. 

The medallion is cab identification.  

The API provide a way to query how many trips one or more cab (medallion) have been
made given a particular pickup date. 

Considering that the query creates a heavy load on the database, the results are cache in Redis. 

## Requirements
Build & Run 
- OpenJDK 11+
- Gradle
- Docker

Run MySQL, the import SQL scripts & Redis  
`chmod +x scripts.sh`  
`./scripts.sh`

Check Redis cache  
`docker exec -it redis-cab redis-cli`

## Environment Variables
- CAB_LISTEN_PORT (default 7000)
- CAB_HOST_DB (default localhost)
- CAB_LISTEN_PORT_DB (default 3306)
- CAB_DATABASE_DB (default ny_cab_data)
- CAB_USERNAME_DB (default root)
- CAB_PASSWORD_DB (default password)
- CAB_HOST_REDIS (default localhost)
- CAB_LISTEN_PORT_REDIS (default 6379)

```
export env CAB_LISTEN_PORT=7000  
export env CAB_HOST_DB=localhost  
export env CAB_LISTEN_PORT_DB=3306  
export env CAB_DATABASE_DB=ny_cab_data  
export env CAB_USERNAME_DB=root  
export env CAB_PASSWORD_DB=password  
export env CAB_HOST_REDIS=localhost  
export env CAB_LISTEN_PORT_REDIS=6379  
```

## Run the app

`./gradlew tasks`  
`./gradlew run`

## Test locally

`./gradlew test`

## Endpoints (with example)

- GET /trips/clear-cache/  

`http://localhost:7000/trips/clear-cache/` 

- GET /trips/:pickupDate/?medallions=med1,med2,med3&cache=true  

`http://localhost:7000/trips/2013-12-01/?medallions=2B1A06E9228B7278227621EF1B879A1D,1B0575AC55B9F5FBD158A360A1808D81&cache=true`

```
[
  {
    "medallionId": "2B1A06E9228B7278227621EF1B879A1D",
    "total": 4
  },
  {
    "medallionId": "1B0575AC55B9F5FBD158A360A1808D81",
    "total": 3
  }
]
```

## Improvements
- Handle exceptions (query param of medallion when empty)
- Add elements in the cache on a thread (improve performance)
- Add error response format when something wrong
- Add integration test
