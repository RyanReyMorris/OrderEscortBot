package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.Product;
import com.github.ryanreymorris.orderescortbot.repository.ProductRepository;
import com.github.ryanreymorris.orderescortbot.util.ImageUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * Implementation of {@link ProductService} interface.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${product.photo.directory}")
    private String photosDirectory;

    @Autowired
    private ProductRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Product product) {
        byte[] compressedPhoto = ImageUtil.compressImage(product.getPhoto());
        product.setPhoto(compressedPhoto);
        repository.save(product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createCaption(Product product) {
        StringBuilder caption = new StringBuilder();
        caption.append(MessageFormat.format(":gear: Артикул: {0}; \n",product.getId()));
        caption.append(MessageFormat.format(":gear: Название: {0}; \n",product.getName()));
        caption.append(MessageFormat.format(":gear: Цена: {0} рублей; \n",product.getPrice()));
        caption.append(MessageFormat.format(":gear: Количество на складе: {0} штук; \n",product.getQuantity()));
        caption.append(MessageFormat.format(":gear: Продавец: {0}; \n",product.getSeller()));
        return caption.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product findById(Long id) {
        Product product = repository.findById(id).
                orElseThrow(() -> new RuntimeException(MessageFormat.format("Товара с id = {0} не было найдено", id)));
        byte[] decompressedPhoto = ImageUtil.decompressImage(product.getPhoto());
        product.setPhoto(decompressedPhoto);
        return product;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initProducts() {
        try {
            File[] files = new ClassPathResource(photosDirectory).getFile().listFiles();
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                Product product = new Product();
                String fileNameWithoutExtension = FilenameUtils.removeExtension(files[i].getName());
                product.setId(Long.valueOf(fileNameWithoutExtension));
                product.setSeller("Егор");
                product.setName("Продукт №" + i+1);
                product.setQuantity(50);
                product.setPrice(1000L);
                product.setPhoto(FileUtils.readFileToByteArray(files[i]));
                save(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
