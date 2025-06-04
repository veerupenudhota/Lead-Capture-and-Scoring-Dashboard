# Lead Capture & Scoring Dashboard

A comprehensive lead management system built with Spring Boot, React, and AI-powered scoring capabilities to help businesses effectively capture, track, and evaluate potential leads.

## 🚀 Features

- **Lead Capture Form**: Dynamic form to collect lead information
- **Dashboard Interface**: Modern React-based dashboard for lead visualization
- **AI-Powered Scoring**: Automated lead scoring using AI algorithms
- **Real-time Updates**: Instant lead status and score updates
- **Data Analytics**: Comprehensive lead analytics and reporting
- **User Authentication**: Secure login and role-based access control
- **RESTful API**: Well-documented API endpoints for lead management

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.x
- **Frontend**: React 18.x with TypeScript
- **Database**: MySQL 8.x
- **Authentication**: JWT (JSON Web Tokens)
- **API Documentation**: Swagger/OpenAPI
- **Build Tools**: Maven (Backend), npm (Frontend)

## 📋 Prerequisites

- Java Development Kit (JDK) 17 or higher
- Node.js 16.x or higher
- MySQL 8.x
- Maven 3.x
- Git

## 🔧 Setup & Installation

### Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Lead-Capture-Scoring-Dashboard.git
   cd Lead-Capture-Scoring-Dashboard
   ```

2. Configure MySQL:
   - Create a new database named `lead_dashboard`
   - Update `application.properties` with your MySQL credentials

3. Build and run the Spring Boot application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The backend server will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```
   The frontend application will be available at `http://localhost:3000`

## 🚦 API Endpoints

- `POST /api/leads` - Create a new lead
- `GET /api/leads` - Retrieve all leads
- `GET /api/leads/{id}` - Get lead by ID
- `PUT /api/leads/{id}` - Update lead information
- `GET /api/leads/score/{id}` - Get lead score
- `POST /api/auth/login` - User authentication

## 🔐 Environment Variables

Create a `.env` file in the root directory with the following variables:

```properties
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/lead_dashboard
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
AI_API_KEY=your_ai_api_key
```

## 📊 Database Schema

The application uses the following core tables:
- `leads` - Store lead information
- `users` - User authentication data
- `scores` - Lead scoring history
- `activities` - Lead interaction tracking

## 🏃‍♂️ Running the Application

### Development Mode

1. Start the MySQL server
2. Run the Spring Boot backend:
   ```bash
   mvn spring-boot:run
   ```
3. Run the React frontend:
   ```bash
   cd frontend
   npm start
   ```

### Production Mode

1. Build the frontend:
   ```bash
   cd frontend
   npm run build
   ```

2. Build and run the Spring Boot application:
   ```bash
   mvn clean package
   java -jar target/lead-dashboard-0.0.1-SNAPSHOT.jar
   ```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Veera - *Initial work* - [YourGithub](https://github.com/veerupenudhota)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React community for the frontend tools
- All contributors who have helped with the project
