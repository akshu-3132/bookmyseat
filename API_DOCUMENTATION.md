# BookMySeat API - Comprehensive Documentation

**Version:** 1.0.0  
**Last Updated:** March 18, 2026  
**Base URL:** `http://localhost:8080`  
**Content-Type:** `application/json`

---

## Table of Contents

1. [General/Health Endpoints](#general-health-endpoints)
2. [User Management](#user-management)
3. [City Management](#city-management)
4. [Theatre Management](#theatre-management)
5. [Show Management](#show-management)
6. [Ticket Booking](#ticket-booking)
7. [Data Models & Enums](#data-models--enums)
8. [Error Handling](#error-handling)
9. [Testing Workflow](#testing-workflow)

---

## General/Health Endpoints

### 1. Welcome / Health Check

#### GET `/`

**Description:** Get application welcome message and version information.

**Method:** `GET`  
**Path:** `/`  
**Parameters:** None

**Response (200 OK):**
```json
{
  "message": "Welcome to BookMySeat Application",
  "status": "application is running",
  "version": "1.0.0"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/
```

**Postman Example:**
- Method: GET
- URL: `http://localhost:8080/`
- Headers: None
- Body: None

---

### 2. Health Status

#### GET `/health`

**Description:** Check application health status and service availability.

**Method:** `GET`  
**Path:** `/health`  
**Parameters:** None

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "BookMySeat"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/health
```

**Postman Example:**
- Method: GET
- URL: `http://localhost:8080/health`
- Headers: None
- Body: None

---

## User Management

### 1. Create User

#### POST `/api/users`

**Description:** Create a new user account for ticket booking.

**Method:** `POST`  
**Path:** `/api/users`  
**Base Path Used:** `/api/users`

**Request Parameters:**
- **Body Type:** JSON Object (`CreateUserRequestDto`)
- **Required Fields:**
  - `email` (string) - User's email address, must be unique

**Request Body Example:**
```json
{
  "email": "john.doe@example.com"
}
```

**Response (200 OK):**
```json
{
  "user": {
    "id": 1,
    "email": "john.doe@example.com"
  }
}
```

**Response Fields:**
- `user` (object) - Created user object
  - `id` (Long) - Unique user identifier
  - `email` (String) - User's email address

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Invalid email format | Bad Request |
| 409 | Email already exists | Conflict |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com"
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/users`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "email": "john.doe@example.com"
}
```

---

## City Management

### 1. Create City

#### POST `/api/cities`

**Description:** Create a new city for theatre operations.

**Method:** `POST`  
**Path:** `/api/cities`  
**Base Path Used:** `/api/cities`

**Request Parameters:**
- **Body Type:** JSON Object (`CreateCityRequestDto`)
- **Required Fields:**
  - `name` (string) - Name of the city

**Request Body Example:**
```json
{
  "name": "Mumbai"
}
```

**Response (200 OK):**
```json
{
  "city": {
    "id": 1,
    "name": "Mumbai",
    "theatres": []
  }
}
```

**Response Fields:**
- `city` (object) - Created city object
  - `id` (Long) - Unique city identifier
  - `name` (String) - City name
  - `theatres` (List<Theatre>) - List of theatres in the city (initially empty)

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Invalid city name (empty) | Bad Request |
| 409 | City already exists | Conflict |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/cities \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mumbai"
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/cities`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "name": "Mumbai"
}
```

---

## Theatre Management

### 1. Create Theatre

#### POST `/api/theatres/{cityId}`

**Description:** Create a new theatre in a specific city.

**Method:** `POST`  
**Path:** `/api/theatres/{cityId}`  
**Base Path Used:** `/api/theatres`

**Path Parameters:**
- `cityId` (Long, required) - ID of the city where theatre will be created

**Request Parameters:**
- **Body Type:** JSON Object (`CreateTheatreRequestDto`)
- **Required Fields:**
  - `name` (string) - Theatre name
  - `address` (string) - Theatre address/location

**Request Body Example:**
```json
{
  "name": "PVR Cinemas",
  "address": "Fort, Mumbai"
}
```

**Response (201 Created):**
```json
{
  "theatre": {
    "id": 1,
    "name": "PVR Cinemas",
    "address": "Fort, Mumbai",
    "auditoriums": []
  }
}
```

**Response Fields:**
- `theatre` (object) - Created theatre object
  - `id` (Long) - Unique theatre identifier
  - `name` (String) - Theatre name
  - `address` (String) - Theatre address
  - `auditoriums` (List<Auditorium>) - List of auditoriums (initially empty)

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Missing required fields | Bad Request |
| 404 | City not found (invalid cityId) | Not Found |
| 409 | Theatre name already exists in city | Conflict |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/theatres/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "PVR Cinemas",
    "address": "Fort, Mumbai"
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/theatres/1`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "name": "PVR Cinemas",
  "address": "Fort, Mumbai"
}
```

---

### 2. Add Auditorium to Theatre

#### POST `/api/theatres/{theatreId}/auditoriums`

**Description:** Add a new auditorium (screen) to a theatre.

**Method:** `POST`  
**Path:** `/api/theatres/{theatreId}/auditoriums`  
**Base Path Used:** `/api/theatres`

**Path Parameters:**
- `theatreId` (Long, required) - ID of the theatre to add auditorium to

**Request Parameters:**
- **Body Type:** JSON Object (`CreateAuditoriumRequestDto`)
- **Required Fields:**
  - `name` (string) - Auditorium/screen name
  - `capacity` (integer) - Seating capacity

**Request Body Example:**
```json
{
  "name": "Screen 1",
  "capacity": 150
}
```

**Response (201 Created):**
```json
{
  "auditorium": {
    "id": 1,
    "name": "Screen 1",
    "capacity": 150,
    "seats": [],
    "auditoriumFeatures": [],
    "theatre": {
      "id": 1,
      "name": "PVR Cinemas",
      "address": "Fort, Mumbai"
    }
  }
}
```

**Response Fields:**
- `auditorium` (object) - Created auditorium object
  - `id` (Long) - Unique auditorium identifier
  - `name` (String) - Auditorium name
  - `capacity` (int) - Seating capacity
  - `seats` (List<Seat>) - List of seats (initially empty, added separately)
  - `auditoriumFeatures` (List<AuditoriumFeature>) - Features (DOLBY, IMAX, etc.)
  - `theatre` (object) - Associated theatre object

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Invalid capacity (≤ 0) | Bad Request |
| 404 | Theatre not found (invalid theatreId) | Not Found |
| 409 | Duplicateauditorium name in theatre | Conflict |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/theatres/1/auditoriums \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Screen 1",
    "capacity": 150
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/theatres/1/auditoriums`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "name": "Screen 1",
  "capacity": 150
}
```

---

### 3. Add Seats to Auditorium

#### POST `/api/theatres/{auditoriumId}/seats`

**Description:** Add seats to an auditorium with specified seat types and quantities.

**Method:** `POST`  
**Path:** `/api/theatres/{auditoriumId}/seats`  
**Base Path Used:** `/api/theatres`

**Path Parameters:**
- `auditoriumId` (Long, required) - ID of the auditorium to add seats to

**Request Parameters:**
- **Body Type:** JSON Object (Map<SeatType, Integer>)
- **Format:** Key-value pairs where key is seat type and value is quantity
- **Valid Seat Types:** `VIP`, `PLATINUM`, `GOLD`, `SILVER`

**Request Body Example:**
```json
{
  "VIP": 20,
  "PLATINUM": 30,
  "GOLD": 50,
  "SILVER": 50
}
```

**Response (201 Created):**
```json
{
  "message": "Seats added successfully",
  "totalSeatsCreated": 150,
  "seats": [
    {
      "id": 1,
      "seatNumber": "VIP1",
      "seatType": "VIP"
    },
    {
      "id": 2,
      "seatNumber": "VIP2",
      "seatType": "VIP"
    },
    {
      "id": 3,
      "seatNumber": "PLATINUM1",
      "seatType": "PLATINUM"
    }
  ]
}
```

**Response Fields:**
- `message` (String) - Success message
- `totalSeatsCreated` (int) - Total number of seats created
- `seats` (List<Seat>) - Array of created seat objects
  - `id` (Long) - Unique seat identifier
  - `seatNumber` (String) - Seat number/identifier (e.g., "VIP1")
  - `seatType` (String) - Type of seat (VIP, PLATINUM, GOLD, SILVER)

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Invalid seat type or count | Bad Request |
| 404 | Auditorium not found (invalid auditoriumId) | Not Found |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/theatres/1/seats \
  -H "Content-Type: application/json" \
  -d '{
    "VIP": 20,
    "PLATINUM": 30,
    "GOLD": 50,
    "SILVER": 50
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/theatres/1/seats`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "VIP": 20,
  "PLATINUM": 30,
  "GOLD": 50,
  "SILVER": 50
}
```

---

## Show Management

### 1. Create Show

#### POST `/api/shows`

**Description:** Create a new movie show in an auditorium with seat pricing.

**Method:** `POST`  
**Path:** `/api/shows`  
**Base Path Used:** `/api/shows`

**Request Parameters:**
- **Body Type:** JSON Object (`CreateShowRequestDto`)
- **Required Fields:**
  - `movieId` (Long) - ID of the movie to be shown
  - `startTime` (Date) - Show start time (ISO 8601 format: `YYYY-MM-DDTHH:mm:ss`)
  - `endTime` (Date) - Show end time
  - `auditoriumId` (Long) - ID of the auditorium where show will run
  - `seatPrices` (Map<SeatType, Integer>) - Price mapping for each seat type
  - `language` (String) - Language of the show (HINDI, ENGLISH, PUNJABI, TELUGU, TAMIL)

**Request Body Example:**
```json
{
  "movieId": 1,
  "startTime": "2026-03-20T18:30:00",
  "endTime": "2026-03-20T21:30:00",
  "auditoriumId": 1,
  "seatPrices": {
    "VIP": 300,
    "PLATINUM": 250,
    "GOLD": 200,
    "SILVER": 150
  },
  "language": "HINDI"
}
```

**Response (200 OK):**
```json
{
  "show": {
    "id": 1,
    "movie": {
      "id": 1,
      "name": "Inception",
      "length": 148,
      "rating": 8.8,
      "languages": ["HINDI", "ENGLISH"],
      "actors": [],
      "movieFeatures": []
    },
    "startTime": "2026-03-20T18:30:00",
    "endTime": "2026-03-20T21:30:00",
    "auditorium": {
      "id": 1,
      "name": "Screen 1",
      "capacity": 150,
      "theatre": {
        "id": 1,
        "name": "PVR Cinemas"
      }
    },
    "language": "HINDI",
    "showSeats": [],
    "showSeatTypes": [],
    "showFeatures": []
  }
}
```

**Response Fields:**
- `show` (object) - Created show object
  - `id` (Long) - Unique show identifier
  - `movie` (object) - Movie information
  - `startTime` (Date) - Show start time
  - `endTime` (Date) - Show end time
  - `auditorium` (object) - Auditorium information
  - `language` (String) - Language of the show
  - `showSeats` (List<ShowSeat>) - Show seat availability tracking
  - `showFeatures` (List<ShowFeature>) - Special features (SUBTITLE, IMAX, etc.)

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Invalid date/time format or missing fields | Bad Request |
| 404 | Movie or Auditorium not found | Not Found |
| 409 | Show time conflict with existing show | Conflict |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/shows \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": 1,
    "startTime": "2026-03-20T18:30:00",
    "endTime": "2026-03-20T21:30:00",
    "auditoriumId": 1,
    "seatPrices": {
      "VIP": 300,
      "PLATINUM": 250,
      "GOLD": 200,
      "SILVER": 150
    },
    "language": "HINDI"
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/shows`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "movieId": 1,
  "startTime": "2026-03-20T18:30:00",
  "endTime": "2026-03-20T21:30:00",
  "auditoriumId": 1,
  "seatPrices": {
    "VIP": 300,
    "PLATINUM": 250,
    "GOLD": 200,
    "SILVER": 150
  },
  "language": "HINDI"
}
```

---

## Ticket Booking

### 1. Book Ticket

#### POST `/api/tickets`

**Description:** Book a ticket for a show by selecting available seats.

**Method:** `POST`  
**Path:** `/api/tickets`  
**Base Path Used:** `/api/tickets`

**Request Parameters:**
- **Body Type:** JSON Object (`CreateTicketRequestDto`)
- **Required Fields:**
  - `showId` (Long) - ID of the show to book ticket for
  - `showSeatIds` (List<Long>) - Array of show seat IDs to be booked
  - `userId` (Long) - ID of the user booking the ticket

**Request Body Example:**
```json
{
  "showId": 1,
  "showSeatIds": [1, 2, 3],
  "userId": 1
}
```

**Response (200 OK):**
```json
{
  "ticket": {
    "id": 1,
    "bookedBy": {
      "id": 1,
      "email": "john.doe@example.com"
    },
    "show": {
      "id": 1,
      "movie": {
        "name": "Inception"
      },
      "startTime": "2026-03-20T18:30:00",
      "auditorium": {
        "name": "Screen 1"
      }
    },
    "showSeats": [
      {
        "id": 1,
        "seatNumber": "VIP1",
        "seatType": "VIP"
      },
      {
        "id": 2,
        "seatNumber": "VIP2",
        "seatType": "VIP"
      },
      {
        "id": 3,
        "seatNumber": "PLATINUM1",
        "seatType": "PLATINUM"
      }
    ],
    "totalAmount": 850,
    "ticketStatus": "CONFIRMED",
    "timeOfBooking": "2026-03-18T10:30:45"
  }
}
```

**Response Fields:**
- `ticket` (object) - Booked ticket object
  - `id` (Long) - Unique ticket identifier
  - `bookedBy` (object) - User information
  - `show` (object) - Show information
  - `showSeats` (List<ShowSeat>) - Booked seats
  - `totalAmount` (double) - Total booking amount
  - `ticketStatus` (String) - Status (CONFIRMED, CANCELLED, etc.)
  - `timeOfBooking` (Date) - Booking timestamp

**Error Responses:**
| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Sea seats already booked or invalid seat IDs | Bad Request |
| 404 | Show or User not found | Not Found |
| 409 | Seat unavailable (ShowSeatNotAvailableException) | Conflict |
| 500 | Server error | Internal Server Error |

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "showId": 1,
    "showSeatIds": [1, 2, 3],
    "userId": 1
  }'
```

**Postman Example:**
- Method: POST
- URL: `http://localhost:8080/api/tickets`
- Headers:
  - Content-Type: application/json
- Body (raw JSON):
```json
{
  "showId": 1,
  "showSeatIds": [1, 2, 3],
  "userId": 1
}
```

---

## Data Models & Enums

### SeatType Enum

Available seat types in theatres:

```java
VIP        // Premium seats with best view
PLATINUM   // High-quality seats
GOLD       // Standard quality seats
SILVER     // Budget seats
```

### Language Enum

Supported movie languages:

```java
HINDI      // Hindi language
ENGLISH    // English language
PUNJABI    // Punjabi language
TELUGU     // Telugu language
TAMIL      // Tamil language
```

### TicketStatus Enum

Possible ticket statuses:

```java
CONFIRMED  // Ticket successfully booked
CANCELLED  // Ticket cancelled by user
EXPIRED    // Ticket expired (show date passed)
```

### AuditoriumFeature Enum

Optional auditorium features:

```java
DOLBY      // Dolby Digital audio
IMAX       // IMAX screen format
3D_READY   // 3D projection capability
AC         // Air-conditioned
```

### MovieFeature Enum

Optional movie features:

```java
SUBTITLE   // Movie has subtitles
DUBBED     // Dubbed version
```

### ShowFeature Enum

Optional show features:

```java
EARLY_BIRD // Early bird discount
FESTIVAL   // Festival show
PRIVATE    // Private screening
```

---

## Error Handling

### Standard Error Response Format

All errors follow this standard format:

```json
{
  "timestamp": "2026-03-18T10:30:45Z",
  "status": 404,
  "error": "Not Found",
  "message": "City not found with ID: 999",
  "path": "/api/theatres/999"
}
```

### HTTP Status Codes

| Code | Meaning | Use Case |
|------|---------|----------|
| 200 | OK | Request successful (GET, POST for non-creation) |
| 201 | Created | Resource successfully created (POST) |
| 400 | Bad Request | Invalid request body or parameters |
| 404 | Not Found | Resource not found (city, theatre, show, etc.) |
| 409 | Conflict | Resource already exists or conflict (duplicate) |
| 500 | Server Error | Internal server error or exception |

### Common Exceptions

| Exception | HTTP Status | Scenario |
|-----------|------------|----------|
| `CityNotFoundException` | 404 | City with given ID doesn't exist |
| `ShowSeatNotAvailableException` | 409 | Selected seat(s) already booked |
| `DataIntegrityViolationException` | 400 | Duplicate entry or constraint violation |
| `EntityNotFoundException` | 404 | Theatre, Show, or User not found |

---

## Testing Workflow

### Complete End-to-End Flow

This workflow demonstrates the complete booking process:

#### Step 1: Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email": "customer@example.com"}'
```
**Response:** User ID (e.g., 1)

---

#### Step 2: Create City
```bash
curl -X POST http://localhost:8080/api/cities \
  -H "Content-Type: application/json" \
  -d '{"name": "Mumbai"}'
```
**Response:** City ID (e.g., 1)

---

#### Step 3: Create Theatre
```bash
curl -X POST http://localhost:8080/api/theatres/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "PVR Cinemas", "address": "Fort, Mumbai"}'
```
**Response:** Theatre ID (e.g., 1)

---

#### Step 4: Add Auditorium
```bash
curl -X POST http://localhost:8080/api/theatres/1/auditoriums \
  -H "Content-Type: application/json" \
  -d '{"name": "Screen 1", "capacity": 150}'
```
**Response:** Auditorium ID (e.g., 1)

---

#### Step 5: Add Seats
```bash
curl -X POST http://localhost:8080/api/theatres/1/seats \
  -H "Content-Type: application/json" \
  -d '{"VIP": 20, "PLATINUM": 30, "GOLD": 50, "SILVER": 50}'
```
**Response:** Seat IDs (e.g., 1-150)

---

#### Step 6: Create Movie
> **Note:** This endpoint is not currently exposed via REST API. Movies should be created via database seeding or separate admin API.

---

#### Step 7: Create Show
```bash
curl -X POST http://localhost:8080/api/shows \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": 1,
    "startTime": "2026-03-20T18:30:00",
    "endTime": "2026-03-20T21:30:00",
    "auditoriumId": 1,
    "seatPrices": {"VIP": 300, "PLATINUM": 250, "GOLD": 200, "SILVER": 150},
    "language": "HINDI"
  }'
```
**Response:** Show ID (e.g., 1)

---

#### Step 8: Book Ticket
```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "showId": 1,
    "showSeatIds": [1, 2, 3],
    "userId": 1
  }'
```
**Response:** Ticket ID and booking confirmation

---

#### Step 9: Verify Booking (Optional)
> **Note:** GET endpoint for retrieving ticket details is not currently exposed. Consider adding GET endpoints for:
> - `GET /api/tickets/{ticketId}` - Get ticket details
> - `GET /api/shows/{showId}` - Get show details
> - `GET /api/cities/{cityId}` - Get city details

---

## API Summary

### Endpoint Overview

| Module | Method | Path | Status | Notes |
|--------|--------|------|--------|-------|
| **General** | GET | `/` | ✅ Working | Welcome message |
| **General** | GET | `/health` | ✅ Working | Health check |
| **User** | POST | `/api/users` | ✅ Working | Create user |
| **City** | POST | `/api/cities` | ✅ Working | Create city |
| **Theatre** | POST | `/api/theatres/{cityId}` | ✅ Working | Create theatre |
| **Theatre** | POST | `/api/theatres/{theatreId}/auditoriums` | ✅ Working | Add auditorium |
| **Theatre** | POST | `/api/theatres/{auditoriumId}/seats` | ✅ Working | Add seats |
| **Show** | POST | `/api/shows` | ✅ Working | Create show |
| **Ticket** | POST | `/api/tickets` | ✅ Working | Book ticket |

**Total Endpoints:** 10 (2 GET, 8 POST)

---

### Suggested Improvements & Missing Endpoints

For a complete production-ready API, consider implementing:

1. **GET Endpoints for Data Retrieval**
   - `GET /api/cities` - List all cities
   - `GET /api/cities/{cityId}` - Get city details
   - `GET /api/theatres/{theatreId}` - Get theatre details
   - `GET /api/shows/{showId}` - Get show details with available seats
   - `GET /api/tickets/{ticketId}` - Get ticket details

2. **PUT/PATCH Endpoints for Updates**
   - `PUT /api/shows/{showId}` - Update show details
   - `PATCH /api/tickets/{ticketId}/status` - Update ticket status

3. **DELETE Endpoints**
   - `DELETE /api/tickets/{ticketId}` - Cancel ticket

4. **Filtering & Search**
   - `GET /api/shows?city={cityId}&date={date}&movie={movieId}` - Search shows
   - `GET /api/shows/{showId}/available-seats` - Get available seats for booking

5. **Admin Endpoints**
   - `POST /api/movies` - Create movie (currently requires DB seeding)
   - `GET /api/admin/reports` - Generate reports

6. **Authentication & Authorization**
   - `POST /api/auth/login` - User authentication
   - `POST /api/auth/logout` - User logout
   - Role-based access control (Admin, User)

---

## Notes

- **Base URL** format can be modified in `application.properties`
- All endpoints return data in **JSON** format
- Request/Response times typically **< 250ms** for standard operations
- Database: **PostgreSQL 12+** (localhost:5432)
- The application is **stateless** and designed for **horizontal scaling**
- **Seat pricing** is set per show based on seat type
- **Payment processing** is not yet implemented (placeholder in Ticket model)

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-03-18 | Initial API documentation with all 10 endpoints, Theatre API fixes applied |

---

**Generated:** March 18, 2026  
**Author:** API Documentation System  
**Framework:** Spring Boot 3.3.4 with Spring Web  
**Java Version:** Java 21 (OpenJDK Temurin)
