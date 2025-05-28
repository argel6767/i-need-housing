# TODO List

## The following tasks must be implemented

- Authentication and Authorization
  - All endpoints authenticated except auth with JWT, but register requires a key to allow only those with it to be able to register
  - [Docs](https://quarkus.io/guides/security-jwt-build)
- Implement connection with Azure container registry
  - delete images older than two weeks, but only if there will be 3 left over leaving images that can be reverted
  - [Docs](https://learn.microsoft.com/en-us/java/api/overview/azure/containers-containerregistry-readme?view=azure-java-stable)
- Implement connection with Azure PostgreSQL DB via datasource
  - delete listings that are over a few months old (2-3?)
- Implement logging of tasks done and save log files in Azure blob container
  - Also implement a logging Queue to send messages via websockets to the admin dashboard
  - [Docs](https://quarkus.io/guides/websockets-next-reference)
- Make all tasks schedule to allow service to be cron jobs
  - [Docs](https://quarkus.io/guides/websockets-next-reference)