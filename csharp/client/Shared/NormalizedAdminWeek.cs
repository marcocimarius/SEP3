using Google.Protobuf.WellKnownTypes;
using Via.Dk;

namespace DefaultNamespace;

public class NormalizedAdminWeek
{
    public int Id { get; set; } 
    public Timestamp WeekStartDate { get; set; }
    public Timestamp WeekEndDate { get; set; }
    public IEnumerable<Recipe> Recipes { get; set; }
}