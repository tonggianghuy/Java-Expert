/**
 * Ngày 10 - DTO cho tạo sản phẩm
 *
 * So sánh với Spring Boot (Day 6):
 *   @IsString()   ←→  @NotBlank
 *   @IsNumber()   ←→  @NotNull + kiểu number
 *   @Min(0)       ←→  @Min(value = 0)
 *   @IsOptional() ←→  không có @NotNull
 */
import {
  IsString,
  IsNumber,
  IsNotEmpty,
  Min,
  Max,
  IsOptional,
  IsInt,
} from 'class-validator';

export class CreateProductDto {
  @IsString()
  @IsNotEmpty({ message: 'Tên sản phẩm không được trống' })
  name: string;

  @IsNumber({}, { message: 'Giá phải là số' })
  @Min(0, { message: 'Giá phải >= 0' })
  @Max(999999999, { message: 'Giá không vượt quá 999,999,999' })
  price: number;

  @IsString()
  @IsOptional()
  description?: string;

  @IsInt({ message: 'Số lượng phải là số nguyên' })
  @Min(0, { message: 'Số lượng phải >= 0' })
  quantity: number;

  @IsInt({ message: 'Category ID phải là số nguyên' })
  categoryId: number;

  @IsString()
  @IsOptional()
  imageUrl?: string;
}
