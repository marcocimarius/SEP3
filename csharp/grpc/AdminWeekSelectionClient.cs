using Grpc.Net.Client;
using Via.Dk;

namespace grpc;

public class AdminWeekSelectionClient
{
    private readonly AdminWeekSelectionService.AdminWeekSelectionServiceClient _adminWeekSelectionServiceClient;

    public AdminWeekSelectionClient(GrpcChannel channel)
    {
        this._adminWeekSelectionServiceClient = new AdminWeekSelectionService.AdminWeekSelectionServiceClient(channel);
    }

    public async Task<string> Create(CreateAdminWeekSelectionRequest request)
    {
        return (await _adminWeekSelectionServiceClient.CreateAdminWeekSelectionAsync(request)).Status;
    }

    public async Task<string> Delete(DeleteAdminWeekSelectionRequest request)
    {
        return (await _adminWeekSelectionServiceClient.DeleteAdminWeekSelectionAsync(request)).Status;
    }

    public async Task<string> Update(UpdateAdminWeekSelectionRequest request)
    {
        return (await _adminWeekSelectionServiceClient.UpdateAdminWeekSelectionAsync(request)).Status;
    }

    public async Task<RetrieveAdminWeekSelectionResponse> GetAdminWeekRecipes(RetrieveAdminWeekSelectionRequest request)
    {
        return (await this._adminWeekSelectionServiceClient.RetrieveAdminWeekSelectionAsync(request));
    }
}