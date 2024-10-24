package shop.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import shop.model.Cart;
import shop.service.CartService;
import shop.util.Enums.CartState;

//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins= "https://ecommerce-angular-55932.web.app/")
@RestController
//Falta agregar un @RequestMapping("/cart") para no repetir
public class CartController {

	@Autowired
	CartService cartService;

	@GetMapping("/cart/get") //Trae todos los carritos
	public Iterable<Cart> getAll() {
		return cartService.findAll();
	}

	@GetMapping("/cart/get/{id}") //Trae 1 carrito por ID
        public ResponseEntity<?> findById(@PathVariable int id) {
        Map<String, Object> cartDetails = cartService.getCartWithTotals(id); //llama a cartService para descuentos
            if (cartDetails != null) {
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
            } else {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
            }
        }


	@PostMapping("/cart/add")
        public ResponseEntity<Map<String, String>> save(@RequestBody Cart c) {
            try {
                // aca indicamos que el estado siempre es OPEN al crear el carrito
                c.setState(CartState.ABIERTO); 
                Cart newCarrito = cartService.save(c);

                // ponemos la respuesta en formato JSON
                Map<String, String> response = new HashMap<>();
                response.put("message", "Carrito creado con éxito: " + newCarrito.getId());

                return new ResponseEntity<>(response, HttpStatus.CREATED);
                } catch (Exception e) {
                // dejamos la respuesta de error en formato JSON
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error al crear el carrito: " + e.getMessage());

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }
        }

	@PutMapping("/cart/update")
	public Cart update(@RequestBody Cart c) {
		return cartService.save(c);
	}

	@DeleteMapping("/cart/delete/{id}")
	public void delete(@PathVariable int id) {
		cartService.delete(id);
	}
        
}
