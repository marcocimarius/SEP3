namespace DefaultNamespace;

public class TypeMap
{
    public static string GetTypeName(int id)
    {
        return id switch
        {
            1 => "vegan",
            2 => "vegetarian",
            3 => "pescatarian",
            4 => "unrestricted",
            _ => throw new ArgumentOutOfRangeException()
        };
    }

    public static int GetTypeId(string name)
    {
        return name.ToLower() switch
        {
            "vegan" => 1,
            "vegetarian" => 2,
            "pescatarian" => 3,
            "unrestricted" => 4,
            _ => throw new ArgumentOutOfRangeException()
        };
    }
}