# Capgemini Engineering - Smart Tech Start Java Internship <!-- omit in toc -->
## Banking Transactions System <!-- omit in toc -->

### - High level functionalities

![functionalities](assets/Functionalities.jpg)

### - REST API
#### Endpoints:
##### - User:
- POST /api/registration - register new user
- GET /api/registration/confirmation?token= - provide user with verification link
- POST /api/bank-account - create new link between account number and user id
- POST /api/user/bank-account - send a request for a new bank account
- DELETE /api/user/bank-account - send a bank account removal request 
- GET /api/token/refresh - provide user with new access token
##### - Bank Account:
- POST /api/account - create new bank account
- DELETE /api/account - delete bank account
- PUT /api/account - process a2a transfer
##### - Transaction:
- POST /api/transfer - create new transfer request and send it to bank-account service
