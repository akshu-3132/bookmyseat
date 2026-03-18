# BookMySeat API - Quick Reference Guide

**Version:** 1.0.0 | **Last Updated:** March 18, 2026

---

## Base URL
```
http://localhost:8080
```

---

## All Endpoints at a Glance

### 1. General Endpoints

| # | Method | Endpoint | Purpose |
|---|--------|----------|---------|
| 1 | GET | `/` | Welcome message |
| 2 | GET | `/health` | Health status |

### 2. User Endpoints

| # | Method | Endpoint | Purpose | Request | Response |
|---|--------|----------|---------|---------|----------|
| 3 | POST | `/api/users` | Create user | `{ "email": "user@example.com" }` | User ID, Email |

### 3. City Endpoints

| # | Method | Endpoint | Purpose | Request | Response |
|---|--------|----------|---------|---------|----------|
| 4 | POST | `/api/cities` | Create city | `{ "name": "Mumbai" }` | City ID, Theatres[] |

### 4. Theatre Endpoints

| # | Method | Endpoint | Purpose | Request | Response |
|---|--------|----------|---------|---------|----------|
| 5 | POST | `/api/theatres/{cityId}` | Create theatre | `{ "name": "PVR", "address": "Fort" }` | Theatre ID, Auditoriums[] |
| 6 | POST | `/api/theatres/{theatreId}/auditoriums` | Add auditorium | `{ "name": "Screen 1", "capacity": 150 }` | Auditorium ID, Capacity |
| 7 | POST | `/api/theatres/{auditoriumId}/seats` | Add seats | `{ "VIP": 20, "PLATINUM": 30 }` | Seat count, Seat IDs[] |

### 5. Show Endpoints

| # | Method | Endpoint | Purpose | Request | Response |
|---|--------|----------|---------|---------|----------|
| 8 | POST | `/api/shows` | Create show | Movie, Show time, Prices | Show ID, Seats[] |

### 6. Ticket Endpoints

| # | Method | Endpoint | Purpose | Request | Response |
|---|--------|----------|---------|---------|----------|
| 9 | POST | `/api/tickets` | Book ticket | Show ID, Seat IDs, User ID | Ticket ID, Total Price |

---

## Common Request/Response Patterns

### Pattern 1: Simple Creation (User, City)
```
REQUEST:  { "field1": "value1", "field2": "value2" }
RESPONSE: { "object": { ... } }
STATUS:   200 OK
```

### Pattern 2: Nested Resource Creation (Theatre in City, Auditorium in Theatre)
```
REQUEST:  POST /api/parent/{parentId}/resource
          { "field1": "value1" }
RESPONSE: { "resource": { ... } }
STATUS:   201 Created
```

### Pattern 3: Complex Creation (Seats with Mapping)
```
REQUEST:  POST /api/theatres/{auditoriumId}/seats
          { "SeatType": count, ... }
RESPONSE: { "message": "...", "totalCount": N, "items": [...] }
STATUS:   201 Created
```

---

## Quick Postman Setup

### 1. Set Base URL Variable
In Postman, create a **Collection Variable**:
- **Variable:** `base_url`
- **Value:** `http://localhost:8080`

### 2. Use in Requests
```
{{base_url}}/api/users
{{base_url}}/api/cities
{{base_url}}/api/theatres/1
```

---

## Typical Data Flow

```
1. Create User
   ↓
2. Create City
   ↓
3. Create Theatre (in City)
   ↓
4. Add Auditorium (to Theatre)
   ↓
5. Add Seats (to Auditorium)
   ↓
6. Create Movie (DB seeding)
   ↓
7. Create Show (in Auditorium)
   ↓
8. Book Ticket (for Show)
```

---

## HTTP Status Codes

| Code | Status | Used For |
|------|--------|----------|
| 200 | OK | Successful operation |
| 201 | Created | New resource created |
| 400 | Bad Request | Invalid input data |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate or unavailable |
| 500 | Server Error | Server error |

---

## Seat Type Values
```
VIP       (Premium)
PLATINUM  (High Quality)
GOLD      (Standard)
SILVER    (Budget)
```

## Language Values
```
HINDI, ENGLISH, PUNJABI, TELUGU, TAMIL
```

## Ticket Status Values
```
CONFIRMED, CANCELLED, EXPIRED
```

---

## cURL Command Templates

### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com"}'
```

### Create City
```bash
curl -X POST http://localhost:8080/api/cities \
  -H "Content-Type: application/json" \
  -d '{"name":"City Name"}'
