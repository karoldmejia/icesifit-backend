# Physical Activity Project 

Plataforma para gestionar actividad física en la Universidad Icesi. Permite administrar usuarios, roles y permisos; registrar rutinas, ejercicios, progreso, eventos y notificaciones en tiempo real, con autenticación JWT y persistencia vía Spring Data JPA.

### Integrantes
- Heiner Danit Rincon - A00402510
- Karold Mejia - A00401806
- David Vergara Laverde - A00402237

### Contexto del problema
La Universidad Icesi requiere una plataforma centralizada que facilite el seguimiento del entrenamiento físico de estudiantes y colaboradores. El sistema permite crear y gestionar rutinas personalizadas, registrar progreso, conectar usuarios con entrenadores certificados, publicar eventos y espacios disponibles, y enviar notificaciones en tiempo real. Este proyecto de SpringBoot implementa la capa de servicios y persistencia necesaria para estas funcionalidades.

### Estructura del proyecto
    .
    ├── descarga.png
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    ├── src
    │   ├── main
    │   │   ├── frontend
    │   │   │   ├── package.json
    │   │   │   ├── package-lock.json
    │   │   │   ├── postcss.config.js
    │   │   │   ├── styles.css
    │   │   │   └── tailwind.config.js
    │   │   ├── java
    │   │   │   └── com
    │   │   │       └── example
    │   │   │           └── physical_activity_project
    │   │   │               ├── config
    │   │   │               │   ├── PasswordConfig.java
    │   │   │               │   └── SecurityConfig.java
    │   │   │               ├── controller
    │   │   │               │   ├── mvc
    │   │   │               │   │   ├── LoginMVCController.java
    │   │   │               │   │   ├── PermissionMVCController.java
    │   │   │               │   │   ├── RolMVCController.java
    │   │   │               │   │   ├── SignUpMVCController.java
    │   │   │               │   │   └── UserMVCController.java
    │   │   │               │   ├── PermissionController.java
    │   │   │               │   ├── RoleController.java
    │   │   │               │   └── UserController.java
    │   │   │               ├── dto
    │   │   │               │   ├── ProgressDTO.java
    │   │   │               │   └── RoleDTO.java
    │   │   │               ├── model
    │   │   │               │   ├── Event.java
    │   │   │               │   ├── EventSchedule.java
    │   │   │               │   ├── Exercise.java
    │   │   │               │   ├── ExerciseProgress.java
    │   │   │               │   ├── Message.java
    │   │   │               │   ├── Notification.java
    │   │   │               │   ├── Permission.java
    │   │   │               │   ├── Recommendation.java
    │   │   │               │   ├── Role.java
    │   │   │               │   ├── RolePermission.java
    │   │   │               │   ├── RoutineExercise.java
    │   │   │               │   ├── Routine.java
    │   │   │               │   ├── Schedule.java
    │   │   │               │   ├── Space.java
    │   │   │               │   ├── UserEvent.java
    │   │   │               │   ├── User.java
    │   │   │               │   ├── UserRoutine.java
    │   │   │               │   └── UserTrainerAssignment.java
    │   │   │               ├── PhysicalActivityProjectApplication.java
    │   │   │               ├── repository
    │   │   │               │   ├── IEventRepository.java
    │   │   │               │   ├── IEventScheduleRepository.java
    │   │   │               │   ├── IExerciseProgressRepository.java
    │   │   │               │   ├── IExerciseRepository.java
    │   │   │               │   ├── IMessageRepository.java
    │   │   │               │   ├── INotificationRepository.java
    │   │   │               │   ├── IPermissionRepository.java
    │   │   │               │   ├── IRecommendationRepository.java
    │   │   │               │   ├── IRolePermissionRepository.java
    │   │   │               │   ├── IRoleRepository.java
    │   │   │               │   ├── IRoutineExerciseRepository.java
    │   │   │               │   ├── IRoutineRepository.java
    │   │   │               │   ├── IScheduleRepository.java
    │   │   │               │   ├── ISpaceRepository.java
    │   │   │               │   ├── IUserEventRepository.java
    │   │   │               │   ├── IUserRepository.java
    │   │   │               │   ├── IUserRoutineRepository.java
    │   │   │               │   └── IUserTrainerAssignmentRepository.java
    │   │   │               ├── security
    │   │   │               │   ├── CustomUserDetails.java
    │   │   │               │   └── SecurityAuthority.java
    │   │   │               ├── services
    │   │   │               │   ├── auth
    │   │   │               │   │   └── CustomUserDetailsService.java
    │   │   │               │   ├── impl
    │   │   │               │   │   ├── ExerciseProgressServiceImpl.java
    │   │   │               │   │   ├── ExerciseServiceImpl.java
    │   │   │               │   │   ├── PermissionServiceImpl.java
    │   │   │               │   │   ├── RolePermissionServiceImpl.java
    │   │   │               │   │   ├── RoleServiceImpl.java
    │   │   │               │   │   ├── RoutineExerciseServiceImpl.java
    │   │   │               │   │   ├── RoutineServiceImpl.java
    │   │   │               │   │   ├── UserRoutineServiceImpl.java
    │   │   │               │   │   └── UserServiceImpl.java
    │   │   │               │   ├── IExerciseProgressService.java
    │   │   │               │   ├── IExerciseService.java
    │   │   │               │   ├── IPermissionService.java
    │   │   │               │   ├── IRolePermissionService.java
    │   │   │               │   ├── IRoleService.java
    │   │   │               │   ├── IRoutineExerciseService.java
    │   │   │               │   ├── IRoutineService.java
    │   │   │               │   ├── IUserRoutineService.java
    │   │   │               │   └── IUserService.java
    │   │   │               └── ServletInitializer.java
    │   │   └── resources
    │   │       ├── application.properties
    │   │       ├── data.sql
    │   │       ├── static
    │   │       │   ├── css
    │   │       │   │   ├── components
    │   │       │   │   │   ├── header_user_mngmt.css
    │   │       │   │   │   └── main.css
    │   │       │   │   └── users
    │   │       │   │       ├── forms.css
    │   │       │   │       ├── personas.css
    │   │       │   │       └── styles.css
    │   │       │   ├── images
    │   │       │   │   └── logo
    │   │       │   │   │   ├── icesifit_logo_blanco.svg
    │   │       │   │   │   ├── icesifit_logo_negro.svg
    │   │       │   │   │   └── icesifit_logo_verde.svg
    │   │       │   │   ├── login_1.png
    │   │       │   │   ├── login_2.png
    │   │       │   │   └── login_3.png
    │   │       │   ├── js
    │   │       │   │   └── users
    │   │       │   │       └── scripts.js
    │   │       │   └── main.css
    │   │       └── templates
    │   │           ├── admin
    │   │           │   └── headers
    │   │           │   │   └── header_user_mngmt.html
    │   │           │   ├── permissions
    │   │           │   │   ├── add.html
    │   │           │   │   ├── edit.html
    │   │           │   │   └── list.html
    │   │           │   ├── roles
    │   │           │   │   ├── add.html
    │   │           │   │   ├── edit.html
    │   │           │   │   └── list.html
    │   │           │   ├── users
    │   │           │   │   ├── add.html
    │   │           │   │   ├── edit.html
    │   │           │   │   └── list.html
    │   │           ├── login
    │   │           │   ├── login.html
    │   │           │   └── signup.html
    │   │           ├── trainer
    │   │           │   └── dashboard.html
    │   │           └── user
    │   │               └── dashboard.html
    │   └── test
    │       ├── java
    │       │   └── com
    │       │       └── example
    │       │           └── physical_activity_project
    │       │               ├── ExerciseProgressServiceImplTest.java
    │       │               ├── ExerciseServiceImplTest.java
    │       │               ├── PermissionServiceTest.java
    │       │               ├── RolePermissionServiceImplTest.java
    │       │               ├── RoleServiceTest.java
    │       │               ├── RoutineExerciseServiceImplTest.java
    │       │               ├── RoutineServiceImplTest.java
    │       │               ├── UserRoutineServiceTest.java
    │       │               └── UserServiceTest.java
    │       └── resources
    │           └── application-test.properties

