/**
 * Ngày 10 - NestJS Entry Point
 *
 * Chạy: npm run start:dev
 * Server: http://localhost:3000
 * Swagger: http://localhost:3000/api/docs (nếu setup)
 */
import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Global prefix
  app.setGlobalPrefix('api');

  // Global validation pipe (tương tự @Valid trong Spring Boot)
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true, // Bỏ fields không có trong DTO
      forbidNonWhitelisted: true, // Throw error nếu có fields lạ
      transform: true, // Auto-transform types
    }),
  );

  // CORS
  app.enableCors({
    origin: 'http://localhost:3001',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
  });

  await app.listen(3000);
  console.log('\n🚀 E-Commerce API is running at http://localhost:3000/api');
  console.log('📝 Test with Postman or Thunder Client\n');
}

bootstrap();
