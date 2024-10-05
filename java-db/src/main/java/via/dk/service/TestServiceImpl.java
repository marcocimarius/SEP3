package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.TestRequest;
import via.dk.TestResponse;
import via.dk.TestServiceGrpc;
import via.dk.dao.ITestDao;
import via.dk.dao.TestDaoImpl;
import via.dk.model.Test;

public class TestServiceImpl extends TestServiceGrpc.TestServiceImplBase {
	ITestDao testDao;

	public TestServiceImpl() {
		this.testDao = new TestDaoImpl();
	}

	@Override
	public void hello(
			TestRequest request, StreamObserver<TestResponse> responseObserver) {
		String greeting = "Name:" +
				request.getName() +
				"Description: " +
				request.getDescription();

		try {
			testDao.add(new Test(request.getName(), request.getDescription()));
		} catch (Exception e) {
			responseObserver.onError(e);
		}

		TestResponse response = TestResponse.newBuilder()
				.setGreeting(greeting)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
