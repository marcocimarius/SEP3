package via.dk.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException	 {
		Server server = ServerBuilder.forPort(8181)
				.addService(new RegistrationServiceImpl())
				.addService(new RecipeServiceImpl())
				.addService(new IngredientServiceImp())
				.build();

		server.start();
		System.out.println("Server started at " + server.getPort());
		server.awaitTermination();
	}
}