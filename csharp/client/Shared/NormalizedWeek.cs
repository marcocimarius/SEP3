using BlazorApp1.Pages;
using Google.Protobuf.WellKnownTypes;
using Recipe = Via.Dk.Recipe;

namespace DefaultNamespace;

public class NormalizedWeek
{
    public Timestamp WeekStartDate { get; set; }
    public Timestamp WeekEndDate { get; set; }
    public IEnumerable<Recipe> Recipes { get; set; }
}