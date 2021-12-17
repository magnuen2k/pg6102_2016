# PG6100_2016

## Instructions

Run API's independently:

- All API's can be started independently from and IDE with `LocalApplicationRunner` which is located under the API's test folder. However, since both `trips` api and `booking` api depends on rabbitmq, they will not be able to run without disabling the rabbitmq functionality. Therefore, another way to see Swagger Documentation is to expose unique ports for each service in `docker-compose.yml` and open that way. 

Run the full application:

- Build the project with: 
       
     `mvn package -DskipTests` (Skipping tests)
     or
     `mvn clean verify`
     
- Run the application with docker compose:

    `docker-compose up` / `docker-compose up --build --force-recreate` (to force a build and recreation of containers)

- Application is then accessible at port `80` at your machine.

- When running the application, one can either sign up or use any of these premade users

        * userId: test
        * password: test
        
        * userId: foo
        * password: bar

Run `e2e-tests`:

- Make sure project is buildt. Run `RestIT` test class from `e2e-tests` module. Tests are disabled by default so it is not run while building project.

## Covered exam topics

I did R1, R2, R3, R4 and R5.

R4:

- I used AMQP to notify `booking` api about trip creation and weather changes. Verification of AMQP is covered by e2e-test: `testCreateTripAndVerifyAMQPOnBookingService()`. 
- `NOTE:` I had to copy/paste the DTOs I wanted to use from `booking` api into `e2e-tests` to be able to use them. I should be able to use them directly as it worked properly in intellij. However, I got a "Compilation Error: Unresolved Reference" to the dtos when building the project with maven.

R5:

- Made with React and TypeScript (Made with Node v14.16.0)
- CSS with React-Bootstrap
- Hosting static build on nginx host in docker container
- Built when running mvn commands
- `BUG`: Since nginx only has `index.html` specified in configuration, it will not work to reload pages if you are not on home page.

## Exam Topic

When interpreting the topic for this exam, I stumbled upon a few logical issues I can't understand. In my opinion, some requirements does not work together. Therefore, I am discussing how I interpreted and made the application on each of the topic points.  

### T1: Necessary but not sufficient requirements for E
- `REST API to handle keeping track of planned trips: ports of origin and destination, boat and crew for each, and statuses for each trip.`

- I interpret this as making a new REST API for handling `trips`. I also made entities in this API for `boats` and `ports`.

- `When the API starts, it must have some already existing data`

- Existing boats and ports are both added when starting the application.

- `All requirements in R1 must be satisfied`

- Everything is covered.


### T2: Necessary but not sufficient requirements for C
- `REST API to handle the creation of new trips.`

- Handling creation of new trips can mean a lot of things. As specified in T3, this API is listening for when a trip is created (assumed to be the `trips` API from T1). Handling this can mean initializing data when this happens.

- `Each port has some kind of weather that changes at specific intervals (e.g. every few seconds for testing purposes).`

- I interpret this as making a `weather` api.

- `A logged-in user can create a new trip, between two existing ports, selecting the boat they will use, with a minimum and maximum number of passengers and crew depending on the boat.`

- As it does NOT specify which API can create a trip AND in T3 it is specified that each time a new trip is added, the API in T2 should listen and initialize data for booking, I create a trip in the `trips` API.

- `All requirements in R1 must be satisfied`

- Everything is covered.


### T3: Necessary but not sufficient requirements for B
- `Every time a new trip is added to the API, this relevant service should do a broadcast with AMQP. Then, the API in T2 should listen to such event, and initialize all the needed data for booking that trip.`

- This is not specific at all, but I read this as `trips` API is notifying T2 API when a new trip is added.

- `Every time the weather changes in a port, the relevant service sends messages to the service managing the trips. That service should listen for such events and update the statuses of relevant trips (e.g. issue warnings if one of the ports is experiencing a storm, of if a port no longer has free spots for additional boats).`

- I read this as a `weather` api running and sending a message via AMQP when the weather changes. `trips` API should listen for this all ports should fetch new weather status. It is specified that a trip has weather status, so I read this as having the weather status of the destination port on the trip itself.

### T4: Necessary but not sufficient requirements for A
- `Build frontend GUI`

- `Should be able to see trips associated with a port (either as origin or destination) or with a boat.`

- `Must be able to create/login/logout users`

- `A logged-in user should see a welcome message`

- `A logged-in user must be able to reserve a trip (i.e., setup a trip including a boat, between 2 ports), and see the trips they have reserved.`

- All frontend requirements are fulfilled.

## Functionality

As I don't see all the topic requirements logically working together, I have made some modifications for the topic (as discussed above).

- Everyone can GET all trips and filter by boat name (only per page).
- A user can either sign up or login. All logged in users can logout.
- A logged in user will get a welcome message.
- A logged in user can PLAN a trip, choosing from existing ports and boats (trip will be added through `trips` api).
- A logged in user can BOOK a trip and add crew and passengers. (Does not really make sense to have crew and passenger on trip object, but it is done as it is stated in T1).
- A logged in user can SEE their booked trips.
- Weather changes every 2 seconds to simulate weather changes in ports. Every time the weather changes the `weather` API broadcasts AMQP message to `trips` API and all ports will fetch new weather data from `weather` API.

Every functionality is tested from frontend and runs smoothly on my machine. All source code compiles and builds, and all tests runs properly.

## Known bugs
While developing I sometimes wasn't able to see the trip I added instantly.


## Docker note:
- As I had alot of docker images and containers on my machine, some services such as `postgres` started to exit. Cleaning docker images, containers and volumes did the job.

## Credits

All copied code from class is credited in the source files.

## Final Note

In the exam text the specified zip name has the wrong class code. As it is specified in the exam text I have named my zip `pg6100_2016`.