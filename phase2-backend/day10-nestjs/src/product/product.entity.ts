/**
 * Ngày 10 - TypeORM Entity: Product
 *
 * So sánh với JPA (Day 7):
 *   @Entity()          ←→  @Entity
 *   @PrimaryGeneratedColumn() ←→  @Id @GeneratedValue
 *   @Column()          ←→  @Column
 *   @ManyToOne()       ←→  @ManyToOne
 *   @CreateDateColumn() ←→  @PrePersist
 */
import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  CreateDateColumn,
  UpdateDateColumn,
  JoinColumn,
} from 'typeorm';
import { Category } from './category.entity';

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

  @Column({ name: 'image_url', nullable: true })
  imageUrl: string;

  @Column({ default: true })
  active: boolean;

  @ManyToOne(() => Category, (category) => category.products, {
    eager: true, // Auto-load category khi query product
  })
  @JoinColumn({ name: 'category_id' })
  category: Category;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;
}
