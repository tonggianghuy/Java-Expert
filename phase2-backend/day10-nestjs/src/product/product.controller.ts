/**
 * Ngày 10 - Product Controller
 *
 * Endpoints:
 *   GET    /api/products              → Danh sách (search, filter, pagination)
 *   GET    /api/products/:id          → Chi tiết
 *   POST   /api/products              → Tạo mới (Admin)
 *   PUT    /api/products/:id          → Cập nhật (Admin)
 *   DELETE /api/products/:id          → Xóa (Admin)
 */
import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  ParseIntPipe,
  HttpCode,
  HttpStatus,
  DefaultValuePipe,
} from '@nestjs/common';
import { ProductService } from './product.service';
import { CreateProductDto } from './dto/create-product.dto';

@Controller('products')
export class ProductController {
  constructor(private readonly productService: ProductService) {}

  // GET /api/products?page=0&size=10&name=iphone&categoryId=1&minPrice=1000000
  @Get()
  async findAll(
    @Query('page', new DefaultValuePipe(0), ParseIntPipe) page: number,
    @Query('size', new DefaultValuePipe(10), ParseIntPipe) size: number,
    @Query('name') name?: string,
    @Query('categoryId') categoryId?: number,
    @Query('minPrice') minPrice?: number,
    @Query('maxPrice') maxPrice?: number,
  ) {
    return this.productService.findAll({
      page,
      size,
      name,
      categoryId,
      minPrice,
      maxPrice,
    });
  }

  // GET /api/products/1
  @Get(':id')
  async findOne(@Param('id', ParseIntPipe) id: number) {
    return this.productService.findOne(id);
  }

  // POST /api/products
  // TODO: Add @UseGuards(JwtAuthGuard, RolesGuard) @Roles('ADMIN')
  @Post()
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() createProductDto: CreateProductDto) {
    return this.productService.create(createProductDto);
  }

  // PUT /api/products/1
  // TODO: Add @UseGuards(JwtAuthGuard, RolesGuard) @Roles('ADMIN')
  @Put(':id')
  async update(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateProductDto: Partial<CreateProductDto>,
  ) {
    return this.productService.update(id, updateProductDto);
  }

  // DELETE /api/products/1
  // TODO: Add @UseGuards(JwtAuthGuard, RolesGuard) @Roles('ADMIN')
  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  async remove(@Param('id', ParseIntPipe) id: number) {
    return this.productService.remove(id);
  }
}
