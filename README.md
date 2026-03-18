# BookMySeat

A scalable, event-driven theatre ticket booking system built with Spring Boot, designed to handle complex seat allocation, concurrent booking requests, and payment processing with database-level consistency guarantees.

## Overview

BookMySeat solves the **distributed double-booking problem** in ticketing systems. When multiple users attempt to book the same seat simultaneously, traditional approaches risk selling the same seat twice. This system implements database-level constraints and optimistic booking logic to prevent overselling while maintaining acceptable performance under concurrent load.

The system is designed for **high-frequency concurrent requests** typical of entertainment ticketing (e.g., popular movie shows opening for booking). Unlike naive implementations, seat availability is enforced at the database constraint level, not just application logic.

## Features

- **User Management**: Register and manage customer accounts
- **Multi-City Theatre Networks**: Support multiple cities, theatres, and screens per theatre
- **Smart Seat Allocation**: Allocate seats by type (VIP, Platinum, Gold, Silver) with capacity constraints
- **Show Management**: Create shows with specific start/end times, languages, and dynamic pricing per seat type
- **Concurrent Booking**: Handle simultaneous booking requests with database-level conflict detection
- **Payment Integration Ready**: Payment status tracking for future payment gateway integration
- **Comprehensive API**: RESTful endpoints for all operations with consistent request/response patterns
- **Audit Trail**: Track booking times, payment timestamps, and ticket status history

## System Design & Architecture

### Layered Architecture
```
Controllers (REST API) 
    ↓
DTOs (Data Transfer Objects - Request/Response)
    ↓
Service Layer (Business Logic)
    ↓
Repositories (Data Access - Spring Data JPA)
    ↓
Domain Models (JPA Entities)
    ↓
PostgreSQL Database
```

### Domain Model Relationships
```
City 1:M Theatre
Theatre 1:M Auditorium
Auditorium 1:M Seat
Show 1:M ShowSeat (Seat + State binding)
Auditorium 1:M Show (Temporal relationship)
Movie 1:M Show
Show 1:M Ticket
User 1:M Ticket
```

### Key Design Patterns

**1. State Management Pattern (ShowSeat)**
Instead of modifying `Seat` directly, we maintain a separate `ShowSeat` entity that tracks seat state **per show**. This allows the same physical seat to have different states across different show timings.

**Example:** Seat #A1 can be AVAILABLE in the 10:00 AM show but BOOKED in the 3:00 PM show.

**2. Feature Tracking (Composition)**
Auditoriums, Movies, and Shows have separate feature tables (`AuditoriumFeature`, `MovieFeature`, `ShowFeature`) to track attributes like "IMAX", "Dolby Atmos", "4K", etc. This avoids hardcoding and allows dynamic feature addition.

**3. DTOs as API Contracts**
Request/Response DTOs are strictly separated from domain models. This decouples API contracts from database schema evolution, allowing backward compatibility even if internal models change.

## Concurrency & Performance

### Concurrency Handling (Current)

**Database-Level Constraint Protection:**
- PostgreSQL `UNIQUE` constraints on (Show, Seat) pairs ensure a seat cannot be booked twice
- Foreign keys enforce referential integrity across entities
- Transaction isolation at `READ_COMMITTED` level (PostgreSQL default)

**Application-Level Detection:**
- Services attempt ticket creation and catch constraint violations
- Failed bookings trigger `ShowSeatNotAvailableException` with clear feedback to client
- Prevents duplicate bookings from being processed

**Example Scenario:**
```
User A and User B both try to book Seat #A1 for Show #5 at T=0

User A: INSERT ShowSeat (Show=5, Seat=1, State=BOOKED)  ✓ Succeeds
User B: INSERT ShowSeat (Show=5, Seat=1, State=BOOKED)  ✗ UNIQUE constraint violation

User B receives: HTTP 409 Conflict with ShowSeatNotAvailableException message
```

### Performance Characteristics

