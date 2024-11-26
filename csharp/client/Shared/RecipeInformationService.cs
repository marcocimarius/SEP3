namespace DefaultNamespace;

public class RecipeInformationService
{
    private string recipeTitle { get; set; }
    public bool IsButtonClicked { get; set; }

    public void SetButtonClicked()
    {
        IsButtonClicked = true;
    }
    public void SetButtonNotClicked()
    {
        IsButtonClicked = false;
    }

    public void SetTitle(string title)
    {
        recipeTitle = title;
    }
    public string GetTitle()
    {
        return recipeTitle;
    }
}