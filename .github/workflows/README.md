# Production Deployment Workflow

This GitHub Actions workflow automatically deploys the backend and cron job service to Azure when changes are pushed to the `production` branch.

## Key Features

- **Smart Change Detection**: Only builds and deploys services that have actual changes
- **Eliminates Redundant Builds**: Prevents unnecessary deployments when no changes are detected
- **Sequential Deployment**: Backend deploys first, then cron service (if needed)
- **Comprehensive Logging**: Clear visibility into what changed and what was deployed

## Prerequisites

1. **Azure Container Registry (ACR)**: The workflow uses `ineedhousing.azurecr.io`
2. **Azure App Services**:
   - `i-need-housing-backend` for the backend service
   - `cron-job-service` for the cron job service
3. **Resource Group**: `INeedHousing`

## How Change Detection Works

The workflow automatically detects changes in:

- **Backend changes**: Any files in the `backend/` directory
- **Cron service changes**: Any files in the `cron_job_service/` directory  
- **Shared dependencies**: Changes to `scripts/`, `pom.xml`, or `Dockerfile` files

### Change Detection Logic

1. **Service-specific changes**: Only the affected service is built and deployed
2. **Shared dependency changes**: Both services are built and deployed (since they might be affected)
3. **No changes**: No builds or deployments occur - workflow completes successfully

### Example Scenarios

| Changes Made | Backend Deployed | Cron Service Deployed |
|--------------|------------------|----------------------|
| Only `backend/src/` files | ✅ Yes | ❌ No |
| Only `cron_job_service/src/` files | ❌ No | ✅ Yes |
| Changes to `scripts/` or `pom.xml` | ✅ Yes | ✅ Yes |
| No changes detected | ❌ No | ❌ No |

## Required GitHub Secrets

You need to set the following secrets in your GitHub repository:

1. Go to your repository → Settings → Secrets and variables → Actions
2. Add the following repository secrets:

### `AZURE_TENANT_ID`

- **Value**: Your Azure tenant ID

### `AZURE_USERNAME`

- **Value**: Your Azure username/email

### `AZURE_PASSWORD`

- **Value**: Your Azure password
- **Note**: Use a service principal password for production environments

## Workflow Steps

1. **Check Changes**: Analyzes git diff to determine what services need deployment
2. **Deploy Backend**: Builds and deploys backend (only if changes detected)
3. **Deploy Cron Service**: Builds and deploys cron service (only if changes detected)
4. **Deployment Summary**: Provides comprehensive status and logs

## Manual Trigger

You can also manually trigger the workflow:

1. Go to Actions tab in your repository
2. Select "Deploy to Production"
3. Click "Run workflow"

**Note**: Manual triggers will always check for changes and only deploy what's necessary.

## Security Notes

- **Never commit credentials** to your repository
- Consider using Azure service principals instead of user credentials
- The workflow uses the latest Ubuntu runner with Docker and Azure CLI
- All sensitive information is stored in GitHub secrets

## Troubleshooting

If deployment fails:

1. Check the Actions tab for detailed error logs
2. Verify your GitHub secrets are correctly set
3. Ensure your Azure resources exist and are accessible
4. Check that your Azure Container Registry has the correct permissions

## Performance Benefits

- **Faster workflows**: No unnecessary builds when no changes exist
- **Reduced costs**: Fewer Docker builds and Azure deployments
- **Better resource utilization**: Only deploys what actually changed
- **Improved CI/CD efficiency**: Streamlined deployment process 