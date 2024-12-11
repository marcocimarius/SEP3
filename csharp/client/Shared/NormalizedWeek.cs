using BlazorApp1.Pages;
using Google.Protobuf.WellKnownTypes;
using Recipe = Via.Dk.Recipe;

namespace DefaultNamespace;

public class NormalizedWeek
{
    public int Id { get; set; }
    public Timestamp? WeekStartDate { get; set; }
    public Timestamp? WeekEndDate { get; set; }
    public List<Recipe> Recipes { get; set; } = new List<Recipe>();
}