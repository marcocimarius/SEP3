package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.CreateRegistrationRequest;
import via.dk.CreateRegistrationResponse;
import via.dk.dao.RegistrationDaoImpl;
import via.dk.model.Registration;
import via.dk.RegistrationServiceGrpc;
import via.dk.dao.IRegistrationDao;

public class RegistrationServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase {
	IRegistrationDao registrationDao;

	public RegistrationServiceImpl() {
		registrationDao = new RegistrationDaoImpl();
	}

	@Override
	public void createRegistration(CreateRegistrationRequest request, StreamObserver<CreateRegistrationResponse> responseObserver) {
		Registration registration = new Registration(request.getEmail(), request.getPassword(), request.getIsAdmin());
		try {
			int result = registrationDao.create(registration);
			if (result == 0) {
				throw new Exception("FAIL: Registration failed");
			}
		} catch (Exception e) {
			responseObserver.onError(e);
		}
		CreateRegistrationResponse response = CreateRegistrationResponse.newBuilder().setStatus("SUCCESS: Registration successful").build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();

	}
}
