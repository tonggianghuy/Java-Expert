# NGÀY 10: NestJS - TypeScript Backend

---

## 1. NESTJS LÀ GÌ?

### 1.1 Tổng quan

NestJS = Framework backend cho Node.js, viết bằng TypeScript.
Lấy cảm hứng từ Angular (frontend) và Spring Boot (Java).

```
Spring Boot (Java)              NestJS (TypeScript)
───────────────                 ──────────────────
@RestController          ←→    @Controller
@Service                 ←→    @Injectable
@Repository              ←→    TypeORM Repository
@Autowired               ←→    Constructor Injection
@GetMapping              ←→    @Get()
@PostMapping             ←→    @Post()
@RequestBody             ←→    @Body()
@PathVariable            ←→    @Param()
@Valid                   ←→    ValidationPipe
application.yml          ←→    .env + ConfigService
JPA Entity               ←→    TypeORM Entity
```

### 1.2 Tại sao học NestJS?

| Lý do | Chi tiết |
|-------|---------|
| **TypeScript native** | Type safety giống Java |
| **Giống Spring Boot** | Chuyển đổi dễ dàng |
| **Module system** | Tổ chức code rõ ràng |
| **Ecosystem** | Dùng mọi npm package |
| **Performance** | Node.js event loop, non-blocking I/O |

---

## 2. CÀI ĐẶT & TẠO PROJECT

```bash
# Cài NestJS CLI
npm install -g @nestjs/cli

# Tạo project
nest new ecommerce-api
# Chọn npm hoặc yarn

# Chạy
cd ecommerce-api
npm run start:dev    # Watch mode - tự restart khi code thay đổi

# Server chạy tại http://localhost:3000
```

### 2.1 Cấu trúc project

```
ecommerce-api/
├── src/
│   ├── main.ts                  # Entry point
│   ├── app.module.ts            # Root module
│   ├── app.controller.ts        # Root controller
│   └── app.service.ts           # Root service
├── test/                        # E2E tests
├── package.json
├── tsconfig.json
├── nest-cli.json
└── .env
```

### 2.2 Entry Point

```typescript
// main.ts
import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Global validation pipe (tương tự @Valid trong Spring)
  app.useGlobalPipes(new ValidationPipe({
    whitelist: true,          // Bỏ fields không có trong DTO
    forbidNonWhitelisted: true, // Throw error nếu có fields lạ
    transform: true,          // Auto-transform types
  }));

  // CORS
  app.enableCors({
    origin: 'http://localhost:3001',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
  });

  // Global prefix
  app.setGlobalPrefix('api');

  await app.listen(3000);
  console.log('Server running on http://localhost:3000');
}
bootstrap();
```

---

## 3. CORE CONCEPTS

### 3.1 Modules

Module = container tổ chức code theo feature.

```typescript
// product.module.ts
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ProductController } from './product.controller';
import { ProductService } from './product.service';
import { Product } from './product.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Product])],  // Register entity
  controllers: [ProductController],                 // Register controller
  providers: [ProductService],                      // Register service
  exports: [ProductService],                        // Export để module khác dùng
})
export class ProductModule {}

// app.module.ts (Root module)
@Module({
  imports: [
    TypeOrmModule.forRoot({                        // Database connection
      type: 'postgres',
      host: 'localhost',
      port: 5432,
      database: 'ecommerce',
      entities: [__dirname + '/**/*.entity{.ts,.js}'],
      synchronize: true,                            // DEV only!
    }),
    ProductModule,
    CartModule,
    OrderModule,
    AuthModule,
  ],
})
export class AppModule {}
```

### 3.2 Controllers

```typescript
import { Controller, Get, Post, Put, Delete, Body, Param, Query, HttpStatus,
         HttpCode, ParseIntPipe, UseGuards } from '@nestjs/common';

@Controller('products')  // /api/products
export class ProductController {
  constructor(private readonly productService: ProductService) {}

  // GET /api/products?page=0&size=10&category=electronics
  @Get()
  async findAll(
    @Query('page', new DefaultValuePipe(0), ParseIntPipe) page: number,
    @Query('size', new DefaultValuePipe(10), ParseIntPipe) size: number,
    @Query('category') category?: string,
  ) {
    return this.productService.findAll({ page, size, category });
  }

  // GET /api/products/1
  @Get(':id')
  async findOne(@Param('id', ParseIntPipe) id: number) {
    return this.productService.findOne(id);
  }

  // POST /api/products
  @Post()
  @HttpCode(HttpStatus.CREATED)
  @UseGuards(JwtAuthGuard, RolesGuard)       // Cần đăng nhập + role
  @Roles('ADMIN')
  async create(@Body() createProductDto: CreateProductDto) {
    return this.productService.create(createProductDto);
  }

  // PUT /api/products/1
  @Put(':id')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles('ADMIN')
  async update(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateProductDto: UpdateProductDto,
  ) {
    return this.productService.update(id, updateProductDto);
  }

  // DELETE /api/products/1
  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles('ADMIN')
  async remove(@Param('id', ParseIntPipe) id: number) {
    return this.productService.remove(id);
  }
}
```

