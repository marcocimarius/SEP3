Repository for the GoodbyeRotten semester project. GoodbyeRotten is a mealkit delivery system like HelloFresh.

# How to run

## Java

Open the `java-db` folder in IntelliJ.

### GRPC
Navigate to `pom.xml` file, on the right bar click the **Maven** icon and then double click `compile` to generate the java grpc types.
<img width="481" alt="image" src="https://github.com/user-attachments/assets/76657a34-85e8-42cc-a058-039f79f3b6ef">

### Database

Navigate to the `util/DatabaseConnection.java` file and change the values of the database connection string to match your PostgreSQL instance and database. To check what they are you can open DataGrip and check the port and the database you need.

After that navigate to `db/init.sql` and run the whole file in a console connected to your database! You can do that in IntelliJ if you have your database set there or in DataGrip.

After that is done run the `service/Main.java` file. The output on your console should look like that.
<img width="1017" alt="image" src="https://github.com/user-attachments/assets/974c9258-130d-4958-a5a6-2d11bc312968">

## CSharp

Open the `csharp` folder in Rider.

### API
Navigate to the `csharp/api` Solution, open a new terminal and go to `csharp/api` then run `dotnet watch run`
Alternatively you can choose from the dropdown on the top right `api: http` and run that <img width="440" alt="image" src="https://github.com/user-attachments/assets/7b06f1f6-9cab-4705-9997-14a6d982d4d4">

### Client
Navigate to the `csharp/client` Solution, open a new terminal and go to `csharp/client` then run `dotnet watch run`
Alternatively you can choose from the dropdown on the top right `client: http` and run that <img width="374" alt="image" src="https://github.com/user-attachments/assets/684c97c7-429d-412f-96d2-8afa1402e712">

### Styles
Open a terminal in Rider and navigate to the the `csharp/client/wwwroot` folder. Then run `npm install` and after that `npm styles`. After you run `npm styles` you should see <img width="891" alt="image" src="https://github.com/user-attachments/assets/fa26cd26-bd27-4d29-9778-065dd4e2cd73">




