package via.dk.util;

import java.sql.Timestamp;

public class TimeConverter
{
  public static Timestamp toJavaTimestamp(com.google.protobuf.Timestamp protoTimestamp) {
    if (protoTimestamp == null) {
      return null;
    }

    long seconds = protoTimestamp.getSeconds();
    int nanos = protoTimestamp.getNanos();
    long millis = seconds * 1000 + nanos / 1_000_000;
    return new Timestamp(seconds * 1000 + nanos / 1_000_000);
  }

  public static com.google.protobuf.Timestamp toProtobufTimestamp(Timestamp javaTimestamp) {
    if (javaTimestamp == null) {
      return null;
    }

    long millis = javaTimestamp.getTime();
    long seconds = millis / 1000;
    int nanos = (int) ((millis % 1000) * 1_000_000);
    return com.google.protobuf.Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build();
  }

}
