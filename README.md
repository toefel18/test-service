# Intergamma Inventory Test

Integramma programming test 

## Run application

    docker run --name intergamma-inventory-postgres -p 5432:5432 -e POSTGRES_USER=inventory -e POSTGRES_PASSWORD=inventory -d postgres



## CURL

    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/store -d '{"name": "rotterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/store -d '{"name": "amsterdam"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/store -d '{"name": "utrecht"}'
    curl http://localhost:8080/store
    curl http://localhost:8080/store/utrecht
    curl -X DELETE http://localhost:8080/store/utrecht
    
    
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/product -d '{"name": "beitel", "productCode": "9877132546"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/product -d '{"name": "hamer", "productCode": "1166555798"}'
    curl -X POST -H 'Content-Type: application/json' http://localhost:8080/product -d '{"name": "makita", "productCode": "6668446888"}'
    curl http://localhost:8080/product
    curl http://localhost:8080/product/1166555798
    curl -X DELETE http://localhost:8080/prododuct/1166555798