/**
 * Ngày 10 - Voucher Entity cho E-Commerce
 */
import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

export enum VoucherType {
  PERCENTAGE = 'PERCENTAGE',
  FIXED_AMOUNT = 'FIXED_AMOUNT',
}

@Entity('vouchers')
export class Voucher {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true, length: 50 })
  code: string;

  @Column({ type: 'enum', enum: VoucherType })
  type: VoucherType;

  @Column('decimal', { precision: 15, scale: 2 })
  value: number; // 10 (= 10%) hoặc 50000 (= 50K VND)

  @Column('decimal', { name: 'min_order_value', precision: 15, scale: 2, default: 0 })
  minOrderValue: number;

  @Column('decimal', { name: 'max_discount', precision: 15, scale: 2, nullable: true })
  maxDiscount: number | null; // Cap cho PERCENTAGE type

  @Column({ name: 'expiry_date' })
  expiryDate: Date;

  @Column({ name: 'usage_limit', default: 100 })
  usageLimit: number;

  @Column({ name: 'used_count', default: 0 })
  usedCount: number;

  @Column({ default: true })
  active: boolean;
}
