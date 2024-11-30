namespace DefaultNamespace;

public class ButtonStateService
{
    public bool IsButtonClicked { get; set; }

    public void SetButtonClicked()
    {
        IsButtonClicked = true;
    }
}