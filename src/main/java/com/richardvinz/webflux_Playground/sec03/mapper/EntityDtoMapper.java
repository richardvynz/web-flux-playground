package com.richardvinz.webflux_Playground.sec03.mapper;

import com.richardvinz.webflux_Playground.sec03.dto.CustomerDTO;
import com.richardvinz.webflux_Playground.sec03.entity.Customer;
import org.springframework.beans.BeanUtils;

public class EntityDtoMapper {
    public static Customer toCustomerEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public static CustomerDTO tocustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }
}
