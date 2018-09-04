# Intergamma Inventory Test

Programming test for intergamma. Built with Java8 and SpringBoot2.

This implements a service that manages the inventory of a product for a specific store. 

### Running the application

The application requires a PostgresSQL database. Tables are created automatically if not present. 
The datasource.url is hardcoded in application.properties to: `spring.datasource.url=jdbc:postgresql://localhost:5432/inventory`
Use this docker command to quickly start a postgres database with the required credentials:

    docker run --name intergamma-inventory-postgres -p 5432:5432 -e POSTGRES_USER=inventory -e POSTGRES_PASSWORD=inventory -d postgres

Building the application:

    ./gradlew build
    
Running the application:

    java -jar build/inventory-0.0.1-SNAPSHOT.jar

## Using the application

    # create stores
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "rotterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "amsterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "utrecht"}'
    
    # view stores
    curl http://localhost:8080/stores
    curl http://localhost:8080/stores/utrecht
    
    # delete store
    curl -X DELETE http://localhost:8080/stores/utrecht
    
    # add products
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "beitel", "productCode": "9877132546"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "hamer", "productCode": "1166555798"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "makita", "productCode": "6668446888"}'
    
    # view products
    curl http://localhost:8080/products
    curl http://localhost:8080/products/1166555798
    
    # delete product
    curl -X DELETE http://localhost:8080/products/1166555798
    
    # view inventory
    curl -X GET "http://localhost:8080/inventories/stores/rotterdam/products/1166555798"
    
    # set inventory of "hamer" in "rotterdam" to 5 items
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798" -d '{"amount": 5}'
    
    # kees reserves 3 hamers
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees" -d '{"amount": 3}'
    
    # view inventory, you can now see the reservations
    curl -X GET "http://localhost:8080/inventories/stores/rotterdam/products/1166555798"
    
    # klaas also tries to reserve 3 hamers, but gets an exception because there is not enough inventory taking the other reservations into account
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees" -d '{"amount": 3}'
    
    # delete the reservation made by kees
    curl -X DELETE  "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees"
    
    # now klaas can make the reservation
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees" -d '{"amount": 3}'
    
    