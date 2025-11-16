package com.pm.billingservice.grpc;


import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
// we tell spring that this is a grpc service and that we want to have it managed by the spring boot lifecycle
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    // Whenever we receive a grpc request to create a billing account, that's going to get handled here, so we receive the request and convert it to a BillingRequest class, so that we can interact with the request and once we have completed any business logic and ready to return a response we use what's called StreamObserver
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", billingRequest.toString());

        // Business Logic  - e.g save to database, perform calculation etc.

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);   // used to send a response from our grpc service in other words the billing service back to the client, which in this case is going to be the patient service
        responseObserver.onCompleted();   // we are ready to end the cycle in this response
    }


}
