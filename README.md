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

    java -jar build/libs/inventory-0.0.1-SNAPSHOT.jar

## Design

The service stores the inventory as a list of ADD and REMOVE events. The actual inventory is the sum of ADD events minus
the sum of REMOVE events. Similar how a cash register in the supermarket works. 

Storing events this way has some advantages over storing just a value. For example:
- Audit log, a complete history of events that mutate the inventory.
- Analysis options for determining in which period there is a peak.

Reservations are made by clients. A client can only have 1 outstanding reservation for a given product at a given
store. 

Terms used:

- inventory: the amount of actual goods present
- availableInventory: the amount of actual goods minus the reserved goods.

## Missing pieces

1. Preferred way of working is that a client reserves a product, then buys the product which should automatically cancel the 
   reservations made by that customer. Right now these are two separate calls, that creates a gap between these two actions
   that should be part of the same transaction. To fix this, I would change the API to accept InventoryRestock and ProductSold
   events. On ProductSold events, the reservations would be automatically cancelled. 
   
   It should not be possible for products to be sold without an open reservation!
   
1. Optional exercises (tests, spring security, oauth2)

## Using the application

First, create stores

    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "rotterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "amsterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "utrecht"}'
    
    # view stores
    curl http://localhost:8080/stores
    curl http://localhost:8080/stores/utrecht
    
    # delete store
    curl -X DELETE http://localhost:8080/stores/utrecht
    
Then create some productions 

    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "beitel", "productCode": "9877132546"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "hamer", "productCode": "1166555798"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "makita", "productCode": "6668446888"}'
    
    # view products
    curl http://localhost:8080/products
    curl http://localhost:8080/products/1166555798
    
    # delete product
    curl -X DELETE http://localhost:8080/products/1166555798
    
Configure the inventory of the 'hamer' in 'rotterdam' to 5 items

    # set inventory of "hamer" in "rotterdam" to 5 items
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798" -d '{"amount": 5}'
    # view the inventory of that product
    curl -X GET "http://localhost:8080/inventories/stores/rotterdam/products/1166555798"
    
    # kees reserves 3 hamers
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees" -d '{"amount": 3}'
    
    # view inventory, you can now see the reservations
    curl -X GET "http://localhost:8080/inventories/stores/rotterdam/products/1166555798"
    
    # klaas also tries to reserve 3 hamers, but gets an exception because there is not enough inventory taking the other reservations into account
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/klaas" -d '{"amount": 3}'
    
    # delete the reservation made by kees
    curl -X DELETE  "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees"
    
    # now klaas can make the reservation
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798/reservations/kees" -d '{"amount": 3}'
    
    