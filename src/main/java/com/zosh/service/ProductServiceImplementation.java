package com.zosh.service;

import com.zosh.exception.ProductException;
import com.zosh.model.Category;
import com.zosh.model.Product;
import com.zosh.repository.CategoryRepository;
import com.zosh.repository.OrderItemRepository; // ADDED: Import OrderItemRepository
import com.zosh.repository.ProductRepository;
import com.zosh.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository; // ADDED: Repository for checking orders

    // MODIFIED: Added OrderItemRepository to the constructor
    public ProductServiceImplementation(ProductRepository productRepository,
                                        UserService userService,
                                        CategoryRepository categoryRepository,
                                        OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.orderItemRepository = orderItemRepository; // ADDED
    }

    // ... (Your other methods like createProduct, updateProduct, etc., remain the same) ...

    @Override
    public Product createProduct(CreateProductRequest req) {
        // ... (No changes needed here)
        Category topLevel = categoryRepository.findByName(req.getToplavelCategory());
        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getToplavelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParant(req.getSecondlavelCategory(), topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondlavelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByNameAndParant(req.getThirdlavelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdlavelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPersent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    // MODIFIED: The entire deleteProduct method is updated with the safety logic.
    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);

        // Safety Check: Count how many order items are linked to this product.
        int orderedItemsCount = orderItemRepository.countByProductId(productId);

        if (orderedItemsCount > 0) {
            // If the product is in use, throw our custom exception with a clear message.
            throw new ProductException("Cannot delete product with ID " + productId +
                    ". It is referenced by " + orderedItemsCount + " existing order(s).");
        }

        // If the check passes (count is 0), then we can safely delete the product.
        product.getSizes().clear(); // This helps JPA to remove associations before deleting
        productRepository.delete(product);

        return "Product deleted successfully.";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        // ... (No changes needed here)
        Product product = findProductById(productId);
        if (req.getQuantity() != 0) {
            product.setQuantity(req.getQuantity());
        }
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        // ... (No changes needed here)
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new ProductException("Product not found with id: " + id);
    }

    // ... (Your other methods remain the same) ...
    @Override
    public List<Product> findProductByCategory(String category) {
        return List.of();
    }

    @Override
    public Page<Product> getAllProduct(String category,
                                       List<String> colors,
                                       List<String> sizes,
                                       Integer minPrice,
                                       Integer maxPrice,
                                       Integer minDiscount,
                                       String sort,
                                       String stock,
                                       Integer pageNumber,
                                       Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        if (colors != null && !colors.isEmpty()) {
            products = products.stream()
                    .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if (sizes != null && !sizes.isEmpty()) {
            // TODO: implement size filtering if needed
        }

        if (stock != null && !stock.isEmpty()) {
            if (stock.equalsIgnoreCase("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equalsIgnoreCase("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, products.size());
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}