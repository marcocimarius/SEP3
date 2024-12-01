using Google.Protobuf.WellKnownTypes;

namespace api.dto.adminWeek;

public class UpdateAdminWeekSelectionDto
{
    public int AdminWeekId { get; set; }
    public int CreatedById { get; set; }
    public Timestamp WeekStartDate { get; set; }
    public Timestamp WeekEndDate { get; set; }
    public List<int> RecipeIds { get; set; }
}