# NN take home task by Wojtek

This Spring Boot app provides API for creation and fetching of accounts, and for converting PLN <-> USD currency pair within the account, accoriding to a rate obtained from NBP public API.

## Considerations and scope

This section is to outline additional considerations that might be outside of the scope of the task, as well as describe limitations and assumptions around the current solution.



1. **Balances** - Regular application of this kind, would not store balances as a field in the wallet entity. It would most likely have a separate ledger service, with double entry book-keeping and other mechanisms, ensuring data consistency. This of course would be outside of the scope of such task, so instead, the balances are simply amount fields in wallets.
2. **Extensibility** - Application is implemented in such a way, as not to overcomplicate things by making it 100% customisable, but also make potential extensions possible. For instance - it avoids dealing with specific currencies in the code, and instead supported currencies are configured via environment variable, which is easily updateable.
3. **Tests** - There are integration and unit tests in this project. Because of the sheer volume of potential things to check for, I decided to only implement the two most important integration tests according to requirements (convert currency PLN -> USD and vice versa), and 2 simple success/failure Unit tests for one of the methods, simply to demonstrate the knowledge of unit tests and Mockito. In real life production ready projects, there would be way more tests, including for the account API and more integration tests for the conversion functionality, checking all the edge cases.
4. **Account/Wallet API** - In real application there would be more endpoints used to manage every aspect of those entities lifecycle, but that was outside of the scope of this task and not mentioned in requirements.
5. **Security** - This type of API in real life, should definitely use some authentication/authorization service, such as KeyCloak, API Key, or third party provider solution - but it was not mentioned in requirements and out of scope of this kind of task.
<br/>


## Technology

- Java 21
- Spring Boot 3.3.0
- Maven
- Postgres
- Docker


## API

Swagger documentation can be accessed at:

`/api/v1/swagger-ui/index.html`


## Entities

The database is fairly simple, as it consists of just two tables:

- `account` - Represents a bank account. Users open bank accounts providing first name, last name, and initial balance in PLN. Accounts have one to many relationship with wallets.
    - `id` -  UUID Identifier for the account entity
    - `created_at` -  Creation date timestamp for account
    - `first_name` - First name of the person opening the account
    - `last_name` - Last name of the person opening the account
      <br /><br />
- `wallet` - Represents a wallet, which is where a balance of a given currency holdings is held. Wallets belong to accounts, and have many to one relationship with accounts.
    - `id` -  UUID Identifier for the wallet entity
    - `created_at` -  Creation date timestamp for wallet
    - `wallet_currency` - Currency in which the balance of the wallet is held
    - `account` - Foreign key specifying the account to which the wallet belongs
      <br /><br />



## Running the Application locally
Run the following commands to start postgres docker container:
```
docker build -t nntask_db .
docker run --name nntask_db -p 5432:5432 -d nntask_db
```
And then start the SpringBoot application, either with command or using InteliJ's run configuration. Use local profile: `-Dspring.profiles.active=local`

<br/>

### Environment variables


| Name                   | Suggested Value                            | Description                                                                                                                                                                |
|:-----------------------|:-------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `SUPPORTED_CURRENCIES` | PLN,USD                                    | Supported currencies. Currently it's only PLN and USD, but this environment variable is here to make this application easily extensible to other currencies in the future  |
| `BASE_CURRENCY`        | PLN                                        | Base currency used to evaluate all the other currencies against. Currently it's PLN, but this environment variable makes it easier to change the rates provider if needed. |
| `NBP_URL`              | https://api.nbp.pl/api/exchangerates/rates | URL of the NBP API                                                                                                                                                         | | super_secret_key                   | This is the key needed to access the API. Provide it in the X-API-KEY header of each request you make. Swagger paths are excluded from authentication                                                                                                                                                                           |



## Authors

- [@Wojtek Malek](https://www.github.com/remembertomorrow)

