# Loan Service

It is an app that allows authenticated users to go through a loan application. Users can request loans, view their loans and pay instalments. User will be allocated loan once it is approved by admin.

Loan has 3 type of status:
* PENDING - when the loan is created
* APPROVED - when the loan is approved by admin
* PAID - once all instalments are PAID

Loan instalment has 2 type of status:
* PENDING - when payment of instalment is PENDING
* PAID - when instalment is paid

**Assumption:**
* Interest is 0%.
* Instalment frequency is Weekly.
* There is only one type of loan.

## Features
**RBAC**
1. Register user: A new user will be registered with role either ADMIN or USER.
2. Authenticate: An api to authenticate user
3. User can access APIs only after they are authenticated and if they have required authority to access API.

**Loan Service APIs**
1. GET /api/v1/loan/fetchAll - to get all loans of a authenticated user.
2. POST /api/v1/loan/create - create a new loan. Request by user.
3. PUT /api/v1/loan/approve - api for admin to approve loan.
4. POST /api/v1/loan/instalment - pay loan instalment.

## Local Setup
**Prerequisite**
1. java 17
2. mysql

**Setup**:
1. Add values for following fields in application.properties
* spring.datasource.url
* spring.datasource.username
* spring.datasource.password
* application.security.jwt.secret-key=

2. mvn compile
3. mvn spring-boot:run