### 3.3 Services

```typescript
import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';

@Injectable()  // Tương tự @Service trong Spring
export class ProductService {
  constructor(
    @InjectRepository(Product)
    private readonly productRepository: Repository<Product>,
  ) {}

  async findAll(query: { page: number; size: number; category?: string }) {
    const { page, size, category } = query;

    const queryBuilder = this.productRepository.createQueryBuilder('product');

    if (category) {
      queryBuilder.where('product.category = :category', { category });
    }

    const [data, total] = await queryBuilder
      .skip(page * size)
      .take(size)
      .orderBy('product.createdAt', 'DESC')
      .getManyAndCount();

    return {
      data,
      total,
      page,
      pageSize: size,
      totalPages: Math.ceil(total / size),
    };
  }

  async findOne(id: number): Promise<Product> {
    const product = await this.productRepository.findOne({ where: { id } });
    if (!product) {
      throw new NotFoundException(`Product #${id} not found`);
    }
    return product;
  }

  async create(dto: CreateProductDto): Promise<Product> {
    const product = this.productRepository.create(dto);
    return this.productRepository.save(product);
  }

  async update(id: number, dto: UpdateProductDto): Promise<Product> {
    const product = await this.findOne(id);
    Object.assign(product, dto);
    return this.productRepository.save(product);
  }

  async remove(id: number): Promise<void> {
    const product = await this.findOne(id);
    await this.productRepository.remove(product);
  }
}
```

---

## 4. TYPEORM - DATABASE

### 4.1 Entities

```typescript
import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany,
         CreateDateColumn, UpdateDateColumn } from 'typeorm';

@Entity('products')
export class Product {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ length: 100 })
  name: string;

  @Column('decimal', { precision: 15, scale: 2 })
  price: number;

  @Column({ nullable: true })
  description: string;

  @Column({ default: 0 })
  quantity: number;

  @Column({ default: true })
  active: boolean;

  @ManyToOne(() => Category, (category) => category.products)
  category: Category;

  @OneToMany(() => OrderItem, (item) => item.product)
  orderItems: OrderItem[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}

@Entity('categories')
export class Category {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  name: string;

  @OneToMany(() => Product, (product) => product.category)
  products: Product[];
}

@Entity('orders')
export class Order {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  userId: number;

  @Column('decimal', { precision: 15, scale: 2 })
  totalAmount: number;

  @Column({ type: 'enum', enum: OrderStatus, default: OrderStatus.PENDING })
  status: OrderStatus;

  @OneToMany(() => OrderItem, (item) => item.order, { cascade: true })
  items: OrderItem[];

  @CreateDateColumn()
  createdAt: Date;
}

enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPING = 'SHIPPING',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED',
}
```

---

## 5. VALIDATION (DTOs)

```typescript
import { IsString, IsNumber, IsNotEmpty, Min, Max, IsOptional,
         IsEmail, MinLength, IsEnum } from 'class-validator';
import { PartialType } from '@nestjs/mapped-types';

export class CreateProductDto {
  @IsString()
  @IsNotEmpty({ message: 'Tên sản phẩm không được trống' })
  name: string;

  @IsNumber()
  @Min(0, { message: 'Giá phải >= 0' })
  price: number;

  @IsString()
  @IsOptional()
  description?: string;

  @IsNumber()
  @Min(0)
  quantity: number;

  @IsNumber()
  categoryId: number;
}

// UpdateProductDto: tất cả fields optional (kế thừa từ Create)
export class UpdateProductDto extends PartialType(CreateProductDto) {}

// Register DTO
export class RegisterDto {
  @IsString()
  @MinLength(3)
  username: string;

  @IsString()
  @MinLength(8, { message: 'Mật khẩu ít nhất 8 ký tự' })
  password: string;

  @IsEmail()
  email: string;
}
```

---

## 6. AUTHENTICATION (JWT)

### 6.1 Setup

```bash
npm install @nestjs/jwt @nestjs/passport passport passport-jwt bcrypt
npm install -D @types/passport-jwt @types/bcrypt
```

### 6.2 Auth Module

```typescript
// auth.module.ts
@Module({
  imports: [
    JwtModule.register({
      secret: process.env.JWT_SECRET || 'your-secret-key',
      signOptions: { expiresIn: '24h' },
    }),
    TypeOrmModule.forFeature([User]),
  ],
  controllers: [AuthController],
  providers: [AuthService, JwtStrategy],
  exports: [AuthService],
})
export class AuthModule {}