```

### Create Theatre
```bash
curl -X POST http://localhost:8080/api/theatres/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Theatre Name","address":"Address"}'
```

### Add Auditorium
```bash
curl -X POST http://localhost:8080/api/theatres/1/auditoriums \
  -H "Content-Type: application/json" \
  -d '{"name":"Screen 1","capacity":150}'
```

### Add Seats
```bash
curl -X POST http://localhost:8080/api/theatres/1/seats \
  -H "Content-Type: application/json" \
  -d '{"VIP":20,"PLATINUM":30,"GOLD":50,"SILVER":50}'
```

### Create Show
```bash
curl -X POST http://localhost:8080/api/shows \
  -H "Content-Type: application/json" \
  -d '{
    "movieId":1,
    "startTime":"2026-03-20T18:30:00",
    "endTime":"2026-03-20T21:30:00",
    "auditoriumId":1,
    "seatPrices":{"VIP":300,"PLATINUM":250,"GOLD":200,"SILVER":150},
    "language":"HINDI"
  }'
```

### Book Ticket
```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{"showId":1,"showSeatIds":[1,2,3],"userId":1}'
```

---

## Response Examples

### User Response
```json
{
  "user": {
    "id": 1,
    "email": "user@example.com"
  }
}
```

### City Response
```json
{
  "city": {
    "id": 1,
    "name": "Mumbai",
    "theatres": []
  }
}
```

### Theatre Response
```json
{
  "theatre": {
    "id": 1,
    "name": "PVR Cinemas",
    "address": "Fort, Mumbai",
    "auditoriums": [],
    "upcomingShows": []
  }
}
```

### Auditorium Response
```json
{
  "auditorium": {
    "id": 1,
    "name": "Screen 1",
    "capacity": 150,
    "seats": [],
    "theatre": { "id": 1, "name": "PVR Cinemas" }
  }
}
```

### Seats Response
```json
{
  "message": "Seats added successfully",
  "totalSeatsCreated": 150,
  "seats": [
    {"id": 1, "seatNumber": "VIP1", "seatType": "VIP"},
    {"id": 2, "seatNumber": "VIP2", "seatType": "VIP"}
  ]
}
```

### Show Response
```json
{
  "show": {
    "id": 1,
    "movie": {"id": 1, "name": "Inception", "rating": 8.8},
    "startTime": "2026-03-20T18:30:00",
    "endTime": "2026-03-20T21:30:00",
    "language": "HINDI",
    "auditorium": {"id": 1, "name": "Screen 1"}
  }
}
```

### Ticket Response
```json
{
  "ticket": {
    "id": 1,
    "bookedBy": {"id": 1, "email": "user@example.com"},
    "show": {"id": 1, "movie": {"name": "Inception"}},
    "showSeats": [
      {"id": 1, "seatNumber": "VIP1", "seatType": "VIP"}
    ],
    "totalAmount": 300,
    "ticketStatus": "CONFIRMED",
    "timeOfBooking": "2026-03-18T10:30:45"
  }
}
```

---

## Key Enums

### SeatType
- VIP
- PLATINUM
- GOLD
- SILVER

### Language
- HINDI
- ENGLISH
- PUNJABI
- TELUGU
- TAMIL

### TicketStatus
- CONFIRMED
- CANCELLED
- EXPIRED

---

## Error Examples

### 404 Not Found
```json
{
  "timestamp": "2026-03-18T10:30:45Z",
  "status": 404,
  "error": "Not Found",
  "message": "City not found with ID: 999"
}
```

### 400 Bad Request
```json
{
  "timestamp": "2026-03-18T10:30:45Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request body"
}
```

### 409 Conflict
```json
{
  "timestamp": "2026-03-18T10:30:45Z",
  "status": 409,
  "error": "Conflict",
  "message": "Seats already booked"
}
```

---

## Database Relationships

```
City 1:M Theatre
Theatre 1:M Auditorium
Auditorium 1:M Seat
Theatre 1:M Show
Auditorium 1:M Show
Movie 1:M Show
Show 1:M ShowSeat
Show 1:M Ticket
User 1:M Ticket
ShowSeat M:M Ticket
```

---

## Notes
- All timestamps in **ISO 8601** format: `YYYY-MM-DDTHH:mm:ss`
- All endpoints return **JSON**
- All POST endpoints create new resources (no updates)
- Database: **PostgreSQL**
- Framework: **Spring Boot 3.3.4**

---

**Quick Reference Version 1.0.0 | Last Updated: March 18, 2026**
