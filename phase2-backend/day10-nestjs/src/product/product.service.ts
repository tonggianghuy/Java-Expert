/**
 * Ngày 10 - Product Service
 *
 * So sánh với Spring Boot (Day 8):
 *   @Injectable()  ←→  @Service
 *   Repository<T>  ←→  JpaRepository<T, ID>
 *   .find()        ←→  .findAll()
 *   .findOne()     ←→  .findById()
 *   .save()        ←→  .save()
 *   .remove()      ←→  .deleteById()
 */
import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Product } from './product.entity';
import { Category } from './category.entity';
import { CreateProductDto } from './dto/create-product.dto';

@Injectable()
export class ProductService {
  constructor(
    @InjectRepository(Product)
    private readonly productRepository: Repository<Product>,
    @InjectRepository(Category)
    private readonly categoryRepository: Repository<Category>,
  ) {}

  /**
   * Lấy tất cả sản phẩm với search, filter, pagination
   */
  async findAll(query: {
    page?: number;
    size?: number;
    name?: string;
    categoryId?: number;
    minPrice?: number;
    maxPrice?: number;
  }) {
    const { page = 0, size = 10, name, categoryId, minPrice, maxPrice } = query;

    const qb = this.productRepository
      .createQueryBuilder('product')
      .leftJoinAndSelect('product.category', 'category')
      .where('product.active = :active', { active: true });

    if (name) {
      qb.andWhere('LOWER(product.name) LIKE LOWER(:name)', {
        name: `%${name}%`,
      });
    }

    if (categoryId) {
      qb.andWhere('product.category_id = :categoryId', { categoryId });
    }

    if (minPrice !== undefined) {
      qb.andWhere('product.price >= :minPrice', { minPrice });
    }

    if (maxPrice !== undefined) {
      qb.andWhere('product.price <= :maxPrice', { maxPrice });
    }

    const [data, total] = await qb
      .orderBy('product.createdAt', 'DESC')
      .skip(page * size)
      .take(size)
      .getManyAndCount();

    return {
      data,
      total,
      page,
      pageSize: size,
      totalPages: Math.ceil(total / size),
    };
  }

  /**
   * Lấy 1 sản phẩm theo ID
   */
  async findOne(id: number): Promise<Product> {
    const product = await this.productRepository.findOne({
      where: { id },
      relations: ['category'],
    });

    if (!product) {
      throw new NotFoundException(`Không tìm thấy sản phẩm #${id}`);
    }

    return product;
  }

  /**
   * Tạo sản phẩm mới (Admin only)
   */
  async create(dto: CreateProductDto): Promise<Product> {
    // Verify category exists
    const category = await this.categoryRepository.findOne({
      where: { id: dto.categoryId },
    });
    if (!category) {
      throw new NotFoundException(
        `Không tìm thấy danh mục #${dto.categoryId}`,
      );
    }

    const product = this.productRepository.create({
      name: dto.name,
      price: dto.price,
      description: dto.description,
      quantity: dto.quantity,
      imageUrl: dto.imageUrl,
      category: category,
    });

    return this.productRepository.save(product);
  }

  /**
   * Cập nhật sản phẩm (Admin only)
   */
  async update(id: number, dto: Partial<CreateProductDto>): Promise<Product> {
    const product = await this.findOne(id);

    if (dto.categoryId) {
      const category = await this.categoryRepository.findOne({
        where: { id: dto.categoryId },
      });
      if (!category) {
        throw new NotFoundException(
          `Không tìm thấy danh mục #${dto.categoryId}`,
        );
      }
      product.category = category;
    }

    Object.assign(product, {
      ...(dto.name && { name: dto.name }),
      ...(dto.price !== undefined && { price: dto.price }),
      ...(dto.description !== undefined && { description: dto.description }),
      ...(dto.quantity !== undefined && { quantity: dto.quantity }),
      ...(dto.imageUrl !== undefined && { imageUrl: dto.imageUrl }),
    });

    return this.productRepository.save(product);
  }

  /**
   * Xóa sản phẩm (soft delete - set active = false)
   */
  async remove(id: number): Promise<void> {
    const product = await this.findOne(id);
    product.active = false;
    await this.productRepository.save(product);
  }
}
