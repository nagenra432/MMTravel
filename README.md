# MMTravel
Calculate time duration between two Geo locations 

This API accepting source and destination geo co-ordinates from user and sending response Max and Min Travel time with all possible modes like driving,walking,bicycling and transit mode in bus,subway,train,tram 

Google distance matrix API calling to fetch the Results.

find below API inforamtion 

Request Info
------------

Method Type :- GET
URL         :- http://{host}:{port}/managedmethods/api/caltraveTime/{latitude1}:{longitude1}/{latitude2}:{longitude2}


Results :-
-------
i) Google Status = OK

Response :-

{
  "minTime": {
    "driving": "7 hous 29 mins "
  },
  "maxTime": {
    "walking": "6 days 6 hous "
  },
  "statusCode": "OK"
}

ii)
Google Status = NOT_FOUND|ZERO_RESULTS|MAX_ROUTE_LENGTH_EXCEEDED
{
  "minTime": null,
  "maxTime": null,
  "statusCode": "ZERO_RESULTS"|"NOT_FOUND"|"MAX_ROUTE_LENGTH_EXCEEDED"
}
