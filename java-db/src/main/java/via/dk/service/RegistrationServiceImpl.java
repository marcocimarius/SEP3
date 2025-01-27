package via.dk.service;

import io.grpc.stub.StreamObserver;
import via.dk.*;
import via.dk.dao.customerInformation.CustomerInformationDaoImpl;
import via.dk.dao.customerInformation.ICustomerInformationDao;
import via.dk.model.auth.CustomerInformation;
import via.dk.model.auth.ICustomerInformation;
import via.dk.dao.auth.ILoginDao;
import via.dk.dao.auth.LoginDaoImpl;
import via.dk.dao.auth.RegistrationDaoImpl;
import via.dk.model.auth.Login;
import via.dk.model.auth.Registration;
import via.dk.dao.auth.IRegistrationDao;

/**
 * Service class for the RegistrationService. Based on the RegistrationServiceGrpc.RegistrationServiceImplBase class.
 * Extends the Grpc-generated RegistrationServiceImplBase class.
 */
public class RegistrationServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase {
	private final IRegistrationDao registrationDao;
	private final ILoginDao loginDao;
	private final ICustomerInformationDao customerInformationDao;

	/**
	 * Constructor for the RegistrationServiceImpl class.
	 */
	public RegistrationServiceImpl() {
		registrationDao = new RegistrationDaoImpl();
		loginDao = new LoginDaoImpl();
		customerInformationDao = new CustomerInformationDaoImpl();
	}

	/**
	 * 	* Creates a registration.
	 * @param request The request to create a registration.
	 * @param responseObserver A response observer required by gRPC to send the response.
	 */
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

	/**
	 * Logs in a user.
	 * @param request The request to log in a user.
	 * @param responseObserver A response observer required by gRPC to send the response.
	 */
	@Override
	public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
		Login login = new Login(request.getEmail(), request.getPassword());
		Registration reg = null;
		try {
			 reg = loginDao.login(login);
		} catch (Exception e) {
			responseObserver.onError(e);
		}
		if(reg == null) {
			responseObserver.onError(new Exception("User does not exist"));
			return;
		}
		LoginResponse response = LoginResponse.newBuilder()
				.setId(reg.getId())
				.setEmail(reg.getEmail())
				.setPassword(reg.getPassword())
				.setIsAdmin(reg.getIsAdmin())
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	/**
	 * Creates a customer information.
	 * @param request The request to create a customer information.
	 * @param responseObserver A response observer required by gRPC to send the response.
	 */
	@Override
	public void createCustomerInformation(CreateCustomerInformationRequest request, StreamObserver<CreateCustomerInformationResponse> responseObserver) {
		ICustomerInformation ci = new CustomerInformation(request.getUserId(), request.getFirstName(), request.getLastName(), request.getPhone(), request.getStreetName(), "", request.getCityName(), request.getPostNumber(), request.getCountryName());
		try {
			int result = customerInformationDao.create(ci);
			if (result == 0) {
				throw new Exception("FAIL: Customer information creation failed");
			}
		} catch (Exception e) {
			responseObserver.onError(e);
			return;
		}
		CreateCustomerInformationResponse response = CreateCustomerInformationResponse.newBuilder().setStatus("SUCCESS: Customer information creation successful").build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
