package shop.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.model.Cart;
import shop.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public Iterable<Cart> findAll() {
            return cartRepository.findAll();
    }

    public Cart save(Cart c) {
            return cartRepository.save(c);
    }

    public void delete(int id) {
            cartRepository.deleteById(id);
    }

    public Optional<Cart> findById(int id) {
            return cartRepository.findById(id);
    }
        
    //Servicio para calcular total y descuentos
    public Map<String, Object> getCartWithTotals(int id) {
        Optional<Cart> optionalCart = findById(id);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            // Calculamos el total sin descuentos
            double total = cart.getProducts().stream()
                .mapToDouble(product -> product.getPrice())
                .sum();

            // Calculamos el descuento, declaramos el descuento en 0, luego lo vamos pisando 
            double discount = 0;

            int productCount = cart.getProducts().size();

            // Descuento del 'general' 25% si hay exactamente 4 productos
            if (productCount == 4) {
                discount = total * 0.25; // 25% de descuento
            }

            // Descuentos si hay más de 10 productos
            if (productCount > 10) {
                switch (cart.getType()) {
                    case COMUN:
                        discount = 100; // Descuento fijo de 100 para carritos COMUN
                        break;
                    case FECHA_ESPECIAL:
                        discount = 300; // Descuento fijo de 300 para carritos FECHA_ESPECIAL
                        break;
                    case VIP:
                        // Descuento del producto más barato + 500 adicionales
                        double minPrice = cart.getProducts().stream()
                            .min(Comparator.comparingDouble(product -> product.getPrice())) // Aquí compara precios como double
                            .map(product -> product.getPrice().doubleValue()) // Convertimos explícitamente el precio a double
                            .orElse(0.0); // Si no hay productos, el mínimo es 0
                        discount = minPrice + 500;
                        break;

                }
            }

            // acá calculamos el total con el descuento que hemos aplicado
            double totalWithDiscount = total - discount;

            // Crear la respuesta
            Map<String, Object> response = new LinkedHashMap<>(); // Usamos LinkedHashMap para mantener este orden            
            response.put("id", cart.getId());
            response.put("state", cart.getState());
            response.put("type", cart.getType());
            response.put("total", total);
            response.put("totalWithDiscount", totalWithDiscount);
            response.put("products", cart.getProducts());

            return response;
        }
        return null;
    }
}
