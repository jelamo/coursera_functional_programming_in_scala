# coursera_functional_programming_in_scala
course assignments
```mermaid
graph LR;
 non-legacy-api-user([api.tarabutgateway.io <br> >>unified API domain>>]):::client;
 legacy-api-user([api-payments.tarabutgateway.io <br> >>legacy API domain / ZAIN>>]):::client;
 web-frontend-user([web-payments.tarabutgateway.io <br> >>consent web link>>]):::client;
 app-frontend-user([payments.app.tarabutgateway.io <br> >>consent app link>>]):::client;

 pisp-api-transaction[pisp-api-transaction <br> >>service>>]:::service
 pisp-api-report[pisp-api-report <br> >>service>>]:::service
 pisp-web-consent[pisp-web-consent <br> >>service>>]:::service

 ingress-trasanction-1[pisp-api-transaction-payments<br>>>Ingress>>]:::ingress;
 ingress-transaction-2[pisp-api-transaction-providers<br>>>Ingress>>]:::ingress;
 ingress-transaction-3[pisp-api-transaction<br>>>Ingress>>]:::ingress;
 ingress-transaction-4[pisp-unified-ingress<br>>>Ingress>>]:::ingress;
 ingress-report-1[pisp-api-report-payments<br>>>Inbress>>]:::ingress;
 ingress-report-2[pisp-api-report-preferences<br>>>Ingress>>]:::ingress;
 ingress-report-3[pisp-api-report-payments<br>>>Ingress>>]:::ingress;
 ingress-report-4[pisp-api-report<br>>>Ingress>>]:::ingress;
 ingress-webconsent-1[pisp-web-consent-app<br>>>Ingress>>]:::ingress;
 ingress-webconsent-2[pisp-web-consent<br>>>Ingress>>]:::ingress;
 
 non-legacy-api-user-. /paymentInitiation/v1/payments .-> ingress-trasanction-1;
 non-legacy-api-user-. /v1/providers .-> ingress-transaction-2;
 non-legacy-api-user-. /payments/transaction/v1/** .-> ingress-transaction-4;
 non-legacy-api-user-. /payments/report/v1/** .-> ingress-transaction-4;
 non-legacy-api-user-. /paymentInitiation/v1/payments/** .-> ingress-report-1;
 non-legacy-api-user-. /paymentInitiation/v1/preferences/** .-> ingress-report-2;
 legacy-api-user-. /transaction/** .-> ingress-transaction-3;
 legacy-api-user-. /payments/** .-> ingress-report-3;
 legacy-api-user-. /report/** .-> ingress-report-4;
 app-frontend-user-. /select-bank/**<br>/confirm/**<br>/register/**<br>/token/getAccessToken/**<br>/authorise/**<br>/decline/**<br>/mock-redirect/**<br>/health/**<br>/static/**<br>/complete/**<br>/apple-app-site-association/**<br>/.well-known/**<br>/banks/**<br>/callback/**<br>/save/**<br>/generate/report/** .-> ingress-webconsent-1;
 web-frontend-user-. /select-bank/**<br>/confirm/**<br>/register/**<br>/token/getAccessToken/**<br>/authorise/**<br>/decline/**<br>/mock-redirect/**<br>/health/**<br>/static/**<br>/complete/**<br>/apple-app-site-association/**<br>/.well-known/**<br>/banks/**<br>/callback/**<br>/save/**<br>/generate/report/** .-> ingress-webconsent-2;

 subgraph "K8s Cluster"
    ingress-trasanction-1-->|/paymentInitiation/v1/payments | pisp-api-transaction;
    ingress-transaction-2-->|/payments/lookup/bank | pisp-api-transaction;
    ingress-transaction-3-->|/transaction/** | pisp-api-transaction;
    ingress-transaction-4-->|/payments/transaction/v1/** <br> !!!WRONG: endpoints do not exit!!!! | pisp-api-transaction;
    ingress-transaction-4-->|/payments/report/v1/** | pisp-api-report;
    ingress-report-1-->|/payments/report/v1/transaction/** | pisp-api-report;
    ingress-report-2-->|/report/v1/preferences/** <br> !!!WRONG: should go to /paymentInitiation/v1/preferences/**!!!| pisp-api-report;
    ingress-report-3-->|/report/v1/payment/** | pisp-api-report;
    ingress-report-4-->|/report/** | pisp-api-report;
    ingress-webconsent-1-->|/select-bank/**<br>/confirm/**<br>/register/**<br>/token/getAccessToken/**<br>/authorise/**<br>/decline/**<br>/mock-redirect/**<br>/health/**<br>/static/**<br>/complete/**<br>/apple-app-site-association/**<br>/.well-known/**<br>/banks/**<br>/callback/**<br>/save/**<br>/generate/report/** | pisp-web-consent;
    ingress-webconsent-2-->|/select-bank/**<br>/confirm/**<br>/register/**<br>/token/getAccessToken/**<br>/authorise/**<br>/decline/**<br>/mock-redirect/**<br>/health/**<br>/static/**<br>/complete/**<br>/apple-app-site-association/**<br>/.well-known/**<br>/banks/**<br>/callback/**<br>/save/**<br>/generate/report/** | pisp-web-consent;
 end

classDef service fill:#326ce5,stroke:#fff,stroke-width:4px,color:#fff;
classDef ingress fill:#bf42f5,stroke:#fff,stroke-width:4px,color:#fff;
classDef cluster fill:#fff,stroke:#bbb,stroke-width:2px,color:#326ce5;
```
