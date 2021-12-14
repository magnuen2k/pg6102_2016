# PG6100_2016

## Instructions

Run API's independently:

- All API's can be started independently from and IDE with `LocalApplicationRunner` which is located under the API's test folder.

Run the full application:

- Build the project with: 
       
     `mvn package -DskipTests` (Skipping tests)
     or
     `mvn clean verify`
     
- Run the application with docker compose:

    `docker-compose up` / `docker-compose up --build --force-recreate` (to force a build and recreation of containers)

- Application is then accessible at port `80` at your machine.

## Exam Topic

When interpreting the topic for this exam, I stumbled upon a few logical issues I can't understand. Therefore, I am discussing how I interpreted and made the application on each of the topic points.  

##### T1: Necessary but not sufficient requirements for E
- `• REST API to handle keeping track of planned trips: ports of origin and destination, boat and crew for each, and statuses for each trip.`

- I interpret this as making a new REST API for handling TRIPS. I also made entities in this API for BOATS and PORTS.

- `When the API starts, it must have some already existing data`

- Existing boats and ports are both added when starting the application.

- `All requirements in R1 must be satisfied`

- Everything is covered.


##### T2: Necessary but not sufficient requirements for C
- `REST API to handle the creation of new trips.`

- Handling creation of new trips can mean a lot of things. As specified in T3, this API is listening for when a trip is created (assumed to be the `trips` API from T1). Handling this can mean initializing data when this happens.

- `Each port has some kind of weather that changes at specific intervals (e.g. every few seconds for testing purposes).`

- `A logged-in user can create a new trip, between two existing ports, selecting the boat they will use, with a minimum and maximum number of passengers and crew depending on the boat.`

- As it does NOT specify which API can create a trip AND in T3 it is specified that each time a new trip is added, the API in T2 should listen and initialize data for booking, I create a trip in the `trips` API.

- `All requirements in R1 must be satisfied`


##### T3: Necessary but not sufficient requirements for B
- `Every time a new trip is added to the API, this relevant service should do a broadcast with AMQP. Then, the API in T2 should listen to such event, and initialize all the needed data for booking that trip.`

- This is not specific at all, but I read this as `trips` API is notifying T2 API when a new trip is added. Initilizing data for booking the trip will mean handling the space on the ports.

- `Every time the weather changes in a port, the relevant service sends messages to the service managing the trips. That service should listen for such events and update the statuses of relevant trips (e.g. issue warnings if one of the ports is experiencing a storm, of if a port no longer has free spots for additional boats).`

- A new service running and sending a message via AMQP when the weather changes on a port. `trips` API should listen for this and update the trips with the changed port.

##### T4: Necessary but not sufficient requirements for A
• Build frontend GUI
• Should be able to see trips associated with a port (either as origin or destination) or with a boat.
• Must be able to create/login/logout users
• A logged-in user should see a welcome message
• A logged-in user must be able to reserve a trip (i.e., setup a trip including a boat, between 2 ports), and see the trips they have reserved.


## Credits

- All copied code from class is credited in the source files.

## Final Note

In the exam text the specified zip name has the wrong class code.