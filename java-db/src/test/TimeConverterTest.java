
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class TimeConverterTest {

	@Test
	public void testProtoTsToJavaTs() {
		// Arrange
		Timestamp timestamp = new Timestamp(0);
		com.google.protobuf.Timestamp protoTimestamp = com.google.protobuf.Timestamp.newBuilder().setSeconds(0).setNanos(0).build();
		// Act
		Timestamp result = via.dk.util.TimeConverter.toJavaTimestamp(protoTimestamp);
		// Assert
		Assertions.assertEquals(timestamp, result);
	}

	@Test
	public void testJavaTsToProto() {
		// Arrange
		Timestamp timestamp = new Timestamp(0);
		com.google.protobuf.Timestamp protoTimestamp = com.google.protobuf.Timestamp.newBuilder().setSeconds(0).setNanos(0).build();
		// Act
		com.google.protobuf.Timestamp result = via.dk.util.TimeConverter.toProtobufTimestamp(timestamp);
		// Assert
		Assertions.assertEquals(protoTimestamp, result);
	}
}
