# Food Ordering System

## Classes
-  <code>CookingSlot</code> : DTO(Data Transfer Object) class meant for data related to restaurant fullfilment requirements.
-  <code>Order</code> : DTO(Data Transfer Object) class meant for parsing json order data.
- <code>ResponseParser</code> : Class for parsing json responses from network requests or files.
- <code>Restaurant</code> : Interface to implement <code>Factory pattern</code> for Restaurant related classes.
- <code>RestaurantStandardImpl</code> : Standard implementation of a Restaurant interface which uses standard Restaurant parameters
- <code>DeliveryConstants</code> : Delivery related constants.
- <code>RestaurantConstants</code> : Restaurant specific constants.
- <code>Utils</code> : Generic reusable functions.

# No Code Challenge
- <b>Select a Database you want to use to solve this problem.</b>

    Redis for short term storage and MySQL for long term storage(can go for NoSQL db too).

- <b>Write down which tables you are going to make and mention the keys (primary, partition, sort keys, indexes and whichever apply to you etc)</b>

### Tables (SQL)

Primary keys can be used for indexing in case of both tables.

<code> User </code> table

|Columns|Type|Description|
|-|-|-|
|phone| string|Primary Key| 
|email| string||
|address| string||

<code> Orders </code> table

|Columns|Type|Description|
|-|-|-|
|orderId|string|Primary Key| 
|meals|string(considering meals are single characters)|| 
|phone|string|foreign Key| 
|placeAt|string|(time at which the order should be placed considering future orders)| 
|specialInstructions|string|(lets users mention exact recipe they want)|
|recurring|string|(daily, weekly, monthly)| 

### Structure (Redis)
    We use redis to store orders say upto 24 hours from current time to keep redis form getting filled with data. In redis we use a key-value structure with time of order as key and a list of orderId for that time as the value.
    With orderId being stored we don't have to worry about updating redis in case the order gets modified.




- <b>Write down the flow of CRUD. (Eg. When someone creates and order, what all entries will get made.) (Eg. How will you manage scheduled orders as in how will you actually go about processing them at the given time when they are saved in your table)</b>

    There are two processes happening parallely:

    <b>1. Order placement and editing by user</b>
    
    <b>2. Order fullfillment by a cron service</b>

### 1. Order placement and editing by user  
Process for order placement will be something like this 

1. The user registers on the service and that data is stored in the user table.
2. The user goes to the interface and places an order with current time or a future time and the order details including special instructions.This data is persisted in the Orders table.
3. If the time of order is within next 24 hours it is also sent to redis updating the redis key-value pair for the order time. Ex: if the order is supposed to be placed at 9:00 pm it is updated in redis against the key for 21:00 like {"21:00":[ orderId ]}
 
Process for order modification or deletion will be something like this 

1. The user goes to the interface and modifies an order with current time or a future time or the order details including special instructions.This data is modified in the Orders table.
2. If the time of order is changed and is within next 24 hours it is also sent to redis.
(In case of deletion the order is removed from the orders table)

### 2. Order fullfillment by a cron service
Cron service does two tasks:

- <code> Updating the redis for orders past 24 hours after some fixed duration say every hour</code>. This way we don't have to scrape the database too soon and we decrease db calls which is what redis is used for. 
- <code>Goes through redis every minute(or even second) and places order for that instant</code> (ttl of the entries are just the moment when the order is supposed to be fulfilled)

Before placing order the cron service verifies the order details from the db so in only the updated details get passed in case of modification and in case of deletion if db entry is not found order is not sent for fullfillment.

For example, when cron comes to fulfill all orders for 9pm it fetches the 21:00 key from redis and gets the array of orderIds to be fulfilled,say [12,21]. Now it checks for these orderId in db Order table for order details. In case the orderId is not in Orders table in case of order deletion the order is not passed to the restaurant.


- <b>You did something our of the box? Great! Mention why you think its a better solution and how is it making the solution better</b>

    Using redis for this use case makes the design more effective, fast and robust with the least number of db calls which decreases latency.
