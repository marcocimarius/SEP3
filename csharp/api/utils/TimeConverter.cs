namespace api.utils;

using Google.Protobuf.WellKnownTypes;
using System;

public class TimeConverter
{
    // Convert from Google.Protobuf.Timestamp to System.DateTime
    public static DateTime? ToDateTime(Timestamp? protoTimestamp)
    {
        if (protoTimestamp == null)
        {
            return null;
        }

        return DateTimeOffset.FromUnixTimeSeconds(protoTimestamp.Seconds)
            .AddTicks(protoTimestamp.Nanos / 100)
            .UtcDateTime;
    }

    // Convert from System.DateTime to Google.Protobuf.Timestamp
    public static Timestamp? ToProtobufTimestamp(DateTime? dateTime)
    {
        if (dateTime == null)
        {
            return null;
        }

        DateTimeOffset dto = new DateTimeOffset(dateTime.Value.ToUniversalTime());
        return new Timestamp
        {
            Seconds = dto.ToUnixTimeSeconds(),
            Nanos = dto.Millisecond * 1_000_000
        };
    }
}