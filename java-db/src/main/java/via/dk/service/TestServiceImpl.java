package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.TestRequest;
import via.dk.TestResponse;
import via.dk.TestServiceGrpc;

public class TestServiceImpl extends TestServiceGrpc.TestServiceImplBase {
	@Override
	public void hello(
			TestRequest request, StreamObserver<TestResponse> responseObserver) {

		String greeting = "Hello, " +
				request.getName() +
				" " +
				request.getDescription();

		TestResponse response = TestResponse.newBuilder()
				.setGreeting(greeting)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
