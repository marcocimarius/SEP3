using System.Collections;
using Microsoft.VisualBasic.CompilerServices;

namespace api.dto;

public class UpdateIngredientDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public int Calories { get; set; }
    public bool IsAllergen { get; set; }
    public int TypeId { get; set; }
}