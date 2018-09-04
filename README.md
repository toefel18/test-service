# Intergamma Inventory Test

Integramma programming test 

## Run application

    docker run --name intergamma-inventory-postgres -p 5432:5432 -e POSTGRES_USER=inventory -e POSTGRES_PASSWORD=inventory -d postgres



## CURL

    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "rotterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "amsterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/stores -d '{"name": "utrecht"}'
    curl http://localhost:8080/stores
    curl http://localhost:8080/stores/utrecht
    curl -X DELETE http://localhost:8080/stores/utrecht
    
    
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "beitel", "productCode": "9877132546"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "hamer", "productCode": "1166555798"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/products -d '{"name": "makita", "productCode": "6668446888"}'
    curl http://localhost:8080/products
    curl http://localhost:8080/products/1166555798
    curl -X DELETE http://localhost:8080/products/1166555798
    
    curl -X GET -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798"
    curl -X PUT -H 'Content-Type: application/json' "http://localhost:8080/inventories/stores/rotterdam/products/1166555798" -d '{"amount": 5}'