**Current Implementation:**
- **Seat Lookup**: O(1) direct query by ShowSeat ID
- **Show Availability Check**: O(n) scan of all seats for a show (acceptable for typical auditorium sizes: 100-300 seats)
- **Booking Creation**: O(1) insert after availability check
- **Database Connections**: HikariCP connection pooling (default 10 connections)

**Bottlenecks & Scaling Points:**
1. **Availability page rendering** - For shows with 1000+ seats, query could be slow
2. **Concurrent booking requests** - Limited by database connection pool and write throughput
3. **No caching** - Every API request hits the database

## Trade-offs & Design Decisions

### Why Spring Boot Monolith (Not Microservices)?

**Decision:** Single monolithic Spring Boot application instead of services split by domain

**Rationale:**
- **Simplicity**: Ticket booking requires atomic transactions across User → Show → Seat → Ticket entities. Distributed transactions add complexity and latency
- **Deployment**: Single deployable unit reduces operational overhead (easier scaling, monitoring)
- **Development Speed**: Moving fast in MVP phase; architecture can evolve as complexity grows
- **Strong Consistency**: Database transactions provide guaranteed consistency; microservices introduce eventual consistency challenges

**When to Migrate:**
- Payment processing becomes bottleneck (move to separate service)
- Marketing/analytics need independent scaling
- Theatre chains demand isolated deployments per region

### Enum-Based Seat Types (Not Table-Driven)

**Decision:** `SeatType` (VIP, PLATINUM, GOLD, SILVER) as Java enum, not database table

**Advantages:**
- Seat type is rarely added/removed; treating as domain constant is appropriate
- Type safety in Java code; compiler catches invalid types
- No runtime lookup overhead

**Trade-off:**
- Adding new seat type requires code change + redeployment
- Cannot change seat type mappings without code restart

**When to Reconsider:**
- If theatre chains need custom seat hierarchies (unlikely; pricing strategy drives types instead)

### ShowSeat Binding (Denormalization)

**Decision:** Creating explicit `ShowSeat` entity instead of computing availability on demand

**Rationale:**
- Availability state changes with every booking; queries would need aggregation
- `ShowSeat` table allows O(1) state lookup and O(n) availability scan
- Denormalization trade-off: slightly more storage for much faster queries

**Cost:**
- Additional table with Show × Seat rows (typical: 100-300 seats × shows = 10K-100K rows)
- Worth it for frequently accessed data

### No Paid/Free Tier Separation

**Decision:** Single theatre/show model; no multi-tenancy or subscription tiers

**Current Scope:** Works for small-to-medium chains
**Limitation:** Doesn't scale for SaaS platforms (WhatsTheShow, TimeOut, etc.)

**If SaaS is goal:** Implement tenant isolation at schema level (row-level security) or database level

## Tech Stack

| Component | Technology | Version | Rationale |
|-----------|-----------|---------|-----------|
| **Framework** | Spring Boot | 3.3.4 | Latest stable; native compilation support (GraalVM) for future optimization |
| **Language** | Java | 21 | Modern Java features (records, pattern matching available for future refactoring) |
| **Database** | PostgreSQL | 13+ | Advanced constraints, JSONB support, excellent Spring integration |
| **ORM** | Spring Data JPA | 3.1.5 | Zero-boilerplate repository pattern; native queries for complex queries |
| **Build Tool** | Maven | 3.9+ | Stable, industry standard; good CI/CD integration |
| **API Documentation** | Manual (Markdown) + Postman | - | Lightweight; consider adding Springdoc-OpenAPI (Swagger) for auto-generation |

## How to Run

### Prerequisites
- Java 21+ (`java -version`)
- PostgreSQL 13+ (`psql --version`)
- Maven 3.9+ (`mvn -v`)

### Setup

**1. Clone repository:**
```bash
git clone https://github.com/akshu-3132/bookmyseat.git
cd bookmyseat
```

**2. Create PostgreSQL database:**
```bash
psql -U postgres -c "CREATE DATABASE bookmyseat;"
```

