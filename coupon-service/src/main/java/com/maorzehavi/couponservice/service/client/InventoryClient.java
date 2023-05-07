package com.maorzehavi.couponservice.service.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
@LoadBalancerClient(name = "inventory-service", configuration = LoadBalancerClientConfiguration.class)
public interface InventoryClient {

    @DeleteMapping("/api/v1/inventory/{id}")
    Void deleteCouponById(@PathVariable Long id);
}
