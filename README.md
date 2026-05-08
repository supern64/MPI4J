# MPI4J
*The Future of Online Banking - for Everyone™*  
An implementation of the [MyPayIndia v2](https://mypayindia.com/docs/api) API for Java 17+ clients.
## Structure
The library is divided into 2 main classes, `MPIUserClient` for user interactions and `MPIMerchantClient` for the merchant checkout flow.
All API functions return a `CompletableFuture<Response<T>>` for async compatibility.
## Quick Start
```java
public class MyClientIndia {
    public static void main(String[] args) {
        MPIUserClient mpi = new MPIUserClient();
        Response<LoginResponse> loginRes = mpi.auth().login("sam_onlinebanking", "securepassword").join();

        if (loginRes.success()) {
            System.out.println("Successfully logged in as " + loginRes.data().user().username());
        }
    }
}
```
Further documentation is available through JavaDoc in source code. Your IDE should be able to pick these up.