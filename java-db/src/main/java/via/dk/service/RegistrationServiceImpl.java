package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.ILoginDao;
import via.dk.dao.LoginDaoImpl;
import via.dk.dao.RegistrationDaoImpl;
import via.dk.model.auth.Login;
import via.dk.model.auth.Registration;
import via.dk.dao.IRegistrationDao;

public class RegistrationServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase {
	private final IRegistrationDao registrationDao;
	private final ILoginDao loginDao;

	public RegistrationServiceImpl() {
		registrationDao = new RegistrationDaoImpl();
		loginDao = new LoginDaoImpl();
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

	@Override
	public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
		Login login = new Login(request.getEmail(), request.getPassword());
		Registration reg;
		try {
			 reg = loginDao.login(login);
		} catch (Exception e) {
			responseObserver.onError(e);
		}
		if(reg == null) {
			responseObserver.onError(new Exception("User does not exist"));
		}
		LoginResponse response = LoginResponse.newBuilder()
				.setEmail(reg.getEmail())
				.setPassword(reg.getPassword())
				.setIsAdmin(reg.getIsAdmin())
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