// auth.service.ts
@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User)
    private userRepository: Repository<User>,
    private jwtService: JwtService,
  ) {}

  async register(dto: RegisterDto) {
    const existing = await this.userRepository.findOne({
      where: { username: dto.username },
    });
    if (existing) {
      throw new ConflictException('Username đã tồn tại');
    }

    const hashedPassword = await bcrypt.hash(dto.password, 12);
    const user = this.userRepository.create({
      ...dto,
      password: hashedPassword,
    });

    const saved = await this.userRepository.save(user);
    return { id: saved.id, username: saved.username, email: saved.email };
  }

  async login(dto: LoginDto) {
    const user = await this.userRepository.findOne({
      where: { username: dto.username },
    });

    if (!user || !(await bcrypt.compare(dto.password, user.password))) {
      throw new UnauthorizedException('Username hoặc password sai');
    }

    const payload = { sub: user.id, username: user.username, role: user.role };
    return {
      token: this.jwtService.sign(payload),
      expiresIn: 86400,
      user: { id: user.id, username: user.username, role: user.role },
    };
  }
}

// jwt.strategy.ts
@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor() {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      secretOrKey: process.env.JWT_SECRET || 'your-secret-key',
    });
  }

  async validate(payload: any) {
    return { id: payload.sub, username: payload.username, role: payload.role };
  }
}

// jwt-auth.guard.ts
@Injectable()
export class JwtAuthGuard extends AuthGuard('jwt') {}
```

---

## 7. SO SÁNH SPRING BOOT VS NESTJS

| Feature | Spring Boot (Java) | NestJS (TypeScript) |
|---------|-------------------|-------------------|
| **Language** | Java (strongly typed) | TypeScript (typed JS) |
| **Runtime** | JVM | Node.js (V8) |
| **Threading** | Multi-threaded | Single-threaded (event loop) |
| **ORM** | JPA/Hibernate | TypeORM/Prisma |
| **DI** | Spring IoC Container | NestJS DI Container |
| **Validation** | Bean Validation (Jakarta) | class-validator |
| **Testing** | JUnit + Mockito | Jest |
| **Build** | Maven/Gradle | npm/yarn |
| **Hot Reload** | Spring DevTools | --watch flag |
| **Startup Time** | Chậm hơn (~3-5s) | Nhanh hơn (~1-2s) |
| **Memory** | Nhiều hơn (~200MB+) | Ít hơn (~50-100MB) |
| **Best for** | Banking core, Enterprise | API gateway, Microservices, BFF |

**Khi nào chọn gì?**
- **Spring Boot:** Core banking, complex transactions, enterprise systems
- **NestJS:** API gateway, BFF (Backend for Frontend), real-time features, microservices

---

## 8. EXCEPTION FILTERS

```typescript
// http-exception.filter.ts
@Catch()
export class AllExceptionsFilter implements ExceptionFilter {
  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse();

    let status = HttpStatus.INTERNAL_SERVER_ERROR;
    let message = 'Internal server error';

    if (exception instanceof HttpException) {
      status = exception.getStatus();
      const exResponse = exception.getResponse();
      message = typeof exResponse === 'string'
        ? exResponse
        : (exResponse as any).message;
    }

    response.status(status).json({
      success: false,
      data: null,
      message: Array.isArray(message) ? message.join(', ') : message,
      timestamp: new Date().toISOString(),
    });
  }
}

// Đăng ký global trong main.ts:
app.useGlobalFilters(new AllExceptionsFilter());
```

---

## 9. TÓM TẮT NGÀY 10

```
┌──────────────────────────────────────────────────┐
│               NGÀY 10 - TÓM TẮT                 │
├──────────────────────────────────────────────────┤
│ NestJS = Spring Boot cho TypeScript:             │
│ ✓ Module → tổ chức code theo feature            │
│ ✓ Controller → nhận request, trả response       │
│ ✓ Service (@Injectable) → business logic        │
│ ✓ Entity (TypeORM) → database mapping           │
│                                                  │
│ So sánh với Spring Boot:                         │
│ ✓ @Controller = @RestController                  │
│ ✓ @Body() = @RequestBody                        │
│ ✓ @Param() = @PathVariable                      │
│ ✓ ValidationPipe = @Valid                       │
│ ✓ Guards = Spring Security filters              │
│                                                  │
│ TypeORM:                                         │
│ ✓ @Entity, @Column, @ManyToOne, @OneToMany      │
│ ✓ Repository pattern (giống JpaRepository)       │
│ ✓ QueryBuilder cho complex queries              │
│                                                  │
│ Auth:                                            │
│ ✓ JWT + Passport                                │
│ ✓ Guards cho protected routes                    │
│ ✓ bcrypt cho password hashing                    │
│                                                  │
│ ⚠️ synchronize: true CHỈ dùng cho development   │
│ ⚠️ NestJS dùng Decorators (giống Annotations)   │
└──────────────────────────────────────────────────┘
```

---

## CÂU HỎI ÔN TẬP

1. NestJS Module system hoạt động thế nào? Khác gì Spring Boot?
2. `@Injectable()` tương đương gì trong Spring Boot?
3. TypeORM Entity khác JPA Entity ở điểm nào?
4. Guard trong NestJS tương đương gì trong Spring Security?
5. So sánh ưu nhược điểm Spring Boot vs NestJS cho 1 Ecommerce app.
6. Tại sao `synchronize: true` nguy hiểm cho production?
