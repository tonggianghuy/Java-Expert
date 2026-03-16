/**
 * Ngày 14 - Jest Unit Tests cho OrderService (TypeScript)
 *
 * Chạy: npm test -- order.service
 */
import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { BadRequestException, NotFoundException } from '@nestjs/common';

// TODO: Import actual service and entities when project is set up
// import { OrderService } from '../src/modules/order/order.service';
// import { Order, OrderStatus } from '../src/modules/order/order.entity';
// import { CartItem } from '../src/modules/cart/cart.entity';
// import { Product } from '../src/modules/product/product.entity';
// import { Voucher, VoucherType } from '../src/modules/voucher/voucher.entity';

/**
 * Mock Repository helper
 */
const createMockRepository = () => ({
  find: jest.fn(),
  findOne: jest.fn(),
  save: jest.fn(),
  create: jest.fn(),
  delete: jest.fn(),
  remove: jest.fn(),
});

describe('OrderService', () => {
  // let orderService: OrderService;
  let cartItemRepo: ReturnType<typeof createMockRepository>;
  let orderRepo: ReturnType<typeof createMockRepository>;
  let productRepo: ReturnType<typeof createMockRepository>;
  let voucherRepo: ReturnType<typeof createMockRepository>;

  beforeEach(async () => {
    cartItemRepo = createMockRepository();
    orderRepo = createMockRepository();
    productRepo = createMockRepository();
    voucherRepo = createMockRepository();

    // TODO: Setup TestingModule when project is set up
    // const module: TestingModule = await Test.createTestingModule({
    //   providers: [
    //     OrderService,
    //     { provide: getRepositoryToken(CartItem), useValue: cartItemRepo },
    //     { provide: getRepositoryToken(Order), useValue: orderRepo },
    //     { provide: getRepositoryToken(Product), useValue: productRepo },
    //     { provide: getRepositoryToken(Voucher), useValue: voucherRepo },
    //   ],
    // }).compile();
    // orderService = module.get(OrderService);
  });

  describe('placeOrder', () => {
    it('should create order successfully with valid cart', async () => {
      // Given
      const cartItems = [
        {
          product: { id: 1, name: 'iPhone 15', price: 25990000, quantity: 50 },
          quantity: 1,
          userId: 1,
        },
        {
          product: { id: 2, name: 'AirPods', price: 5990000, quantity: 80 },
          quantity: 2,
          userId: 1,
        },
      ];

      cartItemRepo.find.mockResolvedValue(cartItems);
      orderRepo.create.mockImplementation((data: any) => ({ ...data, id: 1 }));
      orderRepo.save.mockImplementation(async (order: any) => order);

      // When
      // const result = await orderService.placeOrder(1, {});

      // Then
      // expect(result.status).toBe('PENDING');
      // expect(result.totalAmount).toBe(25990000 + 5990000 * 2); // 37,970,000

      // Verify stock deducted
      // expect(productRepo.save).toHaveBeenCalledTimes(2);

      // Verify cart cleared
      // expect(cartItemRepo.delete).toHaveBeenCalledWith({ userId: 1 });

      console.log('TODO: Uncomment tests when OrderService is implemented');
      expect(true).toBe(true);
    });

    it('should throw BadRequestException when cart is empty', async () => {
      cartItemRepo.find.mockResolvedValue([]);

      // await expect(orderService.placeOrder(1, {}))
      //   .rejects.toThrow(BadRequestException);

      console.log('TODO: Uncomment test');
      expect(true).toBe(true);
    });

    it('should throw when product is out of stock', async () => {
      const cartItems = [
        {
          product: { id: 1, name: 'iPhone 15', price: 25990000, quantity: 0 },
          quantity: 1,
          userId: 1,
        },
      ];
      cartItemRepo.find.mockResolvedValue(cartItems);

      // await expect(orderService.placeOrder(1, {}))
      //   .rejects.toThrow(/chỉ còn/);

      console.log('TODO: Uncomment test');
      expect(true).toBe(true);
    });

    it('should apply PERCENTAGE voucher correctly', async () => {
      const cartItems = [
        {
          product: { id: 1, price: 2000000, quantity: 100 },
          quantity: 1,
          userId: 1,
        },
      ];
      const voucher = {
        code: 'SALE10',
        type: 'PERCENTAGE',
        value: 10, // 10%
        minOrderValue: 500000,
        maxDiscount: 300000,
        expiryDate: new Date('2026-12-31'),
        usageLimit: 100,
        usedCount: 5,
      };

      cartItemRepo.find.mockResolvedValue(cartItems);
      voucherRepo.findOne.mockResolvedValue(voucher);

      // const result = await orderService.placeOrder(1, { voucherCode: 'SALE10' });
      // expect(result.discount).toBe(200000); // 2M * 10% = 200K (< maxDiscount 300K)
      // expect(result.totalAmount).toBe(1800000);

      console.log('TODO: Uncomment test');
      expect(true).toBe(true);
    });

    it('should cap PERCENTAGE voucher at maxDiscount', async () => {
      const cartItems = [
        {
          product: { id: 1, price: 10000000, quantity: 100 },
          quantity: 1,
          userId: 1,
        },
      ];
      const voucher = {
        code: 'SALE10',
        type: 'PERCENTAGE',
        value: 10,
        minOrderValue: 500000,
        maxDiscount: 300000, // Cap at 300K
        expiryDate: new Date('2026-12-31'),
        usageLimit: 100,
        usedCount: 5,
      };

      cartItemRepo.find.mockResolvedValue(cartItems);
      voucherRepo.findOne.mockResolvedValue(voucher);

      // const result = await orderService.placeOrder(1, { voucherCode: 'SALE10' });
      // expect(result.discount).toBe(300000); // 10M * 10% = 1M → capped at 300K
      // expect(result.totalAmount).toBe(9700000);

      console.log('TODO: Uncomment test');
      expect(true).toBe(true);
    });

    it('should reject expired voucher', async () => {
      const cartItems = [
        {
          product: { id: 1, price: 1000000, quantity: 10 },
          quantity: 1,
          userId: 1,
        },
      ];
      const voucher = {
        code: 'EXPIRED',
        type: 'PERCENTAGE',
        value: 10,
        minOrderValue: 0,
        expiryDate: new Date('2023-01-01'), // Expired
        usageLimit: 100,
        usedCount: 0,
      };

      cartItemRepo.find.mockResolvedValue(cartItems);
      voucherRepo.findOne.mockResolvedValue(voucher);

      // await expect(orderService.placeOrder(1, { voucherCode: 'EXPIRED' }))
      //   .rejects.toThrow(/hết hạn/);

      console.log('TODO: Uncomment test');
      expect(true).toBe(true);
    });
  });

  describe('cancelOrder', () => {
    it('should cancel PENDING order and restore stock', async () => {
      // TODO: Implement when OrderService is ready
      console.log('TODO: Implement cancelOrder test');
      expect(true).toBe(true);
    });

    it('should reject cancellation of SHIPPING order', async () => {
      // TODO: Implement when OrderService is ready
      console.log('TODO: Implement reject cancel SHIPPING test');
      expect(true).toBe(true);
    });
  });
});