**3. Configure database connection:**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmyseat
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create-drop  # auto-creates schema (dev only)
```

**4. Build project:**
```bash
./mvnw clean package
```

**5. Run application:**
```bash
./mvnw spring-boot:run
```

Application starts on `http://localhost:8080`

### Verify Installation

**Check health:**
```bash
curl http://localhost:8080/health
# Expected: 200 OK
```

**View API endpoints:**
See [API_QUICK_REFERENCE.md](API_QUICK_REFERENCE.md) for full endpoint list

### Running Tests
```bash
./mvnw test
```

### Sample API Workflow
```bash
# 1. Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com"}'

# 2. Create city
curl -X POST http://localhost:8080/api/cities \
  -H "Content-Type: application/json" \
  -d '{"name":"Mumbai"}'

# 3. Create theatre (replace {cityId} with response from step 2)
curl -X POST http://localhost:8080/api/theatres/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"PVR Cinemas","address":"Fort, Mumbai"}'

# See PostmanCollection_BookMySeat_Complete.json for complete workflow
```

### Using Postman
Import `PostmanCollection_BookMySeat_Complete.json` into Postman for pre-configured requests with proper request/response validation.

## Folder Structure

```
bookmyseat/
├── src/
│   ├── main/java/com/akshadip/bookmyseat/
│   │   ├── controllers/          # REST API endpoints
│   │   ├── services/             # Business logic (User, Theatre, Show, Ticket services)
│   │   ├── repositories/         # Spring Data JPA interfaces
│   │   ├── models/               # JPA domain entities
│   │   ├── dtos/                 # Request/Response contracts
│   │   └── exceptions/           # Custom exceptions
│   ├── main/resources/
│   │   └── application.properties # Database and server configuration
│   └── test/                      # Unit tests
├── pom.xml                        # Maven dependencies
├── API_DOCUMENTATION.md           # Detailed API reference
├── API_QUICK_REFERENCE.md         # Quick API cheat sheet
└── PostmanCollection_BookMySeat_Complete.json  # Postman collection
```

## Future Improvements

### Phase 2: Robustness
- **Seat Selection Optimization** - Implement seat recommendation algorithm (suggest nearby available seats if first choice unavailable)
- **Cancellation Handling** - Add ticket cancellation with refund logic
- **Show Status Tracking** - CANCELLED, POSTPONED, SCREENING states
- **Error Recovery** - Implement idempotent booking (user can retry safely without double-charging)

### Phase 3: Performance & Scale
- **Caching Layer** - Redis for show availability and movie metadata
- **Seat Reservation System** - Hold seats for 5 minutes during checkout
- **Async Processing** - Background jobs for payment confirmations, ticket delivery (email/SMS)
- **Query Optimization** - Add database indexes on (Show, Status) for faster availability checks

### Phase 4: Features
- **Multiple Payment Methods** - Credit card, UPI, Wallets (currently payment schema ready, integration pending)
- **Dynamic Pricing** - Surge pricing for high-demand shows
- **Loyalty Program** - Points/rewards on bookings
- **Regional Pricing** - Different prices by geographic region
- **Bulk Booking** - Group reservations with volume discounts

### Phase 5: Enterprise
- **Multi-Tenancy** - Support independent theatre chains with data isolation
- **Analytics Dashboard** - Revenue, occupancy rate, peak demand tracking
- **Admin Panel** - Show management, manual seat blocking, reports
- **Mobile App Integration** - Native iOS/Android clients instead of web-only
- **Gateway Integration** - Real Razorpay/Stripe payment processing

## Lessons Learned

1. **Constraint-First Validation** - Let the database prevent invalid states; don't rely only on application logic
2. **State Entities > Direct Modification** - `ShowSeat` as separate entity prevents complex multi-table updates
3. **DTOs > Direct Entity Exposure** - API stability even when internal schema changes
4. **Start Simple, Scale Later** - Monolith is perfectly fine for MVP; premature microservices architecture kills projects

---

**Built with ❤️ by Akshadip | Last Updated: March 18, 2026**
