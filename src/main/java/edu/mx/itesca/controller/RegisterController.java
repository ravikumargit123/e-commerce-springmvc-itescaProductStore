package edu.mx.itesca.controller;

import edu.mx.itesca.model.BillingAddress;
import edu.mx.itesca.model.Customer;
import edu.mx.itesca.model.ShippingAddress;
import edu.mx.itesca.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by dario on 03/11/2016.
 */
@Controller
public class RegisterController {

    @Autowired
    CustomerService customerService;

    @RequestMapping("/registro")
    public String registro(Model model) {
        Customer customer = new Customer();
        BillingAddress billing = new BillingAddress();
        ShippingAddress shipping = new ShippingAddress();

        customer.setBillingAddress(billing);
        customer.setShippingAddress(shipping);

        model.addAttribute("customer", customer);

        return "registerCustomer";
    }

    @RequestMapping(value = "/registro", method = RequestMethod.POST)
    public String registroPost(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,
                               Model model, @AuthenticationPrincipal User user) {
        if(result.hasErrors()){
            return "registerCustomer";
        }

        List<Customer> customerList = customerService.getAllCustomers();

        for (int i=0; i< customerList.size(); i++){
            if(customer.getCustomer_email().equals(customerList.get(i).getCustomer_email())){
                model.addAttribute("customer_email", "Este correo ya esta registrado.");

                return "registerCustomer";
            }

            if(customer.getCustomer_username().equals(customerList.get(i).getCustomer_username())){
                model.addAttribute("customer_username", "Este nombre de usuario ya esta registrado.");

                return "registerCustomer";
            }
        }

        customer.setEnabled(true);
        customerService.addCustomer(customer);
        return "registroCustomerSuccess";
    }
}