### Requisitos previos
*   Java Development Kit (JDK) 17 o superior.
*   Apache Maven 3.6 o superior.
*   Una base de datos PostgreSQL en ejecución.

### Configuración
1) Clonar el repositorio (GitHub Classroom).
2) El proyecto está configurado para usar una **base de datos H2 en memoria**, por lo que no se necesita instalar ni configurar una base de datos externa.  
El archivo `src/main/resources/application.properties` ya contiene la configuración necesaria:
 ```
spring.application.name=physical_activity_project
server.servlet.context-path=/compu2-class

spring.datasource.url=jdbc:h2:mem:compu2-class
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.username=admin
spring.datasource.password=123456
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
jwt.secret=r3XuWWx2W/jMkJGWwrgJKv2XzhQDgiaTMSf5aQXtFrg=
jwt.expiration=86400000
 ```
4) Carga de esquema/datos iniciales `data.sql`, Spring Boot los ejecutará automáticamente al iniciar.
5) La aplicación estará disponible en:  
`http://localhost:8080/compu2-class`
La consola de H2 estará disponible en:  
`http://localhost:8080/compu2-class/h2-console`  
(JDBC URL: `jdbc:h2:mem:compu2-class`, usuario: `admin`, contraseña: `123456`).


### Ejecución
- Linux/macOS:

        mvn spring-boot:run

- Windows:
    
        ./mvnw clean spring-boot:run

### Despliegue
Se desplegó el proyecto en el equipo 105 del salon 104M. La dirección para acceder es:
`192.168.131.105:8080/icesiFit`


### Pruebas y cobertura
- Ejecutar pruebas:
    
        mvn test

- Reporte de cobertura con JaCoCo:
    
        # Abrir: target/site/jacoco/index.html

