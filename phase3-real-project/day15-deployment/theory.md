# NGÀY 15: Deployment, Presentation & Review

---

## 1. DOCKER

### 1.1 Docker là gì?

Docker = đóng gói ứng dụng + tất cả dependencies vào 1 container.
"It works on my machine" → "It works on EVERY machine"

```
Không có Docker:                  Có Docker:
Cài JDK 17 ✓                    docker-compose up
Cài PostgreSQL ✓                 → Tất cả chạy ngay!
Config database ✓
Cài Node.js ✓
npm install ✓
Setup env vars ✓
```

### 1.2 Dockerfile - Banking App (Java)

```dockerfile
# Multi-stage build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 1.3 Dockerfile - Ecommerce App (NestJS)

```dockerfile
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM node:18-alpine
WORKDIR /app
COPY --from=build /app/dist ./dist
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/package.json .
EXPOSE 3000
CMD ["node", "dist/main"]
```

### 1.4 Docker Compose

```yaml
version: '3.8'

services:
  # PostgreSQL Database
  db:
    image: postgres:15-alpine
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password123
      POSTGRES_DB: appdb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  # Banking API (Java)
  banking-api:
    build: ./banking-app
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/appdb
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: password123
      JWT_SECRET: your-super-secret-key
    depends_on:
      - db

  # Ecommerce API (NestJS)
  ecommerce-api:
    build: ./ecommerce-app
    ports:
      - "3000:3000"
    environment:
      DB_HOST: db
      DB_PORT: 5432
      DB_NAME: appdb
      DB_USER: admin
      DB_PASSWORD: password123
      JWT_SECRET: your-super-secret-key
    depends_on:
      - db

volumes:
  pgdata:
```

### 1.5 Chạy Docker

```bash
# Build và start tất cả services
docker-compose up --build

# Chạy ở background
docker-compose up -d

# Xem logs
docker-compose logs -f banking-api

# Dừng
docker-compose down

# Dừng và xóa data
docker-compose down -v
```

---

## 2. DEPLOY LÊN CLOUD (Free Tier)

### 2.1 Lựa chọn

| Platform | Free Tier | Phù hợp cho |
|----------|-----------|-------------|
| **Railway** | $5 credit/month | Database + Backend |
| **Render** | 750h/month | Backend APIs |
| **Fly.io** | 3 shared VMs | Docker containers |
| **Supabase** | 500MB database | PostgreSQL |
| **Neon** | 512MB database | PostgreSQL |

### 2.2 Deploy lên Railway (đơn giản nhất)

```bash
# 1. Cài Railway CLI
npm install -g @railway/cli

# 2. Login
railway login

# 3. Tạo project
railway init

# 4. Thêm PostgreSQL
railway add --plugin postgresql

# 5. Deploy
railway up

# 6. Lấy URL
railway open
```

---

## 3. PRESENTATION GUIDE

### 3.1 Cấu trúc trình bày (30 phút)

```
1. Giới thiệu (2 phút)
   - Tên project
   - Tech stack
   - Mục tiêu

2. Architecture Overview (3 phút)
   - Sơ đồ kiến trúc
   - Database ERD
   - Layered architecture

3. Live Demo (15 phút)
   Banking App:
   - Register → Login → Get token
   - Create account → Deposit → Withdraw
   - Transfer (thành công + thất bại)
   - Xem transaction history
   - Admin: freeze account

   Ecommerce App:
   - Browse products → Search → Filter
   - Add to cart → Update quantity
   - Apply voucher → Place order
   - View order → Cancel order
   - Admin: manage products

4. Code Highlights (5 phút)
   - Show TransferService: business logic
   - Show OrderService: order workflow
   - Show test cases
   - Show security config

5. Challenges & Lessons Learned (3 phút)
   - Khó khăn gặp phải
   - Cách giải quyết
   - Điều sẽ làm khác nếu làm lại

6. Q&A (2 phút)
```

### 3.2 Demo Tips

```
✅ LÀM:
- Chuẩn bị Postman collection sẵn
- Seed data để demo có sẵn products/accounts
- Test demo flow trước khi present
- Có backup plan nếu demo fail

❌ KHÔNG:
- Code live (trừ khi fix bug đơn giản)
- Giải thích từng dòng code
- Demo feature chưa hoàn thành
- Nói "nó hoạt động trên máy tôi"
```

---

## 4. SELF-REVIEW CHECKLIST

### Code Quality
```
□ Không còn TODO comments
□ Không có code bị comment out
□ Không có console.log / System.out.println thừa
□ Naming conventions đúng
□ Methods <= 20 dòng
□ Single Responsibility per class/method
```

### Functionality
```
□ Tất cả CRUD operations hoạt động
□ Authentication & Authorization đúng
□ Business rules enforced (fees, limits, stock)
□ Error handling cho mọi edge case
□ Consistent API response format
```

### Security
```
□ Passwords được hash (BCrypt)
□ JWT authentication
□ RBAC (User vs Admin)
□ Input validation
□ Không log sensitive data
□ .env không bị commit
```

### Testing & Docs
```
□ Unit tests pass
□ Swagger documentation
□ README với setup instructions
□ Postman collection
```

---

## 5. ROADMAP SAU 15 NGÀY

### Ngắn hạn (2-4 tuần)
- Frontend: React + TypeScript cho Ecommerce
- Kết nối frontend với backend API
- Deploy full-stack application

### Trung hạn (1-3 tháng)
- Microservices: tách Banking thành services nhỏ
- Message queue: RabbitMQ / Kafka
- Caching: Redis
- CI/CD: GitHub Actions

### Dài hạn (3-6 tháng)
- Domain-Driven Design (DDD)
- Event Sourcing cho Banking
- Payment Gateway (Stripe, VNPay)
- Monitoring: ELK Stack, Prometheus + Grafana
- Cloud: AWS / GCP certified

---

## CHÚC MỪNG!

Bạn đã hoàn thành 15 ngày học tập intensive.
Từ đây, bạn có nền tảng vững chắc để tiếp tục phát triển
thành một Java / TypeScript developer trong lĩnh vực Banking & Ecommerce.

**Điều quan trọng nhất: Tiếp tục code mỗi ngày.**
